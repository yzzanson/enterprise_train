package com.enterprise.service.suites.impl;


import com.enterprise.base.entity.SuitesEntity;
import com.enterprise.mapper.suites.SuitesMapper;
import com.enterprise.service.suites.SuitesService;
import com.enterprise.util.AssertUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * SuitesServiceImpl
 *
 * @Author shisan
 * @Date 2018/3/26 上午10:01
 */
@Service
public class SuitesServiceImpl implements SuitesService {

    @Resource
    private SuitesMapper suitesMapper;

    @Override
    public SuitesEntity getSuitesentity(SuitesEntity suites) {
        AssertUtil.notNull(suites, "suites查询条件不能为空!");
        AssertUtil.notNull(suites.getId(), "suites套间Id不能为空!");

        return suitesMapper.getSuitesentity(suites);
    }

    @Override
    public void modifySuites(SuitesEntity suites) {
        AssertUtil.notNull(suites, "suites查询条件不能为空!");
        AssertUtil.notNull(suites.getId(), "suites套间Id不能为空!");

        suitesMapper.modifySuites(suites);
    }

    @Override
    public List<SuitesEntity> seachRefeshSuites() {
        return suitesMapper.seachRefeshSuites();
    }

    @Override
    public SuitesEntity getSuiteByKey(String suiteKey) {
        return suitesMapper.getSuiteByKey(suiteKey);
    }
}