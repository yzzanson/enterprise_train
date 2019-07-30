package com.enterprise.service.redis.impl;

import com.alibaba.fastjson.JSON;
import com.enterprise.service.redis.RedisService;
import com.enterprise.util.SerializeUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/11/9 下午5:18
 */
@Service
public class RedisServiceImpl implements RedisService {

    private static final Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);

    /**
     * 不设置过期时长
     */
    public final static long NOT_EXPIRES = -1;

    @Resource
    private RedisTemplate redisTemplate;
    //private RedisTemplate redisTemplate;


    @Override
    public boolean setnx(String key, String value, long time) {
        if(redisTemplate.getConnectionFactory().getConnection().setNX(key.getBytes(),value.getBytes())){
            redisTemplate.expire(key,time,TimeUnit.MILLISECONDS);
        }
        return false;
    }

    @Override
    public boolean setnx(String key, String value) {
        return redisTemplate.getConnectionFactory().getConnection().setNX(key.getBytes(),value.getBytes());
    }

    @Override
    public void releasenx(String key) {
        this.del(key);
    }

    @Override
    public Object getnx(String key) {
        try {
            byte[] byteKey = SerializeUtil.serialize(key);
            return redisTemplate.getConnectionFactory().getConnection().get(byteKey);
        } catch (Exception e) {
            e.printStackTrace();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
            return null;
        }
    }

    @Override
    public void expirenx(String key, long time) {
        redisTemplate.expire(key,time,TimeUnit.MILLISECONDS);
    }

    //=============================common============================
    @Transactional
    @Override
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
            return false;
        }

    }

    @Override
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    @Override
    public boolean exists(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
            return false;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void del(String... key) {
        try {
            if (key != null && key.length > 0) {
                if (key.length == 1) {
                    redisTemplate.delete(key[0]);
                } else {
                    redisTemplate.delete(CollectionUtils.arrayToList(key));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    public void delBatch(String keyStartWith) {
        Set<String> keys = redisTemplate.keys(keyStartWith);
        redisTemplate.delete(keys);
    }

    //============================String=============================

    @Override
    public Object getObj(String key) {
        Object object = null;
        if (key == null) {
            return null;
        }
        try {
            object = redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            e.printStackTrace();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return object;
    }

    @Override
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
            return false;
        }

    }

    @Override
    public String get(String key) {
        if (key == null) {
            return null;
        }
        Object o = null;
        try {
            o = redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            e.printStackTrace();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
            return null;
        }
        return o == null ? null : toJson(o);
    }

    @Override
    public boolean set(String key, long extime, String content) {
        return set(key, content, extime);
    }

    @Override
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
            return false;
        }
    }

    @Override
    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        try {
            return redisTemplate.opsForValue().increment(key, delta);
        } catch (Exception e) {
            e.printStackTrace();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return 0;
    }

    @Override
    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        try {
            return redisTemplate.opsForValue().increment(key, -delta);
        } catch (Exception e) {
            e.printStackTrace();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return 0;
    }

    //============================Object=============================
    @Override
    public Object getSerializeObj(String key) {
        if (key == null)
            return null;
        byte[] byteArray = (byte[]) redisTemplate.opsForValue().get(key);
        if (byteArray == null) {
            return null;
        }
        return SerializeUtil.unSerialize(byteArray);
    }

    @Override
    public boolean setSerialize(String key, Object value) {
        try {
            byte[] byteArray = SerializeUtil.serialize(value);
            redisTemplate.opsForValue().set(key, byteArray);
            return true;
        } catch (Exception e) {
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
            return false;
        }
    }

    @Override
    public boolean setSerialize(String key, long extime, Object content) {
        try {
            if (extime > 0) {
                byte[] byteArray = SerializeUtil.serialize(content);
                if (byteArray == null) {
                    return false;
                }
                redisTemplate.opsForValue().set(key, byteArray, extime, TimeUnit.SECONDS);
            } else {
                byte[] byteArray = SerializeUtil.serialize(content);
                setSerialize(key, byteArray);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
            return false;
        }
    }

    @Override
    public boolean setSerialize(String key, Object value, long time) {
        return setSerialize(key, time, value);
    }

    //================================Map=================================

    @Override
    public Object hget(String key, String item) {
        try {
            return redisTemplate.opsForHash().get(key, item);
        } catch (Exception e) {
            e.printStackTrace();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
            return false;
        }
    }

    @Override
    public Map<Object, Object> hmget(String key) {
        try {
            return redisTemplate.opsForHash().entries(key);
        } catch (Exception e) {
            e.printStackTrace();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
            return null;
        }
    }

    @Override
    public boolean hmset(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
            return false;
        }
    }

    @Override
    public boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
            return false;
        }
    }

    @Override
    public boolean hset(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
            return false;
        }
    }

    @Override
    public boolean hset(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
            return false;
        }
    }

    @Override
    public void hdel(String key, Object... item) {
        try {
            redisTemplate.opsForHash().delete(key, item);
        } catch (Exception e) {
            e.printStackTrace();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    @Override
    public boolean hHasKey(String key, String item) {
        try {
            return redisTemplate.opsForHash().hasKey(key, item);
        } catch (Exception e) {
            e.printStackTrace();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
            return false;
        }
    }

    @Override
    public double hincr(String key, String item, double by) {
        try {
            return redisTemplate.opsForHash().increment(key, item, by);
        } catch (Exception e) {
            e.printStackTrace();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return 0;
    }

    @Override
    public double hdecr(String key, String item, double by) {
        try {
            return redisTemplate.opsForHash().increment(key, item, -by);
        } catch (Exception e) {
            e.printStackTrace();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return 0;
    }

    //============================set=============================

    @Override
    public Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
            return null;
        }
    }

    @Override
    public Object sGetOne(String key) {
        try {
            return redisTemplate.opsForSet().randomMember(key);
        } catch (Exception e) {
            e.printStackTrace();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
            return null;
        }
    }

    @Override
    public boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
            return false;
        }
    }

    @Override
    public long sSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
            return 0;
        }
    }

    @Override
    public long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0) expire(key, time);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
            return 0;
        }
    }

    @Override
    public long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
            return 0;
        }
    }

    @Override
    public long setRemove(String key, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
            return 0;
        }
    }
    //===============================list=================================

    @Override
    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
            return null;
        }
    }

    @Override
    public long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
            return 0;
        }
    }

    @Override
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
            return null;
        }
    }

    @Override
    public boolean lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
            return false;
        }
    }

    @Override
    public boolean lSet(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) expire(key, time);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
            return false;
        }
    }

    @Override
    public boolean lSet(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
            return false;
        }
    }

    @Override
    public boolean lSet(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) expire(key, time);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
            return false;
        }
    }

    @Override
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
            return false;
        }
    }

    @Override
    public long lRemove(String key, long count, Object value) {
        try {
            Long remove = redisTemplate.opsForList().remove(key, count, value);
            return remove;
        } catch (Exception e) {
            e.printStackTrace();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
            return 0;
        }
    }

    public void leftPush(String key, Object value) {
        try {
            redisTemplate.opsForList().leftPush(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    public Long size(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return null;
    }

    public Object rightPop(String key) {
        try {
            return redisTemplate.opsForList().rightPop(key);
        } catch (Exception e) {
            e.printStackTrace();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
            return null;
        }
    }


    /**
     * 将value转化为JSON字符串
     *
     * @param value
     * @return
     */
    private String toJson(Object value) {
        if (value instanceof Integer || value instanceof Long || value instanceof Float || value instanceof Double || value instanceof Boolean ||
                value instanceof String) {
            return String.valueOf(value);
        }
        return JSON.toJSONString(value);
    }

    /**
     * 将JSON字符串转化为Object对象
     *
     * @param Json
     * @param zlass
     * @param <T>
     * @return
     */
    private <T> T fromJson(String Json, Class<T> zlass) {
        if (StringUtils.isBlank(Json)) {
            return null;
        }
        return JSON.parseObject(Json, zlass);
    }

}
