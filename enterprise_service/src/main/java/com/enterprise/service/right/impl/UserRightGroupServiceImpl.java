package com.enterprise.service.right.impl;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.GlobalConstant;
import com.enterprise.base.common.LoginUser;
import com.enterprise.base.common.ResultJson;
import com.enterprise.base.entity.right.RightGroupEntity;
import com.enterprise.base.entity.right.RightResourceUrlEntity;
import com.enterprise.base.entity.right.UserRightGroupEntity;
import com.enterprise.base.enums.StatusEnum;
import com.enterprise.base.vo.RightResourceUserVO;
import com.enterprise.mapper.companyInfo.CompanyInfoMapper;
import com.enterprise.mapper.right.RightGroupMapper;
import com.enterprise.mapper.right.RightResourceGroupMapper;
import com.enterprise.mapper.right.RightResourceUrlMapper;
import com.enterprise.mapper.right.UserRightGroupMapper;
import com.enterprise.mapper.users.UserXCompanyMapper;
import com.enterprise.oa.OAMessageUtil;
import com.enterprise.service.right.UserRightGroupService;
import com.enterprise.util.AssertUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by anson on 18/3/24.
 */
@Service
public class UserRightGroupServiceImpl implements UserRightGroupService {

    private static final String GLOBAL_MANAGE_ROLE_NAME = GlobalConstant.getManageRoleName();

    @Resource
    private UserRightGroupMapper userRightGroupMapper;

    @Resource
    private RightGroupMapper rightGroupMapper;

    @Resource
    private UserXCompanyMapper userXCompanyMapper;

    @Resource
    private CompanyInfoMapper companyInfoMapper;

    @Resource
    private RightResourceUrlMapper rightResourceUrlMapper;

    @Resource
    private RightResourceGroupMapper rightResourceGroupMapper;

    @Override
    public List<UserRightGroupEntity> findUserRightGroups(Integer companyId, Integer userId) {
        return userRightGroupMapper.findUserRightGroups(new UserRightGroupEntity(companyId, userId, StatusEnum.OK.getValue()));
    }

    @Override
    public UserRightGroupEntity findUserRightGroupByGroupId(UserRightGroupEntity userRightGroupEntity) {
        return userRightGroupMapper.findUserRightGroupByGroupId(userRightGroupEntity);
    }

    @Override
    public Integer getUserRightGroupCount(UserRightGroupEntity userRightGroupEntity) {
        return userRightGroupMapper.getUserRightGroupCount(userRightGroupEntity);
    }

    @Override
    public Integer getUserGroupByCompanyIdAndUserId(Integer companyId,Integer userId) {
        return userRightGroupMapper.getUserGroupByCompanyIdAndUserId(companyId,userId);
    }

    @Override
    public Integer batchInsert(List<UserRightGroupEntity> insertList) {
        return userRightGroupMapper.batchInsert(insertList);
    }

    @Override
    public Integer batchUpdate(List<UserRightGroupEntity> updateList) {
        return userRightGroupMapper.batchUpdate(updateList);
    }

