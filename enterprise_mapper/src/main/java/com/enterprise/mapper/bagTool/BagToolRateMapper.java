package com.enterprise.mapper.bagTool;

import com.enterprise.base.entity.BagToolRateEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description 道具获取概率
 * @Author zezhouyang
 * @Date 18/9/5 下午2:50
 */
public interface BagToolRateMapper {

    /**
     * 根据事件获取详情
     */
    List<BagToolRateEntity> getByEventType(@Param("eventType") Integer eventType);


    /**
     * 根据道具获取详情
     */
    List<BagToolRateEntity> getByToolId(@Param("toolId") Integer toolId);

}
