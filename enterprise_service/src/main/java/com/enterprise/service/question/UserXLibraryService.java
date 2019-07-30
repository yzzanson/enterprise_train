package com.enterprise.service.question;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.PageEntity;
import com.enterprise.base.entity.UserXLibraryEntity;

import java.util.List;


/**
 * @Description UserService
 * @Author anson
 * @Date 2018/3/26 下午14:47
 */
public interface UserXLibraryService {

    JSONObject createOrUpdateUserXLibrary(UserXLibraryEntity userXLibraryEntity);

    /**
     * 后台获取用户学习进度列表
     * */
    JSONObject findUserStudySchedule(Integer libraryId,PageEntity pageEntity);

    /**
     * 前台获取用户所有题库的学习进度
     * */
    JSONObject getUserStudyProcess(Integer userId,Integer companyId,Integer type);

    /**
     * 前台获取用户钉钉官方题库的学习进度
     * */
    JSONObject getDingLibraryProgress(Integer companyId,Integer userId);

    /**
     * 获取用户在某个库中的学习进度
     * */
    UserXLibraryEntity getByUserIdAndLibraryId(Integer companyId,Integer userId,Integer libraryId);


    /**
     * 批量插入用户题库数据
     * */
    Integer batchInsertUserXLibrary(List<UserXLibraryEntity> addUserXLibraryList);

    /**
     * 获取所有有该题库的用户列表
     * */
    List<Integer> getUserByCorpIdAndLibraryId(Integer companyId,String corpId,Integer libraryId);

    /**
     * 更新题库更新状态(标记其更新过)
     * */
    Integer batchUpdateState(Integer libraryId,Integer state);

    /**
     * 获取学习该题库用户列表

    List<UserXLibraryEntity> getByLibraryId(Integer libraryId,Integer status);
     * */

    List<UserXLibraryEntity> getByLibraryId(Integer libraryId);

    /**
     * 用户
     * */
    JSONObject getUserTotalStudy();

    /**
     * 更新用户完成时间
     * */
    JSONObject updateFinishTime();
    /**
     * 更新用户完成时间
     * */
    JSONObject updateFinishTime2();

}
