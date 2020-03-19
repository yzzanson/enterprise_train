package com.enterprise.oa;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.LoginUser;
import com.enterprise.base.common.SpringContextHolder;
import com.enterprise.base.entity.OaMsgEntity;
import com.enterprise.base.vo.TicketVO;
import com.enterprise.mapper.oamsg.OaMsgMapper;
import com.enterprise.service.isvTickets.IsvTicketsService;
import com.enterprise.util.oa.enums.DepartEnum;
import com.enterprise.util.oa.enums.MsgTypeEnum;
import com.enterprise.util.oa.message.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * OA消息工具类
 *
 * @Date:上午11:28 2018/3/9
 * OAMessageUtil
 */
public class OAMessageUtil {

    private static Logger logger = LoggerFactory.getLogger(OAMessageUtil.class);




    /**
     * 发送oa消息到人
     *
     * @param companyId 公司Id
     * @param oaMsg    oa消息体内容
     * @author shisan
     * @date 2018/3/9 下午3:15
     */
    public static void sendOAMessageActionCardToALL(Integer companyId,  String dingUserId,String content,OAMessageActionCard oaMsg) {
//        sendMsgToAll_ActionCard(companyId, content, oaMsg, true);
        sendActionCardMsg(companyId, dingUserId, content, oaMsg, MsgTypeEnum.ACTIONCARD.getValue(), true);
    }

    /**
     * 发送oa消息到人
     *
     * @param companyId 公司Id
     * @param oaMsg    oa消息体内容
     * @author shisan
     * @date 2018/3/9 下午3:15
     */
    public static void sendOAMessageWithStroage(Integer companyId, String dingUserId,String content,OAMessage oaMsg) {
        sendMsg(companyId, dingUserId, content, oaMsg, MsgTypeEnum.OA.getValue(), true);
    }

    public static void sendActionCard(Integer companyId, String dingUserId,String content,OAMessageActionCard oaMessageActionCard) {
        sendMsg(companyId, dingUserId, content, oaMessageActionCard, MsgTypeEnum.ACTIONCARD.getValue(), true);
    }

    public static void sendOAMessageWithStroageV2(Integer companyId, String dingUserId,String content,OAMessage oaMsg) {
        sendMsgV2(companyId, dingUserId, content, oaMsg, MsgTypeEnum.OA.getValue(), true);
    }



    public static void sendLinkMessageWithStroageV2(Integer companyId, String dingUserId,LinkOAMessage oaMsg) {
        sendMsgV2(companyId, dingUserId,"",oaMsg, MsgTypeEnum.LINK.getValue(), true);
    }

    /**
     * 发送oa消息到人和部门
     *
     * @param companyId 公司Id
     * @param oaMsg    oa消息体内容
     * @author shisan
     * @date 2018/3/9 下午3:15
     */
    public static void sendOAMessageWithStroage(Integer companyId,String departmentIds, String dingUserId,String content,OAMessage oaMsg) {
        sendMsg(companyId, departmentIds,dingUserId,content,oaMsg, MsgTypeEnum.OA.getValue(), true);
    }


    /**
     * 发送消息
     *
     * @param content     消息内容
     * @param msgType
     * @param storageFlag 是否入库
     * @author shisan
     * @date 2018/3/9 下午4:31
     */
    private static JSONObject sendMsg(Integer companyId, String dingUserId,String messageContent ,Object content, String msgType, boolean storageFlag) {
        JSONObject resultJson = new JSONObject();
        String failMsg;
        if (companyId == null || content == null) {
            failMsg = "[发送消息到部门] 发送失败,原因:companyId = " + companyId + ", content = " + content;
            resultJson.put("errcode", -1);
            resultJson.put("errmsg", failMsg);
            //发送消息失败时给开发部门发oa消息
            sendTextMsgToDept(failMsg);
            return resultJson;
        }
        //查询配置信息
        IsvTicketsService ticketService = SpringContextHolder.getBean(IsvTicketsService.class);
        TicketVO ticketVO = ticketService.getIsvTicketVOByCompanyId(companyId);

        if (ticketVO == null || companyId==null) {
            failMsg = "[发送消息到部门] 发送失败,原因:未获得公司id";
            logger.error(failMsg);

            resultJson.put("errcode", -1);
            resultJson.put("errmsg", failMsg);

            //发送消息失败时给开发部门发oa消息
            sendTextMsgToDept(failMsg);
            return resultJson;
        }

        //构建Message消息体
        JSONObject messageJson = buildSingleMessage(ticketVO, dingUserId,content, msgType);
        //发送消息
        new Thread(new SendMsgThread(messageJson, "",companyId, ticketVO.getAccessToken(), resultJson, storageFlag)).start();
        return resultJson;
    }


