package com.enterprise.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * json工具类
 *
 * @author Robin 2014年7月14日 上午11:23:21
 */
public class JSONUtil {

    protected static Logger jSONUtil = LoggerFactory.getLogger(JSONUtil.class);

    /**
     * 根据一个对象输出为json格式字符串
     *
     * @param value
     * @return
     */
    public static String object2JSON(Object value) {
        String str = JSON.toJSONString(value);
        return str;
    }

    /**
     * 根据json字符串生成一个java对象
     *
     * @param jsonText
     * @param clazz
     * @return
     */
    public static <T> T json2Object(String jsonText, Class<T> clazz) {
        return JSON.parseObject(jsonText, clazz);
    }

    /**
     * 对单个javabean进行解析
     *
     * @param <T>
     * @param json 要解析的json字符串
     * @param cls
     * @return
     */
    public static <T> T getObj(String json, Class<T> cls) {
        T t = null;
        try {
            t = JSON.parseObject(json, cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }



    /**
     * @param map url中的参数key 和 value
     * @return url和相关参数
     */
    public static String appendJsonParamsToUrl(String url, JSONObject map) {
        StringBuffer stringBuffer = new StringBuffer(url);
        if (map == null || map.size() == 0) {
            return url;
        }
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue() == null ? "" : (String) entry.getValue();
            stringBuffer.append("&").append(key).append("=").append(value);

        }
        return stringBuffer.toString();
    }

}
