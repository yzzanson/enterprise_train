package com.enterprise.base.common;

import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/4/20 上午10:21
 */
public class GlobalConstant {

    //管理的corpId--商旅
    public static final String OPER_CORPID = "ding440a3cea1d4e70b135c2f4657eb6378f";

    // 设置cookie domian 让a,b两域名都能取的到cookie
    public static final String DOMAIN = "taofairy.com";
    public static final String DOMAIN_TEST = "neixuntest.forwe.store";
    public static final String DOMAIN_ONLINE = "neixun.forwe.store";

    public static final String MAIN_PAGE_URL = "/exam-app-v171";

    public static final String CHOOSE_LIBRARY = MAIN_PAGE_URL + "/?corp=%s#/path";

    //首页
    //"https://a.taofairy.com/exam-app/?corp=%s#/main";
    public static final String MAIN_PAGE = MAIN_PAGE_URL + "/?corp=%s#/main";

    public static final String INDEX_PAGE = MAIN_PAGE_URL + "/?corp=%s#/prop";

    //挑战擂台
    public static final String CHALLENGE_PAGE = MAIN_PAGE_URL + "/?corp=%s#/dare-pk-enter/%s/%s";

    //新版本页面
    public static final String NEWVERSION_PAGE = MAIN_PAGE_URL + "/?corp=%s#/oa-set";

    //填写开通云社区页面
    public static final String COMMUNITY_PAGE = MAIN_PAGE_URL + "/?corp=%s#/sign-up?userId=%s&companyId=%s&companyName=%s";

    public static final String NEWVERSION_PIC = "https://static.forwe.store/others/others-2018_07_23_112046.jpg";
    public static final String NEWVERSION_14_MAIN_PIC = "https://neixun.forwe.store/static/neixun/15411426695001.4.0content.jpg";

    //新版本发布头图片
    public static final String NEWVERSION_13PIC = "https://neixun.forwe.store/static/neixun/1538016291260OAhead.jpg";
    //1.4版本图片
    public static final String NEWVERSION_14PIC = "https://neixun.forwe.store/static/neixun/15411420895121.4.0head.jpg";
    //1.6.2
    public static final String NEWVERSION_162PIC = "https://neixun.forwe.store/static/neixun/1553150606905%E5%A4%B4%E5%9B%BE.jpg";

    //pk详情
    public static final String ARENA_PK_DETAIL = MAIN_PAGE_URL + "/?corp=%s#/dare-pk-info/%s";

    //分享页url
    public static final String QRCODE_PAGE = "/exam-manage/#/access/qrcode?url=%s";

    public static final String ONLINE_VERSION = "V1.2.1";
    public static final String TEST_VERSION = "V1.2.1-SNAPSOT";

    public static final String MANAGE_ROLE_NAME = "超级管理员";

    public static final String APPID_ONLINE = "5321";
    public static final String APPID_TEST = "5372";

    public static final String OA_BURY_TYPE = "?oaInfo=";

    public static final String WATERMARK_FILEPATH = "1543202298376logo.png";

    public static final String DEFAULT_VERSION = "免费版";

    public static final Integer DEFAULT_FREE_TIME = 9999;

