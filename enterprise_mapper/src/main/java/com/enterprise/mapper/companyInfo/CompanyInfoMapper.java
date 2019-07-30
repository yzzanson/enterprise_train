package com.enterprise.mapper.companyInfo;

import com.enterprise.base.entity.CompanyInfoEntity;
import com.enterprise.base.vo.CompanyLibraryVO;
import com.enterprise.base.vo.dto.CompanyReportDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description CompanyInfoMapper
 * 
 * @Author shisan
 * @Date 2018/3/26 上午10:59
 */
public interface CompanyInfoMapper {

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


    String getCompanyNameById(@Param("id") Integer id);

    List<CompanyInfoEntity> getAllCompanys();

    List<CompanyInfoEntity> getCompanysByCondition(CompanyReportDTO companyReportDTO);

    List<CompanyInfoEntity> getCompanysByConditionMultiVersion(CompanyReportDTO companyReportDTO);

    CompanyInfoEntity getCompanyById(@Param("companyId") Integer companyId);

    Integer getCompanyCountByDate(@Param("date") String date);

    /**
     * 获取公司详情
     * */
    CompanyInfoEntity getCompanyInfo(CompanyInfoEntity companyInfo);


    List<CompanyLibraryVO> findCompanyList(@Param("search") String search);

}