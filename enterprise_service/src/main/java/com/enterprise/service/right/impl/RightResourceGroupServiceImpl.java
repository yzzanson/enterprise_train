package com.enterprise.service.right.impl;

import com.enterprise.base.entity.right.RightResourceGroupEntity;
import com.enterprise.mapper.right.RightResourceGroupMapper;
import com.enterprise.service.right.RightResourceGroupService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/7/2 下午3:53
 */
@Service
public class RightResourceGroupServiceImpl implements RightResourceGroupService {

    @Resource
    private RightResourceGroupMapper rightResourceGroupMapper;

    @Override
    public Integer saveOrUpdateRightGroup(RightResourceGroupEntity rightResourceGroupEntity) {
        if(rightResourceGroupEntity.getId()==null){
            rightResourceGroupEntity.setCreateTime(new Date());
            rightResourceGroupEntity.setUpdateTime(new Date());
            return rightResourceGroupMapper.createRightResourceGroup(rightResourceGroupEntity);
        }
        return rightResourceGroupMapper.updateRightResourceGroup(rightResourceGroupEntity);
    }

    @Override
    public List<RightResourceGroupEntity> getResourcesByCompanyAndGroup(RightResourceGroupEntity rightResourceGroupEntity) {
        return rightResourceGroupMapper.getResourcesByCompanyAndGroup(rightResourceGroupEntity);
    }

    @Override
    public RightResourceGroupEntity getByResourceIdAndCompany(RightResourceGroupEntity rightResourceGroupEntity) {
        return rightResourceGroupMapper.getByResourceIdAndCompany(rightResourceGroupEntity);
    }

    @Override
    public Integer batchInsert(List<RightResourceGroupEntity> rightResourceGroupList) {
        return rightResourceGroupMapper.batchInsert(rightResourceGroupList);
    }

    @Override
    public Integer batchUpdate(List<RightResourceGroupEntity> rightResourceGroupList) {
        return rightResourceGroupMapper.batchUpdate(rightResourceGroupList);
    }
}
