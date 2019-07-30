package com.enterprise.mapper.users;

import com.enterprise.base.entity.UserXDeptEntity;
import com.enterprise.base.vo.UserXDeptVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by null on 2018-03-23 11:08:46
 */
public interface UserXDeptMapper {

    void createUserXDept(UserXDeptEntity userXDeptEntity);

    void updateUserXDept(UserXDeptEntity userXDeptEntity);

    void batchSafeDelte(UserXDeptEntity userXDeptEntity);

    void batchSafeDelteDepartment(@Param("corpId") String corpId,@Param("deptId") Integer deptId,@Param("status") Integer status);

    Integer getUserDept(@Param("corpId") String corpId,@Param("userId") Integer userId);

    String getUserDeptName(@Param("corpId") String corpId,@Param("userId") Integer userId);

    List<Integer> getDeptsByCorpId(@Param("corpId") String corpId);

    UserXDeptEntity getByUserIdAndCorpIdAndDeptId(@Param("userId") Integer userId,@Param("corpId") String corpId,@Param("deptId") Integer deptId);

    UserXDeptEntity getUserXDeptByCorpIdAndDeptId(UserXDeptEntity userXDeptEntity);

    /**
     * 获取用户的部门id
     * */
    String getUserDeptids(@Param("corpId") String corpId,@Param("userId") Integer userId);

    List<UserXDeptEntity> getUserInDept(@Param("corpId") String corpId,@Param("deptId") Integer deptId,@Param("status") Integer status);

    /**
     * 根据名字模糊搜索
     * */
    List<UserXDeptEntity> getUserInDeptAndName(@Param("corpId") String corpId,@Param("deptId") Integer deptId,@Param("name") String name,@Param("status") Integer status);

    /**
     * 获取用户的部门
     * */
    List<UserXDeptEntity> getUserDepts(@Param("corpId") String corpId,@Param("userId") Integer userId,@Param("type") Integer type);

    List<UserXDeptVO> getMyDeptList(@Param("corpId") String corpId,@Param("userId") Integer userId);

    /**
     * 更新用户部门
     * */
    Integer batcUpdateUserXDepartment(@Param("corpId") String corpId,@Param("status") Integer status);

    /**
     * 批量插入
     * */
    Integer batchInsert(@Param("userXDeptList") List<UserXDeptEntity> userXDeptList);

    /**
     * 批量更新
     * */
    Integer batchUpdate(@Param("userXDeptList") List<UserXDeptEntity> userXDeptList);

}
