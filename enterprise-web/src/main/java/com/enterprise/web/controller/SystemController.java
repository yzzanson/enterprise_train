package com.enterprise.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.GlobalConstant;
import com.enterprise.base.common.LoginUser;
import com.enterprise.base.common.ResultJson;
import com.enterprise.base.entity.CompanyInfoEntity;
import com.enterprise.base.entity.IsvTicketsEntity;
import com.enterprise.base.entity.UserEntity;
import com.enterprise.base.entity.UserXCompany;
import com.enterprise.base.entity.bury.OASendBuryEntity;
import com.enterprise.base.enums.StatusEnum;
import com.enterprise.base.enums.bury.OABuryEnum;
import com.enterprise.base.vo.UserVO;
import com.enterprise.mapper.bury.OASendBuryMapper;
import com.enterprise.oa.OAMessageUtil;
import com.enterprise.service.company.CompanyInfoService;
import com.enterprise.service.isvTickets.IsvTicketsService;
import com.enterprise.service.marketBuy.MarketBuyService;
import com.enterprise.service.report.CompanyDaySummaryService;
import com.enterprise.service.right.RightGroupService;
import com.enterprise.service.user.UserService;
import com.enterprise.service.user.UserXCompanyService;
import com.enterprise.util.dingtalk.DingHelper;
import com.enterprise.util.oa.message.OAMessage;
import com.enterprise.util.oa.message.OAMessageActionCard;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/7/20 下午1:47
 */
@RequestMapping("/system")
@Controller
public class SystemController {

    @Resource
    private CompanyInfoService companyInfoService;

    @Resource
    private IsvTicketsService isvTicketService;

    @Resource
    private RightGroupService rightGroupService;

    @Resource
    private CompanyDaySummaryService companyDaySummaryService;

    @Resource
    private UserXCompanyService userXCompanyService;

    @Resource
    private MarketBuyService marketBuyService;

    @Resource
    private OASendBuryMapper oaSendBuryMapper;

    @Resource
    private UserService userService;

    private static final String NEWVERSION_PAGE = GlobalConstant.getNewVersionPage();

    private static final String NEWVERSION_PIC = GlobalConstant.NEWVERSION_PIC;

    private String NEWVERSION13 = GlobalConstant.NEWVERSION_13PIC;
    private String NEWVERSION14 = GlobalConstant.NEWVERSION_14PIC;
    private String NEWVERSION162 = GlobalConstant.NEWVERSION_162PIC;

    private String TITLE = "道具功能上线啦！";

    private String TITLE_14 = "您好，这是你的小猫猫，请注意查收！";

    private String TITLE_162 = "这个春天 一起做公益吧！";


    private String SUBTITLE = "这是一次好玩的更新";

    private String SUBTITLE14 = "每日精进又双叒叕更新啦！";

    private String SUBTITLE162 = "答题攒爱心 救助流浪动物！";

    private String NEWVERSION13PAGE = GlobalConstant.NEWVERSION_PAGE;

    /**
     * 初始化企业的权限组
     */
    @RequestMapping("/syncUserXLibrary.json")
    @ResponseBody
    public JSONObject syncUserXLibrary() {
        StringBuffer corpids = new StringBuffer();
        List<CompanyInfoEntity> companyInfoList = companyInfoService.getAllCompanys();
        for (int i = 0; i < companyInfoList.size(); i++) {
            CompanyInfoEntity companyInfo = companyInfoList.get(i);
            Integer companyId = companyInfo.getId();
            IsvTicketsEntity isvTicketsEntity = isvTicketService.getIsvTicketByCompanyId(companyId);
            Integer addCount = rightGroupService.initCompanyManageGroup(isvTicketsEntity.getCorpId());
            String oaMessage = companyInfo.getName() + "成功插入" + addCount + "个管理员权限";
            OAMessageUtil.sendTextMsgToDept(oaMessage);
        }
        return ResultJson.succResultJson(corpids.toString());
    }


