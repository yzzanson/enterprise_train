package com.enterprise.service.dingCall.impl;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.ResultJson;
import com.enterprise.base.entity.CompanyInfoEntity;
import com.enterprise.base.entity.IsvTicketsEntity;
import com.enterprise.base.entity.SuitesEntity;
import com.enterprise.mapper.companyInfo.CompanyInfoMapper;
import com.enterprise.mapper.isvTickets.IsvTicketsMapper;
import com.enterprise.mapper.suites.SuitesMapper;
import com.enterprise.service.dingCall.DingCallService;
import com.enterprise.util.dingtalk.AuthHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/4/18 下午4:42
 */
@Service
public class DingCallServiceImpl implements DingCallService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String SUITE_KEY = "suiteejgaehk1pjpcrpip";

    @Resource
    private SuitesMapper suitesMapper;

    @Resource
    private CompanyInfoMapper companyInfoMapper;

    @Resource
    private IsvTicketsMapper isvTicketsMapper;

    //设置管理员
    @Override
    public JSONObject setCallUserList(String userIds) {
        SuitesEntity suitesEntity = suitesMapper.getSuiteByKey(SUITE_KEY);
        return AuthHelper.setCallUserList(suitesEntity.getSuiteAccessToken(), userIds);
    }

    //设置接入人
    @Override
    public JSONObject setAuthUser() {
        StringBuffer successCorpBuf = new StringBuffer();
        StringBuffer failCorpBuf = new StringBuffer();
        SuitesEntity suitesEntity = suitesMapper.getSuiteByKey(SUITE_KEY);
        String suiteTicket = suitesEntity.getSuiteTicket();
        String suiteKey = suitesEntity.getSuiteKey();
        String suiteSecrect = suitesEntity.getSuiteSecret();
        List<CompanyInfoEntity> companyInfoList = companyInfoMapper.getAllCompanys();
        for (int i = 0; i < companyInfoList.size(); i++) {
            Integer companyId = companyInfoList.get(i).getId();
            IsvTicketsEntity isvTicketEntity = isvTicketsMapper.getIsvTicketByCompanyId(companyId);
            String corpId = isvTicketEntity.getCorpId();
            String authUserInfo = AuthHelper.getAuthInfo(suiteTicket, suiteKey, suiteSecrect, corpId);
            if(StringUtils.isNotEmpty(authUserInfo)){
                logger.info("isvid:"+isvTicketEntity.getId()+"corp:"+corpId+",authUserInfo"+authUserInfo);
                isvTicketEntity.setAuthUserId(authUserInfo);
                isvTicketsMapper.modifyIsvTickets(isvTicketEntity);
                successCorpBuf.append(",").append(corpId);
            }else{
                failCorpBuf.append(",").append(corpId);
            }
        }
        Map<String,String> resultMap = new HashMap<>();
        resultMap.put("success",successCorpBuf.toString());
        resultMap.put("fail",failCorpBuf.toString());
        return ResultJson.succResultJson(resultMap);
    }

    @Override
    public JSONObject setAuthUserSingle(Integer companyId) {
        SuitesEntity suitesEntity = suitesMapper.getSuiteByKey(SUITE_KEY);
        String suiteTicket = suitesEntity.getSuiteTicket();
        String suiteKey = suitesEntity.getSuiteKey();
        String suiteSecrect = suitesEntity.getSuiteSecret();
        IsvTicketsEntity isvTicketEntity = isvTicketsMapper.getIsvTicketByCompanyId(companyId);
        String corpId = isvTicketEntity.getCorpId();
        String authUserInfo = AuthHelper.getAuthInfo(suiteTicket, suiteKey, suiteSecrect, corpId);
        if(StringUtils.isNotEmpty(authUserInfo)){
            logger.info("isvid:"+isvTicketEntity.getId()+"corp:"+corpId+",authUserInfo"+authUserInfo);
            isvTicketEntity.setAuthUserId(authUserInfo);
            isvTicketsMapper.modifyIsvTickets(isvTicketEntity);
            return ResultJson.succResultJson(authUserInfo);
        }else{
            return ResultJson.errorResultJson(companyId);
        }
    }

    @Override
    public JSONObject setIsCall() {
//        String suiteSecrect = suitesEntity.getSuiteSecret();
        StringBuffer failCorpBuf = new StringBuffer();
        List<CompanyInfoEntity> companyInfoList = companyInfoMapper.getAllCompanys();
        failCorpBuf.append("errorCorp:");

        for (int i = 0; i < companyInfoList.size(); i++) {
            Integer companyId = companyInfoList.get(i).getId();
            IsvTicketsEntity isvTicketEntity = isvTicketsMapper.getIsvTicketByCompanyId(companyId);
            try {
                Integer isCall = AuthHelper.checkCall(isvTicketEntity.getCorpAccessToken());
                isvTicketEntity.setIsCall(isCall);
                logger.info("companyId:"+companyId+",isCall"+isCall);
                isvTicketsMapper.modifyIsvTickets(isvTicketEntity);
                if (isCall.equals(0)) {
                    failCorpBuf.append(",").append(isvTicketEntity.getCorpId());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ResultJson.succResultJson(failCorpBuf.toString());
    }

    @Override
    public JSONObject setIsCallSingle(Integer companyId) {
//        String suiteSecrect = suitesEntity.getSuiteSecret();
        try {
            StringBuffer failCorpBuf = new StringBuffer();
            failCorpBuf.append("errorCorp:");
            IsvTicketsEntity isvTicketEntity = isvTicketsMapper.getIsvTicketByCompanyId(companyId);
            Integer isCall = AuthHelper.checkCall(isvTicketEntity.getCorpAccessToken());
            isvTicketEntity.setIsCall(isCall);
            logger.info("companyId:"+companyId+",isCall"+isCall);
            isvTicketsMapper.modifyIsvTickets(isvTicketEntity);
            if (isCall!=null && isCall.equals(0)) {
                failCorpBuf.append(",").append(isvTicketEntity.getCorpId());
                return ResultJson.succResultJson(companyId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultJson.errorResultJson(companyId);
    }

    @Override
    public JSONObject raiseCall(String corpId, String raiseUserId) {
        SuitesEntity suitesEntity = suitesMapper.getSuiteByKey(SUITE_KEY);
        String suiteAccessToken = suitesEntity.getSuiteAccessToken();
        IsvTicketsEntity isvTicketEntity = isvTicketsMapper.getIsvTicketByCorpId(corpId);//auth_user_id
        String staffId = isvTicketEntity.getAuthUserId();
        if (StringUtils.isEmpty(staffId)) {
            staffId = AuthHelper.getAuthInfo(suitesEntity.getSuiteTicket(), suitesEntity.getSuiteKey(), suitesEntity.getSuiteSecret(), corpId);
            if (StringUtils.isEmpty(staffId)) {
                return ResultJson.errorResultJson("不存在的管理员id");
            }
        }
        JSONObject resultJson = AuthHelper.raiseCallUserList(suiteAccessToken, raiseUserId, corpId, staffId);
        if (resultJson != null) {
            Integer errcodeCode = resultJson.getInteger("errcode");
            if (errcodeCode!=null && errcodeCode.equals(0)) {
                return ResultJson.succResultJson("success");
            } else {
                String errMessage = resultJson.getString("errmsg");
                return ResultJson.errorResultJson(errcodeCode + errMessage);
            }
        }else{
            return ResultJson.errorResultJson("fail");
        }
    }

    public static void main(String[] args) {
        String str1 = ResultJson.succResultJson("success").toJSONString();

        String str2= ResultJson.errorResultJson("400127" + "企业未授权ISV拨打管理员").toJSONString();
        System.out.println(str1);
        System.out.println(str2);

    }
}
