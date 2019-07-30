package com.enterprise.mapper.right;

import com.enterprise.base.entity.right.RightGroupEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by anson on 18/3/23.
 */
public interface RightGroupMapper {

    Integer createRightGroup(RightGroupEntity rightGroupEntity);

    Integer updateRightGroup(RightGroupEntity rightGroupEntity);

    RightGroupEntity getById(@Param("id") Integer id);

    List<RightGroupEntity> getByCompanyIdAndName(@Param("companyId") Integer companyId,@Param("name") String name,@Param("status") Integer status);

    Integer getMaxValueByCompany(@Param("companyId") Integer companyId);

    RightGroupEntity getSuperManageRightGroup(@Param("companyId") Integer companyId);
}