    @Override
    public JSONObject saveUserRightGroup(String userIds, Integer groupId) {
        LoginUser loginUser = LoginUser.getUser();
        Integer companyId = loginUser.getCompanyID();
        String companyName = companyInfoMapper.getCompanyNameById(companyId);
        String corpId = loginUser.getCorpID();
        String[] userIdArray = userIds.split(",");
        List<UserRightGroupEntity> addUserRightGroupList = new ArrayList<>();
        List<UserRightGroupEntity> updateUserRightGroupList = new ArrayList<>();

        Boolean isManageGroup = false;
        RightGroupEntity rightGroupEntity = rightGroupMapper.getById(groupId);
        if (rightGroupEntity.getName().equals(GLOBAL_MANAGE_ROLE_NAME) && rightGroupEntity.getIsDefault().equals(1)) {
            isManageGroup = true;
        }

        userRightGroupMapper.batchDeleteByGroupId(groupId);
        //是否为超级管理员,如果为超级管理员则超管不能被取消
        for (int i = 0; i < userIdArray.length; i++) {
            Integer iUserId = Integer.valueOf(userIdArray[i]);
            // public UserRightGroupEntity(Integer companyId,Integer userId,Integer rightGroupId, Integer status,Date updateTime)
            UserRightGroupEntity userRightGroupInDB = userRightGroupMapper.findUserRightGroupByGroupId(new UserRightGroupEntity(companyId, iUserId, groupId, null, null));
            if (userRightGroupInDB != null) {
                UserRightGroupEntity updateUserRightGroupEntity = new UserRightGroupEntity(userRightGroupInDB.getId(), groupId, StatusEnum.OK.getValue(), new Date());
                updateUserRightGroupList.add(updateUserRightGroupEntity);
            } else {
                //UserRightGroupEntity(Integer companyId, Integer userId, Integer rightGroupId, Integer status, Date createTime, Date updateTime)
                UserRightGroupEntity updateUserRightGroupEntity = new UserRightGroupEntity(companyId, iUserId, groupId, StatusEnum.OK.getValue(), new Date());
                addUserRightGroupList.add(updateUserRightGroupEntity);
            }
        }

        if (isManageGroup) {
            Integer superManageUserId = userXCompanyMapper.getSuperManageUserId(corpId);
            if (superManageUserId != null && superManageUserId > 0) {
                UserRightGroupEntity userRightGroupInDB = userRightGroupMapper.findUserRightGroupByGroupId(new UserRightGroupEntity(companyId, superManageUserId, groupId, null, null));
                UserRightGroupEntity updateUserRightGroupEntity = new UserRightGroupEntity(userRightGroupInDB.getId(), groupId, StatusEnum.OK.getValue(), new Date());
                updateUserRightGroupList.add(updateUserRightGroupEntity);
            }
        }
        Integer insertCount = 0;
        Integer updateCount = 0;
        if (CollectionUtils.isNotEmpty(addUserRightGroupList)) {
            insertCount = userRightGroupMapper.batchInsert(addUserRightGroupList);
        }
        if (CollectionUtils.isNotEmpty(updateUserRightGroupList)) {
            updateCount = userRightGroupMapper.batchUpdate(updateUserRightGroupList);
        }
        String oaMessage = "公司:" + companyName + ",权限组" + rightGroupEntity.getName() + "成功插入" + insertCount + "条数据,成攻更新" + updateCount + "条数据";
        OAMessageUtil.sendTextMsgToDept(oaMessage);
        return ResultJson.succResultJson(userIds);
    }

    /**
     * 获取用户的菜单
     */
    @Override
    public JSONObject getUserResource() {
        LoginUser loginUser = LoginUser.getUser();
        AssertUtil.notNull(loginUser,"请登录!");
        Integer companyId = loginUser.getCompanyID();
        Integer userId = loginUser.getUserID();
        String corpId = loginUser.getCorpID();
        String operCorpId = GlobalConstant.getOperCorpId();
        //首先获取用户权限组
        List<UserRightGroupEntity> userRightGroupList = userRightGroupMapper.findUserRightGroups(new UserRightGroupEntity(companyId, userId, StatusEnum.OK.getValue()));
        //查找权限组下所有资源resourceId集合
        List<Integer> resourceIdList = rightResourceGroupMapper.getResourcesByCompanyAndGroups(companyId, userRightGroupList);
        List<RightResourceUserVO> rightResourceUrlVOList = new ArrayList<>();
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
        //根据资源id获取其所有父级节点id(包括当前资源)
        Set<Integer> parentChildResourceIdSet = getParentResouceSet(resourceIdList);
        Iterator itor = parentChildResourceIdSet.iterator();
        while (itor.hasNext()) {
            Object nextObj = itor.next();
            if (nextObj == null) {
                parentChildResourceIdSet.remove(nextObj);
            }
        }

        for (int i = 0; i < rightResourceUrlList.size(); i++) {
            RightResourceUrlEntity rightResourceUrl = rightResourceUrlList.get(i);
            if (rightResourceUrl.getParentId() == null && parentChildResourceIdSet.contains(rightResourceUrl.getId())) {
                RightResourceUserVO rightResourceVO = new RightResourceUserVO(rightResourceUrl.getId(), rightResourceUrl.getName(), rightResourceUrl.getParentId(), rightResourceUrl.getType(), rightResourceUrl.getOrders());
                rightResourceUrlVOList.add(rightResourceVO);
            }
        }
        for (RightResourceUserVO rightResourceVO : rightResourceUrlVOList) {
//            if (resourceIdSet.contains(rightResourceVO.getId())) {
            if (parentChildResourceIdSet.contains(rightResourceVO.getId())) {
                rightResourceVO.setSubList(getChild(parentChildResourceIdSet, rightResourceVO.getId(), rightResourceUrlVOList, isOper));
            }
        }
        return ResultJson.succResultJson(rightResourceUrlVOList);
    }


