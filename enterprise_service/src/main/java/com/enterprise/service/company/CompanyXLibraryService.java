package com.enterprise.service.company;

import com.enterprise.base.entity.CompanyXLibraryEntity;

import java.util.List;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/5/4 下午3:39
 */
public interface CompanyXLibraryService {

    /**
     * 新增企业图库信息
     *
     * @param companyXLibraryEntity 企业题库记录
     * @author anson
     * @date 2018/5/4 下午3:39
     */
    Integer createOrUpdateCompanyXLibrary(CompanyXLibraryEntity companyXLibraryEntity);

    CompanyXLibraryEntity findCompanyXLibrary(Integer companyId,Integer libraryId);

    Integer batchInsert(List<CompanyXLibraryEntity> companyXLibraryEntity);

    Integer batchupdate(List<CompanyXLibraryEntity> companyXLibraryEntity);

    /**
     * 获取当前使用该题库企业数
     * */
    Integer findCompanyLibraryCount(Integer libraryId);

    /**
     * 获取全选的默认官方题库
     * */
    List<CompanyXLibraryEntity> getCompanyLibraryList(Integer companyId,Integer status);
}

