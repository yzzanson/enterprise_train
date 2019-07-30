package com.enterprise.service.redis;


import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by pine on 17/4/19.
 */
public interface RedisService {

    boolean setnx(String key, String value, long time);

    boolean setnx(String key, String value);

    void releasenx(String key);

    Object getnx(String key);

    void expirenx(String key, long time);

    //=============================common============================

    boolean expire(String key, long time);

    long getExpire(String key);

    boolean exists(String key);

    void del(String... key);

    void delBatch(String keyStartWith);
    //============================String=============================

    Object getObj(String key);

    boolean set(String key, Object value);

    String get(String key);

    boolean set(String key, long extime, String content);

    boolean set(String key, Object value, long time);

    long incr(String key, long delta);

    long decr(String key, long delta);

    //==============================Object====================+===========

    Object getSerializeObj(String key);

    boolean setSerialize(String key, Object value);

    boolean setSerialize(String key, long extime, Object content);

    boolean setSerialize(String key, Object value, long time);


    //================================Map=================================

    Object hget(String key, String item);

    Map<Object, Object> hmget(String key);

    boolean hmset(String key, Map<String, Object> map);

    boolean hmset(String key, Map<String, Object> map, long time);

    boolean hset(String key, String item, Object value);

    boolean hset(String key, String item, Object value, long time);

    void hdel(String key, Object... item);

    boolean hHasKey(String key, String item);

    double hincr(String key, String item, double by);

    double hdecr(String key, String item, double by);

    //============================set=============================

    Set<Object> sGet(String key);

    Object sGetOne(String key);

    boolean sHasKey(String key, Object value);

    long sSet(String key, Object... values);

    long sSetAndTime(String key, long time, Object... values);

    long sGetSetSize(String key);

    long setRemove(String key, Object... values);
    //===============================list=================================

    List<Object> lGet(String key, long start, long end);

    long lGetListSize(String key);

    Object lGetIndex(String key, long index);

    boolean lSet(String key, Object value);

    boolean lSet(String key, Object value, long time);

    boolean lSet(String key, List<Object> value);

    boolean lSet(String key, List<Object> value, long time);

    boolean lUpdateIndex(String key, long index, Object value);

    long lRemove(String key, long count, Object value);

    void leftPush(String key, Object value);

    Long size(String key);

    Object rightPop(String key);
}