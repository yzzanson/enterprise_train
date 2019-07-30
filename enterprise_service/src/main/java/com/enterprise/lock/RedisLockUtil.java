package com.enterprise.lock;

import com.enterprise.base.common.SpringContextHolder;
import com.enterprise.service.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * @Description
 * @Author zezhouyang
 * @Date 19/2/22 下午1:56
 */
@Component
public class RedisLockUtil {

    private static final Logger logger = LoggerFactory.getLogger(RedisLockUtil.class);


    private Long defaultExpire = 1000000L;

    public RedisService redisService;

    public RedisLockUtil() {
    }


    public RedisLockUtil(Long expireTime) {
        defaultExpire = expireTime;
    }




    public void lock(String key, String value,Long expireTime) {
        redisService = SpringContextHolder.getBean(RedisService.class);
        if(expireTime==null){
            expireTime = defaultExpire;
        }
        Long nowTime = System.nanoTime();
        //请求锁超时时间，毫秒
        final Random r = new Random();
        try{
            while((System.nanoTime() - nowTime) < expireTime){
                Boolean getLock = redisService.setnx(key,value);
                if(getLock){
                    redisService.expirenx(key,expireTime);
                    break;
                }
                logger.info("获取锁失败,重试");
                Thread.sleep(3,r.nextInt(500));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void unlock(String key){
        redisService = SpringContextHolder.getBean(RedisService.class);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(redisService.getnx(key)!=null){
            redisService.releasenx(key);
        }
    }

    public Long getDefaultExpire() {
        return defaultExpire;
    }

    public void setDefaultExpire(Long defaultExpire) {
        this.defaultExpire = defaultExpire;
    }
}