    private static JSONObject sendMsgV2(Integer companyId, String dingUserId,String messageContent ,Object content, String msgType, boolean storageFlag) {
        JSONObject resultJson = new JSONObject();
        String failMsg;
        if (companyId == null || content == null) {
            failMsg = "[发送消息到部门] 发送失败,原因:companyId = " + companyId + ", content = " + content;
            resultJson.put("errcode", -1);
            resultJson.put("errmsg", failMsg);
            //发送消息失败时给开发部门发oa消息
            sendTextMsgToDept(failMsg);
            return resultJson;
        }
        //查询配置信息
        IsvTicketsService ticketService = SpringContextHolder.getBean(IsvTicketsService.class);
        TicketVO ticketVO = ticketService.getIsvTicketVOByCompanyId(companyId);

        if (ticketVO == null || companyId==null) {
            failMsg = "[发送消息到部门] 发送失败,原因:未获得公司id";
            logger.error(failMsg);

            resultJson.put("errcode", -1);
            resultJson.put("errmsg", failMsg);

            //发送消息失败时给开发部门发oa消息
            sendTextMsgToDept(failMsg);
            return resultJson;
        }

        //构建Message消息体
        JSONObject messageJson = buildSingleMessage(ticketVO, dingUserId,content, msgType);
        //发送消息
        new Thread(new SendMsgThreadV2(messageJson, "",companyId, ticketVO.getAccessToken(), resultJson, storageFlag)).start();
        return resultJson;
    }

    /**
     * 发送消息
     *
     * @param content     消息内容
     * @param msgType
     * @param storageFlag 是否入库
     * @author shisan
     * @date 2018/3/9 下午4:31
     */
    private static JSONObject sendActionCardMsg(Integer companyId, String dingUserId,String messageContent ,Object content, String msgType, boolean storageFlag) {
        JSONObject resultJson = new JSONObject();
        String failMsg;
        if (companyId == null || content == null) {
            failMsg = "[发送消息到部门] 发送失败,原因:companyId = " + companyId + ", content = " + content;
            logger.error(failMsg);
            resultJson.put("errcode", -1);
            resultJson.put("errmsg", failMsg);
            //发送消息失败时给开发部门发oa消息
            sendTextMsgToDept(failMsg);
            return resultJson;
        }
        //查询配置信息
        IsvTicketsService ticketService = SpringContextHolder.getBean(IsvTicketsService.class);
        TicketVO ticketVO = ticketService.getIsvTicketVOByCompanyId(companyId);

        if (ticketVO == null || companyId==null) {
            failMsg = "[发送消息到部门] 发送失败,原因:未获得公司id";
            logger.error(failMsg);

            resultJson.put("errcode", -1);
            resultJson.put("errmsg", failMsg);

            //发送消息失败时给开发部门发oa消息
            sendTextMsgToDept(failMsg);
            return resultJson;
        }

        //构建Message消息体
        JSONObject messageJson = buildSingleMessage(ticketVO, dingUserId,content, msgType);
        //发送消息
        new Thread(new SendMsgThreadV2(messageJson, "",companyId, ticketVO.getAccessToken(), resultJson, storageFlag)).start();
        return resultJson;
    }

    /**
     * 发送消息
     *
     * @param content     消息内容
     * @param storageFlag 是否入库
     * @author shisan
     * @date 2018/3/9 下午4:31
     */
    private static JSONObject sendMsgToAll(Integer companyId,String strcontent,Object content,boolean storageFlag) {
        JSONObject resultJson = new JSONObject();
        String failMsg;
        if (companyId == null || content == null) {
            failMsg = "[发送消息到部门] 发送失败,原因:companyId = " + companyId + ", content = " + content;
            logger.error(failMsg);
            resultJson.put("errcode", -1);
            resultJson.put("errmsg", failMsg);
            //发送消息失败时给开发部门发oa消息
            sendTextMsgToDept(failMsg);
            return resultJson;
        }
        //查询配置信息
        IsvTicketsService ticketService = SpringContextHolder.getBean(IsvTicketsService.class);
        TicketVO ticketVO = ticketService.getIsvTicketVOByCompanyId(companyId);

        if (ticketVO == null || companyId==null) {
            failMsg = "[发送消息到部门] 发送失败,原因:未获得公司id";
            logger.error(failMsg);

            resultJson.put("errcode", -1);
            resultJson.put("errmsg", failMsg);

            //发送消息失败时给开发部门发oa消息
            sendTextMsgToDept(failMsg);
            return resultJson;
        }

        //构建Message消息体
        JSONObject messageJson = buildAllMessage(ticketVO, content);
        System.out.println("--------------------------");
        System.out.println(messageJson.toString());
        System.out.println("--------------------------");

        //发送消息
        new Thread(new SendAllMsgThread(messageJson, strcontent,companyId, ticketVO.getAccessToken(), resultJson, storageFlag)).start();
        return resultJson;
    }


