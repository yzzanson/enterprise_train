package com.enterprise.mapper.users;

import com.enterprise.base.entity.UserXLibraryEntity;
import com.enterprise.base.vo.UserDeptVO;
import com.enterprise.base.vo.UserXLibraryScheduleVO;
import com.enterprise.base.vo.UserXLibraryVO;
import com.enterprise.base.vo.UserXLibraryVO2;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by null on 2018-03-23 11:23:39
 */
public interface UserXLibraryMapper {

    Integer createOrUpdateUserXLibrary(UserXLibraryEntity userXLibraryEntity);

    Integer updateUserXLibrary(UserXLibraryEntity userXLibraryEntity);

    Integer updateFinishTime(UserXLibraryEntity userXLibraryEntity);

    Integer batchDelete(UserXLibraryEntity userXLibraryList);
    /**
     * 统计当前题库的学习人数
     * */
    Integer getStudyCountByLibraryId(@Param("companyId") Integer companyId,@Param("libraryId") Integer libraryId);


    List<UserXLibraryVO> findUserStudySchedule(@Param("libraryId") Integer libraryId);

    //List<UserXLibraryVO2> getCompanyLibraryByUser(@Param("userId") Integer userId,@Param("companyId") Integer companyId);

    /**
     * 获取默认的官方题库
     * */
//    List<UserXLibraryVO2> getDefaultPublicQuestionLibrary(@Param("companyId") Integer companyId);

    List<UserXLibraryVO2> getDefaultDingPublicQuestionLibrary(@Param("companyId") Integer companyId,@Param("userId") Integer userId,@Param("search") String search);

    List<UserXLibraryVO2> getPublicLibraryByUser(@Param("userId") Integer userId,@Param("companyId") Integer companyId);

    UserXLibraryEntity getByUserIdAndLibraryId(@Param("companyId") Integer companyId,@Param("userId") Integer userId,@Param("libraryId") Integer libraryId);

    List<UserXLibraryEntity> getByLibraryId(@Param("libraryId") Integer libraryId,@Param("status") Integer status);

    Integer updateSchedule(UserXLibraryEntity userXLibraryEntity);

    List<UserDeptVO> getChallengeUser(@Param("libraryId") Integer libraryId,@Param("corpId") String corpId,@Param("userId") Integer userId,@Param("search") String search);

    /**
     * 获取题库学习的用户id
     * */
    List<Integer> getUserIdsByLibraryId(@Param("libraryId") Integer libraryId);

    /**
     * 批量导入用户题库
     * */
    Integer batchInsertuserxlibrary(@Param("userXLibraryList") List<UserXLibraryEntity> userXLibraryList);

    /**
     * 获取某个公司内部某个题库的状态
     * */
    List<Integer> getUserByCorpIdAndLibraryId(@Param("compnayId") Integer compnayId,@Param("corpId") String corpId,@Param("libraryId") Integer libraryId);


    List<Integer> getByCompanyIdAndUserId(@Param("compnayId") Integer compnayId,@Param("userId") Integer userId,@Param("status") Integer status);

    /**
     * 更新题库更新过状态
     * */
    Integer batchUpdateState(@Param("libraryId") Integer libraryId,@Param("isUpdate") Integer isUpdate);

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * 同步用
     * */
    Integer getTotalCountInDB();

    /**
     * 同步用
     * */
    List<UserXLibraryEntity> getCountRecord(@Param("start") Integer start,@Param("end") Integer end);


//    Integer batchUpdate(@Param("userXLibraryList") List<UserXLibraryEntity> userXLibraryList);

//    Integer batchUpdate2(@Param("userXLibraryList") List<UserXLibraryEntity> userXLibraryList);

    Integer batchUpdateNew(@Param("userXLibraryList") List<UserXLibraryEntity> userXLibraryList);

    List<UserDeptVO> getAllUser(@Param("corpId") String corpId,@Param("search") String search);

    /**
     * 根据是否完成题库学习查询列表 type  1 完成  0 未完成
     * */
    List<UserXLibraryEntity> getStudyCountByLibraryIdAndSchedule(@Param("companyId") Integer companyId,@Param("libraryId") Integer libraryId,@Param("type") Integer type);

    /**
     * 用户对题库中题一次正确答题数
     * */
    Integer getCorrectCount(@Param("companyId") Integer companyId,@Param("userId") Integer userId,@Param("libraryId") Integer libraryId);

    /**
     * 题库列表之 热门题库 or 企业知识
     * */
    List<UserXLibraryVO2> getCommonUserLibrary(@Param("companyId") Integer companyId,@Param("userId") Integer userId,@Param("type") Integer type);

    /**
     * 题库列表之 我的成就
     * */
    List<UserXLibraryVO2> getAchieveUserLibrary(@Param("companyId") Integer companyId,@Param("userId") Integer userId);

    /**
     * 获取回答完的用户题库
     * */
    List<UserXLibraryEntity> getFinishedList();

    /**
     * 获取回答完的用户但是没有完成时间
     * */
    List<UserXLibraryEntity> getFinishedNoTimeList();


    /**
     * 完成的官方题库
     * */
    List<UserXLibraryScheduleVO> getUserAchievePublicLibraryList(@Param("companyId") Integer companyId);

    /**
     * 完成的官方题库名
     * */
    String getUserAchievePublicLibrary(@Param("companyId") Integer companyId,@Param("userId") Integer userId);

}