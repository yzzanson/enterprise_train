package com.enterprise.service.remind.impl;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.ResultJson;
import com.enterprise.base.entity.StudyRemindConfEntity;
import com.enterprise.mapper.remind.StudyRemindConfMapper;
import com.enterprise.service.remind.StudyRemindConfService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/4/23 上午9:45
 */
@Service
public class StudyRemindConfServiceImpl implements StudyRemindConfService {

    @Resource
    private StudyRemindConfMapper studyRemindConfMapper;

    @Override
    public Integer createOrUpdateStudyRemind(StudyRemindConfEntity studyRemindConfEntity) {
        if(studyRemindConfMapper.getByCompanyId(studyRemindConfEntity.getCompanyId())!=null){
            return studyRemindConfMapper.doUpdate(studyRemindConfEntity.getCompanyId(), studyRemindConfEntity.getContent(), studyRemindConfEntity.getIsOpen(),new Date());
        }

        return studyRemindConfMapper.createOrUpdateStudyRemind(studyRemindConfEntity);
    }

    @Override
    public JSONObject getByCompanyId(Integer companyId) {
        return ResultJson.succResultJson(studyRemindConfMapper.getByCompanyId(companyId));
    }
}
