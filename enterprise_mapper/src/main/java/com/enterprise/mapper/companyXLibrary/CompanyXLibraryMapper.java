package com.enterprise.mapper.companyXLibrary;

import com.enterprise.base.entity.CompanyXLibraryEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by null on 2018-03-23 10:21:46
 */
public interface CompanyXLibraryMapper {

    /**
     * 新增企业信息
     *
     * @param companyXLibraryEntity 企业信息
     * @author anson
     * @date 2018/6/11 下午15:58
     */
    Integer createCompanyLibrary(CompanyXLibraryEntity companyXLibraryEntity);

    /**
     * 根据条件修改企业信息
     *
     * @param companyXLibraryEntity 企业信息
     * @author anson
     * @date 2018/6/11 下午15:58
     */
    Integer modifyCompanyLibrary(CompanyXLibraryEntity companyXLibraryEntity);

    /**
     * 查看公司是否接入过某题库
     * */
    CompanyXLibraryEntity findCompanyXLibrary(@Param("companyId")Integer companyId,@Param("libraryId")Integer libraryId);

    /**
     * 批量插入
     * */
    Integer batchInsert(@Param("companyXLibraryList") List<CompanyXLibraryEntity> companyXLibraryList);

    /**
     * 批量更新
     * */
    Integer batchupdate(@Param("companyXLibraryList") List<CompanyXLibraryEntity> companyXLibraryList);


    /**
     * 查找已经安排过的企业
     * */
    List<Integer> getCompanysByLibraryId(@Param("libraryId")Integer libraryId,@Param("status")Integer status);

    /**
     * 批量软删除
     * */
    Integer batchSafeDelete(@Param("libraryId")Integer libraryId,@Param("status")Integer status);

    /**
     * 获取使用某个公共题库的企业数
     * */
    Integer findCompanyLibraryCount(@Param("libraryId")Integer libraryId);

    List<Integer> getCompanyDefaultLibrary(@Param("companyId") Integer companyId);

    /**
     * 获取默认全选的官方题库
     * */
    List<CompanyXLibraryEntity> getCompanyLibraryList(@Param("companyId") Integer companyId,@Param("status")  Integer status);

    /**
     * 获取该企业的官方题库,返回libraryId列表
     * */
    List<Integer> getDefaultPublicLibrary(@Param("companyId") Integer companyId);

}