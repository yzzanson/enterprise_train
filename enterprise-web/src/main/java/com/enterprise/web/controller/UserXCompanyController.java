package com.enterprise.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.*;
import com.enterprise.service.right.UserRightGroupService;
import com.enterprise.service.user.UserXCompanyService;
import com.enterprise.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by anson on 18/3/26.
 */
@Controller
@RequestMapping("/user_company")
public class UserXCompanyController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private UserXCompanyService userXCompanyService;

    @Resource
    private UserRightGroupService userRightGroupService;

    private static final String OPER_CORPID = GlobalConstant.getOperCorpId();

    /**
     * 企业信息-获取企业列表
     * */
    @RequestMapping("/getCompanyList.json")
    @ResponseBody
    public JSONObject getCompanyList(String companyName,String corpId,String agentId,String startTime,String endTime,PageEntity pageEntity) {
        LoginUser  loginUser = LoginUser.getUser();
        try {
//            Integer isUserOper = userRightGroupService.getUserRightGroupCount(new UserRightGroupEntity(loginUser.getUserID(), RoleEnum.OPER.getValue(), StatusEnum.OK.getValue()));
//            if(isUserOper>0){
//                userId=null;
//            }
            if(!loginUser.getCorpID().equals(OPER_CORPID)){
                corpId = loginUser.getCorpID();
            }
            Date startTimeD = StringUtils.isNotEmpty(startTime)? DateUtil.parseDate(startTime):null;
            Date endTimeD = StringUtils.isNotEmpty(endTime)?DateUtil.parseDate(endTime):null;
            JSONObject jsonObject= userXCompanyService.findUserCompany(corpId,agentId,companyName,startTimeD,endTimeD,pageEntity);
            return jsonObject;
        } catch (Exception e) {
            logger.error("user_company-getCompanyList异常:"+e.getMessage());
        }
        return ResultJson.succResultJson("SUCCESS", null);
    }


}