    /**
     * 更新后给企业员工
     */
    @RequestMapping("/sendUpdateMessage.json")
    @ResponseBody
    public JSONObject sendUpdateMessage() {
        StringBuffer corpids = new StringBuffer();

        List<CompanyInfoEntity> companyInfoList = companyInfoService.getAllCompanys();
        for (int i = 0; i < companyInfoList.size(); i++) {
            CompanyInfoEntity companyInfo = companyInfoList.get(i);
            Integer companyId = companyInfo.getId();
            IsvTicketsEntity isvTicketsEntity = isvTicketService.getIsvTicketByCompanyId(companyId);
            String manageDingIds = getManageDingids(isvTicketsEntity.getCorpId());
            String pkpage = String.format(NEWVERSION_PAGE, isvTicketsEntity.getCorpId());
//            OAMessageUtil.sendOAMessageWithStroage(companyId, manageDingIds, "", new OAMessage().getOAMessageWithPic(pkpage, pkpage, OAMessageUtil.getMessageContent(""), NEWVERSION_PIC));
            OAMessageUtil.sendOAMessageWithStroage(companyId, manageDingIds, "", new OAMessage().getOAMessageWithPic(pkpage, pkpage, OAMessageUtil.getSimpleMessageContent(""), NEWVERSION_PIC));
        }
        return ResultJson.succResultJson(corpids.toString());
    }


    /**
     * 更新后给企业员工
     */
    @RequestMapping("/sendUpdateMessage1_3.json")
    @ResponseBody
    public JSONObject sendUpdateMessage1_3() {
        StringBuffer corpids = new StringBuffer();
        List<CompanyInfoEntity> companyInfoList = companyInfoService.getAllCompanys();
        for (int i = 0; i < companyInfoList.size(); i++) {
            CompanyInfoEntity companyInfo = companyInfoList.get(i);
            Integer companyId = companyInfo.getId();
            IsvTicketsEntity isvTicketsEntity = isvTicketService.getIsvTicketByCompanyId(companyId);
            String dingUserIds = "";
            List<UserVO> userList = userXCompanyService.getUserByCorpId(isvTicketsEntity.getCorpId());
            String NEW_VERSION_PAGE = String.format(GlobalConstant.getGateWay3() + NEWVERSION13PAGE, isvTicketsEntity.getCorpId());
            OAMessageActionCard oaMessageActionCard = new OAMessageActionCard(TITLE, "![pic](" + NEWVERSION13 + ")   \n   <font color=#1C1C1C face=\"微软雅黑\">" + TITLE + "</font>   \n   <font color=#a4a7a9 face=\"微软雅黑\">" + SUBTITLE + "</font> ", "查看详情", NEW_VERSION_PAGE);
            Integer loopCount = userList.size() % 10 == 0 ? userList.size() / 10 : 1 + (userList.size() / 10);
            for (int j = 0; j < loopCount; j++) {
                List<OASendBuryEntity> oaSendBuryList = new ArrayList<>();
                Integer startIndex = j * 10;
                Integer endIndex = 0;
                if (loopCount <= 1) {
                    endIndex = userList.size();
                } else {
                    if (j < loopCount - 1) {
                        endIndex = (j + 1) * 10;
                    } else {
                        endIndex = userList.size();
                    }
                }

                for (int k = startIndex; k < endIndex; k++) {
                    if (k == startIndex) {
                        dingUserIds += userList.get(k).getDingUserId();
                    } else {
                        dingUserIds += "|" + userList.get(k).getDingUserId();
                    }
                    oaSendBuryList.add(new OASendBuryEntity(companyId, userList.get(k).getId(), OABuryEnum.NEW_VERSION.getValue(), new Date()));
                }
//                String newVersionPage = String.format(NEWVERSION_PAGE, isvTicketsEntity.getCorpId());
//            OAMessageUtil.sendOAMessageWithStroage(companyId, manageDingIds, "", new OAMessage().getOAMessageWithPic(pkpage, pkpage, OAMessageUtil.getMessageContent(""), NEWVERSION_PIC));
//                OAMessage oaMessage = new OAMessage().getOAMessageWithPic(MAINPAGE, newVersionPage, getMessageContent(TITLE, SUBTITLE), NEWVERSION13);
//                OAMessageUtil.sendOAMessageWithStroage(companyId, dingUserIds, "", oaMessage);

                oaSendBuryMapper.batchInsert(oaSendBuryList);

                OAMessageUtil.sendActionCard(isvTicketsEntity.getCompanyId(), dingUserIds, "", oaMessageActionCard);
                dingUserIds = "";
            }
        }
        return ResultJson.succResultJson(corpids.toString());
    }


