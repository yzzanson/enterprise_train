package com.enterprise.oa;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.DDConstant;
import com.enterprise.base.common.SpringContextHolder;
import com.enterprise.base.entity.OaMsgEntity;
import com.enterprise.mapper.oamsg.OaMsgMapper;
import com.enterprise.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SendMsgThread
 *
 * @author shisan
 * @create 2018-03-09 下午3:57
 **/
public class SendMsgLibraryLearnThread implements Runnable {

    private Logger logger = LoggerFactory.getLogger(SendMsgLibraryLearnThread.class);

    private JSONObject msgJsonObject;

    private Integer companyId;

    private Integer libraryId;

    private String accessToken;

    private JSONObject resultJson;

    private boolean storageFlag;

    public SendMsgLibraryLearnThread(JSONObject msgJsonObject, Integer companyId, Integer libraryId,String accessToken, JSONObject resultJson, boolean storageFlag) {
        this.msgJsonObject = msgJsonObject;
        this.companyId = companyId;
        this.accessToken = accessToken;
        this.resultJson = resultJson;
        this.storageFlag = storageFlag;
        this.libraryId = libraryId;
    }

    @Override
    public void run() {
        try {
            resultJson = HttpUtil.httpPost(DDConstant.SEND_OA_ASYNC_URL + accessToken, msgJsonObject);
            logger.info("发送OA结果:"+resultJson.toString());
        } finally {
            sendMsgCallBack(resultJson, companyId, libraryId,msgJsonObject.toJSONString(), storageFlag);
        }
    }

    public static void sendMsgCallBack(JSONObject resultJson,Integer companyId,Integer libraryId, String msg, boolean storageFlag) {
        Integer errcode = resultJson.getInteger("errcode");
        String errmsg =resultJson.getString("errmsg");
        if (Integer.valueOf(0).equals(errcode)) {
            if (companyId != null && storageFlag) {
                String messageId = resultJson.getString("messageId");
                OaMsgEntity msgEntity = new OaMsgEntity();
                msgEntity.setErrCode(errcode);
                msgEntity.setErrMsg(errmsg);
                msgEntity.setCompanyId(companyId);
                msgEntity.setContent(msg);
                msgEntity.setMessageId(messageId);
                msgEntity.setLibraryId(libraryId);
                OaMsgMapper oaMsgMapper = SpringContextHolder.getBean(OaMsgMapper.class);
                oaMsgMapper.createOaMsg(msgEntity);
            }
        } else {
            OAMessageUtil.checkSendSucc(resultJson, companyId);
        }
    }


}
