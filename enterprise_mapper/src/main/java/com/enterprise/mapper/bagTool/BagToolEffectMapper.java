package com.enterprise.mapper.bagTool;

import com.enterprise.base.entity.BagToolEffectEntity;
import com.enterprise.base.vo.bagtool.BagToolEffectTotalDetailVO;
import com.enterprise.base.vo.bagtool.BagToolEffectTotalVO;
import com.enterprise.base.vo.bagtool.BagToolEffectUserVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @Description 用户拥有道具
 * @Author zezhouyang
 * @Date 18/9/5 下午2:50
 */
public interface BagToolEffectMapper {

    /**
     * 使用道具
     */
    Integer useTool(BagToolEffectEntity bagToolEffectEntity);

    Integer dispelTool(@Param("id") Integer id,@Param("status") Integer status);

    List<BagToolEffectUserVO> getUserEffectedTool(@Param("companyId") Integer companyId,@Param("userId") Integer userId,@Param("type") Integer type);

    List<BagToolEffectEntity> getUserEffectedStatusSelf(@Param("companyId") Integer companyId,@Param("userId") Integer userId,@Param("toolId") Integer toolId);

    List<BagToolEffectEntity> getUserEffectedStatusOther(@Param("companyId") Integer companyId,@Param("userId") Integer userId,@Param("toolId") Integer toolId);

    /**
     * 获取恶搞人昵称
     * */
    String getLastToolUser(@Param("companyId") Integer companyId,@Param("userId") Integer userId,@Param("toolId") Integer toolId);

    /**
     * 获取恶搞人数量
     * */
    Integer getToolUserCount(@Param("companyId") Integer companyId,@Param("userId") Integer userId,@Param("toolId") Integer toolId);

    /**
     * 批量更新
     * */
    Integer batchUpdate(@Param("bagToolEffectList") List<BagToolEffectEntity> bagToolEffectList);

    /**
     * 批量更新
     * */
    Integer getResusCount(@Param("startDate") Date startDate,@Param("endDate") Date endDate,@Param("toolType") Integer toolType);
    /**
     * 批量更新
     * */
    Integer batchElimateResus(@Param("startDate") Date startDate,@Param("endDate") Date endDate,@Param("toolType") Integer toolType,@Param("status") Integer status);

    /**
     * 获取负面作用排行
     * */
    List<BagToolEffectTotalVO> getNegativeEffectList(@Param("companyId") Integer companyId,@Param("month") String month);

    /**
     * 获取正面作用排行
     * */
    List<BagToolEffectTotalVO> getPositiveEffectList(@Param("companyId") Integer companyId,@Param("month") String month);

    /**
     * 获取正面作用排行
     * */
    List<BagToolEffectTotalDetailVO> getBagToolEffectDetail(@Param("companyId") Integer companyId,@Param("userId") Integer userId,@Param("month") String month,@Param("type") Integer type);

    /**
     * 获取被作用的道具列表
     * */
    List<BagToolEffectEntity> getEffectedToolList(@Param("companyId") Integer companyId,@Param("userId") Integer userId);

    /**
     * 获取当前的状态
     * */
    BagToolEffectEntity getById(@Param("id") Integer id);

}

