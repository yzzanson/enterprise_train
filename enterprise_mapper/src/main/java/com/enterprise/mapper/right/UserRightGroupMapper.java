package com.enterprise.mapper.right;

import com.enterprise.base.entity.right.UserRightGroupEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by anson on 18/3/23.
 */
public interface UserRightGroupMapper {

    Integer createOrUpdateUserRightGroup(UserRightGroupEntity userRightGroupEntity);

    UserRightGroupEntity findUserRightGroupByGroupId(UserRightGroupEntity userRightGroupEntity);

    Integer updateUserRightGroup(UserRightGroupEntity userRightGroupEntity);

    List<UserRightGroupEntity> findUserRightGroups(UserRightGroupEntity userRightGroupEntity);

    List<UserRightGroupEntity> findUserRightGroups2(UserRightGroupEntity userRightGroupEntity);

    Integer getUserRightGroupCount(UserRightGroupEntity userRightGroupEntity);

    Integer getUserGroupByCompanyIdAndUserId(@Param("companyId") Integer companyId,@Param("userId") Integer userId);

    Integer batchInsert(@Param("insertList") List<UserRightGroupEntity> insertList);

    Integer batchUpdate(@Param("updateList") List<UserRightGroupEntity> updateList);

    Integer batchDeleteByGroupId(@Param("groupId") Integer groupId);

    List<Integer> getUserRightGroupByGroupId(@Param("groupId") Integer groupId,@Param("companyId") Integer companyId);

}
