package com.enterprise.service.remind;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.entity.StudyRemindConfEntity;

/**
 * Created by anson on 18/4/2.
 */
public interface StudyRemindConfService {

    Integer createOrUpdateStudyRemind(StudyRemindConfEntity studyRemindConfEntity);

    JSONObject getByCompanyId(Integer companyId);
}
