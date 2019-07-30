package com.enterprise.mobile.web.job;

import com.enterprise.base.entity.CommunityInviteDetailEntity;
import com.enterprise.mapper.community.CommunityInviteDetailMapper;
import com.enterprise.mapper.companyInfo.CompanyInfoMapper;
import com.enterprise.oa.OAMessageUtil;
import com.enterprise.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Description 剔除开通社区申请的用户
 * @Author zezhouyang
 * @Date 18/10/23 上午11:28
 */
@Component
public class CommunityJob {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private CommunityInviteDetailMapper communityInviteDetailMapper;

    @Resource
    private CompanyInfoMapper companyInfoMapper;

    /**
     * 每天九点通知昨天开通的企业数据
     */
    public void informCommunity() throws InterruptedException {
        String dayDate = DateUtil.getDateYMD(DateUtil.getSeveralDaysLaterDate(new Date(), -1));
        List<CommunityInviteDetailEntity> inviteCommunityList = communityInviteDetailMapper.getCommunityInviteByCompanyAndDay(dayDate);
        Integer userCount = inviteCommunityList.size();
        Integer loopCount = userCount % 3 == 0 ? userCount / 3 : 1 + (userCount / 3);
        StringBuffer sendMessageBuf = new StringBuffer();
        sendMessageBuf.append(dayDate).append(" 社区开通报名").append("\n");
        for (int i = 0; i < loopCount; i++) {
            Integer startIndex = i * 3;
            Integer endIndex = 0;
            if (loopCount <= 1) {
                endIndex = userCount;
            } else {
                if (i < loopCount - 1) {
                    endIndex = (i + 1) * 3;
                } else {
                    endIndex = userCount;
                }
            }
            for (int j = startIndex; j < endIndex; j++) {
                CommunityInviteDetailEntity communityInviteDetailEntity = inviteCommunityList.get(j);
                String companyName = companyInfoMapper.getCompanyNameById(communityInviteDetailEntity.getCompanyId());
                String subMessage = companyName + "/" + communityInviteDetailEntity.getUserName() + "/" + communityInviteDetailEntity.getPhoneNum();
                sendMessageBuf.append(subMessage).append("\n");
            }
            logger.info("第" + i + "次发送开通社区OA消息:" + sendMessageBuf.toString());
            OAMessageUtil.sendTextMsgToDept(sendMessageBuf.toString());
            sendMessageBuf = new StringBuffer(200);
            Thread.sleep(500);
        }
    }

    public static void main(String[] args) {
//        String dayDate = DateUtil.getDateYMD(DateUtil.getSeveralDaysLaterDate(new Date(), -1));
//        System.out.println(dayDate);
//        StringBuffer sendMessageBuf = new StringBuffer();
//        sendMessageBuf.append("2019.3.30 社区开通报名").append("\n");
//        sendMessageBuf.append("江西瑞拓建设工程有限公司").append("/刘卫华").append("/15170072630").append("\n");
//        sendMessageBuf.append("中国民用航空东北地区管理局").append("/王峥").append("/13940091948").append("\n");
//        OAMessageUtil.sendTextMsgToDept("179317278","91323d8f6f27332cb0129fc820f4a21b",sendMessageBuf.toString());
    }

}
