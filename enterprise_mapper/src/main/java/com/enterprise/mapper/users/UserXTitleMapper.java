package com.enterprise.mapper.users;

import com.enterprise.base.entity.UserXTitleEntity;
import com.enterprise.base.vo.UserXTitleLibraryVO;
import com.enterprise.base.vo.UserXTitleVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by anson on 2018-06-28 10:05:39
 */
public interface UserXTitleMapper {

    Integer createUserXTitle(UserXTitleEntity userXTitleEntity);

    Integer updateUserXTitle(UserXTitleEntity userXTitleEntity);

//    List<UserXTitleVO> findUserXTitleByCompany(@Param("companyId") Integer companyId,@Param("userId") Integer userId);

    UserXTitleVO findUserXTitleByCompanyAndLibrary(@Param("companyId") Integer companyId,@Param("libraryId") Integer libraryId,@Param("userId") Integer userId,@Param("status") Integer status);

    Integer batchDeleteChooseFlag(@Param("companyId") Integer companyId,@Param("userId") Integer userId);

    Integer batchUpdate(@Param("userXTitleList") List<UserXTitleEntity> userXTitleList);

    /**
     * 获取用户佩戴的头衔
     * */
    String findUseWearTitleByCompany(@Param("companyId") Integer companyId,@Param("userId") Integer userId);

    UserXTitleVO findUserWearTitleByCompany(@Param("companyId") Integer companyId,@Param("userId") Integer userId);

    List<UserXTitleLibraryVO> getAllList(@Param("status") Integer status);

    UserXTitleEntity findById(@Param("id") Integer id);

    /**
     * 题库用户的头衔
     * */
    String findUserTitleByLibraryId(@Param("companyId") Integer companyId,@Param("userId") Integer userId,@Param("libraryId") Integer libraryId);
}