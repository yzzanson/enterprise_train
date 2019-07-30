package com.enterprise.service.right.impl;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.bean.DingCorpUserDetail;
import com.enterprise.base.common.*;
import com.enterprise.base.entity.*;
import com.enterprise.base.entity.right.RightGroupEntity;
import com.enterprise.base.entity.right.RightResourceGroupEntity;
import com.enterprise.base.entity.right.RightResourceUrlEntity;
import com.enterprise.base.entity.right.UserRightGroupEntity;
import com.enterprise.base.enums.StatusEnum;
import com.enterprise.base.vo.*;
import com.enterprise.mapper.companyInfo.CompanyInfoMapper;
import com.enterprise.mapper.department.DepartmentMapper;
import com.enterprise.mapper.isvTickets.IsvTicketsMapper;
import com.enterprise.mapper.right.RightGroupMapper;
import com.enterprise.mapper.right.RightResourceGroupMapper;
import com.enterprise.mapper.right.RightResourceUrlMapper;
import com.enterprise.mapper.right.UserRightGroupMapper;
import com.enterprise.mapper.users.UserMapper;
import com.enterprise.mapper.users.UserXCompanyMapper;
import com.enterprise.mapper.users.UserXDeptMapper;
import com.enterprise.service.right.RightGroupService;
import com.enterprise.util.DateUtil;
import com.enterprise.util.dingtalk.DingHelper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/7/2 下午3:53
 */
@Service
public class RightGroupServiceImpl implements RightGroupService {

    @Resource
    private RightGroupMapper rightGroupMapper;

    @Resource
    private RightResourceUrlMapper rightResourceUrlMapper;

    @Resource
    private RightResourceGroupMapper rightResourceGroupMapper;

    @Resource
    private UserRightGroupMapper userRightGroupMapper;

    @Resource
    private DepartmentMapper departmentMapper;

    @Resource
    private UserXDeptMapper userXDeptMapper;

    @Resource
    private UserXCompanyMapper userXCompanyMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private IsvTicketsMapper isvTicketsMapper;

    @Resource
    private CompanyInfoMapper companyInfoMapper;

    private final static String OPER_CORPID = GlobalConstant.getOperCorpId();

    private final static String MANAGE_RIGHT_GROUP = GlobalConstant.getManageRoleName();

