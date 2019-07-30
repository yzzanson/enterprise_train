package com.enterprise.mapper.right;

import com.enterprise.base.entity.right.RightResourceGroupEntity;
import com.enterprise.base.entity.right.UserRightGroupEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by anson on 18/3/23.
 */
public interface RightResourceGroupMapper {

    Integer createRightResourceGroup(RightResourceGroupEntity rightResourceGroupEntity);

    Integer updateRightResourceGroup(RightResourceGroupEntity rightResourceGroupEntity);

    List<RightResourceGroupEntity> getResourcesByCompanyAndGroup(RightResourceGroupEntity rightResourceGroupEntity);

    RightResourceGroupEntity getByResourceIdAndCompany(RightResourceGroupEntity rightResourceGroupEntity);

    Integer batchInsert(@Param("insertList") List<RightResourceGroupEntity> rightResourceGroupList);

    Integer batchUpdate(@Param("updateList") List<RightResourceGroupEntity> rightResourceGroupList);

    Integer batchUpdateStatus(@Param("companyId") Integer companyId,@Param("rightGroupId") Integer groupId,@Param("status") Integer status);

    List<Integer> getResourcesByCompanyAndGroups(@Param("companyId") Integer companyId,@Param("rightGrouList") List<UserRightGroupEntity> userRightGroupList);
}
