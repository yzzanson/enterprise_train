package com.enterprise.web.job;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.taobao.api.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Author zezhouyang
 * @Date 19/5/29 上午10:17
 */
@Component
public class PriceJob {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public void sendPriceMsg() throws ApiException {
//        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/robot/send?access_token=566cc69da782ec33e42541b09b08551f09fbe864eb8008112e994b43887");
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/robot/send?access_token=3ce8be37c7362113cedab229ff95e09ad77d73940c91b7e7ee84317d932fff36");
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype("markdown");
        OapiRobotSendRequest.Markdown markdown = new OapiRobotSendRequest.Markdown();
        markdown.setTitle("每日精进收费详情");
        markdown.setText("##### 每日精进收费详情 \n>" +
                "有任何问题请咨询\n\n" +
                "![screenshot](https://neixun.forwe.store/static/neixun/1559095747587lALPDgQ9qoNCkyzNCATNAu4_750_2052.png)\n");
        request.setMarkdown(markdown);
        OapiRobotSendResponse response = client.execute(request);
    }

    public static void main(String[] args) {
        try {
            new PriceJob().sendPriceMsg();
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

}
