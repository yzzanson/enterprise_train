package com.enterprise.service.department.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.open.client.api.model.corp.CorpUserDetail;
import com.dingtalk.open.client.api.model.corp.Department;
import com.dingtalk.open.client.api.model.corp.DepartmentDetail;
import com.enterprise.base.bean.DingCorpUserDetail;
import com.enterprise.base.bean.DingUserDetail;
import com.enterprise.base.common.DDConstant;
import com.enterprise.base.common.LoginUser;
import com.enterprise.base.common.ResultJson;
import com.enterprise.base.entity.*;
import com.enterprise.base.entity.right.RightGroupEntity;
import com.enterprise.base.entity.right.UserRightGroupEntity;
import com.enterprise.base.enums.StatusEnum;
import com.enterprise.mapper.companyInfo.CompanyInfoMapper;
import com.enterprise.mapper.department.DepartmentMapper;
import com.enterprise.mapper.isvTickets.IsvTicketsMapper;
import com.enterprise.mapper.right.RightGroupMapper;
import com.enterprise.mapper.right.UserRightGroupMapper;
import com.enterprise.mapper.users.UserMapper;
import com.enterprise.mapper.users.UserXCompanyMapper;
import com.enterprise.mapper.users.UserXDeptMapper;
import com.enterprise.service.department.DepartmentService;
import com.enterprise.service.isvTickets.IsvTicketsService;
import com.enterprise.util.SerializeUtil;
import com.enterprise.util.dingtalk.DingHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/4/18 下午4:42
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Resource
    private DepartmentMapper departmentMapper;

    @Resource
    private IsvTicketsService isvTicketsService;

    @Resource
    private UserXCompanyMapper userXCompanyMapper;

    @Resource
    private UserXDeptMapper userXDeptMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private IsvTicketsMapper isvTicketsMapper;

    @Resource
    private RightGroupMapper rightGroupMapper;

    @Resource
    private UserRightGroupMapper userRightGroupMapper;

    @Resource
    private CompanyInfoMapper companyInfoMapper;


    @Override
    public String getDeptNameById(Integer companyId, Integer id) {
        return departmentMapper.getDeptNameById(companyId, id);
    }

    @Override
    public void synchronize(JSONArray dingDepts, List<DepartmentEntity> deptsInDB, String corpId) throws Exception {
        List<DepartmentEntity> diffDepts = getNewDepts(dingDepts, deptsInDB, corpId);
        if (diffDepts != null && diffDepts.size() > 0) {
            for (int i = 0; i < diffDepts.size(); i++) {
                DepartmentEntity departmentEntity = diffDepts.get(i);
                DepartmentEntity departmentInDB = departmentMapper.getByCompanyIdAndDingDeptId(departmentEntity.getCompanyId(), departmentEntity.getDingDeptId());
                if (departmentInDB == null) {
                    departmentMapper.insertSingle(departmentEntity);
                } else if (StringUtils.isNotEmpty(departmentEntity.getName()) && StringUtils.isNotEmpty(departmentInDB.getName()) && !departmentEntity.getName().equals(departmentInDB.getName())) {
                    departmentInDB.setName(departmentEntity.getName());
                    departmentMapper.updateDepartment(departmentInDB);
                }
            }
//           departmentMapper.insertBatch(diffDepts);
        }
    }

    /**
     * 创建/修改/删除部门
     */
    @Override
    public synchronized Integer createOrModifyCallBackDepartment(String corpId, String dingDeptId, Integer type) {
        IsvTicketsEntity isvTicketsEntity = isvTicketsService.getIsvTicketByCorpId(corpId);
        Integer companyId = isvTicketsEntity.getCompanyId();
        Integer idInDB = departmentMapper.getIdByCompanyIdAndDingDeptId(companyId, Integer.valueOf(dingDeptId));
        if (type != 2) {//新增,修改 0,1
            JSONObject jsonObject = DingHelper.getDepartmentDetail(corpId, dingDeptId);
            DepartmentDetail department = JSONObject.parseObject(jsonObject.toJSONString(), DepartmentDetail.class);
            if (idInDB == null || (idInDB != null && idInDB <= 0)) {
                DepartmentEntity departmentEntity = new DepartmentEntity(companyId, department.getId().intValue(), department.getName(), StatusEnum.OK.getValue(), department.getParentid().intValue());
                return departmentMapper.insertSingle(departmentEntity);
            } else {
                DepartmentEntity departmentEntity = new DepartmentEntity(idInDB, department.getName(), new Date());
                return departmentMapper.updateDepartment(departmentEntity);
            }
        } else {
            if (idInDB == null) {
                return 0;
            } else {
                DepartmentEntity departmentEntity = new DepartmentEntity(idInDB, StatusEnum.DELETE.getValue(), new Date());
                Integer result = departmentMapper.updateDepartment(departmentEntity);
                //将该部门下的员工清空
                userXDeptMapper.batchSafeDelteDepartment(corpId, idInDB, StatusEnum.DELETE.getValue());
                return result;
            }
        }
    }

    @Override
    public DepartmentEntity getTopDepartment(Integer companyId) {
        return departmentMapper.getByCompanyIdAndDingDeptId(companyId, 1);
    }

    /**
     * 重新同步组织架构
     */
    @Override
    public JSONObject syncCompany(Integer companyId) throws Exception {
        IsvTicketsEntity isvTicketsEntity = isvTicketsMapper.getIsvTicketByCompanyId(companyId);
        String corpId = isvTicketsEntity.getCorpId();
        String accessToken = isvTicketsEntity.getCorpAccessToken();
        //将department部门列表清空
        //将user_company清空
        //将user_department清空
        departmentMapper.batchUpdateDepartment(companyId, StatusEnum.DELETE.getValue());
        userXCompanyMapper.batchUpdateUserXCompany(corpId, StatusEnum.DELETE.getValue());
        userXDeptMapper.batcUpdateUserXDepartment(corpId, StatusEnum.DELETE.getValue());
        JSONObject jsonOjbect = null;
        try {
            jsonOjbect = DingHelper.getDGDepartmentList3(accessToken, DDConstant.RECURSION_DEPT);
            if (jsonOjbect.getInteger("errcode").equals(50004)) {
                JSONObject authDeptJson = DingHelper.getAuthedDept(corpId);
                JSONArray deptIds = authDeptJson.getJSONObject("auth_org_scopes").getJSONArray("authed_dept");
                List<Department> departmentList = new ArrayList<>();
                List<String> dingUserList = new ArrayList<>();
                if (deptIds != null && deptIds.size() > 0) {
                    for (int i = 0; i < deptIds.size(); i++) {
                        JSONObject object = new JSONObject();
                        object.put("id", deptIds.get(i).toString());
                        //查出自己部门
                        JSONObject deptJsonObject = DingHelper.getDepartmentDetail(corpId, deptIds.getString(i));
                        Department department = JSON.parseObject(deptJsonObject.toJSONString(), Department.class);
                        // 子部门 {"createDeptGroup":false,"name":"哈哈哈","id":62196779,"autoAddUser":false,"parentid":62268354}
                        departmentList.add(department);
                    }
                }
                JSONArray userIdJsonArray = authDeptJson.getJSONObject("auth_org_scopes").getJSONArray("authed_user");
                dingUserList = JSONObject.parseArray(userIdJsonArray.toJSONString(), String.class);
                syncDepartmentAndUser(companyId, departmentList, dingUserList);
            } else {
                JSONArray jsonArray = jsonOjbect.getJSONArray("department");
                List<Department> departmentList = JSONArray.parseArray(jsonArray.toJSONString(), Department.class);
                syncDepartment(companyId, departmentList);
                syncUser(companyId, departmentList);
                //给予所有超管后台超管权限
                syncRight(companyId);
                companyInfoMapper.modifyCompanyInfo(new CompanyInfoEntity(companyId, new Date()));
            }
        } catch (Exception e) {

        }
        return ResultJson.succResultJson("同步成功");
    }


    /**
     * 重新同步组织架构
     */
    @Override
    public JSONObject syncCompany() throws Exception {
        LoginUser loginUser = LoginUser.getUser();
        Integer companyId = loginUser.getCompanyID();
        IsvTicketsEntity isvTicketsEntity = isvTicketsMapper.getIsvTicketByCompanyId(companyId);
        String corpId = isvTicketsEntity.getCorpId();
        String accessToken = isvTicketsEntity.getCorpAccessToken();
        //将department部门列表清空
        //将user_company清空
        //将user_department清空
        departmentMapper.batchUpdateDepartment(companyId, StatusEnum.DELETE.getValue());
        userXCompanyMapper.batchUpdateUserXCompany(corpId, StatusEnum.DELETE.getValue());
        userXDeptMapper.batcUpdateUserXDepartment(corpId, StatusEnum.DELETE.getValue());
        JSONObject jsonOjbect = null;
        try {
            jsonOjbect = DingHelper.getDGDepartmentList3(accessToken, DDConstant.RECURSION_DEPT);
            if (jsonOjbect.getInteger("errcode").equals(50004)) {
                JSONObject authDeptJson = DingHelper.getAuthedDept(corpId);
                JSONArray deptIds = authDeptJson.getJSONObject("auth_org_scopes").getJSONArray("authed_dept");
                List<Department> departmentList = new ArrayList<>();
                List<String> dingUserList = new ArrayList<>();
                if (deptIds != null && deptIds.size() > 0) {
                    for (int i = 0; i < deptIds.size(); i++) {
                        JSONObject object = new JSONObject();
                        object.put("id", deptIds.get(i).toString());
                        //查出自己部门
                        JSONObject deptJsonObject = DingHelper.getDepartmentDetail(corpId, deptIds.getString(i));
                        Department department = JSON.parseObject(deptJsonObject.toJSONString(), Department.class);
                        // 子部门 {"createDeptGroup":false,"name":"哈哈哈","id":62196779,"autoAddUser":false,"parentid":62268354}
                        departmentList.add(department);
                    }
                }
                JSONArray userIdJsonArray = authDeptJson.getJSONObject("auth_org_scopes").getJSONArray("authed_user");
                dingUserList = JSONObject.parseArray(userIdJsonArray.toJSONString(), String.class);
                syncDepartmentAndUser(companyId, departmentList, dingUserList);
                companyInfoMapper.modifyCompanyInfo(new CompanyInfoEntity(companyId, new Date()));
            } else {
                JSONArray jsonArray = jsonOjbect.getJSONArray("department");
                List<Department> departmentList = JSONArray.parseArray(jsonArray.toJSONString(), Department.class);
                syncDepartment(companyId, departmentList);
                syncUser(companyId, departmentList);
                //给予所有超管后台超管权限
                syncRight(companyId);
                companyInfoMapper.modifyCompanyInfo(new CompanyInfoEntity(companyId, new Date()));
            }
        } catch (Exception e) {

        }
        return ResultJson.succResultJson("同步成功");
    }


    private void syncDepartmentAndUser(Integer companyId, List<Department> departmentList, List<String> dingUserList) {
        boolean isContainTop = false;
        List<UserXCompany> insertList = new ArrayList<>();
        List<UserXCompany> updateList = new ArrayList<>();
        List<UserXDeptEntity> insertDeptList = new ArrayList<>();
        List<UserXDeptEntity> updateDeptList = new ArrayList<>();
        CompanyInfoEntity companyInfoInDB = companyInfoMapper.getCompanyById(companyId);
        IsvTicketsEntity isvTicketEntity = isvTicketsMapper.getIsvTicketByCompanyId(companyId);
        String corpId = isvTicketEntity.getCorpId();
        for (int i = 0; i < departmentList.size(); i++) {
            Department department = departmentList.get(i);
            Long dingDeptId = department.getId();
            if (department.getId().equals(1)) {
                isContainTop = true;
            }
            DepartmentEntity departmentInDB = departmentMapper.getByCompanyIdAndDingDeptId(companyId, dingDeptId.intValue());
            if (departmentInDB == null) {
                //DepartmentEntity(Integer companyId, Integer dingDeptId, String name, Integer status, Integer parentId, Date createTime, Date updateTime)
                departmentInDB = new DepartmentEntity(companyId, dingDeptId.intValue(), department.getName(), StatusEnum.OK.getValue(), department.getParentid().intValue(), new Date(), new Date());
                departmentMapper.insertSingle(departmentInDB);
            } else {
                departmentInDB.setStatus(StatusEnum.OK.getValue());
                departmentInDB.setName(department.getName());
                departmentInDB.setParentId(department.getParentid().intValue());
                departmentInDB.setUpdateTime(new Date());
                departmentMapper.updateDepartment(departmentInDB);
            }
            if (i == departmentList.size() - 1 && !isContainTop) {
                departmentInDB = departmentMapper.getByCompanyIdAndDingDeptId(companyId, 1);
                if (departmentInDB != null) {
                    departmentInDB.setUpdateTime(new Date());
                    departmentInDB.setStatus(1);
                    departmentMapper.updateDepartment(departmentInDB);
                } else {
                    departmentInDB = new DepartmentEntity(companyId, 1, companyInfoInDB.getName(), StatusEnum.OK.getValue(), null, new Date(), new Date());
                    departmentMapper.insertSingle(departmentInDB);
                }
//                Department departmentTop = new Department();
//                departmentTop.setId(1L);
//                departmentTop.setName(companyInfoInDB.getName());
//                departmentTop.setParentid(null);
//                departmentList.add(departmentTop);
            }
        }
        Set<String> dingUserIdSet = new HashSet<>();
        if(CollectionUtils.isNotEmpty(departmentList)) {
            for (int i = 0; i < departmentList.size(); i++) {
                Long departmentId = departmentList.get(i).getId();
                DepartmentEntity departmentEntity = departmentMapper.getByCompanyIdAndDingDeptId(companyId, departmentId.intValue());
                Integer deptId = departmentEntity.getId();
                List<CorpUserDetail> userList = DingHelper.getMembersByCorpIdAndDeptId(corpId, departmentId);
                if (CollectionUtils.isNotEmpty(userList)) {
                    for (int j = 0; j < userList.size(); j++) {
                        CorpUserDetail corpUserDetail = userList.get(j);
                        String dingUserId = corpUserDetail.getUserid();
                        String dingId = corpUserDetail.getDingId();
                        Integer isAdmin = corpUserDetail.getIsAdmin()!=null&&corpUserDetail.getIsAdmin()?1:0;
                        Integer isBoss = corpUserDetail.getIsBoss()!=null&&corpUserDetail.getIsBoss()?1:0;
                        UserEntity userEntity = userMapper.getUserByDingId(dingId);
                        UserXCompany userXCompany = userXCompanyMapper.getByCompanyIdANdDingUserId(corpId, dingUserId);
                        if (userEntity != null) {
                            if (userXCompany == null && (CollectionUtils.isEmpty(dingUserIdSet) || !dingUserIdSet.contains(dingUserId))) {
                                //public UserXCompany(String corpId, String dingUserId, Integer userId, Integer isAdmin, Integer isBoss, Integer status, Date createTime, Date updateTime, Integer source)
                                userXCompany = new UserXCompany(corpId, dingUserId, userEntity.getId(), isAdmin, isBoss, StatusEnum.OK.getValue(), new Date(), new Date(), null);
                                userXCompany.setIsSuperManage(userXCompany.getIsSuperManage());
                                insertList.add(userXCompany);
                                dingUserIdSet.add(dingUserId);
                            } else if (userXCompany != null && (CollectionUtils.isEmpty(dingUserIdSet) || !dingUserIdSet.contains(dingUserId))) {
                                userXCompany.setIsAdmin(isAdmin);
                                userXCompany.setIsBoss(isBoss);
                                userXCompany.setIsSuperManage(userXCompany.getIsSuperManage());
                                updateList.add(userXCompany);
                                dingUserIdSet.add(dingUserId);
                            }
                            UserXDeptEntity userXDept = userXDeptMapper.getByUserIdAndCorpIdAndDeptId(userEntity.getId(), corpId, deptId);
                            if (userXDept == null) {
                                //String corpId, Integer deptId, String deptName, String dingUserId, Integer userId, Integer status, Date createTime, Date updateTime
                                userXDept = new UserXDeptEntity(corpId, deptId, departmentEntity.getName(), dingUserId, userEntity.getId(), StatusEnum.OK.getValue(), new Date(), new Date());
                                insertDeptList.add(userXDept);
                            } else {
                                userXDept.setDeptName(departmentEntity.getName());
                                userXDept.setUpdateTime(new Date());
                                userXDept.setStatus(StatusEnum.OK.getValue());
                                updateDeptList.add(userXDept);
                            }
                        }

                    }
                }
            }
        }
        if(CollectionUtils.isNotEmpty(dingUserList)) {
            DepartmentEntity departmentEntity = departmentMapper.getByCompanyIdAndDingDeptId(companyId, 1);
            String companyName = companyInfoInDB.getName();
            Integer departmentId = null;
            if (departmentEntity == null) {
                departmentEntity = new DepartmentEntity(companyId, 1, companyName, StatusEnum.OK.getValue(), null, new Date(), new Date());
                departmentMapper.insertSingle(departmentEntity);
                departmentId = departmentEntity.getId();
            } else {
                departmentEntity.setUpdateTime(new Date());
                departmentEntity.setStatus(1);
                departmentMapper.updateDepartment(departmentEntity);
                departmentId = departmentEntity.getId();
            }
            for (int i = 0; i < dingUserList.size(); i++) {
                String dingUserId = dingUserList.get(i);
                CorpUserDetail corpUserDetail = DingHelper.getMemberDetail(corpId, dingUserId);
                Integer isAdmin = corpUserDetail.getIsAdmin()!=null&&corpUserDetail.getIsAdmin()?1:0;
                Integer isBoss = corpUserDetail.getIsBoss()!=null&&corpUserDetail.getIsBoss()?1:0;
                if(corpUserDetail.getDingId()!=null) {
                    UserEntity userEntity = userMapper.getUserByDingId(corpUserDetail.getDingId());
                    Integer userId = null;
                    if (userEntity == null) {
                        //(String name, String dingId, String unionId, String avatar, Integer status, Integer source, Date createTime, Date updateTime)
                        userEntity = new UserEntity(corpUserDetail.getName(),corpUserDetail.getDingId(),null,corpUserDetail.getAvatar(),StatusEnum.OK.getValue(), StatusEnum.OK.getValue(),new Date(),new Date());
                        userMapper.createOrUpdateUser(userEntity);
                        userId = userEntity.getId();
                    }else{
                        userId = userEntity.getId();
                    }
                    UserXCompany userXCompany = userXCompanyMapper.getByCompanyIdANdDingUserId(corpId, dingUserId);
                    if (userXCompany == null && (CollectionUtils.isEmpty(dingUserIdSet) || !dingUserIdSet.contains(dingUserId))) {
                        //public UserXCompany(String corpId, String dingUserId, Integer userId, Integer isAdmin, Integer isBoss, Integer status, Date createTime, Date updateTime, Integer source)
                        userXCompany = new UserXCompany(corpId, dingUserId, userEntity.getId(), isAdmin, isBoss, StatusEnum.OK.getValue(), new Date(), new Date(), null);
//                        userXCompany.setIsSuperManage(userXCompany.getIsSuperManage());
                        insertList.add(userXCompany);
                        dingUserIdSet.add(dingUserId);
                    } else if (userXCompany != null && (CollectionUtils.isEmpty(dingUserIdSet) || !dingUserIdSet.contains(dingUserId))) {
                        userXCompany.setIsAdmin(isAdmin);
                        userXCompany.setIsBoss(isBoss);
                        userXCompany.setStatus(StatusEnum.OK.getValue());
//                        userXCompany.setIsSuperManage(userXCompany.getIsSuperManage());
                        updateList.add(userXCompany);
                        dingUserIdSet.add(dingUserId);
                    }
                    UserXDeptEntity userXDept = userXDeptMapper.getByUserIdAndCorpIdAndDeptId(userEntity.getId(), corpId, departmentId);
                    if (userXDept == null) {
                        //String corpId, Integer deptId, String deptName, String dingUserId, Integer userId, Integer status, Date createTime, Date updateTime
                        userXDept = new UserXDeptEntity(corpId, departmentId, departmentEntity.getName(), dingUserId, userId, StatusEnum.OK.getValue(), new Date(), new Date());
                        insertDeptList.add(userXDept);
                    } else {
                        userXDept.setDeptName(departmentEntity.getName());
                        userXDept.setUpdateTime(new Date());
                        userXDept.setStatus(StatusEnum.OK.getValue());
                        updateDeptList.add(userXDept);
                    }
                }
            }
        }

        if (CollectionUtils.isNotEmpty(insertList)) {
            userXCompanyMapper.batchInsert(insertList);
        }
        if (CollectionUtils.isNotEmpty(updateList)) {
            userXCompanyMapper.batchUpdateUserXCompany2(updateList);
        }
        if (CollectionUtils.isNotEmpty(insertDeptList)) {
            userXDeptMapper.batchInsert(insertDeptList);
        }
        if (CollectionUtils.isNotEmpty(updateDeptList)) {
            userXDeptMapper.batchUpdate(updateDeptList);
        }
        System.out.println("公司:" + companyInfoInDB.getName() + ",插入了" + insertList.size() + "用户公司,更新了" + updateList.size() + "条用户公司,"
                + "插入了" + insertDeptList.size() + "用户部门,更新了" + updateDeptList.size() + "用户部门");


        RightGroupEntity rightGroupEntity = rightGroupMapper.getSuperManageRightGroup(companyId);
        Integer rightGroupId = rightGroupEntity.getId();
        List<UserRightGroupEntity> addUserRightList = new ArrayList<>();
        List<UserXCompany> updateUserXCompanyList = new ArrayList<>();
        if (rightGroupEntity != null) {
            List<DingCorpUserDetail> userDetailList = DingHelper.getCorpManager2(isvTicketEntity.getCorpAccessToken());
            if (CollectionUtils.isNotEmpty(userDetailList)) {
                for (int i = 0; i < userDetailList.size(); i++) {
                    DingCorpUserDetail dingCorpUserDetail = userDetailList.get(i);
                    String dingUserId = dingCorpUserDetail.getUserid();
                    UserXCompany userXCompany = userXCompanyMapper.getByCompanyIdANdDingUserId(corpId, dingUserId);
                    Integer userId = userXCompany.getUserId();
                    if (userXCompany != null && userXCompany.getStatus().equals(1)) {
                        //public UserRightGroupEntity(Integer companyId,Integer userId,Integer rightGroupId, Integer status,Date updateTime)
                        UserRightGroupEntity userRightGroupEntity = userRightGroupMapper.findUserRightGroupByGroupId(new UserRightGroupEntity(companyId, userId, rightGroupId, null, new Date()));
                        if (userRightGroupEntity == null) {
                            //Integer companyId, Integer userId, Integer rightGroupId, Integer status, Date createTime, Date updateTime
                            userRightGroupEntity = new UserRightGroupEntity(companyId, userId, rightGroupId, StatusEnum.OK.getValue(), new Date(), new Date());
                            addUserRightList.add(userRightGroupEntity);
                        }
                        if (userXCompany.getIsAdmin() == null || (userXCompany.getIsAdmin() != null && userXCompany.getIsAdmin().equals(0))) {
                            userXCompany.setIsAdmin(1);
                            userXCompany.setUpdateTime(new Date());
                            updateUserXCompanyList.add(userXCompany);
                        }
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(addUserRightList)) {
                userRightGroupMapper.batchInsert(addUserRightList);
                System.out.println("插入了" + addUserRightList.size() + "条超管用户记录");
            }
            if (CollectionUtils.isNotEmpty(updateUserXCompanyList)) {
                userXCompanyMapper.batchUpdateUserXCompany2(updateUserXCompanyList);
            }
        }


    }

    @Override
    public JSONObject syncRightSingle(Integer companyId) {
        syncRight(companyId);
        return ResultJson.succResultJson(companyId);
    }

    /**
     * 重新同步部门
     */
    private void syncDepartment(Integer companyId, List<Department> departmentList) {
        List<DepartmentEntity> insertList = new ArrayList<>();
        List<DepartmentEntity> updateList = new ArrayList<>();
        for (int i = 0; i < departmentList.size(); i++) {
            Department departmentDing = departmentList.get(i);
            Integer dingDeptId = departmentDing.getId().intValue();
            Integer parentDeptId = null;
            if (departmentDing.getParentid() != null) {
                parentDeptId = departmentDing.getParentid().intValue();
            }
            String dingDeptName = departmentDing.getName();
            DepartmentEntity departmentInDB = departmentMapper.getByCompanyIdAndDingDeptId(companyId, dingDeptId);
            if (departmentInDB != null) {
                departmentInDB.setName(dingDeptName);
                departmentInDB.setParentId(parentDeptId);
                departmentInDB.setStatus(StatusEnum.OK.getValue());
                departmentInDB.setUpdateTime(new Date());
                updateList.add(departmentInDB);
            } else {
                //DepartmentEntity(Integer companyId, Integer dingDeptId, String name, Integer status, Integer parentId, Date createTime, Date updateTime)
                DepartmentEntity addDepartment = new DepartmentEntity(companyId, dingDeptId, dingDeptName, StatusEnum.OK.getValue(), parentDeptId, new Date(), new Date());
                insertList.add(addDepartment);
            }
        }
        if (CollectionUtils.isNotEmpty(updateList)) {
            departmentMapper.batchUpdateDepartment2(updateList);
        }
        if (CollectionUtils.isNotEmpty(insertList)) {
            departmentMapper.insertBatch(insertList);
        }
    }

    /**
     * 重新同步成员
     */
    private void syncUser(Integer companyId, List<Department> departmentList) {
        IsvTicketsEntity ticketVO = isvTicketsMapper.getIsvTicketByCompanyId(companyId);
        String corpId = ticketVO.getCorpId();
        List<UserXCompany> addUserXCompanyList = new ArrayList<>();
        List<UserXCompany> updateUserXCompanyList = new ArrayList<>();
        List<UserXDeptEntity> addUserXDeptList = new ArrayList<>();
        List<UserXDeptEntity> updateUserXDeptList = new ArrayList<>();
        Set<String> userXCompanyDingUserIdSet = new HashSet<>();
        Set<String> userXDepartmentDingUserIdSet = new HashSet<>();
        //遍历部门列表
        for (int i = 0; i < departmentList.size(); i++) {
            Long dingDeptId = departmentList.get(i).getId();
            DepartmentEntity department = departmentMapper.getByCompanyIdAndDingDeptId(companyId, dingDeptId.intValue());
            Integer departmentId = department.getId();
            if (department == null) {
                throw new RuntimeException("公司ID" + companyId + ",钉钉部门ID " + dingDeptId + " 同步异常");
            }
            List<DingUserDetail> corpUserList = DingHelper.getMembersByCorpIdAndDeptId2(corpId, dingDeptId);
            //遍历部门下用户列表
            if (CollectionUtils.isNotEmpty(corpUserList)) {
                for (int j = 0; j < corpUserList.size(); j++) {
                    DingUserDetail dingUserDetail = corpUserList.get(j);
                    String dingId = dingUserDetail.getDingId();
                    String unionId = dingUserDetail.getUnionid();
                    String dingUserId = dingUserDetail.getUserid();
                    UserEntity userEntity = null;
                    if (StringUtils.isNotEmpty(dingId)) {
                        userEntity = userMapper.getUserByDingId(dingId);
                    } else {
                        userEntity = userMapper.getUserByUnionId(unionId);
                    }
                    Integer userId = null;
                    Integer isBoss = (dingUserDetail.getIsBoss() != null && dingUserDetail.getIsBoss() == true) ? 1 : 0;
                    if (userEntity == null) {
                        //UserEntity(String name, String dingId, String avatar, Integer status, Integer source, Date createTime, Date updateTime)
                        String name = SerializeUtil.unicodeToCn(dingUserDetail.getName());
                        userEntity = new UserEntity(name, dingId, unionId, dingUserDetail.getAvatar(), StatusEnum.OK.getValue(), 0, new Date(), new Date());
                        userMapper.createOrUpdateUser(userEntity);
                        userId = userEntity.getId();
                    } else {
                        userId = userEntity.getId();
                    }
                    UserXCompany userXCompany = userXCompanyMapper.getByCompanyIdANdDingUserId(corpId, dingUserId);
                    if (userXCompany != null) {
                        if (!userXCompanyDingUserIdSet.contains(dingUserId)) {
                            userXCompanyDingUserIdSet.add(dingUserId);
                            userXCompany.setStatus(StatusEnum.OK.getValue());
                            userXCompany.setIsBoss(isBoss);
                            userXCompany.setUserId(userId);
//                            userXCompany.setIsAdmin(isAdmin);
                            updateUserXCompanyList.add(userXCompany);
                        }
                    } else {
                        if (!userXCompanyDingUserIdSet.contains(dingUserId)) {
                            userXCompanyDingUserIdSet.add(dingUserId);
                            userXCompany = new UserXCompany(corpId, dingUserId, userId, 0, isBoss, StatusEnum.OK.getValue(), new Date(), new Date(), 0);
                            addUserXCompanyList.add(userXCompany);
                        }
                    }
                    UserXDeptEntity userXDeptEntity = userXDeptMapper.getByUserIdAndCorpIdAndDeptId(userId, corpId, departmentId);
                    String userDeptSetId = departmentId + "_" + userId;
                    if (userXDeptEntity != null) {
                        if (!userXDepartmentDingUserIdSet.contains(userDeptSetId)) {
                            userXDepartmentDingUserIdSet.add(userDeptSetId);
                            userXDeptEntity.setStatus(StatusEnum.OK.getValue());
                            userXDeptEntity.setUpdateTime(new Date());
                            userXDeptEntity.setDeptName(department.getName());
                            userXDeptEntity.setUserId(userId);
                            updateUserXDeptList.add(userXDeptEntity);
                        }
                    } else {
                        if (!userXDepartmentDingUserIdSet.contains(dingUserId)) {
                            userXDepartmentDingUserIdSet.add(userDeptSetId);
                            userXDeptEntity = new UserXDeptEntity(corpId, departmentId, department.getName(), dingUserId, userId, StatusEnum.OK.getValue(), new Date(), new Date());
                            addUserXDeptList.add(userXDeptEntity);
                        }
                    }
                }
            }
        }

        if (CollectionUtils.isNotEmpty(addUserXCompanyList)) {
            userXCompanyMapper.batchInsert(addUserXCompanyList);
            System.out.println("插入了" + addUserXCompanyList.size() + "条公司用户记录");
        }
        if (CollectionUtils.isNotEmpty(updateUserXCompanyList)) {
            userXCompanyMapper.batchUpdateUserXCompany2(updateUserXCompanyList);
            System.out.println("更新了" + updateUserXCompanyList.size() + "条公司用户记录");
        }
        if (CollectionUtils.isNotEmpty(addUserXDeptList)) {
            userXDeptMapper.batchInsert(addUserXDeptList);
            System.out.println("插入了" + addUserXDeptList.size() + "条部门用户记录");
        }
        if (CollectionUtils.isNotEmpty(updateUserXDeptList)) {
            userXDeptMapper.batchUpdate(updateUserXDeptList);
            System.out.println("更新了" + updateUserXDeptList.size() + "条部门用户记录");
        }

    }


    private void syncRight(Integer companyId) {
        IsvTicketsEntity ticketVO = isvTicketsMapper.getIsvTicketByCompanyId(companyId);
        String accessToken = ticketVO.getCorpAccessToken();
        String corpId = ticketVO.getCorpId();
        RightGroupEntity rightGroupEntity = rightGroupMapper.getSuperManageRightGroup(companyId);
        Integer rightGroupId = rightGroupEntity.getId();
        List<UserRightGroupEntity> addUserRightList = new ArrayList<>();
        List<UserXCompany> updateUserXCompanyList = new ArrayList<>();
        if (rightGroupEntity != null) {
            List<DingCorpUserDetail> userDetailList = DingHelper.getCorpManager2(accessToken);
            if (CollectionUtils.isNotEmpty(userDetailList)) {
                for (int i = 0; i < userDetailList.size(); i++) {
                    DingCorpUserDetail dingCorpUserDetail = userDetailList.get(i);
                    String dingUserId = dingCorpUserDetail.getUserid();
                    UserXCompany userXCompany = userXCompanyMapper.getByCompanyIdANdDingUserId(corpId, dingUserId);
                    Integer userId = userXCompany.getUserId();
                    if (userXCompany != null) {
                        //public UserRightGroupEntity(Integer companyId,Integer userId,Integer rightGroupId, Integer status,Date updateTime)
                        UserRightGroupEntity userRightGroupEntity = userRightGroupMapper.findUserRightGroupByGroupId(new UserRightGroupEntity(companyId, userId, rightGroupId, null, new Date()));
                        if (userRightGroupEntity == null) {
                            //Integer companyId, Integer userId, Integer rightGroupId, Integer status, Date createTime, Date updateTime
                            userRightGroupEntity = new UserRightGroupEntity(companyId, userId, rightGroupId, StatusEnum.OK.getValue(), new Date(), new Date());
                            addUserRightList.add(userRightGroupEntity);
                        }
                        if (userXCompany.getIsAdmin() == null || (userXCompany.getIsAdmin() != null && userXCompany.getIsAdmin().equals(0))) {
                            userXCompany.setIsAdmin(1);
                            userXCompany.setUpdateTime(new Date());
                            updateUserXCompanyList.add(userXCompany);
                        }
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(addUserRightList)) {
                userRightGroupMapper.batchInsert(addUserRightList);
                System.out.println("插入了" + addUserRightList.size() + "条超管用户记录");
            }
            if (CollectionUtils.isNotEmpty(updateUserXCompanyList)) {
                userXCompanyMapper.batchUpdateUserXCompany2(updateUserXCompanyList);
            }
        }


    }


    /**
     * 获取所有当前库里不存在的部门
     */
    private List<DepartmentEntity> getNewDepts(JSONArray dingDepts, List<DepartmentEntity> deptsInDB, String corpId) {
        IsvTicketsEntity isvTicketsEntity = isvTicketsService.getIsvTicketByCorpId(corpId);
        Integer companyId = isvTicketsEntity.getCompanyId();
        List<DepartmentEntity> datas = new ArrayList<>();
        JSONObject jsonObject = null;
        Date curr = new Date();

        List<Integer> dingDeptIds = new ArrayList<>();        // 用来删除回调失败的部门
        List<Integer> myDeptDingIds = new ArrayList<>();

        //钉钉获取的部门列表
        if (dingDepts != null && dingDepts.size() > 0) {
            if (deptsInDB != null && deptsInDB.size() > 0) {
                for (int i = 0; i < dingDepts.size(); i++) {
                    jsonObject = (JSONObject) dingDepts.get(i);
                    Integer departmentId = jsonObject.getInteger("id");// 钉钉部门id
                    dingDeptIds.add(departmentId);
                    boolean isDeptNotExist = true;
                    Integer myDeptId = null;
                    for (DepartmentEntity dept : deptsInDB) {
                        if (departmentId.equals(dept.getDingDeptId())) {
                            myDeptId = dept.getId();
                            isDeptNotExist = false;
                            break;
                        }
                    }
                    if (isDeptNotExist) {
                        addNewDepts(datas, jsonObject, companyId);
                    } else {
                        if (myDeptId != null) {
                            DepartmentEntity d = new DepartmentEntity();
                            d.setId(myDeptId);
                            d.setUpdateTime(curr);
                            d.setName(jsonObject.getString("name"));
                            if (StringUtils.isNotEmpty(jsonObject.getString("parentid")))
                                d.setParentId(Integer.valueOf(jsonObject.getString("parentid")));
                            d.setDingDeptId(departmentId);
                            d.setCompanyId(companyId);
                            departmentMapper.updateDepartment(d);
                        }
                    }

                }

                // 遍历数据库中已有的部门,对比ding部门。如果数据库部门多了,则要逻辑删除
                for (DepartmentEntity dept : deptsInDB) {
                    myDeptDingIds.add(dept.getDingDeptId());
                }
                myDeptDingIds.removeAll(dingDeptIds);
                if (myDeptDingIds != null && myDeptDingIds.size() > 0) {
                    //获取库中多余的部门列表
                    List<DepartmentEntity> deptsList = departmentMapper.selectDeptByDingDeptIds(companyId, myDeptDingIds);
                    if (deptsList != null && deptsList.size() > 0) {
                        for (DepartmentEntity d : deptsList) {
                            d.setDingDeptId(d.getId());
                            d.setUpdateTime(curr);
                            d.setStatus(0);
                            d.setCompanyId(companyId);
                        }
                        for (DepartmentEntity d : deptsList) {
                            departmentMapper.updateDepartment(d);
                            //AuthHelper.sendMassge2Dev("corpid:" + corpId + "\n" + "存在回调部门删除失败,同步修正 " + myDeptDingIds.toString());
                        }
                    }
                }
            } else {
                for (int i = 0; i < dingDepts.size(); i++) {
                    jsonObject = (JSONObject) dingDepts.get(i);
                    addNewDepts(datas, jsonObject, companyId);
                }
            }
        }
        return datas;
    }

    /**
     * 新部门
     *
     * @param result
     * @param jsonObject
     * @param companyId
     */
    private void addNewDepts(List<DepartmentEntity> result, JSONObject jsonObject, Integer companyId) {
        DepartmentEntity department = new DepartmentEntity();
        department.setCompanyId(companyId);
        department.setDingDeptId(jsonObject.getInteger("id"));
        department.setName(jsonObject.getString("name"));
        department.setStatus(StatusEnum.OK.getValue());
        department.setCreateTime(new Date());
        department.setUpdateTime(new Date());
        if (StringUtils.isNotBlank(jsonObject.getString("parentid")))
            department.setParentId(Integer.valueOf(jsonObject.getString("parentid")));
        result.add(department);
    }

    @Override
    public List<DepartmentEntity> getDeptsByCompanyId(Integer conpanyId, Integer status) {
        return departmentMapper.getDeptsByCompanyId(conpanyId, status);
    }

}
