package com.enterprise.mapper.remind;

import com.enterprise.base.entity.StudyRemindEntity;
import com.enterprise.base.vo.studyRemind.StudyRemindVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 学习提醒记录
 * Created by anson on 2018-03-23 10:45:39
 */
public interface StudyRemindMapper {

    Integer createOrUpdateStudyRemind(StudyRemindEntity studyRemindEntity);

    Integer batchInsertStudyRemind(@Param("studyRemindList")List<StudyRemindEntity> studyRemindList);
    Integer batchInsertStudyRemindDept(@Param("studyRemindList")List<StudyRemindEntity> studyRemindList);

    /**
     * 获取提醒过的用户,不管是否删除
     * */
    List<StudyRemindEntity> getRemindUserIdList(@Param("companyId") Integer companyId, @Param("libraryId") Integer libraryId, @Param("type") Integer type, @Param("status") Integer status);

    Integer batchSafeDelete(@Param("companyId") Integer companyId, @Param("libraryId") Integer libraryId, @Param("status") Integer status,@Param("updateTime") Date updateTime);

    Integer updateStudyRemind(@Param("id") Integer id, @Param("status") Integer status, @Param("updateTime") Date updateTime);

    StudyRemindEntity getByCondition(@Param("companyId") Integer companyId, @Param("libraryId") Integer libraryId,@Param("departmentId") Integer departmentId,@Param("userId") Integer userId);

    List<StudyRemindVO> getListByCompanyIdAndLibraryIdAndType(@Param("companyId") Integer companyId, @Param("libraryId") Integer libraryId,@Param("type") Integer type);

    String getSendedIds(@Param("libraryId") Integer libraryId, @Param("type") Integer type);

    List<Integer> getRemindUserIds(@Param("companyId") Integer companyId, @Param("libraryId") Integer libraryId,@Param("status") Integer status);

    /**
     * 批量修改用户学习记录
     * */
    Integer batchUpdateStudyRemind(@Param("studyRemindList")List<StudyRemindEntity> studyRemindList);

}