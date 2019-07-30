package com.enterprise.util.dingtalk;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
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
     * 对list类型进行解析
     * 
     * @param <T>
     * @param json
     * @param cls
     * @return
     */
    public static <T> List<T> getListObj(String json, Class<T> cls) {
        List<T> list = null;
        if (!TextUtils.isEmpty(json)) {
            try {
                list = JSON.parseArray(json, cls);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 对MapString类型数据进行解析
     * 
     * @param json
     * @return
     */
    public static Map<String, String> getMapStr(String json) {
        Map<String, String> mapStr = null;
        if (!TextUtils.isEmpty(json)) {
            try {
                mapStr = JSON.parseObject(json, new TypeReference<Map<String, String>>() {
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mapStr;
    }
    /**
     * 对MapInteger类型数据进行解析
     * 
     * @param json
     * @return
     */
    public static Map<Integer, Integer> getMapInt(String json) {
        Map<Integer, Integer> mapStr = null;
        if (!TextUtils.isEmpty(json)) {
            try {
                mapStr = JSON.parseObject(json, new TypeReference<Map<Integer, Integer>>() {
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mapStr;
    }
    /**
     * 对MapObject类型数据进行解析
     * 
     * @param json
     * @return
     */
    public static Map<String, Object> getMapObj(String json) {
        Map<String, Object> mapStr = null;
        if (!TextUtils.isEmpty(json)) {
            try {
                mapStr = JSON.parseObject(json, new TypeReference<Map<String, Object>>() {
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mapStr;
    }

    /**
     * 对list map obj类型进行解析
     * 
     * @param json
     * @return
     */
    public static List<Map<String, Object>> getListMap(String json) {
        List<Map<String, Object>> list = null;
        if (!TextUtils.isEmpty(json)) {
            try {
                list = JSON.parseObject(json, new TypeReference<List<Map<String, Object>>>() {
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 对list map String类型进行解析
     * 
     * @param json
     * @return
     */
    public static List<Map<String, String>> getListMapStr(String json) {
        List<Map<String, String>> list = null;
        if (!TextUtils.isEmpty(json)) {
            try {
                list = JSON.parseObject(json, new TypeReference<List<Map<String, String>>>() {
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }


    /**
     * @param  map  url中的参数key 和 value
     * @return url和相关参数
     */
    public static  String appendJsonParamsToUrl(String url ,JSONObject map){
        StringBuffer stringBuffer = new StringBuffer(url);
        if(map==null || map.size()==0){
            return url;
        }
        for(Map.Entry<String,Object> entry : map.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue()==null?"":(String)entry.getValue();
            stringBuffer.append("&").append(key).append("=").append(value);

        }
        return stringBuffer.toString();
    }

    /**
     * 将list等转为json
     * @Param
     * */
    public static <T> String serialize(T object) {
        return JSON.toJSONString(object);
    }
}