    /**
     * 获取用户的菜单
     */
    @Override
    public JSONObject getUserResource2(String corpId,Integer companyId,Integer userId) {
//        LoginUser loginUser = LoginUser.getUser();
        String operCorpId = GlobalConstant.getOperCorpId();
        //首先获取用户权限组
        List<UserRightGroupEntity> userRightGroupList = userRightGroupMapper.findUserRightGroups(new UserRightGroupEntity(companyId, userId, StatusEnum.OK.getValue()));
        //查找权限组下所有资源resourceId集合
        List<Integer> resourceIdList = rightResourceGroupMapper.getResourcesByCompanyAndGroups(companyId, userRightGroupList);
        List<RightResourceUserVO> rightResourceUrlVOList = new ArrayList<>();
        Set<Integer> resourceIdSet = new HashSet<>(resourceIdList);
        List<RightResourceUrlEntity> rightResourceUrlList = new ArrayList<>();
        boolean isOper = false;
        if (corpId.equals(operCorpId)) {
            isOper = true;
        }
        if (isOper) {
            rightResourceUrlList = rightResourceUrlMapper.getResourceByOperate(1, 1, 1);
        } else {
            rightResourceUrlList = rightResourceUrlMapper.getResourceByOperate(0, 1, 1);
        }
        //根据资源id获取其所有父级节点id(包括当前资源)
        Set<Integer> parentChildResourceIdSet = getParentResouceSet(resourceIdList);
        Iterator itor = parentChildResourceIdSet.iterator();
        while (itor.hasNext()) {
            Object nextObj = itor.next();
            if (nextObj == null) {
                parentChildResourceIdSet.remove(nextObj);
            }
            System.out.println("set:" + nextObj);
        }

        for (int i = 0; i < rightResourceUrlList.size(); i++) {
            RightResourceUrlEntity rightResourceUrl = rightResourceUrlList.get(i);
            if (rightResourceUrl.getParentId() == null && parentChildResourceIdSet.contains(rightResourceUrl.getId())) {
                RightResourceUserVO rightResourceVO = new RightResourceUserVO(rightResourceUrl.getId(), rightResourceUrl.getName(), rightResourceUrl.getParentId(), rightResourceUrl.getType(), rightResourceUrl.getOrders());
                rightResourceUrlVOList.add(rightResourceVO);
            }
        }
        for (RightResourceUserVO rightResourceVO : rightResourceUrlVOList) {
//            if (resourceIdSet.contains(rightResourceVO.getId())) {
            if (parentChildResourceIdSet.contains(rightResourceVO.getId())) {
                rightResourceVO.setSubList(getChild(parentChildResourceIdSet, rightResourceVO.getId(), rightResourceUrlVOList, isOper));
            }
        }
        return ResultJson.succResultJson(rightResourceUrlVOList);
    }

    @Override
    public Integer checkIsSuperManage(Integer companyId, Integer userId) {
        RightGroupEntity rightGroupEntity = rightGroupMapper.getSuperManageRightGroup(companyId);
        if(rightGroupEntity!=null){
            Integer rightGroupId = rightGroupEntity.getId();
            //Integer companyId,Integer userId,Integer rightGroupId, Integer status,Date updateTime
            Integer result = userRightGroupMapper.getUserRightGroupCount(new UserRightGroupEntity(companyId, userId, rightGroupId, StatusEnum.OK.getValue(), null));
            if(result>0){
                return 1;
            }
            return 0;
        }
        return 0;
    }

