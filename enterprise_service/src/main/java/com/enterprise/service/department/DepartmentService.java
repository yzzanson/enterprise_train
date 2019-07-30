package com.enterprise.service.department;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.entity.DepartmentEntity;

import java.util.List;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/4/18 下午4:42
 */
public interface DepartmentService {

    /**
     * 获取部门名字
     * */
    String getDeptNameById(Integer companyId,Integer id);

    /**
     * 获取部门列表
     * */
    List<DepartmentEntity> getDeptsByCompanyId(Integer conpanyId,Integer status);

    /**
     * 同步钉钉和系统的部门
     * */
    void synchronize(JSONArray dingDepts,List<DepartmentEntity> myDepts,String corpId) throws Exception ;

    /**
     * 通讯录回调
     * */
    Integer createOrModifyCallBackDepartment(String corpId,String dingDeptId,Integer type);

    DepartmentEntity getTopDepartment(Integer companyId);

    /**
     * 重新同步通讯录
     * */
    JSONObject syncCompany(Integer companyId) throws Exception;

    JSONObject syncCompany() throws Exception;

    /**
     * 重新同步通讯录
     * */
    JSONObject syncRightSingle(Integer companyId);

}
