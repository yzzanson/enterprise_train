package com.enterprise.service.right;

import com.enterprise.base.entity.right.RightResourceUrlEntity;

import java.util.List;

/**
 * Created by anson on 18/3/23.
 */
public interface RightResourceService {

    List<RightResourceUrlEntity> findResourceByGroupIds(List<Integer> groupIds);
}
