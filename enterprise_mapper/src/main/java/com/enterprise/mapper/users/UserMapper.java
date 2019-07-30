package com.enterprise.mapper.users;

import com.enterprise.base.entity.UserEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Description UserMapper
 * @Author shisan
 * @Date 2018/3/23 上午11:12
 */
@Repository
public interface UserMapper {

    void createOrUpdateUser(UserEntity userEntity);

    void updateUser(UserEntity userEntity);

    /**
     * 根据id查询用户
     *
     * @param id
     * @return
     */
    UserEntity getUserById(Integer id);

    /**
     * 根据手机号查询id(网吧钉钉)
     * -     * @param phone
     * +     * @param name
     * +     * @return
     * +
     */
    Integer getUserIdByName(String name);

    UserEntity findUser(UserEntity userEntity);

    /**
     * 根据条件获取用户信息
     *
     * @param userEntity 查询条件
     * @author shisan
     * @date 2018/3/26 下午3:59
     */
    UserEntity getUserEntity(UserEntity userEntity);

    String getNameById(@Param("id") Integer id);

    Integer getUserCountByDate(@Param("date") String date);

    UserEntity getUserByDingId(@Param("dingId") String dingId);

    UserEntity getUserByUnionId(@Param("unionId") String unionId);

    Integer callInitUserDataFunction(@Param("userId") Integer userId);

}