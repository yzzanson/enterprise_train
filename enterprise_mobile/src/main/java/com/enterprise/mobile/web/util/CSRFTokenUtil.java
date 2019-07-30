package com.enterprise.mobile.web.util;

import com.enterprise.base.common.MobileLoginUser;
import com.enterprise.service.redis.RedisService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/12/4 上午11:04
 */
@Component
public class CSRFTokenUtil {

    public static final String CSRF_TOKEN="csrf-token";
    public static final int THIRTY_MINUTES = 30*60;//token缓存时间30分钟

    @Resource
    private RedisService redisService;

    /**
     * 生成新token 放入redis set中（集合）
     * 每个user最多允许100个token
     * @return
     */
    public String generate() {
        int userId = getUserId();
        Object token = null;
        if(userId > 0){
            String key = "user_token"+userId;
            long snum = redisService.sGetSetSize(key);

            //如果该用户的token数大于100，则随机返回一个已有token，不在生成新token
            if(snum > 100){
                token = redisService.sGetOne(key);
            }else {//否则生成新token
                token = UUID.randomUUID().toString();
                redisService.sSetAndTime(key, THIRTY_MINUTES, token);
            }
        }
        return (String)token;
    }

    /**
     * 验证token(在set中查找)
     * @param page_token
     * @return
     */
    public boolean verifyToken(String page_token){
        if(redisService.sGetSetSize(page_token) > 0){
            int userId = getUserId();

            String key = "user_token"+userId;
            //判断redis集合中是否存在
            if(userId>0 && redisService.sHasKey(key, page_token)){
                return true;
            }
        }
        return false;
    }

    /**
     * 删除token(从set中删除)
     * @param page_token
     */
    public void deleteToken(String page_token){
        int userId = getUserId();
        if (userId>0){
            String key = "user_token"+userId;
            redisService.setRemove(key, page_token);//从redis集合中删除
        }
    }

    private Integer getUserId(){
        return MobileLoginUser.getUser().getUserID();
    }


}
