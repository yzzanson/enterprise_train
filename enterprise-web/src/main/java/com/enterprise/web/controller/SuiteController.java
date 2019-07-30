package com.enterprise.web.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.BaseController;
import com.enterprise.base.common.DDConstant;
import com.enterprise.base.common.GlobalConstant;
import com.enterprise.base.common.SpringContextHolder;
import com.enterprise.base.entity.IsvTicketsEntity;
import com.enterprise.base.entity.SuitesEntity;
import com.enterprise.base.enums.StatusEnum;
import com.enterprise.isv.utils.DingTalkEncryptException;
import com.enterprise.isv.utils.DingTalkEncryptor;
import com.enterprise.oa.OAMessageUtil;
import com.enterprise.service.department.DepartmentService;
import com.enterprise.service.isvTickets.IsvTicketsService;
import com.enterprise.service.suites.SuitesService;
import com.enterprise.service.user.UserService;
import com.enterprise.util.HttpUtil;
import com.enterprise.util.JSONUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/4/25 上午9:43
 */
@RequestMapping("/suite")
@Controller
public class SuiteController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private SuitesService suitesService;

    @Resource
    private IsvTicketsService ticketsService;

    @Resource
    private DepartmentService departmentService;

    @Resource
    private UserService userService;

    private static String GATEWAY_URL = GlobalConstant.getGateWay();

    private static String CALLBACK_REGISTER_URL = DDConstant.DING_CONTACTS_CALLBACK_URL;

    /**
     * user_add_org : 通讯录用户增加
     * user_modify_org : 通讯录用户更改
     * user_leave_org : 通讯录用户离职
     * org_admin_add ：通讯录用户被设为管理员
     * org_admin_remove ：通讯录用户被取消设置管理员
     * org_dept_create ： 通讯录企业部门创建
     * org_dept_modify ： 通讯录企业部门修改
     * org_dept_remove ： 通讯录企业部门删除
     * org_remove ： 企业被解散
     * org_change ： 企业信息发生变更
     * label_user_change ： 员工角色信息发生变更
     * label_conf_add：增加角色或者角色组
     * label_conf_del：删除角色或者角色组
     * label_conf_modify：修改角色或者角色组
     */
    @RequestMapping("/regist_regist/{suitid}/{companyId}")
    @ResponseBody
    public JSONObject suiteCallBackRegister(@PathVariable Integer suitid,@PathVariable  Integer companyId) {

        IsvTicketsEntity isvTicketsEntity = ticketsService.getIsvTicketByCompanyId(companyId);
//        String registCallUrl = CALLBACK_REGISTER_URL+isvTicketsEntity.getCorpAccessToken();
        JSONObject p = new JSONObject();
        p.put("access_token",isvTicketsEntity.getCorpAccessToken());
        String registCallUrl = JSONUtil.appendJsonParamsToUrl(DDConstant.DING_CONTACTS_CALLBACK_URL, p);

        String registCallbackUrl = GlobalConstant.getGateWay()+"/enterprise-web/suite/regist_callback?suiteId="+suitid;
        SuitesEntity suitesEntity = suitesService.getSuitesentity(new SuitesEntity(StatusEnum.OK.getValue(),suitid));
        String token = suitesEntity.getToken();
        String aeskey = suitesEntity.getEncodingAesKey();
        String[] tags = {
                "user_add_org", "user_modify_org", "user_leave_org", "org_admin_add", "org_admin_remove", "org_dept_create", "org_dept_modify", "org_dept_remove", "org_remove"
        };
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("call_back_tag", tags);
        jsonObject.put("token", token);
        jsonObject.put("aes_key", aeskey);
        jsonObject.put("url", registCallbackUrl);
        JSONObject object = HttpUtil.httpPost(registCallUrl, jsonObject);
        return object;
    }


    @RequestMapping("/regist_update/{suitid}/{companyId}")
    @ResponseBody
    public JSONObject suiteCallBackUpdate(@PathVariable Integer suitid,@PathVariable  Integer companyId) {

        IsvTicketsEntity isvTicketsEntity = ticketsService.getIsvTicketByCompanyId(companyId);
//        String registCallUrl = CALLBACK_REGISTER_URL+isvTicketsEntity.getCorpAccessToken();
        JSONObject p = new JSONObject();
        p.put("access_token",isvTicketsEntity.getCorpAccessToken());
        String registCallUrl = JSONUtil.appendJsonParamsToUrl(DDConstant.DING_CONTACTS_CALLBACK_URL_UPDATE, p);

        String registCallbackUrl = GlobalConstant.getGateWay()+"/enterprise-web/suite/regist_callback?suiteId="+suitid;
        SuitesEntity suitesEntity = suitesService.getSuitesentity(new SuitesEntity(StatusEnum.OK.getValue(),suitid));
        String token = suitesEntity.getToken();
        String aeskey = suitesEntity.getEncodingAesKey();
        String[] tags = {
                "user_add_org", "user_modify_org", "user_leave_org", "org_admin_add", "org_admin_remove", "org_dept_create", "org_dept_modify", "org_dept_remove", "org_remove"
        };
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("call_back_tag", tags);
        jsonObject.put("token", token);
        jsonObject.put("aes_key", aeskey);
        jsonObject.put("url", registCallbackUrl);
        JSONObject object = HttpUtil.httpPost(registCallUrl, jsonObject);
        return object;
    }

    @RequestMapping("/regist_callback")
    @ResponseBody
    public JSONObject suiteRegisterCallback(HttpServletRequest request, HttpServletResponse response) {
        JSONObject plainTextJson = null;
        try {
            String msgSignature = request.getParameter("signature");
            String timeStamp = request.getParameter("timestamp");
            String nonce = request.getParameter("nonce");
            String id = request.getParameter("suiteId");
            logger.info("套件ID : " + id);
            Integer suiteId = null;//todo 如果多个套件钉钉并没有传相应的标示。这边自己定义一个
            if (StringUtils.isNotEmpty(id)) {
                suiteId = Integer.valueOf(id);
            } else {
                throw new RuntimeException("套件ID 不能为空");
            }
            /** post数据包数据中的加密数据 **/
            ServletInputStream sis = request.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(sis));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            JSONObject jsonEncrypt = JSONObject.parseObject(sb.toString());
            String encrypt = "";

            /** 取得JSON对象中的encrypt字段， **/
            try {
                encrypt = jsonEncrypt.getString("encrypt");
            } catch (Exception e) {
                e.printStackTrace();
            }
            /** 对encrypt进行解密 **/
            DingTalkEncryptor dingTalkEncryptor = null;
            String plainText = null;
            DingTalkEncryptException dingTalkEncryptException = null;
            SuitesService suitesService = SpringContextHolder.getBean(SuitesService.class);
            SuitesEntity entity = suitesService.getSuitesentity(new SuitesEntity(StatusEnum.OK.getValue(),suiteId));
            if (entity == null) {
                throw new RuntimeException("无效的套件ID : " + suiteId);
            }
            try {
                dingTalkEncryptor = new DingTalkEncryptor(entity.getToken(), entity.getEncodingAesKey(), entity.getSuiteKey());
                plainText = dingTalkEncryptor.getDecryptMsgNew(msgSignature, timeStamp, nonce, encrypt);
            } catch (DingTalkEncryptException e) {
                e.printStackTrace();
            } finally {
            }

            plainTextJson = JSONObject.parseObject(plainText);
            logger.info("plainTextJson:"+plainTextJson.toString());
            logger.info("请求参数 > " + plainTextJson.toJSONString() + "----------");
            String eventType = plainTextJson.getString("EventType");
            String res = "success";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String corpid = plainTextJson.getString("CorpId");
            switch (eventType) {
                case "org_dept_create":
                    logger.info("创建部门 >");
                    try {
                        JSONArray dingDeptIdArray = plainTextJson.getJSONArray("DeptId");//todo  目前是一次一次掉用  暂时不优化。excel 导入新部门
                        String deptId = dingDeptIdArray.get(0).toString();
                        Integer addResult = departmentService.createOrModifyCallBackDepartment(corpid, deptId,0);
                        if(addResult<0){
                            OAMessageUtil.sendTextMsgToDept(buildSendMessage("同步部门失败,corpId"+corpid+",部门id:"+deptId));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error(e.getMessage());
                    }
                    break;
                case "org_dept_modify":
                    logger.info("修改部门 >");
                    try {
                        JSONArray dingDeptIdArray = plainTextJson.getJSONArray("DeptId");//todo  目前是一次一次掉用  暂时不优化。excel 导入新部门
                        String deptId = dingDeptIdArray.get(0).toString();
                        Integer addResult = departmentService.createOrModifyCallBackDepartment(corpid, deptId,1);
                        if(addResult<0){
                            OAMessageUtil.sendTextMsgToDept(buildSendMessage("更新部门失败,corpId"+corpid+",部门id:"+deptId));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error(e.getMessage());
                    }
                    break;
                case "org_dept_remove":
                    logger.info("删除部门 >");
                    try {
                        JSONArray dingDeptIdArray = plainTextJson.getJSONArray("DeptId");//todo  目前是一次一次掉用  暂时不优化。excel 导入新部门
                        String deptId = dingDeptIdArray.get(0).toString();
                        Integer addResult = departmentService.createOrModifyCallBackDepartment(corpid, deptId,2);
                        if(addResult<0){
                            OAMessageUtil.sendTextMsgToDept(buildSendMessage("更新部门失败,corpId"+corpid+",部门id:"+deptId));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error(e.getMessage());
                    }
                    break;
                case "user_add_org":
                    logger.info("新增人员 >");
                    try {
                        JSONArray dingUserIddArray = plainTextJson.getJSONArray("UserId");//todo  目前是一次一次掉用  暂时不优化。excel 导入新部门
                        String dingUserId = dingUserIddArray.get(0).toString();
                        Integer addResult = userService.createOrModifyCallBackDepartment(corpid, dingUserId ,0);
//                        if(addResult<0){
//                            OAMessageUtil.sendTextMsgToDept(buildSendMessage("更新部门失败,corpId"+corpid+",部门id:"+deptId));
//                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error(e.getMessage());
                    }
                    break;
                case "user_modify_org":
                    logger.info("修改人员 >");
                    try {
                        JSONArray dingUserIddArray = plainTextJson.getJSONArray("UserId");//todo  目前是一次一次掉用  暂时不优化。excel 导入新部门
                        String dingUserId = dingUserIddArray.get(0).toString();
                        Integer addResult = userService.createOrModifyCallBackDepartment(corpid, dingUserId ,1);
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error(e.getMessage());
                    }
                    break;
                case "user_leave_org":
                    logger.info("删除人员 >");
                    try {
                        JSONArray dingUserIddArray = plainTextJson.getJSONArray("UserId");//todo  目前是一次一次掉用  暂时不优化。excel 导入新部门
                        String dingUserId = dingUserIddArray.get(0).toString();
                        Integer addResult = userService.createOrModifyCallBackDepartment(corpid, dingUserId ,2);
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error(e.getMessage());
                    }
                    break;
            }
            long timeStampLong = Long.parseLong(timeStamp);
            Map<String, String> jsonMap = null;
            try {
                jsonMap = dingTalkEncryptor.getEncryptedMap(res, timeStampLong, nonce);
            } catch (DingTalkEncryptException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }
            JSONObject json = new JSONObject();
            json.putAll(jsonMap);
            logger.info("最终返回 > " + json.toString());
            return json;
        } catch (Exception e) {
        }
        return null;
    }


    private String buildSendMessage(String content){
        StringBuffer sb = new StringBuffer();
        sb.append("内容:"+content+"\n");
        sb.append("时间:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"\n");
        return sb.toString();
    }



    public static void main(String[] args) {
        JSONObject p = new JSONObject();
        p.put("access_token", "sss");
        String registCallUrl = JSONUtil.appendJsonParamsToUrl(DDConstant.DING_CONTACTS_CALLBACK_URL, p);
        System.out.println(registCallUrl);

//        String token = "neixunMobile";
//        String aeskey = "hevmmyizjo7oos0sdwm8xb0jqwid8ngzcuh455eh9ce";
//        String[] tags = {
//                "user_add_org", "user_modify_org", "user_leave_org", "org_admin_add", "org_admin_remove", "org_dept_create", "org_dept_modify", "org_dept_remove", "org_remove"
//                , "bpms_task_change", "bpms_instance_change"
//        };
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("call_back_tag", tags);
//        jsonObject.put("token", token);
//        jsonObject.put("aes_key", aeskey);
//        jsonObject.put("url", "www.baidu.com");
//        System.out.println(jsonObject.toJSONString());
    }
}
