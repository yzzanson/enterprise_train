package com.enterprise.service.right;

import com.enterprise.base.entity.right.RightResourceGroupEntity;

import java.util.List;

/**
 * Created by anson on 18/3/23.
 */
public interface RightResourceGroupService {

    Integer saveOrUpdateRightGroup(RightResourceGroupEntity rightResourceGroupEntity);

    List<RightResourceGroupEntity> getResourcesByCompanyAndGroup(RightResourceGroupEntity rightResourceGroupEntity);

    RightResourceGroupEntity getByResourceIdAndCompany(RightResourceGroupEntity rightResourceGroupEntity);

    Integer batchInsert(List<RightResourceGroupEntity> rightResourceGroupList);

    Integer batchUpdate(List<RightResourceGroupEntity> rightResourceGroupList);

}
