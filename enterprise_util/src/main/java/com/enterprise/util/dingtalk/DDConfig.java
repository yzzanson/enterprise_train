package com.enterprise.util.dingtalk;

/**
 * 此类只存放公共的url
 *
 * @Author shisan
 * @Date 2018/3/27 下午3:06
 */
public class DDConfig {

    public static final String OAPI_HOST = "https://oapi.dingtalk.com";

    // 套件accesstoken
    public static final String GET_SUITE_TOKEN = OAPI_HOST + "/service/get_suite_token?1=1";

    //授权激活套件
    public static final String GET_ACTIVATE_SUITE = OAPI_HOST + "/service/activate_suite?1=1";

    // 企业永久码
    public static final String GET_PERMANENT_CODE = OAPI_HOST + "/service/get_permanent_code?1=1";

    // 授权企业的accesstoken
    public static final String GET_CORP_TOKEN = OAPI_HOST + "/service/get_corp_token?1=1";

    public static final String GET_CORP_ACCESSTOKEN = OAPI_HOST + "/gettoken?1=1";

    // 获取授权企业的详细信息
    public static final String GET_AUTH_INFO = OAPI_HOST + "/service/get_auth_info?1=1";

    //设置可以打电话的用户
    public static final String SET_USERLIST = OAPI_HOST + "/topapi/call/setuserlist?1=1";

    //发起电话
    public static final String RAISE_CALL = OAPI_HOST + "/topapi/call/calluser?1=1";


    //获取用于js签名的ticket信息
    public static final String GET_CORP_JSAPI_TICKET = OAPI_HOST + "/get_jsapi_ticket?1=1";

    // 获取成员详情
    public static final String GET_USER_DETAILS_URL = OAPI_HOST + "/user/get?1=1";

    // 通过CODE换取用户身份
    public static final String GET_USER_INFO_URL = OAPI_HOST + "/user/getuserinfo?1=1";

    //获取授权用户详情
    public static final String GET_AUTHENTICATED_USER_DETAILS_URL = OAPI_HOST + "/auth/scopes?1=1";

    //发送OA消息url
    public static final String SEND_OA_MSG_URL = "https://oapi.dingtalk.com/message/send";

    public static final String CREATE_SUITE_KEY = "suite4xxxxxxxxxxxxxxx";


}
