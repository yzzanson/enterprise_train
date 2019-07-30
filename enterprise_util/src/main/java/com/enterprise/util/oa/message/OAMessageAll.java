package com.enterprise.util.oa.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.enterprise.util.AssertUtil;

import java.util.Map;

/**
 * 带链接OA消息体
 *
 * @author shisan
 * @create 2018-03-09 下午1:58
 **/
public class OAMessageAll {

    private String msgtype;

    private OA oa;

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public OA getOa() {
        return oa;
    }

    public void setOa(OA oa) {
        this.oa = oa;
    }

    public class OA{

        /**
         * 客户端点击消息时跳转到的H5地址
         */
        private String message_url;

        /**
         * PC端点击消息时跳转到的H5地址
         * (非必填项)
         */
        private String pc_message_url;

        /**
         * 消息头部内容,格式:{"bgcolor":"FFBBBBBB", "text":"头部标题"}
         */
        private Head head;

        /**
         * 消息体
         */
        private JSONObject body;

        public String getMessage_url() {
            return message_url;
        }

        public void setMessage_url(String message_url) {
            this.message_url = message_url;
        }

        public String getPc_message_url() {
            return pc_message_url;
        }

        public void setPc_message_url(String pc_message_url) {
            this.pc_message_url = pc_message_url;
        }

        public Head getHead() {
            return head;
        }

        public void setHead(Head head) {
            this.head = head;
        }

        public JSONObject getBody() {
            return body;
        }

        public void setBody(JSONObject body) {
            this.body = body;
        }


    }






    /**
     ***********************************************************************************
     * ********************************以下为各对象定义**************************************
     * */

    /**
     * 消息头部内容,格式:{"bgcolor":"FFBBBBBB", "text":"头部标题"}
     */
    public class Head {
        /**
         * 消息头部的背景颜色。长度限制为8个英文字符，其中前2为表示透明度，后6位表示颜色值。不要添加0x
         */
        private String bgcolor;

        /**
         * 消息的头部标题（向普通会话发送时有效，向企业会话发送时会被替换为微应用的名字），长度限制为最多10个字符
         */
        private String text;

        public String getBgcolor() {
            return bgcolor;
        }

        public void setBgcolor(String bgcolor) {
            this.bgcolor = bgcolor;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }


    /**
     * 消息体,格式:{"title":"","form":[{key:value},{key:value}...],"rich":{"num":12.3,"unit":"元"},"content":"内容","image":"消息体中的图片media_id","file_count":"3","author":"作者"}
     */
    public class Body {
        /**
         * 消息体的标题(非必填项)
         */
        private String title;
        /**
         * 消息体的表单，最多显示6个，超过会被隐藏(非必填项)
         */
        private JSONArray form;

        /**
         * 单行富文本信息(非必填项)
         */
        private Rich rich;

        /**
         * 消息体的内容，最多显示3行(非必填项)
         */
        private String content;
        /**
         * 消息体中的图片media_id(非必填项)
         */
        private String image;
        /**
         * 自定义的附件数目。此数字仅供显示，钉钉不作验证(非必填项)
         */
        private String file_count;
        /**
         * 自定义的作者名字(非必填项)
         */
        private String author;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public JSONArray getForm() {
            return form;
        }

        public void setForm(JSONArray form) {
            this.form = form;
        }

        public void setForm(Map<String, Object> map) {
            JSONArray array = new JSONArray();
            JSONObject o = null;
            if (map != null) {
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    o = new JSONObject();
                    o.put("key", entry.getKey());
                    o.put("value", entry.getValue());
                    array.add(o);
                }
            }
            this.form = array;
        }

        public Rich getRich() {
            return rich;
        }

        public void setRich(Rich rich) {
            this.rich = rich;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getFile_count() {
            return file_count;
        }

        public void setFile_count(String file_count) {
            this.file_count = file_count;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }
    }

    /**
     * 单行富文本信息(非必填项)
     *
     * @Author shisan
     * @Date 2018/3/9 下午2:31
     */
    public class Rich {
        /**
         * 单行富文本信息的数目(非必填项)
         */
        private String num;
        /**
         * 单行富文本信息的单位(非必填项)
         */
        private String unit;

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }
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
     * @param messageUrl   手机端打开连接
     * @param pcMessageUrl pc端打开连接
     * @param map          消息体
     * @author shisan
     * @date 2018/3/9 下午5:21
     */
    public OAMessageAll getOAMessage(String messageType,String messageUrl, String pcMessageUrl, Map<String, Object> map) {
        OAMessageAll oaMessage = new OAMessageAll();
        OAMessageAll.OA oa = oaMessage.new OA();
        oa.setMessage_url(messageUrl);
        oa.setPc_message_url(pcMessageUrl);
        OAMessageAll.Body body = oaMessage.new Body();
        body.setForm(map);
        oa.setBody(JSON.parseObject(JSONObject.toJSONString(body)));
        oaMessage.setMsgtype(messageType);
        oaMessage.setOa(oa);
        return oaMessage;
    }


    /**
     * 组装OAMessage Json
     *
     * @param messageUrl   手机端打开连接
     * @param pcMessageUrl pc端打开连接
     * @param title        标题
     * @param image        图片
     * @author shisan
     * @date 2018/3/9 下午5:21
     */
    public OAMessageAll getOAMessageWithPic(String messageType,String messageUrl, String pcMessageUrl, String title,String content,String image) {
        OAMessageAll oaMessage = new OAMessageAll();
        oaMessage.setMsgtype(messageType);
        OAMessageAll.OA oa = oaMessage.new OA();
        oa.setMessage_url(messageUrl);
        oa.setPc_message_url(pcMessageUrl);

        OAMessageAll.Body body = oaMessage.new Body();
        body.setTitle(title);
        body.setImage(image);
        body.setContent(content);
        oa.setBody(JSON.parseObject(JSONObject.toJSONString(body)));
        oaMessage.setOa(oa);
        return oaMessage;
    }

    /**
     * 组装OAMessage Json
     *
     * @param messageUrl   手机端打开连接
     * @param pcMessageUrl pc端打开连接
     * @param title        标题
     * @param image        图片
     * @author shisan
     * @date 2018/3/9 下午5:21
     */
    public OAMessageAll getOAMessageWithPic(String messageUrl, String pcMessageUrl, String title,String content,String image) {
        OAMessageAll oaMessage = new OAMessageAll();
        OAMessageAll.OA oa = oaMessage.new OA();
        oa.setMessage_url(messageUrl);
        oa.setPc_message_url(pcMessageUrl);

        OAMessageAll.Body body = oaMessage.new Body();
        body.setTitle(title);
        body.setImage(image);
        body.setContent(content);
        oa.setBody(JSON.parseObject(JSONObject.toJSONString(body)));
        oaMessage.setOa(oa);
        return oaMessage;
    }

    /**
     * 组装OAMessage Json
     *
     * @param head OA消息头
     * @param body OA消息体
     * @author shisan
     * @date 2018/3/9 下午5:21
     */
    public OAMessageAll getOAMessage(Head head, Body body, Rich rich) {
        AssertUtil.notNull(body, "body不能为空!");
        OAMessageAll oaMessage = new OAMessageAll();
        OAMessageAll.OA oa = oaMessage.new OA();
        oa.setHead(head);
        body.setRich(rich);
        oa.setBody(JSON.parseObject(JSONObject.toJSONString(body)));
        return this;
    }

}
