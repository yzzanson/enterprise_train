package com.enterprise.service.remind;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.entity.StudyRemindEntity;

import java.util.List;
import java.util.Set;

/**
 * Created by anson on 18/4/2.
 */
public interface StudyRemindService {

    JSONObject saveOrUpdate(Integer libraryId,String deptids,String userids);

    /**
     * @Description 获取题库总学习人or部门详情列表
     * @Param libraryId 题库id
     * */
    JSONObject getArrangements(Integer libraryId);

    /**
     * @Description 获取安排学习列表
     * @Param libraryId 题库id
     * @Param type 类型0/1部门 成员
     * */
    JSONObject getArrangDetail(Integer libraryId, Integer departmentId,Integer type) throws Exception;

    /**
     * 搜索成员安排学习情况
     * */
    JSONObject search(Integer libraryId, String search);

    /**
     * 获取用户是否提醒过
     * */
    StudyRemindEntity getByCondition(Integer companyId,Integer libraryId, Integer userId);

    /**
     * 批量插入用户学习数据
     * */
    Integer batchInsertStudyRemind(List<StudyRemindEntity> studyRemindList);

    /**
     * 批量更新用户学习提醒
     * */
    Integer batchUpdateStudyRemind(List<StudyRemindEntity> studyRemindList);


    /**
     * 获取公司下存在提醒记录的用户
     * */
    List<Integer> getRemindUserIds(Integer companyId,Integer libraryId,Integer status);


    Set<Integer> getSubDepartment(Integer companyId,String deptids);

}