    @Override
    public Integer saveOrUpdateRightGroup(RightGroupEntity rightGroupEntity) {
        try {
            LoginUser loginUser = LoginUser.getUser();
            if (rightGroupEntity.getId() != null) {
                RightGroupEntity rightGroupEntityInDB = rightGroupMapper.getById(rightGroupEntity.getId());
                if(rightGroupEntityInDB!=null && rightGroupEntityInDB.getIsDefault()!=null && rightGroupEntityInDB.getIsDefault().equals(1)){
                    return 0;
                }
                rightGroupEntity.setUpdateTime(new Date());
                if(rightGroupEntity.getStatus()!=null && rightGroupEntity.getStatus().equals(0)){
                    userRightGroupMapper.batchDeleteByGroupId(rightGroupEntityInDB.getId());
                }
                return rightGroupMapper.updateRightGroup(rightGroupEntity);
            }
            if (StringUtils.isNotEmpty(rightGroupEntity.getName())) {
                List<RightGroupEntity> rightGroupEntityList = rightGroupMapper.getByCompanyIdAndName(loginUser.getCompanyID(), rightGroupEntity.getName(), null);
                if (rightGroupEntityList != null && rightGroupEntityList.size() > 0) {
                    RightGroupEntity rightGroupEntityInDB = rightGroupEntityList.get(0);
                    if(rightGroupEntityInDB!=null && rightGroupEntityInDB.getIsDefault()!=null && rightGroupEntityInDB.getIsDefault().equals(1)){
                        return 0;
                    }
                    BeanUtils.copyProperties(rightGroupEntityInDB, rightGroupEntity);
                    if(rightGroupEntity.getStatus()!=null && rightGroupEntity.getStatus().equals(0)){
                        userRightGroupMapper.batchDeleteByGroupId(rightGroupEntityInDB.getId());
                    }
                    return rightGroupMapper.updateRightGroup(rightGroupEntityInDB);
                }else{
                    Integer maxValue = rightGroupMapper.getMaxValueByCompany(loginUser.getCompanyID());
                    maxValue++;
                    rightGroupEntity.setValue(maxValue);
                    if(rightGroupEntity.getCompanyId()==null){
                        rightGroupEntity.setCompanyId(loginUser.getCompanyID());
                    }
                    return rightGroupMapper.createRightGroup(rightGroupEntity);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return 0;
    }




    @Override
    public Integer saveOrUpdateRightGroupSingle(RightGroupEntity rightGroupEntity) {
        try {
            if (rightGroupEntity.getId() != null) {
                RightGroupEntity rightGroupEntityInDB = rightGroupMapper.getById(rightGroupEntity.getId());
                if(rightGroupEntityInDB!=null && rightGroupEntityInDB.getIsDefault()!=null && rightGroupEntityInDB.getIsDefault().equals(1)){
                    return 0;
                }
                rightGroupEntity.setUpdateTime(new Date());
                if(rightGroupEntity.getStatus()!=null && rightGroupEntity.getStatus().equals(0)){
                    userRightGroupMapper.batchDeleteByGroupId(rightGroupEntityInDB.getId());
                }
                return rightGroupMapper.updateRightGroup(rightGroupEntity);
            }
            if (StringUtils.isNotEmpty(rightGroupEntity.getName())) {
                List<RightGroupEntity> rightGroupEntityList = rightGroupMapper.getByCompanyIdAndName(rightGroupEntity.getCompanyId(), rightGroupEntity.getName(), null);
                if (rightGroupEntityList != null && rightGroupEntityList.size() > 0) {
                    RightGroupEntity rightGroupEntityInDB = rightGroupEntityList.get(0);
                    if(rightGroupEntityInDB!=null && rightGroupEntityInDB.getIsDefault()!=null && rightGroupEntityInDB.getIsDefault().equals(1)){
                        return 0;
                    }
                    BeanUtils.copyProperties(rightGroupEntityInDB, rightGroupEntity);
                    if(rightGroupEntity.getStatus()!=null && rightGroupEntity.getStatus().equals(0)){
                        userRightGroupMapper.batchDeleteByGroupId(rightGroupEntityInDB.getId());
                    }
                    return rightGroupMapper.updateRightGroup(rightGroupEntityInDB);
                }else{
                    Integer maxValue = rightGroupMapper.getMaxValueByCompany(rightGroupEntity.getCompanyId());
                    maxValue++;
                    rightGroupEntity.setValue(maxValue);
                    if(rightGroupEntity.getCompanyId()==null){
                        rightGroupEntity.setCompanyId(rightGroupEntity.getCompanyId());
                    }
                    return rightGroupMapper.createRightGroup(rightGroupEntity);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<RightGroupEntity> getByCompanyIdAndName(Integer companyId, Integer status, String name) {
        return rightGroupMapper.getByCompanyIdAndName(companyId, name, status);
    }

    @Override
    public JSONObject getRightGroups(Integer companyId, PageEntity pageEntity) {
        Integer pageNo = pageEntity.getPageNo();
        Integer pageSize = pageEntity.getPageSize();
        Integer indexStart = (pageNo-1) * pageSize+1;
        PageHelper.startPage(pageNo, pageSize);
        PageInfo<RightGroupEntity> pageInfo = new PageInfo<>(rightGroupMapper.getByCompanyIdAndName(companyId,null,1));
        List<RightGroupVO> rightGroupList = new ArrayList<>();
        for(int i=0;i<pageInfo.getList().size();i++){
            RightGroupEntity rightGroupEntity = pageInfo.getList().get(i);
            RightGroupVO rightGroupVO = new RightGroupVO();
            rightGroupVO.setName(rightGroupEntity.getName());
            rightGroupVO.setId(rightGroupEntity.getId());
            rightGroupVO.setCreateTime(DateUtil.getDisplayYMDHMS(rightGroupEntity.getCreateTime()));
            rightGroupVO.setIndex(indexStart + i);
            rightGroupList.add(rightGroupVO);
        }
        Map<String, Object> dataMap = Maps.newHashMap();
        dataMap.put("list", rightGroupList);
        dataMap.put("total", pageInfo.getTotal());
        dataMap.put("page", new Page(pageInfo.getPrePage(), pageEntity.getPageNo(), pageInfo.getNextPage(), pageInfo.getLastPage()));
        return ResultJson.succResultJson(dataMap);
    }

    /**
     * @Param deptId 钉钉部门id
     * */
    @Override
    public JSONObject getDepartmentGroup(Integer deptId, Integer rightGroupId,String name) throws Exception {
        LoginUser loginUser = LoginUser.getUser();
        Integer companyId = loginUser.getCompanyID();
        String corpId = loginUser.getCorpID();
        if(deptId==null){
            deptId = 1;
        }
        List<DepartmentVO> departmentVOList = new ArrayList<>();
        List<UserRightGroupVO> userRightGroupList = new ArrayList<>();

        String accessToken = isvTicketsMapper.getIsvTicketByCompanyId(companyId).getCorpAccessToken();
        JSONObject jsonOjbect = DingHelper.getDGDepartmentList3(accessToken, DDConstant.RECURSION_DEPT);
        boolean isNoAuthorized = false;
        if (jsonOjbect.getInteger("errcode").equals(50004)) {
            isNoAuthorized = true;
        }
        DepartmentEntity department = departmentMapper.getByCompanyIdAndDingDeptId(companyId, deptId);
        List<DepartmentEntity> departmentList = departmentMapper.getDepartmentsByParentId(companyId, deptId);
        if(isNoAuthorized && (deptId==null||deptId.equals(1))){
            departmentList = departmentMapper.getDeptsByCompanyId(companyId,StatusEnum.OK.getValue());
        }else if(isNoAuthorized && !(deptId==null||deptId.equals(1))){
            departmentList = null;
        }
        List<UserXDeptEntity> userList = userXDeptMapper.getUserInDeptAndName(corpId, department.getId(), name, StatusEnum.OK.getValue());
        //(Integer companyId,Integer userId,Integer rightGroupId, Integer status,Date updateTime)
        List<UserRightGroupEntity> userRightGroupListInDB = userRightGroupMapper.findUserRightGroups2(new UserRightGroupEntity(companyId,null,rightGroupId,StatusEnum.OK.getValue(),null));
        Set<Integer> userRightGroupSet = new HashSet<>();
        if(CollectionUtils.isNotEmpty(userRightGroupListInDB)) {
            for (int i = 0; i < userRightGroupListInDB.size(); i++) {
                userRightGroupSet.add(userRightGroupListInDB.get(i).getUserId());
            }
        }
        DepartmentVO surrentDeptVO = new DepartmentVO(deptId,department.getName(),department.getParentId());

        if(CollectionUtils.isNotEmpty(departmentList)){
            surrentDeptVO.setHasChild(1);
            for (int i = 0; i < departmentList.size(); i++) {
                DepartmentEntity departmentInDB = departmentList.get(i);
                DepartmentVO departmentVO = new DepartmentVO();
                departmentVO.setId(departmentInDB.getDingDeptId());
                departmentVO.setName(departmentInDB.getName());
                departmentVO.setParentId(deptId);
                List<DepartmentEntity> subdepartmentList = departmentMapper.getDepartmentsByParentId(companyId, departmentInDB.getDingDeptId());
                if(CollectionUtils.isNotEmpty(subdepartmentList)){
                    departmentVO.setHasChild(1);
                }else{
                    departmentVO.setHasChild(0);
                }
                departmentVOList.add(departmentVO);
            }
        }else{
            surrentDeptVO.setHasChild(0);
            if(deptId.equals(1)){
                departmentList = departmentMapper.getUnauthDepartments(companyId);
                for (int i = 0; i < departmentList.size(); i++) {
                    DepartmentEntity departmentInDB = departmentList.get(i);
                    DepartmentVO departmentVO = new DepartmentVO();
                    departmentVO.setId(departmentInDB.getDingDeptId());
                    departmentVO.setName(departmentInDB.getName());
                    departmentVO.setParentId(deptId);
                    departmentVOList.add(departmentVO);
                }
            }
        }
        if(CollectionUtils.isNotEmpty(userList)){
            for (int i = 0; i < userList.size(); i++) {
                UserXDeptEntity userInDept = userList.get(i);
                UserEntity userEntity = userMapper.getUserById(userInDept.getUserId());
                UserRightGroupVO userRightGroupVO = new UserRightGroupVO(userEntity.getId(),userEntity.getName(),userEntity.getAvatar());
                if(userRightGroupSet.contains(userEntity.getId())){
                    userRightGroupVO.setIsSelected(1);
                }else{
                    userRightGroupVO.setIsSelected(0);
                }
                userRightGroupList.add(userRightGroupVO);
            }
        }
        CompanyInfoEntity companyInfoEntity = companyInfoMapper.getCompanyById(companyId);

        Map<String, Object> dataMap = Maps.newHashMap();
        dataMap.put("deptInfo", surrentDeptVO);
        dataMap.put("deptList", departmentVOList);
        dataMap.put("userList", userRightGroupList);
        if(companyInfoEntity.getRefreshTime()!=null){
            dataMap.put("refresh_time",DateUtil.getDisplayYMDHMS(companyInfoEntity.getRefreshTime()));
        }else{
            dataMap.put("refresh_time",null);
        }
        return ResultJson.succResultJson(dataMap);
    }

    @Override
    public JSONObject getGroupUser(Integer rightGroupId) {
        LoginUser loginUser = LoginUser.getUser();
        Integer companyId = loginUser.getCompanyID();
        List<UserRightGroupEntity> userRightGroupListInDB = userRightGroupMapper.findUserRightGroups2(new UserRightGroupEntity(companyId, null, rightGroupId, StatusEnum.OK.getValue(), null));
        List<UserRightGroupVO2> userRightList = new ArrayList<>();
        for (int i = 0; i < userRightGroupListInDB.size(); i++) {
            String name = userMapper.getNameById(userRightGroupListInDB.get(i).getUserId());
            UserRightGroupVO2 uGroupVO = new UserRightGroupVO2();
            uGroupVO.setUserId(userRightGroupListInDB.get(i).getUserId());
            if(StringUtils.isNotEmpty(name)){
                uGroupVO.setName(name);
            }
            userRightList.add(uGroupVO);
        }
        return ResultJson.succResultJson(userRightList);
    }

    /**
     * 初始化公司超管权限组
     * */
    @Override
    public Integer initCompanyManageGroup(String corpId) {
        if(corpId.equals(OPER_CORPID)){
            Integer addCount = 0;
            Integer comapnyId = isvTicketsMapper.getCompanyIdByCorpId(corpId);
            List<DingCorpUserDetail> corpManagerList = DingHelper.getCorpManager(corpId);
            List<RightGroupEntity> rightGroupEntityList = rightGroupMapper.getByCompanyIdAndName(comapnyId,MANAGE_RIGHT_GROUP,null);
            if(CollectionUtils.isEmpty(rightGroupEntityList)){
                RightGroupEntity manageRightGroup = new RightGroupEntity(comapnyId,MANAGE_RIGHT_GROUP,1,1,1,new Date(),new Date());
                Integer addRightGroup = rightGroupMapper.createRightGroup(manageRightGroup);
                if(addRightGroup>0){
                    Integer manageRightGroupId = manageRightGroup.getId();
                    List<RightResourceUrlEntity> resourceUrlList = new ArrayList<>();
                    List<RightResourceGroupEntity> addRightResourceGroupList = new ArrayList<>();
                    List<UserRightGroupEntity> addUserRightGroup = new ArrayList<>();
                    if(corpId.equals(OPER_CORPID)){
                        resourceUrlList = rightResourceUrlMapper.getResourceByOperate(null,1,null);
                    }else{
                        resourceUrlList = rightResourceUrlMapper.getResourceByOperate(0,1,null);
                    }
                    for (int i = 0; i < resourceUrlList.size(); i++) {
                        Integer rightResourceId = resourceUrlList.get(i).getId();
                        RightResourceGroupEntity rightResourceGroupEntity = new RightResourceGroupEntity(comapnyId,manageRightGroupId,rightResourceId,StatusEnum.OK.getValue(),new Date(),new Date());
                        addRightResourceGroupList.add(rightResourceGroupEntity);
                    }
                    rightResourceGroupMapper.batchInsert(addRightResourceGroupList);
                    for(int j=0;j<corpManagerList.size();j++){
                        DingCorpUserDetail dingCorpUserDetail = corpManagerList.get(j);
                        String dingUserId = dingCorpUserDetail.getUserid();
                        UserXCompany userXCompany = userXCompanyMapper.getByCompanyIdANdDingUserId(corpId, dingUserId);
                        if(userXCompany!=null) {
                            UserRightGroupEntity userRightGroupEntity = new UserRightGroupEntity(comapnyId, userXCompany.getUserId(), manageRightGroupId, StatusEnum.OK.getValue(), new Date(), new Date());
                            addUserRightGroup.add(userRightGroupEntity);
                        }
                    }
                    if(CollectionUtils.isNotEmpty(addUserRightGroup)){
                        addCount = userRightGroupMapper.batchInsert(addUserRightGroup);
                    }
                    return addCount;
                }
            }else{
                return 0;
            }
        }
        return 0;
    }

    @Override
    public JSONObject findUserRightGroup(Integer rightGroupId, String name) {
        String corpId = LoginUser.getUser().getCorpID();
        Integer companyId = LoginUser.getUser().getCompanyID();
        List<UserVO2> userList = userXCompanyMapper.getUserByName(corpId, name);
        List<UserRightGroupVO> resultList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(userList)){
            List<Integer> rightGroupUserList = userRightGroupMapper.getUserRightGroupByGroupId(rightGroupId,companyId);
            Set<Integer> rightGroupUserIdSet = new HashSet<>(rightGroupUserList);
            for (int i = 0; i < userList.size(); i++) {
                UserVO2 userVO = userList.get(i);
                Integer userId = userVO.getId();
                if(rightGroupUserIdSet!=null && rightGroupUserIdSet.contains(userId)){
                    UserRightGroupVO userRightGroupVO = new UserRightGroupVO(userVO.getId(),userVO.getName(),1);
                    resultList.add(userRightGroupVO);
                }else{
                    UserRightGroupVO userRightGroupVO = new UserRightGroupVO(userVO.getId(),userVO.getName(),0);
                    resultList.add(userRightGroupVO);
                }
            }
            return ResultJson.succResultJson(resultList);
        }else{
            return ResultJson.succResultJson("");
        }
    }

    @Override
    public JSONObject getManageUser() {
        LoginUser loginUser = LoginUser.getUser();
        Integer companyId = loginUser.getCompanyID();
        RightGroupEntity rightGroupEntity = rightGroupMapper.getSuperManageRightGroup(companyId);
        if(rightGroupEntity==null){
            return ResultJson.errorResultJson("不存在的权限组");
        }
        Integer rightGroupId = rightGroupEntity.getId();
        List<Integer> userRightGroupList = userRightGroupMapper.getUserRightGroupByGroupId(rightGroupId, companyId);
        StringBuffer sbf = new StringBuffer();
        if(CollectionUtils.isNotEmpty(userRightGroupList)){
            for (int i = 0; i < userRightGroupList.size(); i++) {
                Integer userId = userRightGroupList.get(i);
                String userName = userMapper.getNameById(userId);
                if(sbf.length()==0){
                    sbf.append(userName);
                }else{
                    sbf.append("、").append(userName);
                }
            }
            return ResultJson.succResultJson(sbf.toString());
        }
        return ResultJson.errorResultJson("数据为空");
    }

    public static void main(String[] args) {
        List<Integer> userIdList = new ArrayList<>();
        userIdList.add(16);
        userIdList.add(10);
        Set<Integer> rightGroupUserIdSet = new HashSet<>(userIdList);
        if(rightGroupUserIdSet.contains(6)){
            System.out.println(1);
        }
//        RightGroupEntity rightGroupEntity = new RightGroupEntity();
//        rightGroupEntity.setIsDefault(1);
//       AssertUtil.isTrue(!rightGroupEntity.getIsDefault().equals(1),"sss");
    }

}
