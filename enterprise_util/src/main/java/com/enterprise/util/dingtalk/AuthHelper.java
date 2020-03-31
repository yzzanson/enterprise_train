package com.enterprise.util.dingtalk;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.DDConstant;
import com.enterprise.base.entity.SuitesEntity;
import com.enterprise.util.HttpUtil;
import com.enterprise.util.JSONUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Formatter;

/**
 * AccessToken和jsticket的获取封装
 */
public class AuthHelper {

    private static Logger logger = LoggerFactory.getLogger(AuthHelper.class);

    /**
     * 签名
     *
     * @author shisan
     * @date 2017/10/23 上午11:34
     */
    public static String sign(String ticket, String nonceStr, long timeStamp, String url) {
        String plain = "jsapi_ticket=" + ticket + "&noncestr=" + nonceStr + "&timestamp=" + String.valueOf(timeStamp) + "&url=" + url;
        MessageDigest sha1 = null;
        try {
            sha1 = MessageDigest.getInstance("SHA-1");
            sha1.reset();
            sha1.update(plain.getBytes("UTF-8"));
        } catch (Exception e) {
            logger.error("签名失败,", e);
        }

        return bytesToHex(sha1.digest());
    }

    private static String bytesToHex(byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }


    /**
     * 计算当前请求的jsapi的签名数据<br/>
     * <p>
     * 如果签名数据是通过ajax异步请求的话，签名计算中的url必须是给用户展示页面的url
     *
     * @param request
     * @return
     */
    public static String getConfig(HttpServletRequest request, String corpId, String agentId, String ticket, String currentUrl) {
        String nonceStr = "abcdefg";
        long timeStamp = System.currentTimeMillis() / 1000;
        String signature = AuthHelper.sign(ticket, nonceStr, timeStamp, currentUrl);

        JSONObject configValue = new JSONObject();
        configValue.put("jsticket", ticket);
        configValue.put("signature", signature);
        configValue.put("nonceStr", nonceStr);
        configValue.put("timeStamp", timeStamp);
        configValue.put("corpId", corpId);
        configValue.put("agentId", agentId);

        logger.info("用于前端请求的参数 configValue = " + configValue.toString());
        return configValue.toString();
    }

    /**
     * **************************************以下供Isv使用*************************************************
     * ***************************************************************************************************
     */

    /**
     * 获取套件的accesstoken
     *
     * @param suiteKey
     * @param suiteSecret
     * @param suiteTicket
     * @return
     */
    public static JSONObject getSuiteAccessToken(String suiteKey, String suiteSecret, String suiteTicket) {
        JSONObject p = new JSONObject();
        p.put("suite_key", suiteKey);
        p.put("suite_secret", suiteSecret);
        p.put("suite_ticket", suiteTicket);
        JSONObject object = HttpUtil.doPost(DDConfig.GET_SUITE_TOKEN, p);
        return object;
    }


    /**
     * 激活企业对应的套件
     *
     * @param suiteAccessToken
     * @param suiteKey
     * @param authCorpId
     */
    public static void getActivateSuite(String suiteAccessToken, String suiteKey, String authCorpId) {
        JSONObject p = new JSONObject();
        p.put("suite_access_token", suiteAccessToken);
        String url = JSONUtil.appendJsonParamsToUrl(DDConfig.GET_ACTIVATE_SUITE, p);

        p.clear();
        p.put("suite_key", suiteKey);
        p.put("auth_corpid", authCorpId);
        JSONObject object = HttpUtil.doPost(url, p);
        logger.info("套件激活结果 > " + object.toJSONString());
    }

    /**
     * 获取企业的accesstoken
     *
     * @param corpId
     * @param suiteToken
     * @param permanentcode
     * @return
     * @throws Exception
     */
    public static String getCorpAccessToken(String corpId, String suiteToken, String permanentcode) {

        JSONObject p = new JSONObject();
        p.put("suite_access_token", suiteToken);
        String url = JSONUtil.appendJsonParamsToUrl(DDConfig.GET_CORP_TOKEN, p);

        p.put("auth_corpid", corpId);
        p.put("permanent_code", permanentcode);
        JSONObject object = HttpUtil.doPost(url, p);

//        logger.info("企业的accesstoken 》 " + object.toJSONString() + ", 参数:" + p.toString());
        return object.getString("access_token");
    }

