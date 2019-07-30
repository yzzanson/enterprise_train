package com.enterprise.mapper.department;

import com.enterprise.base.entity.DepartmentEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by null on 2018-03-23 10:23:18
 */
public interface DepartmentMapper {

    Integer insertSingle(DepartmentEntity departmentEntity);

    void insertBatch(@Param("deptList")List<DepartmentEntity> departmentList);

    String getDeptNameById(@Param("companyId") Integer companyId,@Param("id") Integer id);

    String getDingDepartIdById(@Param("id") Integer id);

    List<DepartmentEntity> getDeptsByCompanyId(@Param("companyId") Integer companyId,@Param("status") Integer status);

    Integer updateDepartment(DepartmentEntity departmentEntity);

    List<DepartmentEntity> selectDeptByDingDeptIds(@Param("companyId") Integer  companyId, @Param("dingDeptIds") List<Integer> dingDeptIds);

    DepartmentEntity getByCompanyIdAndDingDeptId(@Param("companyId") Integer companyId,@Param("dingDeptId") Integer dingDeptId);

    Integer getIdByCompanyIdAndDingDeptId(@Param("companyId") Integer companyId,@Param("dingDeptId") Integer dingDeptId);

    List<DepartmentEntity> getDepartmentsByParentId(@Param("companyId") Integer companyId,@Param("parentId") Integer parentId);

    DepartmentEntity getDepartmentById(@Param("id") Integer id);

    Integer batchUpdateDepartment(@Param("companyId") Integer companyId,@Param("status") Integer status);

    Integer batchUpdateDepartment2(@Param("departmentList") List<DepartmentEntity> departmentList);

    List<DepartmentEntity> getUnauthDepartments(@Param("companyId") Integer companyId);

}