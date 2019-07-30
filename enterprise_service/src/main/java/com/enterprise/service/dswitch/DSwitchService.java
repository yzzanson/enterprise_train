package com.enterprise.service.dswitch;

import com.enterprise.base.entity.DSwitchEntity;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/4/18 下午4:42
 */
public interface DSwitchService {

    /**
     * 新增修改
     * */
    Integer createOrUpdateDSwitch(DSwitchEntity dSwitchEntity);

    DSwitchEntity getOne(Integer type);
}
