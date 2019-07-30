package com.enterprise.service.company.impl;

import com.enterprise.base.entity.CompanyXLibraryEntity;
import com.enterprise.mapper.companyXLibrary.CompanyXLibraryMapper;
import com.enterprise.service.company.CompanyXLibraryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * CompanyInfoServiceImpl
 *
 * @author shisan
 * @create 2018-03-26 上午11:00
 **/
@Service
public class CompanyXLibraryServiceImpl implements CompanyXLibraryService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private CompanyXLibraryMapper companyXLibraryMapper;


    @Override
    public Integer createOrUpdateCompanyXLibrary(CompanyXLibraryEntity companyXLibraryEntity) {
        Integer result = 0;
        if(companyXLibraryEntity.getId()!=null && companyXLibraryEntity.getId()>0){
            return companyXLibraryMapper.modifyCompanyLibrary(companyXLibraryEntity);
        }else{
            return companyXLibraryMapper.createCompanyLibrary(companyXLibraryEntity);
        }
    }

    @Override
    public CompanyXLibraryEntity findCompanyXLibrary(Integer companyId, Integer libraryId) {
        return companyXLibraryMapper.findCompanyXLibrary(companyId,libraryId);
    }

    @Override
    public Integer batchInsert(List<CompanyXLibraryEntity> companyXLibraryEntity) {
        return companyXLibraryMapper.batchInsert(companyXLibraryEntity);
    }

    @Override
    public Integer batchupdate(List<CompanyXLibraryEntity> companyXLibraryEntity) {
        return companyXLibraryMapper.batchupdate(companyXLibraryEntity);
    }

    @Override
    public Integer findCompanyLibraryCount(Integer libraryId) {
        return companyXLibraryMapper.findCompanyLibraryCount(libraryId);
    }

    @Override
    public List<CompanyXLibraryEntity> getCompanyLibraryList(Integer companyId, Integer status) {
        return companyXLibraryMapper.getCompanyLibraryList(companyId, status);
    }
}
