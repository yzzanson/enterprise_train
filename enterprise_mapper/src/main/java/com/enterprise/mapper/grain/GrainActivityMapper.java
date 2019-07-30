package com.enterprise.mapper.grain;

import com.enterprise.base.entity.GrainActivityEntity;
import com.enterprise.base.vo.GrainActivityVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/10/23 上午10:31
 */
public interface GrainActivityMapper {

    GrainActivityEntity getById(@Param("id") Integer id);

    List<GrainActivityVO> getActivityList();


}
