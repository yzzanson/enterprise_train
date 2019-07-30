package com.enterprise.mapper.remind;

import com.enterprise.base.entity.StudyRemindConfEntity;
import com.enterprise.base.vo.studyRemind.StudyRemindConfVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * 设置提醒内容
 * Created by anson on 2018-03-23 10:45:39
 */
public interface StudyRemindConfMapper {

    Integer createOrUpdateStudyRemind(StudyRemindConfEntity studyRemindConfEntity);

    Integer doUpdate(@Param("companyId") Integer companyId, @Param("content") String content,@Param("isOpen") Integer isOpen,@Param("updateTime") Date updateTime);

    StudyRemindConfVO getByCompanyId(@Param("companyId") Integer companyId);
}