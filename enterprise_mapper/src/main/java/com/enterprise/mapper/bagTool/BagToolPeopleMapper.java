package com.enterprise.mapper.bagTool;

import com.enterprise.base.entity.BagToolPeopleEntity;
import com.enterprise.base.vo.bagtool.BagToolPeopleVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description 用户拥有道具
 * @Author zezhouyang
 * @Date 18/9/5 下午2:50
 */
public interface BagToolPeopleMapper {

    /**
     * 用户拥有道具
     */
    List<BagToolPeopleVO> getByToolIdAndCompanyId(@Param("companyId") Integer companyId,@Param("userId") Integer userId);

    /**
     * 获取具体道具id
     * */
    List<Integer> getToolIdByCompanyAndTool(@Param("companyId") Integer companyId,@Param("toolId") Integer toolId,@Param("userId") Integer userId);
    /**
     * 获取道具
     */
    Integer gainTool(BagToolPeopleEntity bigToolPeopleEntity);

    /**
     * 消耗道具
     */
    Integer consumeTool(@Param("id") Integer id,@Param("status") Integer status);

    /**
     * 查询是否获取过某个道具
     * */
    Integer checkIfGetTool(@Param("companyId") Integer companyId,@Param("eventType") Integer eventType,@Param("userId") Integer userId,@Param("date") String date);

    BagToolPeopleEntity getById(@Param("id") Integer id);

    /**
     * 查询企业下用户道具数量
     * */
    Integer getToolCount(@Param("companyId") Integer companyId,@Param("userId") Integer userId,@Param("toolId") Integer toolId);

    /**
     * 批量更新
     * */
    Integer batchUpdate(@Param("bagToolPeopleList") List<BagToolPeopleEntity> bagToolPeopleList);

}
