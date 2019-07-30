package com.enterprise.mapper.bagTool;

import com.enterprise.base.entity.BagToolEffectQuestionEntity;
import org.apache.ibatis.annotations.Param;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/9/17 下午3:17
 */
public interface BagToolEffectQuestionsMapper {

    /**
     * 使用道具
     */
    Integer createOne(BagToolEffectQuestionEntity bagToolEffectQuestionEntity);

    /**
     * 查看道具作用次数
     * */
    Integer getToolEffectCount(@Param("companyId") Integer companyId,@Param("userId") Integer userId,@Param("effectId") Integer effectId);

    /**
     * 更新状态
     * */
    Integer updateOne(BagToolEffectQuestionEntity bagToolEffectQuestionEntity);

    /**
     * 查看道具作用次数
     * */
    Integer getToolEffectCount2(@Param("companyId") Integer companyId,@Param("userId") Integer userId,@Param("effectId") Integer effectId);

}
