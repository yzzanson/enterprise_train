package com.enterprise.mapper.community;

import com.enterprise.base.entity.CommunityInviteEntity;
import org.apache.ibatis.annotations.Param;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/9/5 下午2:50
 */
public interface CommunityInviteMapper {

   Integer createCommunityInvite(CommunityInviteEntity communityInviteEntity);

   CommunityInviteEntity getByCompanyIdAndUserId(@Param("companyId") Integer companyId,@Param("userId") Integer userId);

   Integer getInviteCount(@Param("companyId") Integer companyId,@Param("userId") Integer userId);

   CommunityInviteEntity getByCompanyIdAndInvitedUserId(@Param("companyId") Integer companyId,@Param("userId") Integer userId,@Param("inviteUser") Integer inviteUser);
}