    private List<RightResourceUserVO> getChild(Set<Integer> rightGroupResourceSet, Integer id, List<RightResourceUserVO> rootMenu, boolean isOper) {
        // 子菜单
        List<RightResourceUserVO> childList = new ArrayList<>();
//        for (RightResourceVO menu : rootMenu) {
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
                    RightResourceUserVO rightResourceVO = new RightResourceUserVO(subRightResoueceUrl.getId(), subRightResoueceUrl.getName(), subRightResoueceUrl.getParentId(), subRightResoueceUrl.getOrders(), subRightResoueceUrl.getType());
                    if (rightGroupResourceSet.contains(subRightResoueceUrl.getId())) {
                        childList.add(rightResourceVO);
                    }
                }
            }
        }
//        }
        // 把子菜单的子菜单再循环一遍
        for (RightResourceUserVO menu : childList) {// 没有url子菜单还有子菜单
//            List<RightResourceUrlEntity> rightResourceUrlList = rightResourceUrlMapper.getResourceByParent(menu.getId());
            List<RightResourceUrlEntity> rightResourceUrlList = new ArrayList<>();
            if (isOper) {
                rightResourceUrlList = rightResourceUrlMapper.getResourceByParent(id, null);
            } else {
                rightResourceUrlList = rightResourceUrlMapper.getResourceByParent(id, 0);
            }

            if (rightResourceUrlList.size() > 0) {
                List<RightResourceUserVO> subRightResourceVO = new ArrayList<>();
                for (int i = 0; i < rightResourceUrlList.size(); i++) {
                    RightResourceUrlEntity rg = rightResourceUrlList.get(i);
                    RightResourceUserVO rightSubResourceVO = new RightResourceUserVO(rg.getId(), rg.getName(), rg.getParentId(), rg.getOrders(), rg.getType());
                    if (rightGroupResourceSet.contains(rg.getId())) {
                        subRightResourceVO.add(rightSubResourceVO);
                    }
                }
                // 递归
                menu.setSubList(getChild(rightGroupResourceSet, menu.getId(), subRightResourceVO, isOper));
            }
        } // 递归退出条件
        if (childList.size() == 0) {
            return null;
        }
        return childList;
    }

    /**
     * 获取当前所有资源点的父节点
     */
    private Set<Integer> getParentResouceSet(List<Integer> resourceIdList) {
        Set<Integer> allResourceSet = new HashSet<>(resourceIdList);
        if (CollectionUtils.isNotEmpty(resourceIdList)) {
            List<Integer> parentIdList = rightResourceUrlMapper.getParentIdsByResourceIds(resourceIdList);
            for (int i = 0; i < parentIdList.size(); i++) {
                getParent(allResourceSet, parentIdList.get(i));
            }
        }
        return allResourceSet;

    }


    private Set<Integer> getParent(Set<Integer> rightGroupResourceSet, Integer id) {
        // 子菜单
        List<Integer> parentIdList = new ArrayList();
        if (!rightGroupResourceSet.contains(id) && id != null) {
            rightGroupResourceSet.add(id);
        }
        Integer parentId = null;
//        for (RightResourceVO menu : rootMenu) {
        // 遍历所有节点，将父菜单id与传过来的id比较
        RightResourceUrlEntity rightResourceUrl = rightResourceUrlMapper.getParentByResourceId(id);
//        List<RightResourceUrlEntity> rightResourceUrlLists = rightResourceUrlMapper.getResourceByParent(id);
        if (rightResourceUrl != null) {
            if (!rightGroupResourceSet.contains(rightResourceUrl.getParentId())) {
                parentIdList.add(rightResourceUrl.getParentId());
                if (rightResourceUrl.getParentId() != null) {
                    rightGroupResourceSet.add(rightResourceUrl.getParentId());
                }
            }
        }
        // 把子菜单的子菜单再循环一遍
        for (Integer parentId2 : parentIdList) {// 没有url子菜单还有子菜单
//            List<RightResourceUrlEntity> rightResourceUrlList = rightResourceUrlMapper.getResourceByParent(menu.getId());
            RightResourceUrlEntity rightResourceUrlSub = rightResourceUrlMapper.getParentByResourceId(parentId2);
            if (rightResourceUrlSub != null) {
                // 递归
                getParent(rightGroupResourceSet, rightResourceUrlSub.getId());
            }
        } // 递归退出条件
        if (parentIdList.size() == 0) {
            return null;
        }
        return rightGroupResourceSet;
    }


}
