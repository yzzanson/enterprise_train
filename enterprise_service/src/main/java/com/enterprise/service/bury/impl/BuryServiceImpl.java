package com.enterprise.service.bury.impl;

import com.enterprise.base.entity.bury.AppInviteBuryEntity;
import com.enterprise.base.entity.bury.OAOpenBuryEntity;
import com.enterprise.base.entity.bury.OASendBuryEntity;
import com.enterprise.base.entity.bury.PetRaiseBuryEntity;
import com.enterprise.mapper.bury.AppInviteBuryMapper;
import com.enterprise.mapper.bury.OAOpenBuryMapper;
import com.enterprise.mapper.bury.OASendBuryMapper;
import com.enterprise.mapper.bury.PetRaiseBuryMapper;
import com.enterprise.service.bury.BuryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/10/30 下午3:11
 */
@Service
public class BuryServiceImpl implements BuryService {

    @Resource
    private AppInviteBuryMapper appInviteBuryMapper;

    @Resource
    private OASendBuryMapper oaSendBuryMapper;

    @Resource
    private OAOpenBuryMapper oaOpenBuryMapper;

    @Resource
    private PetRaiseBuryMapper petRaiseBuryMapper;

    @Override
    public Integer buryAppInbite(AppInviteBuryEntity appInviteBuryEntity) {
        return appInviteBuryMapper.createAppInviteBury(appInviteBuryEntity);
    }

    @Override
    public Integer buryOAOpen(OAOpenBuryEntity oaOpenBuryEntity) {
        return oaOpenBuryMapper.createOAOpenBury(oaOpenBuryEntity);
    }

    @Override
    public Integer buryOASend(OASendBuryEntity oaSendBuryEntity) {
        return oaSendBuryMapper.createOASendBury(oaSendBuryEntity);
    }

    @Override
    public Integer buryPetRaise(PetRaiseBuryEntity petRaiseBuryEntity) {
        return petRaiseBuryMapper.createPetRaiseBury(petRaiseBuryEntity);
    }
}
