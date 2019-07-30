package com.enterprise.service.session;

import com.enterprise.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by anson on 17/9/12.
 */
@Service
public class SessionService {

    private static Map<String, Object> sessionHashMap = new HashMap<String, Object>();

    private Logger logger = LoggerFactory.getLogger(this.getClass());

//    private JdkSerializationRedisSerializer jdkSerializer = new JdkSerializationRedisSerializer();

//    @Autowired
//    private UserRedisDao userRedisDao;

    public Map<String, Object> getSession(String sid) {
//        Map<String, Object> sessionMap = new HashMap<>();
        try {
//            Object obj = userRedisDao.getString(RedisKeyUtil.SESSION_DISTRIBUTED_SESSIONID + sid);
            Object obj = sessionHashMap.get(RedisKeyUtil.SESSION_DISTRIBUTED_SESSIONID + sid);
            if (obj != null) {
                //obj = jdkSerializer.deserialize((byte[]) obj);
                sessionHashMap = (Map<String, Object>) obj;
            }
        } catch (Exception e) {
            logger.error("Redis获取session异常" + e.getMessage(), e.getCause());
        }
        return sessionHashMap;
    }

    public void saveSession(String sid, Map<String, Object> sessionMap) {
        try {
//            userRedisDao.setString(RedisKeyUtil.SESSION_DISTRIBUTED_SESSIONID + sid, jdkSerializer.serialize(sessionMap));
            sessionHashMap.put(RedisKeyUtil.SESSION_DISTRIBUTED_SESSIONID + sid, sessionMap);
        } catch (Exception e) {
            logger.error("Redis保存session异常" + e.getMessage(), e.getCause());
        }
    }

    public void removeSession(String sid) {
        try {
            sessionHashMap.remove(RedisKeyUtil.SESSION_DISTRIBUTED_SESSIONID + sid);
        } catch (Exception e) {
            logger.error("Redis删除session异常" + e.getMessage(), e.getCause());
        }
    }


//    public void setUserRedisDao(UserRedisDao userRedisDao) {
//        this.userRedisDao = userRedisDao;
//    }
}
