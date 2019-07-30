package com.enterprise.oa;

import com.enterprise.base.common.DDConstant;
import com.enterprise.base.common.GlobalConstant;
import com.enterprise.base.common.LoginUser;
import com.enterprise.base.enums.bury.OABuryEnum;
import com.enterprise.util.oa.message.OAMessage;

import java.net.URLEncoder;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/7/25 下午3:38
 */
public class WelcomeSingleThread implements Runnable {

    private String dingUserId;

    private String corpId;

    private Integer companyId;

    public WelcomeSingleThread(String dingUserId, String corpId,Integer companyId) {
        this.dingUserId = dingUserId;
        this.corpId = corpId;
        this.companyId = companyId;
    }

    @Override
    public void run() {
        try {

            String MOBILE_INDEX_PAGE = GlobalConstant.getIndexUrl()+GlobalConstant.OA_BURY_TYPE+ OABuryEnum.ENTER.getValue();
            String QRCODE_PAGE = GlobalConstant.getQrCOdePage();
            String WELCOME_WORD = "今天我来到这个世界,陪你成长";
            String WELCOME_PAGE = DDConstant.ARRANGE_STUDY;
            LoginUser loginUser = LoginUser.getUser();
            //OAMessageUtil.sendTextMsgToDeptAndUser(content, deptid,userid);
            OAMessage oaMessage = new OAMessage();
            String chall_page = String.format(MOBILE_INDEX_PAGE, corpId);
            String chall_qr_code = String.format(QRCODE_PAGE, URLEncoder.encode(chall_page));
            OAMessageUtil.sendOAMessageWithStroage(companyId, null, dingUserId, WELCOME_WORD, oaMessage.getOAMessageWithPic(chall_page, chall_qr_code, OAMessageUtil.getSimpleMessageContent(WELCOME_WORD), WELCOME_PAGE));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
