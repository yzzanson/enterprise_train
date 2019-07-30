package com.enterprise.util.oa.message;

/**
 * 带链接OA消息体
 *
 * @author shisan
 * @create 2018-03-09 下午1:58
 **/
public class OAMessageActionCard {

    private String title;

    private String markdown;

    private String single_title;

    private String single_url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMarkdown() {
        return markdown;
    }

    public void setMarkdown(String markdown) {
        this.markdown = markdown;
    }

    public String getSingle_title() {
        return single_title;
    }

    public void setSingle_title(String single_title) {
        this.single_title = single_title;
    }

    public String getSingle_url() {
        return single_url;
    }

    public void setSingle_url(String single_url) {
        this.single_url = single_url;
    }

    public OAMessageActionCard(String title, String markdown, String single_title, String single_url) {
        this.title = title;
        this.markdown = markdown;
        this.single_title = single_title;
        this.single_url = single_url;
    }

    public static void main(String[] args) {
    }

}
