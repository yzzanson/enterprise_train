package com.enterprise.mobile.web.aop;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.RedisConstant;
import com.enterprise.base.common.ResultJson;
import com.enterprise.base.entity.DSwitchEntity;
import com.enterprise.dswitch.DSwitch;
import com.enterprise.service.dswitch.DSwitchService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;


/**
 * @Description
 * @Author zezhouyang
 * @Date 18/11/13 上午11:47
 */
@Component
@Aspect
public class DSwitchAOP {

    @Resource
    private DSwitchService dSwitchService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Long expireTime = RedisConstant.EXPIRE_TIME_COMMON;

    public static String DSWITCH_TYPE_KEY = RedisConstant.DSWITCH_TYPE_KEY;

    @Pointcut("@annotation(com.enterprise.dswitch.DSwitch)")
    public void getDSwitch() {
//        System.out.println("切入");
    }

    /**
     * 在所有标注@getCache的地方切入
     *
     * @param joinPoint
     */
    @Around("getDSwitch()")
    public Object beforeExec(ProceedingJoinPoint joinPoint) {
        //前置：到redis中查询缓存
//        System.out.println("调用从redis中查询的方法...");
        //redis中key格式：    id
        String type = getType(joinPoint);
        String key = DSWITCH_TYPE_KEY + type;
        //获取从redis中查询到的对象
        DSwitchEntity objectFromRedis = dSwitchService.getOne(Integer.valueOf(type.trim()));
        //如果查询到了
        if (null != objectFromRedis && objectFromRedis.getStatus()!=null && objectFromRedis.getStatus().equals(0)) {
            return returnBusyJson();
        }
        //没有查到，那么查询数据库
        Object object = null;
        try {
            object = joinPoint.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        //将查询到的数据返回
        return object;
    }

    private JSONObject returnBusyJson(){
        return ResultJson.busyResponseResultJson("当前用户较多,请稍后再试!");
    }


    private String getType(ProceedingJoinPoint joinPoint) {
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        Method method = ms.getMethod();
        String type = method.getAnnotation(DSwitch.class).type();
        return type;
    }


}
