package com.enterprise.service.bury;

import com.enterprise.base.entity.bury.AppInviteBuryEntity;
import com.enterprise.base.entity.bury.OAOpenBuryEntity;
import com.enterprise.base.entity.bury.OASendBuryEntity;
import com.enterprise.base.entity.bury.PetRaiseBuryEntity;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/9/5 下午5:21
 */
public interface BuryService {

    Integer buryAppInbite(AppInviteBuryEntity appInviteBuryEntity);

    Integer buryOAOpen(OAOpenBuryEntity oaOpenBuryEntity);

    Integer buryOASend(OASendBuryEntity oaSendBuryEntity);

    Integer buryPetRaise(PetRaiseBuryEntity petRaiseBuryEntity);

}