    /**
     * 发送消息
     *
     * @param content     消息内容
     * @param msgType
     * @param storageFlag 是否入库
     * @author shisan
     * @date 2018/3/9 下午4:31
     */
    private static JSONObject sendMsg(Integer companyId, String departmentIds,String dingUserId,String strcontent,Object content, String msgType, boolean storageFlag) {
        JSONObject resultJson = new JSONObject();
        String failMsg;
        if (companyId == null || content == null) {
            failMsg = "[发送消息到部门] 发送失败,原因:companyId = " + companyId + ", content = " + content;
            logger.error(failMsg);
            resultJson.put("errcode", -1);
            resultJson.put("errmsg", failMsg);
            //发送消息失败时给开发部门发oa消息
            sendTextMsgToDept(failMsg);
            return resultJson;
        }
        //查询配置信息
        IsvTicketsService ticketService = SpringContextHolder.getBean(IsvTicketsService.class);
        TicketVO ticketVO = ticketService.getIsvTicketVOByCompanyId(companyId);

        if (ticketVO == null || companyId==null) {
            failMsg = "[发送消息到部门] 发送失败,原因:未获得公司id";
            logger.error(failMsg);

            resultJson.put("errcode", -1);
            resultJson.put("errmsg", failMsg);

            //发送消息失败时给开发部门发oa消息
            sendTextMsgToDept(failMsg);
            return resultJson;
        }

        //构建Message消息体
        JSONObject messageJson = buildMessage(ticketVO, departmentIds, dingUserId, content, msgType);
        //发送消息
        new Thread(new SendMsgThreadV2(messageJson, strcontent,companyId, ticketVO.getAccessToken(), resultJson, storageFlag)).start();
        return resultJson;
    }


    /**
     * 向指定部门发送简单文本消息
     * 该方法只针对网吧钉钉体验站
     *
     * @param content
     */
    public static void sendTextMsgToDept(String content) {
        IsvTicketsService ticketService = SpringContextHolder.getBean(IsvTicketsService.class);
        //查询配置信息
        TicketVO ticketVO = ticketService.getIsvTicketVOByCompanyId(DepartEnum.ACTIVEDEPT.getCompanyId());

        //构建textMessage消息体
        JSONObject messageJson = buildMessage(ticketVO, DepartEnum.ACTIVEDEPT.getDeptId(), new TextMessage(content), MsgTypeEnum.TEXT.getValue());
        //发送消息
        new Thread(new SendMsgThread(messageJson, content,DepartEnum.ACTIVEDEPT.getCompanyId(), ticketVO.getAccessToken(), new JSONObject(), false)).start();
    }

    public static void sendTextMsgToDept(String agentId,String accessToken,String content) {
//        IsvTicketsService ticketService = SpringContextHolder.getBean(IsvTicketsService.class);
//        //查询配置信息
//        TicketVO ticketVO = ticketService.getIsvTicketVOByCompanyId(DepartEnum.ACTIVEDEPT.getCompanyId());
        TicketVO ticketVO = new TicketVO();
        ticketVO.setAgentId(agentId);
        //构建textMessage消息体
        JSONObject messageJson = buildMessage(ticketVO, DepartEnum.ACTIVEDEPT.getDeptId(), new TextMessage(content), MsgTypeEnum.TEXT.getValue());
        //发送消息
        new Thread(new SendMsgThreadV2(messageJson, content,DepartEnum.ACTIVEDEPT.getCompanyId(), accessToken, new JSONObject(), false)).start();
    }

    /**
     * 向指定部门发送简单文本消息
     * 该方法只针对网吧钉钉体验站
     *
     * @param content
     * @param departmentId
     */
    public static void sendTextMsgToDeptAndUser(String content, String departmentId,String userids) {
        IsvTicketsService ticketService = SpringContextHolder.getBean(IsvTicketsService.class);
        //查询配置信息
        TicketVO ticketVO = ticketService.getIsvTicketVOByCompanyId(LoginUser.getUser().getCompanyID());
        ticketVO.setDepartmentId(departmentId);

        //构建textMessage消息体
        JSONObject messageJson = buildMessage(ticketVO, departmentId, userids,new TextMessage(content), MsgTypeEnum.TEXT.getValue());
        //发送消息
        new Thread(new SendMsgThreadV2(messageJson, content,DepartEnum.ACTIVEDEPT.getCompanyId(), ticketVO.getAccessToken(), new JSONObject(), true)).start();
    }


