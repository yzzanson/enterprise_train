package com.enterprise.service.dswitch.impl;

import com.enterprise.base.common.RedisConstant;
import com.enterprise.base.entity.DSwitchEntity;
import com.enterprise.mapper.dswitch.DSwitchMapper;
import com.enterprise.service.dswitch.DSwitchService;
import com.enterprise.service.redis.RedisService;
import com.enterprise.util.AssertUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/12/14 下午4:31
 */
@Service
public class DSwitchServiceImpl implements DSwitchService {

    @Resource
    private DSwitchMapper dSwitchMapper;

    @Resource
    private RedisService redisService;

    public static String DSWITCH_TYPE_KEY = RedisConstant.DSWITCH_TYPE_KEY;

    private Long expireTime = RedisConstant.EXPIRE_TIME_COMMON;

    @Override
    public Integer createOrUpdateDSwitch(DSwitchEntity dSwitchEntity) {
        AssertUtil.isTrue(!StringUtils.isEmpty(dSwitchEntity.getType()), "开关类型不能为空!");
        AssertUtil.isTrue(!StringUtils.isEmpty(dSwitchEntity.getStatus()), "开关状态不能为空!");
        String key = DSWITCH_TYPE_KEY + dSwitchEntity.getType();
        DSwitchEntity dSwitchInDB = dSwitchMapper.getByType(dSwitchEntity.getType());
         Integer result = 0;
        if (dSwitchInDB==null) {
            result = dSwitchMapper.createDSwitch(dSwitchEntity);
        }else{
            dSwitchEntity.setStatus(dSwitchEntity.getStatus());
            dSwitchEntity.setType(dSwitchEntity.getType());
            result = dSwitchMapper.updateDSwitch(dSwitchEntity);
        }
        if(result>=0) {
            redisService.setSerialize(key,dSwitchEntity);
        }
        return result;
    }

    @Override
    public DSwitchEntity getOne(Integer type) {
        String key = DSWITCH_TYPE_KEY + type;
        DSwitchEntity dSwitchEntity = (DSwitchEntity) redisService.getSerializeObj(key);
        if(dSwitchEntity==null){
            dSwitchEntity = dSwitchMapper.getByType(type);
        }
        return dSwitchEntity;
    }
}
