package com.enterprise.mapper.dswitch;

import com.enterprise.base.entity.DSwitchEntity;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/10/23 上午10:31
 */
public interface DSwitchMapper {

    Integer createDSwitch(DSwitchEntity dSwitchEntity);


    Integer updateDSwitch(DSwitchEntity dSwitchEntity);

    DSwitchEntity getByType(Integer type);
}
