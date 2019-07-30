package com.enterprise.util.oa.message;


/**
 * @Description 简单文本消息
 * @Author shisan
 * @Date 2018/3/9 下午1:55
 */
public class TextMessage {

    /**
     * 消息内容
     */
    private String content;

    public TextMessage() {
    }

    public TextMessage(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
