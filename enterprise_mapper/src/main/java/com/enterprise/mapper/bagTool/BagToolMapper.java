package com.enterprise.mapper.bagTool;

import com.enterprise.base.vo.bagtool.BagToolVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/9/5 下午2:50
 */
public interface BagToolMapper {

    /**
     * 根据id获取描述
     */
    BagToolVO getById(@Param("id") Integer id);

    /**
     * 获取所有道具
     */
    List<BagToolVO> getAllTools();

    /**
     * 获取OA模板
     */
    String getOaModelById(@Param("id") Integer id);

}
