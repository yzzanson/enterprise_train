package com.enterprise.aop;

import com.enterprise.base.common.RedisConstant;
import com.enterprise.cache.GetCache;
import com.enterprise.service.redis.RedisService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
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
public class GetCacheAOP {

    @Resource
    private RedisService redisService;

    private Long expireTime = RedisConstant.EXPIRE_TIME_COMMON;


    @Pointcut("@annotation(com.enterprise.cache.GetCache)")
    public void getCache(){
//        System.out.println("切入");
    }

    /**
     * 在所有标注@getCache的地方切入
     * @param joinPoint
     */
    @Around("getCache()")
    public Object beforeExec(ProceedingJoinPoint joinPoint){


        //前置：到redis中查询缓存
//        System.out.println("调用从redis中查询的方法...");

        //redis中key格式：    id
        String redisKey = getCacheKey(joinPoint);

        //获取从redis中查询到的对象
        Object objectFromRedis = redisService.getSerializeObj(redisKey);

        //如果查询到了
        if(null != objectFromRedis){
//            System.out.println("从redis中查询到了数据...不需要查询数据库");
            return objectFromRedis;
        }

//        System.out.println("没有从redis中查到数据...");

        //没有查到，那么查询数据库
        Object object = null;
        try {
            object = joinPoint.proceed();
        } catch (Throwable e) {

            e.printStackTrace();
        }

//        System.out.println("从数据库中查询的数据...");

        //后置：将数据库中查询的数据放到redis中
//        System.out.println("调用把数据库查询的数据存储到redis中的方法...");

        redisService.setSerialize(redisKey, object , expireTime);
//        System.out.println("redis中的数据..."+object.toString());
        //将查询到的数据返回
        return object;
    }

    /**
     * 根据类名、方法名和参数值获取唯一的缓存键
     * @return 格式为 "包名.类名.方法名.参数类型.参数值"，类似 "your.package.SomeService.getById(int).123"
     */

    @SuppressWarnings("unused")
    private String getCacheKey(ProceedingJoinPoint joinPoint) {


        MethodSignature ms=(MethodSignature) joinPoint.getSignature();
        Method method=ms.getMethod();
        String ActionName = method.getAnnotation(GetCache.class).name();
        String fieldList = method.getAnnotation(GetCache.class).value();
        //System.out.println("签名是"+ms.toString());
        for (String field:fieldList.split(","))
//            ActionName +="."+field;
            ActionName +="_"+field;

        //先获取目标方法参数
        String id = null;
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0) {
            id = String.valueOf(args[0]);
        }

//        ActionName += "="+id;
        ActionName += "_"+id;
//        String redisKey = ms+"."+ActionName;
        String redisKey = ActionName;
        return redisKey;
    }

}
