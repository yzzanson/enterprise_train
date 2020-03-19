package com.enterprise.service.rank.impl;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.GlobalConstant;
import com.enterprise.base.common.MobileLoginUser;
import com.enterprise.base.common.ResultJson;
import com.enterprise.base.entity.RankPraiseEntity;
import com.enterprise.base.entity.UserXCompany;
import com.enterprise.base.enums.ResultCodeEnum;
import com.enterprise.mapper.rank.RankPraiseMapper;
import com.enterprise.mapper.users.UserXCompanyMapper;
import com.enterprise.oa.OAMessageUtil;
import com.enterprise.service.rank.RankPraiseService;
import com.enterprise.util.DateUtil;
import com.enterprise.util.oa.message.OAMessage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.util.Date;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/8/14 下午3:20
 */
@Service
public class RankPraiseServiceImpl implements RankPraiseService {

    private static final String PRAISE_MESSAGE_CONTNET = "优秀，藏都藏不住";

    private static final String PRAISE_TITLE = "%s刚刚赞了你";

    private static final String MAIN_PAGE = GlobalConstant.getIndexUrl();

    private static final String MESSAGE_URL = "https://neixun.forwe.store/static/neixun/153447429335720180507114129403.png";

    private String QRCODE_PAGE = GlobalConstant.getQrCOdePage();

    @Resource
    private RankPraiseMapper rankPraiseMapper;

    @Resource
    private UserXCompanyMapper userXCompanyMapper;


    @Override
    public JSONObject praise(Integer userId) {
        MobileLoginUser mobileLoginUser = MobileLoginUser.getUser();
        if (userId.equals(mobileLoginUser.getUserID())) {
            return ResultJson.finishResultJson(ResultCodeEnum.NO_PERMISSION.getValue(), "fail", "留给别人来点赞吧~");
        }
        Integer companyId = mobileLoginUser.getCompanyID();
        //判断是否点赞过,点赞过则不再点
        RankPraiseEntity rankPraiseEntity = rankPraiseMapper.getByCompanyAndUser(companyId,mobileLoginUser.getUserID(),userId);
        if(rankPraiseEntity!=null){
            //return ResultJson.kickoutResultJson("请不要重复点赞~");
            return ResultJson.finishResultJson(ResultCodeEnum.KICKED_OUT.getValue(), "fail", "请不要重复点赞~");
        }

        Integer result = rankPraiseMapper.createRankPraise(new RankPraiseEntity(companyId, mobileLoginUser.getUserID(), userId, new Date()));
        if (result > 0) {
            //发送OA消息
            UserXCompany userXCompany = userXCompanyMapper.getDingIdByCorpIdAndUserId(mobileLoginUser.getCorpID(), userId);
            if (userXCompany!=null) {
                String praiseTitle = String.format(PRAISE_TITLE, mobileLoginUser.getDingName());
                praiseTitle+="\n"+ DateUtil.getDate_Y_M_D_H_M_S();
                OAMessage oaMessage = new OAMessage();
                String mainPage = String.format(MAIN_PAGE,mobileLoginUser.getCorpID());
                String mainPageQrcode = String.format(QRCODE_PAGE, URLEncoder.encode(mainPage));
                OAMessageUtil.sendOAMessageWithStroageV2(companyId, userXCompany.getDingUserId(), "",oaMessage.getSimpleOAMessage(mainPage,mainPageQrcode,praiseTitle));
            }
            return ResultJson.finishResultJson(ResultCodeEnum.SUCCESS.getValue(), "success", "点赞成功");
        }
        return ResultJson.finishResultJson(ResultCodeEnum.FAIL.getValue(), "fail", "点赞失败");
    }

    @Override
    public Integer deleteExpireData(Date date) {
        return rankPraiseMapper.deleteExpireData(date);
    }

}
