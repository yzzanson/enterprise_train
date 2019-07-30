package com.enterprise.service.right.impl;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.GlobalConstant;
import com.enterprise.base.common.LoginUser;
import com.enterprise.base.common.ResultJson;
import com.enterprise.base.entity.right.RightResourceGroupEntity;
import com.enterprise.base.entity.right.RightResourceUrlEntity;
import com.enterprise.base.enums.StatusEnum;
import com.enterprise.base.vo.RightResourceVO;
import com.enterprise.mapper.right.RightResourceGroupMapper;
import com.enterprise.mapper.right.RightResourceUrlMapper;
import com.enterprise.service.right.RightResourceUrlService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by anson on 18/3/24.
 */
@Service
public class RightResourceUrlServiceImpl implements RightResourceUrlService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private RightResourceUrlMapper rightResourceUrlMapper;

    @Resource
    private RightResourceGroupMapper rightResourceGroupMapper;

    @Override
    public List<RightResourceUrlEntity> getResourceByOperate(Integer operator, Integer status, Integer isTop) {
        return rightResourceUrlMapper.getResourceByOperate(operator, status, isTop);
    }

    /**
     * 获取该权限组的所有用户
     */
    @Override
    public List<RightResourceUrlEntity> findRightResourcesByGroupIds(List<Integer> groupIdList) {
        return rightResourceUrlMapper.findRightResourcesByGroupIds(groupIdList);
    }

    /**
     * 获取该权限组的资源列表
     */
    @Override
    public JSONObject getResourcesByGroup(Integer groupId) {
        String corpId = LoginUser.getUser().getCorpID();
        String operCorpId = GlobalConstant.getOperCorpId();
        Integer companyId = LoginUser.getUser().getCompanyID();
        List<RightResourceVO> rightResourceUrlVOList = new ArrayList<>();
        List<RightResourceUrlEntity> rightResourceUrlList = new ArrayList<>();

        boolean isOper = false;
        if (corpId.equals(operCorpId)) {
            isOper = true;
        }
        if (isOper) {
            rightResourceUrlList = rightResourceUrlMapper.getResourceByOperate(null, 1, 1);
        } else {
            rightResourceUrlList = rightResourceUrlMapper.getResourceByOperate(0, 1, 1);
        }
        for (int i = 0; i < rightResourceUrlList.size(); i++) {
            RightResourceUrlEntity rightResourceUrl = rightResourceUrlList.get(i);
            if (rightResourceUrl.getParentId() == null) {
                RightResourceGroupEntity rightResourceGroup = rightResourceGroupMapper.getByResourceIdAndCompany(new RightResourceGroupEntity(companyId, groupId, rightResourceUrl.getId(), StatusEnum.OK.getValue()));
                RightResourceVO rightResourceVO = new RightResourceVO(rightResourceUrl.getId(), rightResourceUrl.getName(), rightResourceUrl.getParentId(), rightResourceUrl.getType(), rightResourceUrl.getOrders());
                if (rightResourceGroup != null) {
                    rightResourceVO.setIsSelected(1);
                } else {
                    rightResourceVO.setIsSelected(0);
                }
                rightResourceUrlVOList.add(rightResourceVO);
            }
        }
        for (RightResourceVO rightResourceVO : rightResourceUrlVOList) {
            rightResourceVO.setSubList(getChild(groupId, rightResourceVO.getId(), rightResourceUrlVOList, isOper));
        }
        return ResultJson.succResultJson(rightResourceUrlVOList);
    }

    @Override
    public JSONObject saveResourceGroup(Integer groupId, String resourceIds) {
        String corpId = LoginUser.getUser().getCorpID();
        String operCorpId = GlobalConstant.getOperCorpId();
        Integer companyId = LoginUser.getUser().getCompanyID();
        Set<Integer> noOperResourceIdSet = new HashSet<>();
        String[] resourceIdArray = resourceIds.split(",");
        boolean isOper = false;

        Set<Integer> saveResourceIdSet = new HashSet<>();

        if (!corpId.equals(operCorpId)) {
            List<RightResourceUrlEntity> rightResourceUrlLists = rightResourceUrlMapper.getResourceByOperate(0, 1, null);
            for (RightResourceUrlEntity rightResourceUrl : rightResourceUrlLists) {
                noOperResourceIdSet.add(rightResourceUrl.getId());
            }
            for (int i = 0; i < resourceIdArray.length; i++) {
                Integer resourceId = Integer.valueOf(resourceIdArray[i]);
                if (noOperResourceIdSet.contains(resourceId)) {
                    saveResourceIdSet.add(resourceId);
                }
            }
        } else {
            isOper = true;
            for (int i = 0; i < resourceIdArray.length; i++) {
                Integer resourceId = Integer.valueOf(resourceIdArray[i]);
                saveResourceIdSet.add(resourceId);
            }
        }
        //先删除所有的
        //遍历获取所有的节点-包括子节点
        List<RightResourceGroupEntity> addResourceGroupList  = new ArrayList<>();
        List<RightResourceGroupEntity> updateResourceGroupList  = new ArrayList<>();

//        Iterator<Integer> resourceIdItor = saveResourceIdSet.iterator();
//        Set<Integer> allSaveResourceSet = new HashSet<>();
//        while(resourceIdItor.hasNext()){
//            Integer resourceId = resourceIdItor.next();
//            getChildResourceId(isOper,resourceId,allSaveResourceSet);
//        }

        Set<Integer> allInDBResourceIdSet = new HashSet<>();
        //ublic RightResourceGroupEntity(Integer companyId, Integer rightGroupId, Integer rightResourceId, Integer status) {
        List<RightResourceGroupEntity> rightResourceGroupList = rightResourceGroupMapper.getResourcesByCompanyAndGroup(new RightResourceGroupEntity(companyId, groupId, null, null));
        rightResourceGroupMapper.batchUpdateStatus(companyId, groupId,StatusEnum.DELETE.getValue());
        if(CollectionUtils.isNotEmpty(rightResourceGroupList)){
            for(int s = 0;s<rightResourceGroupList.size();s++){
                RightResourceGroupEntity rightResourceGroup = rightResourceGroupList.get(s);
                allInDBResourceIdSet.add(rightResourceGroup.getRightResourceId());
            }
        }
        //数据库里有则更新or插入
        if(CollectionUtils.isNotEmpty(allInDBResourceIdSet)) {
            for (Integer resourceId : saveResourceIdSet) {
                if (allInDBResourceIdSet.contains(resourceId)) {
                    RightResourceGroupEntity updateRightResourceGroupEntity = rightResourceGroupMapper.getByResourceIdAndCompany(new RightResourceGroupEntity(companyId, groupId, resourceId));
                    updateRightResourceGroupEntity.setStatus(StatusEnum.OK.getValue());
                    updateRightResourceGroupEntity.setUpdateTime(new Date());
                    updateResourceGroupList.add(updateRightResourceGroupEntity);
                } else {
                    // public RightResourceGroupEntity(Integer companyId, Integer rightGroupId, Integer rightResourceId, Integer status, Date createTime, Date updateTime)
                    RightResourceGroupEntity rightResourceGroupEntity = new RightResourceGroupEntity(companyId, groupId, resourceId, StatusEnum.OK.getValue(), new Date(), new Date());
                    addResourceGroupList.add(rightResourceGroupEntity);
                }

            }
        }else{
            for (Integer resourceId : saveResourceIdSet) {
                RightResourceGroupEntity rightResourceGroupEntity = new RightResourceGroupEntity(companyId, groupId, resourceId, StatusEnum.OK.getValue(), new Date(), new Date());
                addResourceGroupList.add(rightResourceGroupEntity);
            }
        }
        if(CollectionUtils.isNotEmpty(addResourceGroupList)){
            rightResourceGroupMapper.batchInsert(addResourceGroupList);
        }
        if(CollectionUtils.isNotEmpty(updateResourceGroupList)){
            rightResourceGroupMapper.batchUpdate(updateResourceGroupList);
        }
        return ResultJson.succResultJson(saveResourceIdSet);
    }

    /**
     * 获取所有的资源id的子资源id
     * */
    private void getChildResourceId(boolean isOper,Integer resourceId,Set<Integer> allSaveResourceSet){
        List<RightResourceVO> childList = new ArrayList<>();
        List<RightResourceUrlEntity> rightResourceUrlLists = new ArrayList<>();
        allSaveResourceSet.add(resourceId);
        if (isOper) {
            rightResourceUrlLists = rightResourceUrlMapper.getResourceByParent(resourceId, null);
        } else {
            rightResourceUrlLists = rightResourceUrlMapper.getResourceByParent(resourceId, 0);
        }

        for (int i = 0; i < rightResourceUrlLists.size(); i++) {
            RightResourceUrlEntity subRightResoueceUrl = rightResourceUrlLists.get(i);
            allSaveResourceSet.add(subRightResoueceUrl.getId());
            if (subRightResoueceUrl.getParentId() != null) {
                if (subRightResoueceUrl.getParentId().equals(resourceId)) {
                    RightResourceVO rightResourceVO = new RightResourceVO(subRightResoueceUrl.getId(), subRightResoueceUrl.getName(), subRightResoueceUrl.getParentId(), subRightResoueceUrl.getOrders(), subRightResoueceUrl.getType());
                    childList.add(rightResourceVO);
                }
            }
        }
        for (RightResourceVO menu : childList) {// 没有url子菜单还有子菜单
//            List<RightResourceUrlEntity> rightResourceUrlList = rightResourceUrlMapper.getResourceByParent(menu.getId());
            allSaveResourceSet.add(menu.getId());
            List<RightResourceUrlEntity> rightResourceUrlList = new ArrayList<>();
            if (isOper) {
                rightResourceUrlList = rightResourceUrlMapper.getResourceByParent(menu.getId(), null);
            } else {
                rightResourceUrlList = rightResourceUrlMapper.getResourceByParent(menu.getId(), 0);
            }

            if (rightResourceUrlList.size() > 0) {
                List<RightResourceVO> subRightResourceVO = new ArrayList<>();
                for (int i = 0; i < rightResourceUrlList.size(); i++) {
                    RightResourceUrlEntity rg = rightResourceUrlList.get(i);
                    allSaveResourceSet.add(rg.getId());
                    //RightResourceVO rightSubResourceVO = new RightResourceVO(rg.getId(), rg.getName(), rg.getParentId(), rg.getOrders(), rg.getType());
                    //subRightResourceVO.add(rightSubResourceVO);
                }
                // 递归
                getChildResourceId(isOper, menu.getId(), allSaveResourceSet);
            }
        } // 递归退出条件
        if (childList.size() == 0) {
        }
    }



    private List<RightResourceVO> getChild(Integer groupId, Integer id, List<RightResourceVO> rootMenu, Boolean isOper) {
        Integer companyId = LoginUser.getUser().getCompanyID();
        Set<Integer> rightGroupResourceSet = getRightResourceGroupByGroupId(groupId, companyId);
        // 子菜单
        List<RightResourceVO> childList = new ArrayList<>();
//        for (RightResourceVO menu : rootMenu) {
        if(isOper==null){
            isOper=true;
        }
        // 遍历所有节点，将父菜单id与传过来的id比较
        List<RightResourceUrlEntity> rightResourceUrlLists = new ArrayList<>();
        if (isOper) {
            rightResourceUrlLists = rightResourceUrlMapper.getResourceByParent(id, null);
        } else {
            rightResourceUrlLists = rightResourceUrlMapper.getResourceByParent(id, 0);
        }
//        List<RightResourceUrlEntity> rightResourceUrlLists = rightResourceUrlMapper.getResourceByParent(id);
        for (int i = 0; i < rightResourceUrlLists.size(); i++) {
            RightResourceUrlEntity subRightResoueceUrl = rightResourceUrlLists.get(i);
            if (subRightResoueceUrl.getParentId() != null) {
                if (subRightResoueceUrl.getParentId().equals(id)) {
                    RightResourceVO rightResourceVO = new RightResourceVO(subRightResoueceUrl.getId(), subRightResoueceUrl.getName(), subRightResoueceUrl.getParentId(), subRightResoueceUrl.getOrders(), subRightResoueceUrl.getType());
                    if (rightGroupResourceSet.contains(subRightResoueceUrl.getId())) {
                        rightResourceVO.setIsSelected(1);
                    } else {
                        rightResourceVO.setIsSelected(0);
                    }
                    childList.add(rightResourceVO);
                }
            }
        }
//        }
        // 把子菜单的子菜单再循环一遍
        for (RightResourceVO menu : childList) {// 没有url子菜单还有子菜单
//            List<RightResourceUrlEntity> rightResourceUrlList = rightResourceUrlMapper.getResourceByParent(menu.getId());
            List<RightResourceUrlEntity> rightResourceUrlList = new ArrayList<>();
            if (isOper) {
                rightResourceUrlList = rightResourceUrlMapper.getResourceByParent(id, null);
            } else {
                rightResourceUrlList = rightResourceUrlMapper.getResourceByParent(id, 0);
            }

            if (rightResourceUrlList.size() > 0) {
                List<RightResourceVO> subRightResourceVO = new ArrayList<>();
                for (int i = 0; i < rightResourceUrlList.size(); i++) {
                    RightResourceUrlEntity rg = rightResourceUrlList.get(i);
                    RightResourceVO rightSubResourceVO = new RightResourceVO(rg.getId(), rg.getName(), rg.getParentId(), rg.getOrders(), rg.getType());
                    if (rightGroupResourceSet.contains(rg.getId())) {
                        rightSubResourceVO.setIsSelected(1);
                    } else {
                        rightSubResourceVO.setIsSelected(0);
                    }
                    subRightResourceVO.add(rightSubResourceVO);
                }
                // 递归
                menu.setSubList(getChild(groupId, menu.getId(), subRightResourceVO, isOper));
            }
        } // 递归退出条件
        if (childList.size() == 0) {
            return null;
        }
        return childList;
    }

    private Set<Integer> getRightResourceGroupByGroupId(Integer groupId, Integer companyId) {
        Set<Integer> resourceIdSet = new HashSet<>();
        List<RightResourceGroupEntity> rightResourceGroupList = rightResourceGroupMapper.getResourcesByCompanyAndGroup(new RightResourceGroupEntity(companyId, groupId, null, StatusEnum.OK.getValue()));
        for (int i = 0; i < rightResourceGroupList.size(); i++) {
            Integer resourceId = rightResourceGroupList.get(i).getRightResourceId();
            resourceIdSet.add(resourceId);
        }
        return resourceIdSet;
    }

}
