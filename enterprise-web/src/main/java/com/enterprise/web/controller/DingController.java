package com.enterprise.web.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.open.client.api.model.corp.CorpUser;
import com.dingtalk.open.client.api.model.corp.CorpUserDetail;
import com.enterprise.base.bean.DingCorpUserDetail;
import com.enterprise.base.common.*;
import com.enterprise.base.entity.IsvTicketsEntity;
import com.enterprise.base.vo.UserVO;
import com.enterprise.isv.thread.SynchronizePublicSingle;
import com.enterprise.isv.thread.SynchronizeRight;
import com.enterprise.oa.OAMessageUtil;
import com.enterprise.service.isvTickets.IsvTicketsService;
import com.enterprise.service.question.UserXQuestionsService;
import com.enterprise.service.user.UserService;
import com.enterprise.service.user.UserXCompanyService;
import com.enterprise.util.DateUtil;
import com.enterprise.util.dingtalk.DingHelper;
import com.enterprise.util.oa.message.LinkOAMessage;
import com.enterprise.util.oa.message.OAMessage;
import com.enterprise.util.oa.message.OAMessageActionCard;
import com.enterprise.util.oa.message.OAMessageAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TestController
 *
 * @author shisan
 * @create 2018-03-22 上午11:53
 **/
@Controller
@RequestMapping("/ding")
public class DingController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private String QRCODE_PAGE = GlobalConstant.getQrCOdePage();

    @Resource
    private UserService userEntityService;

    @Resource
    private UserXQuestionsService userXQuestionsService;

    @Resource
    private UserXCompanyService userXCompanyService;

    @Resource
    private IsvTicketsService isvTicketsService;


    private String NEWVERSION13 = GlobalConstant.NEWVERSION_13PIC;

    private String TITLE = "道具功能上线啦！";

    private String SUBTITLE = "这是一次好玩的更新";

    private String NEWVERSION13PAGE = GlobalConstant.NEWVERSION_PAGE;


    @RequestMapping(value = "/getUser", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getUser(Integer id) {
        logger.info(id.toString());
        return ResultJson.succResultJson(userEntityService.getUserById(id));
    }

    @RequestMapping("/getUserInSession")
    @ResponseBody
    public JSONObject getUserInSession(HttpServletRequest request) {
        String registerUrl = GlobalConstant.getGateWay() + request.getContextPath() + "/suite/regist_callback?suiteId=" + 21;
        logger.info(registerUrl);
        return ResultJson.succResultJson(LoginUser.getUser());
    }


    @RequestMapping("/getUserInfo.json")
    @ResponseBody
    public JSONObject getUserInfo(String corpId, String dingUserId) {
        CorpUser corpUser = DingHelper.getMemberDetail(corpId, dingUserId);
        return ResultJson.succResultJson(corpUser);
    }

    @RequestMapping("/getDepartmentList.json")
    @ResponseBody
    public JSONObject getDepartmentList(String corpId, String deptId) {
        JSONObject object = new JSONObject();
        object.put("id", deptId);
        try {
            JSONArray jsonArray = DingHelper.getDepartmentList(corpId, object);
            return ResultJson.succResultJson(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultJson.succResultJson(null);
    }

    @RequestMapping("/getAllDepartment.json")
    @ResponseBody
    public JSONObject getAllDepartment(String corpId, String deptId) {
        JSONObject object = new JSONObject();
        object.put("id", deptId);
        try {
            JSONArray jsonArray = DingHelper.getDGDepartmentList(corpId, "true");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject job = jsonArray.getJSONObject(i);
                Integer departmentId = job.getInteger("id");
                System.out.println("did:" + departmentId);
            }
            return ResultJson.succResultJson(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultJson.succResultJson(null);
    }

    @RequestMapping("/getDepartmentInfo.json")
    @ResponseBody
    public JSONObject getDepartmentInfo(String corpId, String deptId) {
        JSONObject department = DingHelper.getDepartmentDetail(corpId, deptId);
        return ResultJson.succResultJson(department);
    }

    @RequestMapping("/getDepartmentUserInfo.json")
    @ResponseBody
    public JSONObject getDepartmentUserInfo(String corpId, String deptId) {
        JSONObject object = new JSONObject();
        object.put("id", deptId);
        try {
            List<CorpUserDetail> list = DingHelper.getMembersByCorpIdAndDeptId(corpId, new Long(deptId));
            return ResultJson.succResultJson(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultJson.succResultJson(null);
    }

    @RequestMapping("/getOrgUserCount.json")
    @ResponseBody
    public JSONObject getOrgUserCount(String corpId, String onlyActive) {
        Integer userCount = DingHelper.getOrgUserCount(corpId, onlyActive);
        return ResultJson.succResultJson(userCount);
    }


    @RequestMapping("/getCorpAdminList.json")
    @ResponseBody
    public JSONObject getCorpAdminList(String corpId) {
        List<DingCorpUserDetail> dingCorpUserDetailList = DingHelper.getCorpManager(corpId);
        return ResultJson.succResultJson(dingCorpUserDetailList);
    }

    @RequestMapping("/getCorpAdminSimpleList.json")
    @ResponseBody
    public JSONObject getCorpAdminSimpleList(String corpId) {
        List<String> dingCorpUserDetailList = DingHelper.getCorpManagerSimple(corpId);
        return ResultJson.succResultJson(dingCorpUserDetailList);
    }

    @RequestMapping("/sendLinkMessage.json")
    @ResponseBody
    public JSONObject sendLinkMessage(Integer companyId, String userid) {
        String MESSAGE_URL = "https://neixun.forwe.store/static/neixun/153447429335720180507114129403.png";
        String corpId = isvTicketsService.getIsvTicketByCompanyId(companyId).getCorpId();
        StringBuffer sb = new StringBuffer();
        String PRAISE_MESSAGE_CONTNET = "优秀，藏都藏不住";
        sb.append(PRAISE_MESSAGE_CONTNET+"\n");
        sb.append("时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "\n");
        String PRAISE_TITLE = "%s刚刚赞了你";
        String mainPage = String.format(corpId);
        String praiseTitle = String.format(PRAISE_TITLE, "SuperStar");
        LinkOAMessage linkOAMessage = new LinkOAMessage();
        OAMessageUtil.sendLinkMessageWithStroageV2(companyId, userid, linkOAMessage.getLinkOAMessage(mainPage, MESSAGE_URL, sb.toString(), praiseTitle));
        return ResultJson.succResultJson("");
    }


    @RequestMapping("/sendSimpleMessage.json")
    @ResponseBody
    public JSONObject sendSimpleMessage(Integer companyId, String userid) {
        String corpId = isvTicketsService.getIsvTicketByCompanyId(companyId).getCorpId();
        String PRAISE_TITLE = "%s刚刚赞了你";
        String praiseTitle = String.format(PRAISE_TITLE, "SuperStar");
        praiseTitle += "\n" + DateUtil.getDate_Y_M_D_H_M_S();

        String WELCOME_PAGE = DDConstant.ARRANGE_STUDY;
        OAMessage oaMessage = new OAMessage();
        String mainPage = String.format(WELCOME_PAGE, corpId);
        String mainPageQrcode = String.format(QRCODE_PAGE, URLEncoder.encode(mainPage));
        OAMessageUtil.sendOAMessageWithStroageV2(companyId, userid, "", oaMessage.getSimpleOAMessage(mainPage, mainPageQrcode, praiseTitle));
        return ResultJson.succResultJson("");
    }

    @RequestMapping("/sendOAMessage.json")
    @ResponseBody
    public JSONObject sendOamessage(String content, String deptid, String userid) {
        String MOBILE_INDEX_PAGE = GlobalConstant.getIndexUrl();
        String QRCODE_PAGE = GlobalConstant.getQrCOdePage();
        String WELCOME_WORD = "今天我来到这个世界,陪你成长";
        String WELCOME_PAGE = DDConstant.ARRANGE_STUDY;
        LoginUser loginUser = LoginUser.getUser();
        //OAMessageUtil.sendTextMsgToDeptAndUser(content, deptid,userid);
        OAMessage oaMessage = new OAMessage();
        String chall_page = String.format(MOBILE_INDEX_PAGE, loginUser.getCorpID());
        String chall_qr_code = String.format(QRCODE_PAGE, URLEncoder.encode(chall_page));
        OAMessageUtil.sendOAMessageWithStroage(loginUser.getCompanyID(), userid, WELCOME_WORD, oaMessage.getOAMessageWithPic(chall_page, chall_qr_code, OAMessageUtil.getSimpleMessageContent(WELCOME_WORD), WELCOME_PAGE));


//        Integer myDeptId = userXDeptService.getDepartmentId(loginUser.getCorpID(), loginUser.getUserID());
        //Integer myDeptId = 1;
        //String deptName = departmentMapper.getDeptNameById(loginUser.getCompanyID(), myDeptId);
//        OAMessage oaMessage = new OAMessage();
//        String chall_page = String.format(MOBILE_INDEX_PAGE,corpId);
//        String chall_qr_code = String.format(QRCODE_PAGE, URLEncoder.encode(chall_page));
//        String departmentmsg = "你的战队:" + deptName;
//        OAMessageUtil.sendOAMessageWithStroage(loginUser.getCompanyID(), userid, WELCOME_WORD, oaMessage.getOAMessageWithPic(chall_page, chall_qr_code, OAMessageUtil.getSimpleMessageContent(WELCOME_WORD), departmentmsg, WELCOME_PAGE));
        OAMessageUtil.sendOAMessageWithStroage(loginUser.getCompanyID(), "", userid, WELCOME_WORD, oaMessage.getOAMessageWithPic(chall_page, chall_qr_code, OAMessageUtil.getSimpleMessageContent(WELCOME_WORD), "", WELCOME_PAGE));

        return ResultJson.succResultJson(content);
    }

    /**
     * 同步所有用户的题库进度
     */
    @RequestMapping("/synchronizeLibrary.json")
    @ResponseBody
    public JSONObject synchronizeLibrary(String corpId) {
        Integer count = userXQuestionsService.synchronizeLibrary(corpId);
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("count", String.valueOf(count));
        resultMap.put("corpId", corpId);
        return ResultJson.succResultJson(resultMap);
    }

    /**
     * 同步所有用户的题库进度
     */
    @RequestMapping("/testSynchronizePublicSingle.json")
    public void testSynchronizePublicSingle(String corpId) {
        new Thread(new SynchronizePublicSingle(corpId)).start();
    }


    /**
     * 同步所有管理
     */
    @RequestMapping("/syncRight.json")
    public void syncRight(String corpId) {
        new Thread(new SynchronizeRight(corpId)).start();
    }

    /**
     * 同步所有管理
     */
    @RequestMapping("/syncPubLibrary.json")
    public void synvPubLibrary(String corpId) {
        new Thread(new SynchronizePublicSingle(corpId)).start();
    }


    @RequestMapping("/sendMessageToAll.json")
    public void sendMessageToAll(String corpId) {
        String dingUserIds = "";
        List<UserVO> userList = userXCompanyService.getUserByCorpId(corpId);
        IsvTicketsEntity isvTicketsEntity = isvTicketsService.getIsvTicketByCorpId(corpId);

        Integer loopCount = userList.size() % 10 == 0 ? userList.size() / 10 : 1 + (userList.size() / 10);
        for (int i = 0; i < loopCount; i++) {
            Integer startIndex = i * 10;
            Integer endIndex = 0;
            if (loopCount <= 1) {
                endIndex = userList.size();
            } else {
                if (i < loopCount - 1) {
                    endIndex = (i + 1) * 10;
                } else {
                    endIndex = userList.size();
                }
            }
            for (int j = startIndex; j < endIndex; j++) {
                if (j == startIndex) {
                    dingUserIds += userList.get(j).getDingUserId();
                } else {
                    dingUserIds += "|" + userList.get(j).getDingUserId();
                }
            }
            String libraryName = "小书童";
            String content = String.format(DDConstant.ARRANGE_STUDY_DEFAULT_MESSAGE, libraryName);
            OAMessage oaMessage = new OAMessage();
            String messageUrl = String.format(GlobalConstant.getChooseLibraryUrl(), corpId);
            String arena_pk_result_qrcode = String.format(GlobalConstant.getQrCOdePage(), URLEncoder.encode(messageUrl));
            logger.info("第" + i + "次发送全体OA消息");
            OAMessageUtil.sendOAMessageWithStroage(isvTicketsEntity.getCompanyId(), dingUserIds, String.format("管理员已为你解锁【%s】,快来挑战吧!", libraryName), oaMessage.getOAMessageWithPic(messageUrl, arena_pk_result_qrcode, OAMessageUtil.getMessageContent(content), DDConstant.ARRANGE_STUDY_DEFAULT_MESSAGE));
            dingUserIds = "";
        }
    }


    @RequestMapping("/sendMarkDown.json")
    public void sendMarkDown(String corpId) {
//        String arena_pk_result_qrcode =  URLEncoder.encode(weburl);

        String NEW_VERSION_PAGE = String.format(GlobalConstant.getGateWay3()+NEWVERSION13PAGE,corpId);
        OAMessageActionCard oaMessageActionCard = new OAMessageActionCard(TITLE,"![pic]("+NEWVERSION13+")   \n   <font color=#1C1C1C face=\"微软雅黑\">"+TITLE+"</font>   \n   <font color=#a4a7a9 face=\"微软雅黑\">"+SUBTITLE+"</font> " ,"查看详情",NEW_VERSION_PAGE);
      //OAMessageActionCard oaMessageActionCard = new OAMessageActionCard(title,"![pic]("+picUrl+") \n <font color=#5856FF face=\"微软雅黑\">"+title+"</font> \n <font color=#a4a7a9 face=\"微软雅黑\">"+singleTitle+"</font> " ,"查看详情",arena_pk_result_qrcode);

        String dingUserIds = "";
        List<UserVO> userList = userXCompanyService.getUserByCorpId(corpId);
        IsvTicketsEntity isvTicketsEntity = isvTicketsService.getIsvTicketByCorpId(corpId);
        Integer loopCount = userList.size() % 10 == 0 ? userList.size() / 10 : 1 + (userList.size() / 10);
        for (int i = 0; i < loopCount; i++) {
            Integer startIndex = i * 10;
            Integer endIndex = 0;
            if (loopCount <= 1) {
                endIndex = userList.size();
            } else {
                if (i < loopCount - 1) {
                    endIndex = (i + 1) * 10;
                } else {
                    endIndex = userList.size();
                }
            }
            for (int j = startIndex; j < endIndex; j++) {
                if (j == startIndex) {
                    dingUserIds += userList.get(j).getDingUserId();
                } else {
                    dingUserIds += "|" + userList.get(j).getDingUserId();
                }
            }
            logger.info("第" + i + "次发送全体OA消息");

        //public OAMessage getOAMessageWithPic(String messageUrl, String pcMessageUrl, String title,String content,String image) {
//            String newVersionPage = String.format(GlobalConstant.getNewVersionPage(), isvTicketsEntity.getCorpId());
//            OAMessage oaMessage = new OAMessage().getOAMessageWithPic(weburl, newVersionPage, getMessageContent(title, singleTitle), picUrl);
//            OAMessageUtil.sendOAMessageWithStroage(isvTicketsEntity.getCompanyId(), dingUserIds, "", oaMessage);
//            OAMessageUtil.sendOAMessageWithStroage(companyId, dingUserIds, "", new OAMessage().getOAMessageWithPic(mainPage, newVersionPage, getMessageContent(TITLE, SUBTITLE), NEWVERSION13));
            OAMessageUtil.sendActionCard(isvTicketsEntity.getCompanyId(), dingUserIds, "", oaMessageActionCard);
            dingUserIds = "";
        }
    }

    public static String getMessageContent(String title,String subTitle){
        StringBuffer sb = new StringBuffer();
        sb.append(title+"\n");
        sb.append(subTitle);
        return sb.toString();
    }


    public static void main(String[] args) {
        //String messageType,String messageUrl, String pcMessageUrl, String title,String content,String image
        OAMessageAll oaMessageAll = new OAMessageAll().getOAMessageWithPic("OA", "www.baidu.com", "www.baidu.com", "", OAMessageUtil.getMessageContent("sss"), "www.a.jpg");
        String dateTime = DateUtil.getCurrentDateStr();
        oaMessageAll = new OAMessageAll().getOAMessageWithPic("OA", "www.baidu.com", "www.baidu.com", dateTime, OAMessageUtil.getMessageContent("sss"), "www.a.jpg");

        JSONObject messageJson = new JSONObject();
        messageJson.put("to_all_user", "true");
        messageJson.put("agentid", "111111");
//        messageJson.put("msgtype", msgType);
        messageJson.put("msg", oaMessageAll);
        System.out.println(messageJson.toString());
    }
}
