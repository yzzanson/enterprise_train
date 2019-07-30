package com.enterprise.mapper.right;

import com.enterprise.base.entity.right.RightResourceUrlEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by anson on 18/3/23.
 */
public interface RightResourceUrlMapper {

    void createRightResourceUrl(RightResourceUrlEntity rightResourceUrlEntity);

    List<RightResourceUrlEntity> getResourceByParent(@Param("parentId") Integer parentId,@Param("isOperate") Integer isOperate);

    List<RightResourceUrlEntity> getResourceByOperate(@Param("isOperate") Integer isOperate,@Param("status") Integer status,@Param("isTop") Integer isTop);

    List<RightResourceUrlEntity> findRightResourcesByGroupIds(@Param("groupIdList") List<Integer> groupIdList);

    List<RightResourceUrlEntity> getAllResources(@Param("userId") Integer userId);

    List<Integer> getParentIdsByResourceIds(@Param("resourceIdList") List<Integer> resourceIdList);

    RightResourceUrlEntity getParentByResourceId(@Param("resourceId") Integer resourceId);
}