    /**
     * 更新后给企业员工
     */
    @RequestMapping("/sendUpdateMessage1_4.json")
    @ResponseBody
    public JSONObject sendUpdateMessage1_4() {
        StringBuffer corpids = new StringBuffer();
        List<CompanyInfoEntity> companyInfoList = companyInfoService.getAllCompanys();
        for (int i = 0; i < companyInfoList.size(); i++) {
            CompanyInfoEntity companyInfo = companyInfoList.get(i);
            Integer companyId = companyInfo.getId();
            IsvTicketsEntity isvTicketsEntity = isvTicketService.getIsvTicketByCompanyId(companyId);
            String dingUserIds = "";
            List<UserVO> userList = userXCompanyService.getUserByCorpId(isvTicketsEntity.getCorpId());
            String mainPageBury = NEWVERSION_PAGE + GlobalConstant.OA_BURY_TYPE + OABuryEnum.NEW_VERSION.getValue();
            String NEW_VERSION_PAGE_OA = String.format(mainPageBury, isvTicketsEntity.getCorpId());
            OAMessageActionCard oaMessageActionCard = new OAMessageActionCard(TITLE_14, "![pic](" + NEWVERSION14 + ")   \n   <font color=#1C1C1C face=\"微软雅黑\">" + TITLE_14 + "</font>   \n   <font color=#a4a7a9 face=\"微软雅黑\">" + SUBTITLE14 + "</font> ", "查看详情", NEW_VERSION_PAGE_OA);
            Integer loopCount = userList.size() % 10 == 0 ? userList.size() / 10 : 1 + (userList.size() / 10);
            for (int j = 0; j < loopCount; j++) {
                List<OASendBuryEntity> oaSendBuryList = new ArrayList<>();
                Integer startIndex = j * 10;
                Integer endIndex = 0;
                if (loopCount <= 1) {
                    endIndex = userList.size();
                } else {
                    if (j < loopCount - 1) {
                        endIndex = (j + 1) * 10;
                    } else {
                        endIndex = userList.size();
                    }
                }

                for (int k = startIndex; k < endIndex; k++) {
                    if (k == startIndex) {
                        dingUserIds += userList.get(k).getDingUserId();
                    } else {
                        dingUserIds += "|" + userList.get(k).getDingUserId();
                    }
                    oaSendBuryList.add(new OASendBuryEntity(companyId, userList.get(k).getId(), OABuryEnum.NEW_VERSION.getValue(), new Date()));
                }
                oaSendBuryMapper.batchInsert(oaSendBuryList);
//                OAMessageUtil.sendActionCard(companyId, userXCompany.getDingUserId(), "", oaMessageActionCard);
                OAMessageUtil.sendActionCard(isvTicketsEntity.getCompanyId(), dingUserIds, "", oaMessageActionCard);
                dingUserIds = "";
            }
        }
        return ResultJson.succResultJson(corpids.toString());
    }

