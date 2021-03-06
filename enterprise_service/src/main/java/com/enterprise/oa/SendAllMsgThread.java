package com.enterprise.oa;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.DDConstant;
import com.enterprise.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SendMsgThread
 *
 * @author shisan
 * @create 2018-03-09 下午3:57
 **/
public class SendAllMsgThread implements Runnable {

    private Logger logger = LoggerFactory.getLogger(SendAllMsgThread.class);

    private JSONObject msgJsonObject;

    private Integer companyId;

    private String accessToken;

    private JSONObject resultJson;

    private boolean storageFlag;

    private String content;

    public SendAllMsgThread(JSONObject msgJsonObject, String content, Integer companyId, String accessToken, JSONObject resultJson, boolean storageFlag) {
        this.msgJsonObject = msgJsonObject;
        this.content = content;
        this.companyId = companyId;
        this.accessToken = accessToken;
        this.resultJson = resultJson;
        this.storageFlag = storageFlag;
    }

    @Override
    public void run() {
        try {
            resultJson = HttpUtil.httpPost(DDConstant.SEND_OA_ASYNC_URL + accessToken, msgJsonObject);
            logger.info("发送OA结果:"+resultJson.toString());
        } finally {
            OAMessageUtil.sendMsgCallBack(resultJson, companyId, msgJsonObject.toJSONString(), content,storageFlag);
        }
    }

}
