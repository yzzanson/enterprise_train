package com.enterprise.service.company.impl;

import com.enterprise.base.common.RedisConstant;
import com.enterprise.base.entity.CompanyInfoEntity;
import com.enterprise.cache.GetCache;
import com.enterprise.mapper.companyInfo.CompanyInfoMapper;
import com.enterprise.service.company.CompanyInfoService;
import com.enterprise.service.redis.RedisService;
import com.enterprise.util.AssertUtil;
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
public class CompanyInfoServiceImpl implements CompanyInfoService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Long expireTime = RedisConstant.EXPIRE_TIME_COMMON;

    @Resource
    private CompanyInfoMapper companyInfoMapper;

    @Resource
    private RedisService redisService;

    private static String COMPANYKEY = RedisConstant.COMPANY_KET;

    @Override
    public void createCompanyInfo(CompanyInfoEntity companyInfo) {
        AssertUtil.notNull(companyInfo, "企业信息不能为空!");
        companyInfoMapper.createCompanyInfo(companyInfo);
        String key = COMPANYKEY + "id_" + companyInfo.getId();
        redisService.setSerialize(key,expireTime,companyInfo);
    }

    @Override
    @GetCache(name="company",value="id")
    public CompanyInfoEntity getCompanyById(Integer id) {
        return companyInfoMapper.getCompanyById(id);
    }

    @Override
    public void modifyCompanyInfo(CompanyInfoEntity companyInfo) {
        AssertUtil.notNull(companyInfo, "企业信息不能为空!");
        AssertUtil.notNull(companyInfo.getId(), "企业Id不能为空!");
        companyInfoMapper.modifyCompanyInfo(companyInfo);
        String key = COMPANYKEY + "id_" + companyInfo.getId();
        redisService.setSerialize(key, expireTime,companyInfo);
    }

    @Override
    public CompanyInfoEntity getCompanyInfo(CompanyInfoEntity companyInfo) {
        AssertUtil.notNull(companyInfo, "企业信息不能为空!");
        AssertUtil.notNull(companyInfo.getId(), "企业Id不能为空!");
        return companyInfoMapper.getCompanyInfo(companyInfo);
    }

    @Override
    public List<CompanyInfoEntity> getAllCompanys() {
        return companyInfoMapper.getAllCompanys();
    }

    @Override
    public String getCompanyNameById(Integer id) {
        return companyInfoMapper.getCompanyNameById(id);
    }


}
