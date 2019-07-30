package com.enterprise.service.user;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.entity.UserEntity;

/**
 * @Description UserService
 * @Author shisan
 * @Date 2018/3/23 上午11:14
 */
public interface UserService {

    /**
     * 根据id查询用户
     *
     * @param id
     * @return
     */
    UserEntity getUserById(Integer id);

    void synchronize(JSONArray dingDepts, String corpId) throws Exception;

    void createOrUpdateUser(UserEntity userEntity);

    void updateUser(UserEntity userEntity);

    UserEntity findUser(UserEntity userEntity);

    /**
     * 根据条件获取用户信息
     *
     * @param userEntity 查询条件
     * @author shisan
     * @date 2018/3/26 下午3:59
     */
    UserEntity getUserEntity(UserEntity userEntity);

    /**
     * 用户是否为新用户
     * */
    JSONObject isNewbie();

    /**
     * 获取企业的新用户数
     * */
    Integer getNewUserByCompanyIdAndDate(Integer companyId,String date);

    /**
     * 获取个人信息
     * */
    JSONObject getUserInfo(Integer userId,String corpId);

    /**
     * 用户回调信息更新
     * */
    Integer createOrModifyCallBackDepartment(String corpId,String dingUserId,Integer type);

    /**
     * 邀请同事
     * */
    JSONObject invite(Integer userId);
}