    /**
     * 获取企业的accesstoken
     *
     * @param appKey
     * @param appSecrect
     * @return
     * @throws Exception
     */
    public static String getAccessToken(String appKey, String appSecrect) {
        JSONObject p = new JSONObject();
        String url = JSONUtil.appendJsonParamsToUrl(DDConfig.GET_CORP_ACCESSTOKEN, p);
        p.put("appkey", appKey);
        p.put("appsecret", appSecrect);
        JSONObject object = HttpUtil.get(url, p);
        return object.getString("access_token");
    }


    /**
     * 获取企业的ticket
     *
     * @param accessToken
     * @return
     * @throws Exception
     */
    public static String getCorpTicket(String accessToken) {
        JSONObject p = new JSONObject();
        p.put("access_token", accessToken);
        String url = JSONUtil.appendJsonParamsToUrl(DDConfig.GET_CORP_JSAPI_TICKET, p);
        JSONObject object = HttpUtil.get(url);

        return object.getString("ticket");
    }

    /**
     * 注册钉钉回调
     * */
    public static JSONObject registerCallBack(String corpAccessToken,SuitesEntity suites,String registerUrl) throws IOException {
        JSONObject p = new JSONObject();
        p.put("access_token", corpAccessToken);
        String url = JSONUtil.appendJsonParamsToUrl(DDConstant.DING_CONTACTS_CALLBACK_URL, p);

        p.clear();
        String[] tags = {
                "user_add_org", "user_modify_org", "user_leave_org", "org_admin_add", "org_admin_remove",
                "org_dept_create", "org_dept_modify", "org_dept_remove", "org_remove"
        };
        p.put("call_back_tag", tags);
        p.put("token", suites.getToken());
        p.put("aes_key", suites.getEncodingAesKey());
        p.put("url", registerUrl);
        JSONObject object = HttpUtil.httpPost(url, p);
//        System.out.println(p.toJSONString());
        logger.info("注册事件回调 > " + object.toJSONString()+" 回调接口 》 " + registerUrl);
        return object;
    }

