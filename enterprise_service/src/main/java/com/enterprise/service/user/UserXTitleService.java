package com.enterprise.service.user;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.entity.UserXTitleEntity;
import com.enterprise.base.vo.UserXTitleVO;

import java.util.List;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/4/16 上午10:03
 */
public interface UserXTitleService {

    Integer saveOrUpdateUserXTitle(UserXTitleEntity userXTitleEntity);

    List<UserXTitleVO> findUserXTitleListByCompany(Integer companyId,Integer userId);

    /**
     * 佩戴头衔
     * @Param id 用户头衔id
     * */
    JSONObject wearTitle(Integer companyId,Integer userId,Integer id);

    /**
     * 查找用户佩戴的头衔
     * */
    String findUseWearTitleByCompany(Integer companyId,Integer userId);

    UserXTitleVO findUserWearTitleByCompany(Integer companyId,Integer userId);

    /**
     * 修复用户头衔
     * */
    JSONObject updateUserTitle(Integer libraryId);


    JSONObject clearTitle();
}
