package com.enterprise.service.report;

import com.enterprise.base.entity.UserXOpenEntity;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/4/13 上午9:53
 */
public interface UserXOpenService {

    Integer createOrUpdateUserXOpen(UserXOpenEntity userXOpenEntity);

    Integer getCountByCompanyId(Integer companyId,String date);

}