    //部署时可能要直接写死
    public static Integer modelType = null;
    static {
        try {
            //读取配置文件中的值来决定环境
            Properties properties = PropertiesLoaderUtils.loadAllProperties("modelType.properties");
            modelType = 2;//Integer.valueOf(properties.get("modelType").toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getDomain(){
        if(modelType == 1){
            return "forwe.store";// 钉钉云测试
        }else if(modelType == 2){
            return "forwe.store";// 钉钉云正式
        }else if(modelType == 0){
            return "https://yfl2.taofairy.com";
        }else if(modelType == 3){
            return "https://b.taofairy.com:9988";
        }
        return "https://neixuntest.forwe.store";
    }

    /**
     * 0 dev
     * 1 正式
     * 2 yfl2
     * */
    public static String getGateWay(){
        if(modelType == 1){
            return "https://neixun.forwe.store/test";// 钉钉云测试
        }else if(modelType == 2){
//            return "http://120.77.209.222";
            return "https://neixun.forwe.store";// 钉钉云正式
        }else if(modelType == 0){
            return "https://yfl2.tpaofairy.com";
        }else if(modelType == 3){
            return "https://b.taofairy.com:9988";
        }
        return "GateWay类型无效";
    }

    /**
     * 0 dev
     * 1 正式
     * 2 yfl2
     * */
    public static String getGateWay2(){
        if(modelType == 1){
            return "https://neixun.forwe.store";// 钉钉云测试
        }else if(modelType == 2){
//            return "http://120.77.209.222";
            return "https://neixun.forwe.store";// 钉钉云正式
        }else if(modelType == 0){
            return "https://yfl2.tpaofairy.com";
        }else if(modelType == 3){
            return "https://b.taofairy.com:9988";
        }
        return "GateWay类型无效";
    }
    /**
     * 0 dev
     * 1 正式
     * 2 yfl2
     * */
    public static String getGateWay3(){
        if(modelType == 1){
            return "https://neixun.forwe.store/web/test";// 钉钉云测试
        }else if(modelType == 2){
//            return "http://120.77.209.222";
            return "https://neixun.forwe.store";// 钉钉云正式
        }else if(modelType == 0){
            return "https://yfl2.tpaofairy.com";
        }else if(modelType == 3){
            return "https://b.taofairy.com:9988";
        }
        return "GateWay类型无效";
    }


    public static String getChooseLibraryUrl(){
        if(modelType == 1){
            return "https://neixun.forwe.store/web/test"+CHOOSE_LIBRARY;// 钉钉云测试
        }else if(modelType == 2){
//            return "http://120.77.209.222";
            return "https://neixun.forwe.store"+CHOOSE_LIBRARY;// 钉钉云正式
        }else if(modelType == 0){
            return "https://yfl2.taofairy.com"+CHOOSE_LIBRARY;
        }else if(modelType == 3){
            return "https://b.taofairy.com:9988"+CHOOSE_LIBRARY;
        }else{
            return "https://neixuntest.forwe.store"+CHOOSE_LIBRARY;// 钉钉云
        }
    }



    public static String getCommunityPage(){
        if(modelType == 1){
            return "https://neixun.forwe.store/web/test"+COMMUNITY_PAGE;// 钉钉云测试
        }else if(modelType == 2){
//            return "http://120.77.209.222";
            return "https://neixun.forwe.store"+COMMUNITY_PAGE;// 钉钉云正式
        }else if(modelType == 0){
            return "https://yfl2.taofairy.com"+COMMUNITY_PAGE;
        }else if(modelType == 3){
            return "https://b.taofairy.com:9988"+COMMUNITY_PAGE;
        }else{
            return "https://neixuntest.forwe.store"+COMMUNITY_PAGE;// 钉钉云
        }
    }

    public static String getIndexUrl(){
        if(modelType == 1){
            return "https://neixun.forwe.store/web/test"+MAIN_PAGE;// 钉钉云测试
        }else if(modelType == 2){
//            return "http://120.77.209.222";
            return "https://neixun.forwe.store"+MAIN_PAGE;// 钉钉云正式
        }else if(modelType == 0){
            return "https://yfl2.taofairy.com"+MAIN_PAGE;
        }else if(modelType == 3){
            return "https://b.taofairy.com:9988"+MAIN_PAGE;
        }else{
            return "https://neixuntest.forwe.store"+MAIN_PAGE;// 钉钉云
        }
    }

    public static String getUserIndexUrl(){
        if(modelType == 1){
            return "https://neixun.forwe.store/web/test"+INDEX_PAGE;// 钉钉云测试
        }else if(modelType == 2){
//            return "http://120.77.209.222";
            return "https://neixun.forwe.store"+INDEX_PAGE;// 钉钉云正式
        }else if(modelType == 0){
            return "https://yfl2.taofairy.com"+INDEX_PAGE;
        }else if(modelType == 3){
            return "https://b.taofairy.com:9988"+INDEX_PAGE;
        }else{
            return "https://neixuntest.forwe.store"+INDEX_PAGE;// 钉钉云
        }
    }

    public static String getArenaPKUrl(){
        if(modelType == 1){
            return "https://neixun.forwe.store/web/test"+CHALLENGE_PAGE;//  钉钉云测试
        }else if(modelType == 2){
//            return "http://120.77.209.222";
            return "https://neixun.forwe.store"+CHALLENGE_PAGE;//  钉钉云正式
        }else if(modelType == 0){
            return "https://yfl2.taofairy.com"+CHALLENGE_PAGE;
        }else if(modelType == 3){
            return "https://b.taofairy.com:9988"+CHALLENGE_PAGE;
        }else{
            return "https://neixuntest.forwe.store"+CHALLENGE_PAGE;// 钉钉云
        }
    }

    public static String getArenaDetailUrl(){
        if(modelType == 1){
            return "https://neixun.forwe.store/web/test"+ARENA_PK_DETAIL;// 钉钉云测试
        }else if(modelType == 2){
//            return "http://120.77.209.222";
            return "https://neixun.forwe.store"+ARENA_PK_DETAIL;// 钉钉云正式
        }else if(modelType == 0){
            return "https://yfl2.taofairy.com"+ARENA_PK_DETAIL;
        }else if(modelType == 3){
            return "https://b.taofairy.com:9988"+ARENA_PK_DETAIL;
        }else{
            return "https://neixuntest.forwe.store"+ARENA_PK_DETAIL;// 钉钉云
        }
    }

    public static String getQrCOdePage(){
        if(modelType == 1){
            return "https://neixun.forwe.store/web/test"+QRCODE_PAGE;// 钉钉云测试
        }else if(modelType == 2){
//            return "http://120.77.209.222";
            return "https://neixun.forwe.store"+QRCODE_PAGE;// 钉钉云正式
        }else if(modelType == 0){
            return "https://yfl2.taofairy.com"+QRCODE_PAGE;
        }else if(modelType == 3){
            return "https://b.taofairy.com:9988"+QRCODE_PAGE;
        }else{
            return "https://neixuntest.forwe.store"+QRCODE_PAGE;// 钉钉云
        }
    }

    public static String getSystemVersion(){
        if(modelType == 1){
            return TEST_VERSION;
        }else if(modelType == 2){
//            return "http://120.77.209.222";
            return ONLINE_VERSION;// domian顺便也改
        }else if(modelType == 0){
            return TEST_VERSION;
        }else if(modelType == 3){
            return TEST_VERSION;
        }else{
            return TEST_VERSION;
        }
    }

    public static String getPicFilePath(){
        if(modelType == 1){  //neixuntest
            return "/home/www/tomcat_dev/pic_dir/neixun/";
        }else if(modelType == 2){ //neixun
            return "/home/www/tomcat_online/pic_dir/neixun/";
        }else if(modelType == 0){ //yfl2
            return "/alidata/www/tomcat_dev/pci_dir/neixun/";
        }else if(modelType == 3){ //local
            return "/Users/zezhouyang/Desktop/apache-tomcat-8.5.5/pic_dir/neixun/";
        }else{                    //neixuntest
            return "/home/www/tomcat_dev/pic_dir/neixun/";
        }
    }

    public static String getOperCorpId(){
        return OPER_CORPID;
    }

    public static String getManageRoleName(){
        return MANAGE_ROLE_NAME;
    }


    public static String getNewVersionPage(){
        if(modelType == 1){
            return "https://neixun.forwe.store/web/test"+NEWVERSION_PAGE;//  钉钉云测试
        }else if(modelType == 2){
//            return "http://120.77.209.222";
            return "https://neixun.forwe.store"+NEWVERSION_PAGE;//  钉钉云正式
        }else if(modelType == 0){
            return "https://neixuntest.forwe.store"+NEWVERSION_PAGE;
        }else if(modelType == 3){
            return "https://neixuntest.forwe.store"+NEWVERSION_PAGE;
        }else{
            return "https://neixuntest.forwe.store"+NEWVERSION_PAGE;// 钉钉云
        }
    }

    public static String getAppId(){
        if(modelType == 1){
            return APPID_TEST;// 钉钉云测试
        }else if(modelType == 2){
            return APPID_ONLINE;// 钉钉云正式
        }else if(modelType == 0){
            return APPID_TEST;
        }else if(modelType == 3){
            return APPID_TEST;
        }
        return APPID_TEST;
    }

    public static String getRootPath(){
        if(modelType == 1){
            return "/home/www/tomcat_dev/pic_dir/neixun/";// 钉钉云测试
        }else if(modelType == 2){
            return "/home/www/tomcat_online/pic_dir/neixun/";// 钉钉云正式
        }else if(modelType == 0){
            return "/";
        }else if(modelType == 3){
            return "/Users/zezhouyang/Desktop/apache-tomcat-8.5.15/pic_dir/neixun/";
        }
        return "/home/www/tomcat_online/pic_dir/neixun/";
    }


}
