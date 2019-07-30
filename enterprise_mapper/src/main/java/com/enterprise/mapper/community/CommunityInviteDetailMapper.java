package com.enterprise.mapper.community;

import com.enterprise.base.entity.CommunityInviteDetailEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/9/5 下午2:50
 */
public interface CommunityInviteDetailMapper {

   Integer createCommunityInviteDetail(CommunityInviteDetailEntity communityInviteDetailEntity);

   CommunityInviteDetailEntity getByCompanyId(@Param("companyId") Integer companyId);

   List<CommunityInviteDetailEntity> getCommunityInviteByCompanyAndDay(@Param("date") String date);
}