    public static String getAuthUserInfo(String suiteTicket, String suiteKey,String suiteSecrect,String corpId) {
        try {
            JSONObject p = new JSONObject();
            Date date = new Date();
            String suiteSignature = getSuiteSign(suiteTicket, suiteSecrect, date);
            p.put("signature", suiteSignature);
            p.put("timestamp", String.valueOf(date.getTime()));
            p.put("suiteTicket", suiteTicket);
            p.put("accessKey", suiteKey);
            String url = JSONUtil.appendJsonParamsToUrl(DDConfig.GET_AUTH_INFO, p);

            p.clear();
            p.put("auth_corpid", corpId);
            JSONObject object = HttpUtil.doPost(url, p);
            if (object.get("errcode").equals(0)) {
                String dingUserId = (String) JSONObject.parseObject(object.get("auth_user_info").toString()).get("userId");
//            System.out.println(dingUserId);
                return dingUserId;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    public static JSONObject getAuthInfo(String suiteTicket, String suiteKey,String suiteSecrect,String corpId) {
        try {
            JSONObject p = new JSONObject();
            Date date = new Date();
            String suiteSignature = getSuiteSign(suiteTicket, suiteSecrect, date);
            p.put("signature", suiteSignature);
            p.put("timestamp", String.valueOf(date.getTime()));
            p.put("suiteTicket", suiteTicket);
            p.put("accessKey", suiteKey);
            String url = JSONUtil.appendJsonParamsToUrl(DDConfig.GET_AUTH_INFO, p);

            p.clear();
            p.put("auth_corpid", corpId);
            JSONObject object = HttpUtil.doPost(url, p);
            logger.info("企业授权信息结果 > " + object.toJSONString());
            return object;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject setCallUserList(String suiteAccessToken,String userList){
        try {
            JSONObject p = new JSONObject();
            p.put("access_token", suiteAccessToken);
            String url = JSONUtil.appendJsonParamsToUrl(DDConfig.SET_USERLIST, p);
            p.clear();
            p.put("staff_id_list", userList);
            JSONObject object = HttpUtil.doPost(url, p);
            return object;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject raiseCallUserList(String suiteAccessToken,String staffid,String corpId,String authUserId){
        try {
            JSONObject p = new JSONObject();
            p.put("access_token", suiteAccessToken);
            String url = JSONUtil.appendJsonParamsToUrl(DDConfig.RAISE_CALL, p);
            p.clear();
            p.put("staff_id", staffid);
            p.put("authed_corp_id", corpId);
            p.put("authed_staff_id", authUserId);
            logger.info("发起电话 > " + p.toJSONString());
            JSONObject object = HttpUtil.doPost(url, p);
            return object;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    public static Integer checkCall(String accessToken){
        try {
            JSONObject p = new JSONObject();
            p.put("access_token", accessToken);
            String url = JSONUtil.appendJsonParamsToUrl(DDConfig.GET_AUTHENTICATED_USER_DETAILS_URL, p);
            JSONObject object = HttpUtil.get(url, p);
            logger.info("设置发起只能电话员工 > " + object.toJSONString());
            String conditionField = object.get("condition_field").toString();
            if(StringUtils.isNotEmpty(conditionField)){
                if(conditionField.contains("contact_call")){
                    return 1;
                }
                return 0;
            }else{
                return 0;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) {
        String suiteTicket = "ONkkIA2bqGUPvgzZq0TBnvjpl1ryM73zto4MaXV2GVuTcsOL2zNbXSFxit1d5M6qkuKXE2vlwX7fggqgPXIX9E";
        String suiteKey = "suiteejgaehk1pjpcrpip";
        String suiteSecrect = "7qtQ1i51bABnqwPdNgkcN9uLg0qEE2-0MDMgLiheYNzyUGvj84YCnrnfXs5orITn";

        String suiteAccessToken = "7135c11a9dd63d68a2b19e08c3a8dbcc";
        String corpId = "ding6f050412432e082d35c2f4657eb6378f";
        //拨打人
//        String staffId = "045122333326332477";
        String staffId = "0731543202-1606789160";
        //接听人
        String raiseUserId = "202554274226362672";

        String jsonResult = getAuthUserInfo(suiteTicket, suiteKey, suiteSecrect, corpId);
        System.out.println(jsonResult);

//        System.out.println("0::"+jsonResult.toJSONString());
//        String authUserInfo = jsonResult.get("auth_user_info").toString();
//        System.out.println("1::"+authUserInfo);
//        JSONObject authUserInfoJson = JSONObject.parseObject(authUserInfo);
//        System.out.println("2::"+authUserInfoJson.toString());
//        Object userDingId = authUserInfoJson.get("userId");
//        System.out.println("3::"+userDingId);
//        JSONObject result = setCallUserList(suiteAccessToken,"032261275826281866");
//        JSONObject result = setCallUserList("ceec6e31cdfa3f5b8f650ab43d802f2a","073154320226265834");
//        JSONObject result = setCallUserList("ceec6e31cdfa3f5b8f650ab43d802f2a","0731543202-1606789160");

//        丸子 蓝波 无式
//        JSONObject result = setCallUserList(suiteAccessToken,"0731543202-1606789160,045122333326332477,032261275826281866");
//        JSONObject result = setCallUserList(suiteAccessToken,"04512233331082277");
//        System.out.println(result.toJSONString());

//        System.out.println(checkCall("4a35230cd9423d3eb4fa27856fcb9131"));

        AuthHelper.raiseCallUserList(suiteAccessToken, staffId, corpId, raiseUserId);

//        String corpAccessToken = "";
//        //public SuitesEntity(Integer id, String suiteKey, String suiteSecret, String token, String encodingAesKey, String suiteAccessToken, Date suiteAccessTokenExpireTime, String description, String suiteTicket, String corpAppid, Integer status, Date createTime, Date updateTime) {
//        SuitesEntity suitesEntity = new SuitesEntity(1,"suitee7djrliftc2qilpq","9gV8TFIeMqMh2uTfPiYJr7qiiUWUoKkfHRM1PDqsWMKfJ0rjyFttv6XayKTm__IA","neixunMobile","hevmmyizjo7oos0sdwm8xb0jqwid8ngzcuh455eh9ce","9e4f030bc9b63df685f8037dc8e59982",new Date(),"每日精进测试","oaq3QZPFAtymLGiGz0Qaa7mXoNGn5KbdQCL7rs67qoIkDeUQWB7b0PsUBx7pehCqFRNgUWXMmCEJ1JXIjfWRhJ","5372",1,new Date(),new Date());
//        String registerCallBackUrl = "https://neixun.forwe.store/test/enterprise-web/suite/regist_callback?suiteId=1";
//        try {
//            registerCallBack("19f3a9f6959b378b97d2b021daf5dfd2",suitesEntity,registerCallBackUrl);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    private static String getSuiteSign(String suiteTicket,String suiteSecret,Date date){
        String stringToSign = date.getTime() +"\n"+suiteTicket;
        Mac mac = null;
        try {
            mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(suiteSecret.getBytes("UTF-8"), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
            return new String(Base64.encodeBase64(signData));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }


}