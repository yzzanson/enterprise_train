package com.enterprise.isv.thread;

import com.enterprise.base.common.DDConstant;
import com.enterprise.base.common.GlobalConstant;
import com.enterprise.base.common.SpringContextHolder;
import com.enterprise.base.entity.IsvTicketsEntity;
import com.enterprise.base.vo.UserVO;
import com.enterprise.oa.OAMessageUtil;
import com.enterprise.service.isvTickets.IsvTicketsService;
import com.enterprise.service.user.UserXCompanyService;
import com.enterprise.util.oa.message.OAMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.util.List;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/7/25 下午3:38
 */
public class WelcomeThread implements Runnable {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private String corpId;

    private IsvTicketsService isvTicketsService = SpringContextHolder.getBean(IsvTicketsService.class);

    private UserXCompanyService userXCompanyService = SpringContextHolder.getBean(UserXCompanyService.class);

    public WelcomeThread(String corpId) {
        this.corpId = corpId;
    }

    @Override
    public void run() {
        try {
            //给全员发送OA
            String MOBILE_INDEX_PAGE = GlobalConstant.getIndexUrl();
            String QRCODE_PAGE = GlobalConstant.getQrCOdePage();
            String WELCOME_WORD = "今天我来到这个世界,陪你成长";
            String WELCOME_PAGE = DDConstant.ARRANGE_STUDY;
            OAMessage oaMessage = new OAMessage();
            IsvTicketsEntity isvTicketsEntity = isvTicketsService.getIsvTicketByCorpId(corpId);
            Integer companyId = isvTicketsEntity.getCompanyId();
            String welcome_page = String.format(MOBILE_INDEX_PAGE, corpId);
            String welcome_qr_code = String.format(QRCODE_PAGE, URLEncoder.encode(welcome_page));
            logger.info("发送全员首次接入消息:" + WELCOME_WORD);
            //10个人发送一次
            StringBuffer dingUserSbf = new StringBuffer(200);
            List<UserVO> userList = userXCompanyService.getUserByCorpId(corpId);
            Integer userCount = userList.size();
            Integer loopCount = userCount%10 ==0 ? userCount/10 : 1+(userCount/10);
            for (int i = 0; i < loopCount; i++) {
                Integer startIndex = i*10;
                Integer endIndex = 0;
                if(loopCount<=1){
                    endIndex = userList.size();
                }else{
                    if(i<loopCount-1){
                        endIndex = (i+1)*10;
                    }else{
                        endIndex =  userCount;
                    }
                }
                for(int j=startIndex;j<endIndex;j++){
                    if(j==startIndex){
                        dingUserSbf.append(userList.get(j).getDingUserId());
                    }else{
                        dingUserSbf.append("|").append(userList.get(j).getDingUserId());
                    }
                }
                logger.info("第" + i + "次发送全体OA消息:" + dingUserSbf.toString());
                OAMessageUtil.sendOAMessageWithStroage(companyId, dingUserSbf.toString(), WELCOME_WORD, oaMessage.getOAMessageWithPic(welcome_page, welcome_qr_code, OAMessageUtil.getMessageContent(WELCOME_WORD), WELCOME_PAGE));
                dingUserSbf = new StringBuffer(200);
                Thread.sleep(500);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
