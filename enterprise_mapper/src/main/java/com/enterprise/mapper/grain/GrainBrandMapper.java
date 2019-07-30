package com.enterprise.mapper.grain;

import com.enterprise.base.entity.GrainBrandEntity;
import org.apache.ibatis.annotations.Param;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/10/23 上午10:31
 */
public interface GrainBrandMapper {

    GrainBrandEntity getById(@Param("id") Integer id);
}