    /**
     * 构建OA消息体
     *
     * @param isvTicketVO
     * @author shisan
     * @date 2018/3/9 下午3:53
     */
    private static JSONObject buildMessage(TicketVO isvTicketVO, String departmentId, Object msgObject, String msgType) {
        JSONObject messageJson = new JSONObject();
        messageJson.put("toparty", departmentId);
        messageJson.put("agentid", isvTicketVO.getAgentId());
        messageJson.put("msgtype", msgType);
        messageJson.put(msgType, msgObject);
        return messageJson;
    }
    private static JSONObject buildMessage(TicketVO isvTicketVO, String departmentId, String userids,Object msgObject, String msgType) {
        JSONObject messageJson = new JSONObject();
        messageJson.put("toparty", departmentId);
        messageJson.put("touser", userids);
        messageJson.put("agentid", isvTicketVO.getAgentId());
        messageJson.put("msgtype", msgType);
        messageJson.put(msgType, msgObject);
        return messageJson;
    }

    private static JSONObject buildSingleMessage(TicketVO isvTicketVO, String dingUserId, Object msgObject, String msgType) {
        JSONObject messageJson = new JSONObject();
        messageJson.put("touser", dingUserId);
        messageJson.put("agentid", isvTicketVO.getAgentId());
        messageJson.put("msgtype", msgType);
        messageJson.put(msgType, msgObject);
        return messageJson;
    }

    private static JSONObject buildAllMessage(TicketVO isvTicketVO, Object msgObject) {
        JSONObject messageJson = new JSONObject();
        messageJson.put("to_all_user", "true");
        messageJson.put("agent_id", isvTicketVO.getAgentId());
//        messageJson.put("msgtype", msgType);
        messageJson.put("msg", msgObject);
        return messageJson;
    }


    private static JSONObject buildAllMessageActionCard(TicketVO isvTicketVO,String messageType,OAMessageActionCard  msgObject) {
        JSONObject messageJson = new JSONObject();
        messageJson.put("to_all_user", "true");
        messageJson.put("agent_id", isvTicketVO.getAgentId());
        messageJson.put("msgtype", messageType);
        messageJson.put("action_card", msgObject);
        return messageJson;
    }


    /**
     * 发送消息回调
     *
     * @param resultJson  发送消息的返回结果
     * @param companyId   公司id
     * @param msg         消息内容
     * @param storageFlag 是否将消息ruku
     * @author shisan
     * @date 2018/3/12 下午2:41
     */
    public static void sendMsgCallBack(JSONObject resultJson,Integer companyId, String msg,String content, boolean storageFlag) {
        Integer errcode = resultJson.getInteger("errcode");
        String errmsg =resultJson.getString("errmsg");
        if (Integer.valueOf(0).equals(errcode)) {
            if (companyId != null && storageFlag) {
                String messageId = resultJson.getString("messageId");
                OaMsgEntity msgEntity = new OaMsgEntity();
                msgEntity.setErrCode(errcode);
                msgEntity.setErrMsg(errmsg);
                msgEntity.setCompanyId(companyId);
                msgEntity.setContent(content);
                msgEntity.setMessageId(messageId);
                OaMsgMapper oaMsgMapper = SpringContextHolder.getBean(OaMsgMapper.class);
                oaMsgMapper.createOaMsg(msgEntity);
            }
        } else {
            checkSendSucc(resultJson, companyId);
        }
    }

    /**
     * 检查消息是否发送成功,失败则给开发部门发消息
     *
     * @param resultJson 消息发送结果
     * @param companyId   网吧Id
     * @author shisan
     * @date 2018/3/12 下午3:03
     */
    public static void checkSendSucc(JSONObject resultJson, Integer companyId) {
        if (!new Integer(0).equals(resultJson.getInteger("errcode"))) {
            String msg = "发送OA消息失败,公司id:" +companyId  + "返回结果:" + resultJson.toString();
            logger.error(msg);
        }
    }

    public static String getMessageContent(String content){
        StringBuffer sb = new StringBuffer();
        sb.append(content+"\n");
        sb.append("时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "\n");
        return sb.toString();
    }

    public static String getSimpleMessageContent(String content){
        StringBuffer sb = new StringBuffer();
        sb.append(content+"\n");
        return sb.toString();
    }

    public static void main(String[] args) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("errcode",0);
        jsonObject.put("errmsg","test");
        sendMsgCallBack(jsonObject,12,"just test","ssssssssss",false);
    }
}
