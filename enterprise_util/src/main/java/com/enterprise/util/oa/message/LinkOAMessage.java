package com.enterprise.util.oa.message;

import com.alibaba.fastjson.JSONObject;

/**
 * 带链接OA消息体
 *
 * @author shisan
 * @create 2018-03-09 下午1:58
 **/
public class LinkOAMessage {


    /**
     ***********************************************************************************
     * ********************************以下为各对象定义**************************************
     * */

    /**
     * 链接消息格式
     *  "link": {
     "messageUrl": "http://s.dingtalk.com/market/dingtalk/error_code.php",
     "picUrl":"@lALOACZwe2Rk",
     "title": "测试",
     "text": "测试"
     }
     */
    /**
     * 消息点击链接地址
     */
    private String messageUrl;

    /**
     * 消息的头部标题 图片媒体文件id，可以调用上传媒体文件接口获取
     */
    private String picUrl;

    //消息标题
    private String title;

    //消息描述
    private String text;

    public String getMessageUrl() {
        return messageUrl;
    }

    public void setMessageUrl(String messageUrl) {
        this.messageUrl = messageUrl;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    /**
     * 将oamessage转成jsonObject
     *
     * @author shisan
     * @date 2018/3/9 下午2:36
     */
    public JSONObject toJsonObject() {
        return null;
    }

    /**
     * 组装OAMessage Json
     *
     * @param messageUrl 手机端打开连接
     * @param picUrl     图片
     * @param text       消息描述
     * @param title      标题
     * @author shisan
     * @date 2018/3/9 下午5:21
     */
    public LinkOAMessage getLinkOAMessage(String messageUrl, String picUrl, String text, String title) {
        LinkOAMessage oaMessage = new LinkOAMessage();
        oaMessage.setMessageUrl(messageUrl);
        oaMessage.setPicUrl(picUrl);
        oaMessage.setText(text);
        oaMessage.setTitle(title);
        return oaMessage;
    }


}
