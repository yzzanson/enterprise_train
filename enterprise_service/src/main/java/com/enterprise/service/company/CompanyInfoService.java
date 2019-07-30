package com.enterprise.service.company;

import com.enterprise.base.entity.CompanyInfoEntity;

import java.util.List;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/4/16 下午2:26
 */
public interface CompanyInfoService {

    /**
     * 新增企业信息
     *
     * @param companyInfo 企业信息
     * @author shisan
     * @date 2018/3/26 上午10:51
     */
    void createCompanyInfo(CompanyInfoEntity companyInfo);

    /**
     * 根据条件修改企业信息
     *
     * @param companyInfo 企业信息
     * @author shisan
     * @date 2018/3/26 上午11:11
     */
    void modifyCompanyInfo(CompanyInfoEntity companyInfo);

    /**
     * 根据条件查询企业信息
     *
     * @param companyInfo 查询条件
     * @author shisan
     * @date 2018/3/26 下午5:06
     */
    CompanyInfoEntity getCompanyInfo(CompanyInfoEntity companyInfo);


    /**
     * 获取所有公司的列表
     */
    List<CompanyInfoEntity> getAllCompanys();

    String getCompanyNameById(Integer id);

    CompanyInfoEntity getCompanyById(Integer id);

}