    /**
     * 更新后给企业员工
     */
    @RequestMapping("/sendUpdateMessage1_6_2_test.json")
    @ResponseBody
    public JSONObject sendUpdateMessage1_6_2_test(Integer companyId, Integer userId) {
        StringBuffer corpids = new StringBuffer();
        IsvTicketsEntity isvTicketsEntity = isvTicketService.getIsvTicketByCompanyId(companyId);
        UserXCompany userXCompany = userXCompanyService.getUserXCompany(new UserXCompany(isvTicketsEntity.getCorpId(), userId));
        String mainPageBury = NEWVERSION_PAGE + GlobalConstant.OA_BURY_TYPE + OABuryEnum.NEW_VERSION.getValue();
        String NEW_VERSION_PAGE_OA = String.format(mainPageBury, isvTicketsEntity.getCorpId());
        OAMessageActionCard oaMessageActionCard = new OAMessageActionCard(TITLE_162, "![pic](" + NEWVERSION162 + ")   \n   <font color=#1C1C1C face=\"微软雅黑\">" + TITLE_162 + "</font>   \n   <font color=#a4a7a9 face=\"微软雅黑\">" + SUBTITLE162 + "</font> ", "查看详情", NEW_VERSION_PAGE_OA);
        OAMessageUtil.sendActionCard(isvTicketsEntity.getCompanyId(), userXCompany.getDingUserId(), "", oaMessageActionCard);
        return ResultJson.succResultJson(corpids.toString());
    }


    /**
     * 更新后给企业员工
     */
    @RequestMapping("/sendUpdateMessage1_6_2_test2.json")
    @ResponseBody
    public JSONObject sendUpdateMessage1_6_2_test2(Integer companyId, Integer userId) {
        StringBuffer corpids = new StringBuffer();
        IsvTicketsEntity isvTicketsEntity = isvTicketService.getIsvTicketByCompanyId(companyId);
        UserXCompany userXCompany = userXCompanyService.getUserXCompany(new UserXCompany(isvTicketsEntity.getCorpId(), userId));
        String mainPageBury = NEWVERSION_PAGE + GlobalConstant.OA_BURY_TYPE + OABuryEnum.NEW_VERSION.getValue();
        String NEW_VERSION_PAGE_OA = String.format(mainPageBury, isvTicketsEntity.getCorpId());
        OAMessageActionCard oaMessageActionCard = new OAMessageActionCard(TITLE_162, "![pic](" + NEWVERSION162 + ")   \n   <font color=#1C1C1C face=\"微软雅黑\">" + TITLE_162 + "</font>   \n   <font color=#a4a7a9 face=\"微软雅黑\">" + SUBTITLE162 + "</font> ", "查看详情", NEW_VERSION_PAGE_OA);
        OAMessageUtil.sendActionCard(isvTicketsEntity.getCompanyId(), userXCompany.getDingUserId(), "", oaMessageActionCard);
        return ResultJson.succResultJson(corpids.toString());
    }


