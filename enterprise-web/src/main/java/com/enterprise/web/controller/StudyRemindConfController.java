package com.enterprise.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.BaseController;
import com.enterprise.base.common.LoginUser;
import com.enterprise.base.common.ResultJson;
import com.enterprise.base.entity.StudyRemindConfEntity;
import com.enterprise.service.remind.StudyRemindConfService;
import com.enterprise.util.AssertUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * StudyRemindController
 *
 * @author anson
 * @create 2018-04-23 上午9:57
 **/
@Controller
@RequestMapping("/study_remind_conf")
public class StudyRemindConfController extends BaseController {

    @Resource
    private StudyRemindConfService studyRemindConfService;

    @RequestMapping(value="/saveOrUpdate.json",method= RequestMethod.POST)
    @ResponseBody
    public JSONObject saveOrUpdate(StudyRemindConfEntity studyRemindConfEntity) {
        if(StringUtils.isNotEmpty(studyRemindConfEntity.getContent())) {
            AssertUtil.isTrue(studyRemindConfEntity.getContent().length() <= 15, "提醒内容超过限制!");
        }
        Integer companyId = LoginUser.getUser().getCompanyID();
        studyRemindConfEntity.setCompanyId(companyId);
        Integer result = studyRemindConfService.createOrUpdateStudyRemind(studyRemindConfEntity);
        if(result>0){
            return studyRemindConfService.getByCompanyId(studyRemindConfEntity.getCompanyId());
        }
        return ResultJson.errorResultJson("新增or更新失败");
    }

    @RequestMapping("/getByCompany.json")
    @ResponseBody
    public JSONObject getByCompany() {
        Integer companyId = LoginUser.getUser().getCompanyID();
        return ResultJson.succResultJson(studyRemindConfService.getByCompanyId(companyId));
    }



}
