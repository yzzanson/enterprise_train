package com.enterprise.base.common;

/**
 * DDConstant
 *
 * @author shisan
 * @create 2018-03-26 下午2:29
 **/
public class DDConstant {

    //cookie 加密相关
    public static final String AES_KEY = "alsdjsowSksa@#!$";
    public static final String COOKIE_NAME = "cookie_name";//后台
    public static final String COOKIE_NAME_MOBILE = "mcookie_name";//前台
    public static final String USER_MOBILE = "mcookie_user";//前台用户
    public static final String USER_WEB = "wcookie_user";//前台用户
    public static final String USER_REQUEST_TOKEN = "token";//前台用户

    //钉钉授权相关字段
    public static final String CODE = "code";
    public static final String CORP_ID = "corpid";
    public static final String USER_ID = "userid";

    public static final String OPERATOR_CORP_ID = "ding0d0be1f4df9f899a35c2f4657eb6378f";
    public static final String OPERATOR_CORP_ID2 = "ding440a3cea1d4e70b135c2f4657eb6378f";
    public static final String OPERATOR_CORP_ID3 = "ding614949de72a0b8a2";

    public static final String ACCESS_TOKEN = "access_token";

    public static final String OAPI_HOST = "https://oapi.dingtalk.com";

    //发送OA消息
    public static final String SEND_OA_URL = OAPI_HOST+"/message/send?access_token=";

    public static final String SEND_OA_ASYNC_URL = OAPI_HOST+"/topapi/message/corpconversation/asyncsend_v2?access_token=";

    public static final String SEND_ALL_OA_URL = OAPI_HOST+"/message/corpconversation/asyncsend_v2?access_token=";

    public static final String SEND_ALL_OA_URL2 = OAPI_HOST+"/message/send_to_conversation?access_token=";


    public static final String IS_FETCH_CHILD = "fetch_child";

    public static String RECURSION_DEPT = "true";

    //获取部门列表
    public static final String GET_DEPARTMENT_LIST_URL = "https://oapi.dingtalk.com/department/list?1=1";

    //获取部门下用户列表
    public static final String GET_USER_LIST_URL = "https://oapi.dingtalk.com/user/list?1=1";

    //获取用户详情
    public static final String GET_MEMBER_DETAIL_URL = "https://oapi.dingtalk.com/user/get?1=1";

    //获取部门详情
    public static final String GET_DEPARTMENT_DETAIL = "https://oapi.dingtalk.com/department/get?1=1";

    //获取公司用户人数
    public static final String GET_ORG_USER_COUNT = "https://oapi.dingtalk.com/user/get_org_user_count?1=1";

    //发送安排学习时的OA图片信息
    public static final String ARRANGE_OA_PIC = "https://static.forwe.store/others/others-2018_05_18_140046.png";

    //默认发送安排学习时的文字
    public static final String ARRANGE_STUDY_DEFAULT_MESSAGE = "管理员已为你解锁了【%s】,快去看看吧!";//"新的知识库已解锁,快来挑战";

    //钉钉通讯录回调注册url
    // 注册事件回调接口
    public static final String DING_CONTACTS_CALLBACK_URL = OAPI_HOST +"/call_back/register_call_back?1=1";
    public static final String DING_CONTACTS_CALLBACK_URL_UPDATE = OAPI_HOST +"/call_back/update_call_back?1=1";

    //获取管理员url
    public static final String GET_CORP_MANAGE = "https://oapi.dingtalk.com/user/get_admin?1=1";

    public static final Integer MIN_LEVEL = 4;

//    public static final String ARRANGE_STUDY = "https://static.forwe.store/others/others-2018_05_18_140046.png";
    public static final String ARRANGE_STUDY = "https://neixun.forwe.store/static/neixun/1548070778680%E5%AE%89%E6%8E%92%E5%AD%A6%E4%B9%A0.png";

    public static final String NEW_LIB = "https://neixun.forwe.store/static/neixun/1548070825049%E6%96%B0%E7%9F%A5%E8%AF%86%E5%BA%93.png";

    public static final String INVITE_COMMUNITY = "%s邀请您开启学习社区";

    public static void main(String[] args) {
    }


}