    /**
     * 更新后给企业员工
     */
    @RequestMapping("/sendUpdateMessage_company_1_6_2.json")
    @ResponseBody
    public JSONObject sendUpdateMessageCompany1_6_2(Integer companyId) {
        IsvTicketsEntity isvTicketsEntity = isvTicketService.getIsvTicketByCompanyId(companyId);
        String dingUserIds = "";
        List<UserVO> userList = userXCompanyService.getUserByCorpId2(isvTicketsEntity.getCorpId(), StatusEnum.OK.getValue());
        String mainPageBury = NEWVERSION_PAGE + GlobalConstant.OA_BURY_TYPE + OABuryEnum.NEW_VERSION.getValue();
        String NEW_VERSION_PAGE_OA = String.format(mainPageBury, isvTicketsEntity.getCorpId());
        OAMessageActionCard oaMessageActionCard = new OAMessageActionCard(TITLE_162, "![pic](" + NEWVERSION162 + ")   \n   <font color=#1C1C1C face=\"微软雅黑\">" + TITLE_162 + "</font>   \n   <font color=#a4a7a9 face=\"微软雅黑\">" + SUBTITLE162 + "</font> ", "查看详情", NEW_VERSION_PAGE_OA);
        Integer loopCount = userList.size() % 10 == 0 ? userList.size() / 10 : 1 + (userList.size() / 10);
        for (int j = 0; j < loopCount; j++) {
            List<OASendBuryEntity> oaSendBuryList = new ArrayList<>();
            Integer startIndex = j * 10;
            Integer endIndex = 0;
            if (loopCount <= 1) {
                endIndex = userList.size();
            } else {
                if (j < loopCount - 1) {
                    endIndex = (j + 1) * 10;
                } else {
                    endIndex = userList.size();
                }
            }

            for (int k = startIndex; k < endIndex; k++) {
                if (k == startIndex) {
                    dingUserIds += userList.get(k).getDingUserId();
                } else {
                    dingUserIds += "|" + userList.get(k).getDingUserId();
                }
                oaSendBuryList.add(new OASendBuryEntity(companyId, userList.get(k).getId(), OABuryEnum.NEW_VERSION.getValue(), new Date()));
            }
            oaSendBuryMapper.batchInsert(oaSendBuryList);
//                OAMessageUtil.sendActionCard(companyId, userXCompany.getDingUserId(), "", oaMessageActionCard);
            OAMessageUtil.sendActionCard(isvTicketsEntity.getCompanyId(), dingUserIds, "", oaMessageActionCard);
            dingUserIds = "";
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return ResultJson.succResultJson(companyId.toString());
    }

    /**
     * 更新后给企业员工
     */
    @RequestMapping("/sendUpdateMessage1_6_2.json")
    @ResponseBody
    public JSONObject sendUpdateMessage1_6_2() {
        StringBuffer corpids = new StringBuffer();
        List<CompanyInfoEntity> companyInfoList = companyInfoService.getAllCompanys();
        for (int i = 0; i < companyInfoList.size(); i++) {
            CompanyInfoEntity companyInfo = companyInfoList.get(i);
            Integer companyId = companyInfo.getId();
            IsvTicketsEntity isvTicketsEntity = isvTicketService.getIsvTicketByCompanyId(companyId);
            String dingUserIds = "";
            List<UserVO> userList = userXCompanyService.getUserByCorpId2(isvTicketsEntity.getCorpId(), StatusEnum.OK.getValue());
            String mainPageBury = NEWVERSION_PAGE + GlobalConstant.OA_BURY_TYPE + OABuryEnum.NEW_VERSION.getValue();
            String NEW_VERSION_PAGE_OA = String.format(mainPageBury, isvTicketsEntity.getCorpId());
            OAMessageActionCard oaMessageActionCard = new OAMessageActionCard(TITLE_162, "![pic](" + NEWVERSION162 + ")   \n   <font color=#1C1C1C face=\"微软雅黑\">" + TITLE_162 + "</font>   \n   <font color=#a4a7a9 face=\"微软雅黑\">" + SUBTITLE162 + "</font> ", "查看详情", NEW_VERSION_PAGE_OA);
            Integer loopCount = userList.size() % 10 == 0 ? userList.size() / 10 : 1 + (userList.size() / 10);
            for (int j = 0; j < loopCount; j++) {
                List<OASendBuryEntity> oaSendBuryList = new ArrayList<>();
                Integer startIndex = j * 10;
                Integer endIndex = 0;
                if (loopCount <= 1) {
                    endIndex = userList.size();
                } else {
                    if (j < loopCount - 1) {
                        endIndex = (j + 1) * 10;
                    } else {
                        endIndex = userList.size();
                    }
                }

                for (int k = startIndex; k < endIndex; k++) {
                    if (k == startIndex) {
                        dingUserIds += userList.get(k).getDingUserId();
                    } else {
                        dingUserIds += "|" + userList.get(k).getDingUserId();
                    }
                    oaSendBuryList.add(new OASendBuryEntity(companyId, userList.get(k).getId(), OABuryEnum.NEW_VERSION.getValue(), new Date()));
                }
                oaSendBuryMapper.batchInsert(oaSendBuryList);
//                OAMessageUtil.sendActionCard(companyId, userXCompany.getDingUserId(), "", oaMessageActionCard);
                OAMessageUtil.sendActionCard(isvTicketsEntity.getCompanyId(), dingUserIds, "", oaMessageActionCard);
                dingUserIds = "";
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return ResultJson.succResultJson(corpids.toString());
    }

    public static String getMessageContent(String title, String subTitle) {
        StringBuffer sb = new StringBuffer();
        sb.append(title + "\n");
        sb.append(subTitle);
        return sb.toString();
    }


    /**
     * 新版本更新后给企业员工
     */
    @RequestMapping("/sendUMessageSingle.json")
    @ResponseBody
    public JSONObject sendUMessageSingle(String corpId, Integer userId) {
        IsvTicketsEntity isvTicketsEntity = isvTicketService.getIsvTicketByCorpId(corpId);
        UserXCompany userXCompany = userXCompanyService.getUserXCompany(new UserXCompany(corpId, userId));
        String mainPageBury = NEWVERSION_PAGE + GlobalConstant.OA_BURY_TYPE + OABuryEnum.NEW_VERSION.getValue();
        String NEW_VERSION_PAGE_OA = String.format(mainPageBury, isvTicketsEntity.getCorpId());
        String title = TITLE_14 + Math.random();
        String subtitle = SUBTITLE14 + Math.random();
        OAMessageActionCard oaMessageActionCard = new OAMessageActionCard(title, "![pic](" + NEWVERSION14 + ")   \n   <font color=#1C1C1C face=\"微软雅黑\">" + title + "</font>   \n   <font color=#a4a7a9 face=\"微软雅黑\">" + subtitle + "</font> ", "查看详情", NEW_VERSION_PAGE_OA);
        OAMessageUtil.sendActionCard(isvTicketsEntity.getCompanyId(), userXCompany.getDingUserId(), "", oaMessageActionCard);
        return ResultJson.succResultJson(corpId);
    }

    /**
     * 更新错误的企业统计信息
     */
    @RequestMapping("/updateCompanyDaySummary.json")
    @ResponseBody
    public JSONObject updateCompanyDaySummary(String date) {
        return companyDaySummaryService.updateCompanyDaySummary(date);
    }


    private String getManageDingids(String corpId) {
        StringBuffer stringBuffer = new StringBuffer();
        List<String> corpManagerList = DingHelper.getCorpManagerSimple(corpId);
        for (int i = 0; i < corpManagerList.size(); i++) {
            String dingUserId = corpManagerList.get(i);
            if (stringBuffer.length() == 0) {
                stringBuffer.append(dingUserId);
            } else {
                stringBuffer.append("|").append(dingUserId);
            }
        }
        return stringBuffer.toString();
    }

    /**
     * 获取当前系统版本信息
     */
    @RequestMapping("/getMarketVersionMessage.json")
    @ResponseBody
    public JSONObject getMarketVersionMessage() {
        return marketBuyService.getMarketVersionMessage();
    }


    /**
     * 获取当前系统版本信息
     */
    @RequestMapping("/getUser.json")
    @ResponseBody
    public JSONObject getUser() {
        LoginUser loginUser = LoginUser.getUser();
        return ResultJson.succResultJson(loginUser);
    }

    /**
     * 获取当前系统版本信息
     */
    @RequestMapping("/changeUser.json")
    @ResponseBody
    public JSONObject changeUser(String corpid, String dingUserId) {
        Integer result = userService.createOrModifyCallBackDepartment(corpid, dingUserId, 1);
        return ResultJson.succResultJson(result);
    }


    /**
     * 获取当前系统版本信息
     */
    @RequestMapping("/updateUser.json")
    @ResponseBody
    public JSONObject updateUser(Integer id) {
        try {
            String name = new String("\uD83C\uDDE8\uD83C\uDDF3陈栖文   ♻️『近视防控-孝感™".getBytes(), "utf-8");
            UserEntity userEntity = new UserEntity(id, name, null, new Date());
            userService.updateUser(userEntity);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return ResultJson.succResultJson(id);
    }


}
