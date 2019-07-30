package com.enterprise.base.common;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.enums.ResultCodeEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * ResultJson
 *
 * @author shisan
 * @create 2017-07-20 上午11:51
 **/
public class ResultJson {

    /**
     * 结果码
     */
    private String result;

    /**
     * 消息
     */
    private String msg = "";

    private Map<String, Object> data;

    public ResultJson() {
    }

    public ResultJson(String result) {
        this.result = result;
    }

    public static JSONObject succResultJson(String msg, Map<String, Object> data) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", ResultCodeEnum.SUCCESS.getValue().toString());
        jsonObject.put("msg", msg);
        jsonObject.put("data", data);

        return jsonObject;
    }

    public static JSONObject succResultJson(Map<String, Object> data) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", ResultCodeEnum.SUCCESS.getValue().toString());
        jsonObject.put("msg", "");
        jsonObject.put("data", data);


        return jsonObject;
    }

    public static JSONObject succResultJson(String key, Object value) {
        Map<String, Object> data = new HashMap<>();
        data.put(key, value);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", ResultCodeEnum.SUCCESS.getValue().toString());
        jsonObject.put("msg", "");
        jsonObject.put("data", data);
        return jsonObject;
    }

    public static JSONObject errorResultJson(String msg) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", ResultCodeEnum.FAIL.getValue());
        jsonObject.put("msg", msg);
        jsonObject.put("data", new HashMap<>());
        return jsonObject;
    }

    public static JSONObject errorResultJson(Object obj) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", ResultCodeEnum.FAIL.getValue());
        jsonObject.put("msg", "error");
        jsonObject.put("data", obj);
        return jsonObject;
    }

    public static JSONObject finishResultJson(String msg) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", ResultCodeEnum.KICKED_OUT.getValue());
        jsonObject.put("msg", msg);
        jsonObject.put("data", new HashMap<>());
        return jsonObject;
    }

//    public static JSONObject errorResultJson(Map<String, Object> data) {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("result", ResultCodeEnum.FAIL.getValue().toString());
//        jsonObject.put("msg", "");
//        jsonObject.put("data", data);
//        return jsonObject;
//    }

    public static JSONObject succResultJson(Integer result,Object value) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", result);
        jsonObject.put("msg", "ok");
        jsonObject.put("data", value);
        return jsonObject;
    }

    public static JSONObject finishResultJson(Integer result,String message,Object value) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", result);
        jsonObject.put("msg", message);
        jsonObject.put("data", value);
        return jsonObject;
    }

    public static JSONObject succResultJson(Object value) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", ResultCodeEnum.SUCCESS.getValue().toString());
        jsonObject.put("msg", "ok");
        jsonObject.put("data", value);
        return jsonObject;
    }

    public static JSONObject finishResultJson(Object value) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", ResultCodeEnum.KICKED_OUT.getValue().toString());
        jsonObject.put("msg", "ok");
        jsonObject.put("data", value);
        return jsonObject;
    }

    public static JSONObject failResultJson(Object value) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", ResultCodeEnum.FAIL.getValue().toString());
        jsonObject.put("msg", "fail");
        jsonObject.put("data", value);
        return jsonObject;
    }

    public static JSONObject kickoutResultJson(Object value) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", ResultCodeEnum.KICKED_OUT.getValue().toString());
        jsonObject.put("msg", "fail");
        jsonObject.put("data", value);
        return jsonObject;
    }

    public static JSONObject errorResultJson(String msg, Object value) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", ResultCodeEnum.FAIL.getValue().toString());
        jsonObject.put("msg", msg);
        jsonObject.put("data", value);
        return jsonObject;
    }


    public static JSONObject noPermissionResultJson(String msg) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", ResultCodeEnum.NO_PERMISSION.getValue());
        jsonObject.put("msg", msg);
        jsonObject.put("data", new HashMap<>());
        return jsonObject;
    }

    public static JSONObject kickedutResultJson(String msg) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", ResultCodeEnum.KICKED_OUT.getValue());
        jsonObject.put("msg", msg);
        jsonObject.put("data", new HashMap<>());
        return jsonObject;
    }

    public static JSONObject busyResponseResultJson(String msg) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", ResultCodeEnum.BUSY_RESPONSE.getValue());
        jsonObject.put("msg", msg);
        jsonObject.put("data", new HashMap<>());
        return jsonObject;
    }

    public static JSONObject overloadResponseResultJson(String msg) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", ResultCodeEnum.OVERLOAD.getValue());
        jsonObject.put("msg", msg);
        jsonObject.put("data", new HashMap<>());
        return jsonObject;
    }

    /**
     * crm未登录时返回信息
     *
     * @param msg 返回消息
     * @author shisan
     * @date 2017/10/11 下午7:37
     */
    public static JSONObject hasLogin(String msg) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", ResultCodeEnum.NOT_LOGIN.getValue());
        jsonObject.put("msg", msg);
        jsonObject.put("data", new HashMap<>());
        return jsonObject;
    }

    /**
     * 未填写手机号
     *
     * @param msg 返回消息
     * @author shisan
     * @date 2017/10/11 下午7:37
     */
    public static JSONObject hasMobile(String msg, Integer code) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", code);
        jsonObject.put("msg", msg);
        jsonObject.put("data", new HashMap<>());
        return jsonObject;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public static void main(String[] args) {
//        System.out.println(succResultJson(1).toJSONString());
//        System.out.println(errorResultJson("1").toJSONString());
        System.out.println(succResultJson("同步成功").toJSONString());
    }
}
