package com.enterprise.service.right.impl;

import com.enterprise.base.entity.right.RightResourceUrlEntity;
import com.enterprise.mapper.right.RightResourceUrlMapper;
import com.enterprise.service.right.RightResourceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by anson on 18/3/24.
 */
@Service
public class RightResourceServiceImpl implements RightResourceService {

    @Resource
    private RightResourceUrlMapper rightResourceUrlMapper;

    @Override
    public List<RightResourceUrlEntity> findResourceByGroupIds(List<Integer> groupIds) {
        return rightResourceUrlMapper.findRightResourcesByGroupIds(groupIds);
    }
}
