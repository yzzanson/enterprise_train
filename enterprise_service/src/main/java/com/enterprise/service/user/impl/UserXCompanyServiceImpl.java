package com.enterprise.service.user.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.open.client.api.model.corp.Department;
import com.enterprise.base.common.DDConstant;
import com.enterprise.base.common.Page;
import com.enterprise.base.common.PageEntity;
import com.enterprise.base.common.ResultJson;
import com.enterprise.base.entity.IsvTicketsEntity;
import com.enterprise.base.entity.UserEntity;
import com.enterprise.base.entity.UserXCompany;
import com.enterprise.base.entity.UserXDeptEntity;
import com.enterprise.base.enums.StatusEnum;
import com.enterprise.base.vo.UserVO;
import com.enterprise.base.vo.UserXCompanyVO;
import com.enterprise.mapper.department.DepartmentMapper;
import com.enterprise.mapper.users.UserMapper;
import com.enterprise.mapper.users.UserXCompanyMapper;
import com.enterprise.service.isvTickets.IsvTicketsService;
import com.enterprise.service.user.UserService;
import com.enterprise.service.user.UserXCompanyService;
import com.enterprise.service.user.UserXDeptService;
import com.enterprise.util.AssertUtil;
import com.enterprise.util.HttpUtil;
import com.enterprise.util.JSONUtil;
import com.enterprise.util.SerializeUtil;
import com.enterprise.util.dingtalk.DDConfig;
import com.enterprise.util.dingtalk.DingHelper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by anson on 18/3/26.
 */
@Service
public class UserXCompanyServiceImpl implements UserXCompanyService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private UserXCompanyMapper userXCompanyMapper;

    @Resource
    private UserXDeptService userXDeptService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserService userService;

    @Resource
    private IsvTicketsService isvTicketsService;

    @Resource
    private DepartmentMapper departmentMapper;

    @Override
    public void createUserXCompany(UserXCompany userXCompany) {
        AssertUtil.notNull(userXCompany, "用户和企业的关联关系不能为空!");
        AssertUtil.isTrue(!StringUtils.isEmpty(userXCompany.getCorpId()), "CorpId不能为空!");
//        AssertUtil.isTrue(!StringUtils.isEmpty(userXCompany.getDingId()), "DingId不能为空!");
        AssertUtil.isTrue(!StringUtils.isEmpty(userXCompany.getDingUserId()), "DingUserId不能为空!");
        AssertUtil.isTrue(!StringUtils.isEmpty(userXCompany.getUserId()), "UserId不能为空!");

        userXCompanyMapper.createUserXCompany(userXCompany);
    }

    private void createUserXDept(String corpId, Integer userId, String dingUserId, String deptIds) {
        AssertUtil.notNull(deptIds, "用户部门不能为空!");
        String[] deptIdArray = deptIds.split(",");
        UserXDeptEntity userXDeptEntity = new UserXDeptEntity();
        userXDeptEntity.setDingUserId(dingUserId);
        userXDeptEntity.setUserId(userId);
        userXDeptEntity.setCorpId(corpId);
        userXDeptEntity.setStatus(StatusEnum.OK.getValue());
        userXDeptEntity.setCreateTime(new Date());
        userXDeptEntity.setUpdateTime(new Date());
        //调用获取部门详情接口
        boolean isAccessTokenExist = false;
        IsvTicketsEntity isvTicketsInDB = isvTicketsService.getIsvTicketByCorpId(corpId);
        if (isvTicketsInDB != null && !StringUtils.isEmpty(isvTicketsInDB.getCorpAccessToken())) {
            isAccessTokenExist = true;
        }

        for (int i = 0; i < deptIdArray.length; i++) {
            String deptId = deptIdArray[i];
            if (isAccessTokenExist) {
                JSONObject deptJson = DingHelper.getDepartmentDetail(isvTicketsInDB.getCorpId(), deptId);
                if (deptJson != null || (deptJson != null && deptJson.getInteger("errcode") == 0)) {
                    Department department = JSONObject.parseObject(deptJson.toJSONString(), Department.class);
                    if (department != null && !StringUtils.isEmpty(department.getName())) {
                        userXDeptEntity.setDeptName(department.getName());
                    }
                }
            }
            Integer deptIdInDB = departmentMapper.getIdByCompanyIdAndDingDeptId(isvTicketsInDB.getCompanyId(), Integer.valueOf(deptId));
            if (deptIdInDB!=null) {
                userXDeptEntity.setDeptId(deptIdInDB);
                UserXDeptEntity userXDeptEntityDB = userXDeptService.getByUserIdAndCorpIdAndDeptId(new UserXDeptEntity(corpId, userId, deptIdInDB, StatusEnum.OK.getValue()));
                if (userXDeptEntityDB != null) {
                    userXDeptEntity.setDeptName(userXDeptEntity.getDeptName());
                    userXDeptEntity.setDingUserId(dingUserId);
                    userXDeptEntity.setId(userXDeptEntityDB.getId());
                }
                userXDeptService.createUserXDept(userXDeptEntity);
            }
        }
    }

    @Override
    public JSONObject findUserCompany(String corpId, String agentId, String companyName, Date startTime, Date endTime, PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPageNo(), pageEntity.getPageSize());
        String compLogInfo = "corpId:%s,agentId:%s,companyName:%s,startTime:%s,endTime:%s";
        logger.info(String.format(compLogInfo, corpId, agentId, companyName, startTime, endTime));
        PageInfo<UserXCompanyVO> pageInfo = new PageInfo<>(userXCompanyMapper.findUserCompany(corpId, agentId, companyName, startTime, endTime));
        Map<String, Object> dataMap = Maps.newHashMap();
        dataMap.put("list", pageInfo.getList());
        dataMap.put("total", pageInfo.getTotal());
        dataMap.put("page", new Page(pageInfo.getPrePage(), pageEntity.getPageNo(), pageInfo.getNextPage(), pageInfo.getLastPage()));
        return ResultJson.succResultJson(dataMap);
    }

    @Override
    public UserXCompany getUserXCompany(UserXCompany userXCompany) {
        AssertUtil.notNull(userXCompany, "用户和企业的关联关系不能为空!");
//        AssertUtil.notNull(userXCompany.getStatus(), "状态不能为空!");
//        AssertUtil.isTrue(userXCompany.getId() == null || (userXCompany.getCorpId() == null || userXCompany.getDingId() == null) || (userXCompany.getCorpId() == null && userXCompany.getUserId() == null), "id,corpId,dingId,userId 不能同时为空!");

        return userXCompanyMapper.getUserXCompany(userXCompany);
    }

    @Override
    public UserXCompany createOrUpdateUserXCompany(UserXCompany userXCompany) {
        AssertUtil.notNull(userXCompany, "用户和企业关联信息不能为空!");
        AssertUtil.notNull(userXCompany.getCorpId(), "corpId不能为空!");
        AssertUtil.notNull(userXCompany.getDingUserId(), "dingUserId不能为空!");

        UserXCompany userXCompanyInDB = getUserXCompany(userXCompany);
        IsvTicketsEntity isvTicketsInDB = isvTicketsService.getIsvTicketByCorpId(userXCompany.getCorpId());
        AssertUtil.notNull(isvTicketsInDB, "请先安装微应用!");
        //获取用户信息
        JSONObject object = new JSONObject();
        object.put(DDConstant.ACCESS_TOKEN, isvTicketsInDB.getCorpAccessToken());
        object.put(DDConstant.USER_ID, userXCompany.getDingUserId());

        //获取授权用户详情Url
        String authUrl = JSONUtil.appendJsonParamsToUrl(DDConfig.GET_AUTHENTICATED_USER_DETAILS_URL, object);
        JSONObject authUrlResult = HttpUtil.get(authUrl);
        logger.info("userid:" + userXCompany.getDingUserId() + "获取企业授权用户信息:" + authUrlResult.toString());

        //获取用户信息url
        String getUserInfoUrl = JSONUtil.appendJsonParamsToUrl(DDConfig.GET_USER_DETAILS_URL, object);
        JSONObject userInfoResult = HttpUtil.get(getUserInfoUrl);
        if (userXCompanyInDB == null) {
            //用户在钉钉范围内唯一id
            String dingId = userInfoResult.getString("dingId");
            UserEntity userEntity = new UserEntity();
            userEntity.setDingId(dingId);
            userEntity.setAvatar(userInfoResult.getString("avatar"));
            String name = SerializeUtil.unicodeToCn(userInfoResult.getString("name"));
            userEntity.setName(name);
            userEntity.setStatus(StatusEnum.OK.getValue());
            userEntity.setSource(userXCompany.getSource());
            //新增或修改用户信息
            UserEntity userInDB = userService.getUserEntity(new UserEntity(dingId));
            if (userInDB == null) {
                userService.createOrUpdateUser(userEntity);
            } else {
                userEntity.setUpdateTime(new Date());
                userEntity.setId(userInDB.getId());
                userMapper.updateUser(userEntity);
            }

            userXCompany.setDingUserId(userInfoResult.getString("userid"));
            userXCompany.setUserId(userEntity.getId());
            userXCompany.setStatus(StatusEnum.OK.getValue());

            //新增用户关联部门信息记录
            String deptArrString = userInfoResult.getString("department");
            if (deptArrString.startsWith("[")) {
                deptArrString = new String(deptArrString.substring(1));
            }
            if (deptArrString.endsWith("]")) {
                deptArrString = new String(deptArrString.substring(0, deptArrString.length() - 1));
            }
            createUserXDept(userXCompany.getCorpId(), userEntity.getId(), userXCompany.getDingUserId(), deptArrString);
            //新增用户关联企业信息记录
            createUserXCompany(userXCompany);
            userXCompanyInDB = userXCompany;
        } else {
            Boolean needUpdate = false;
            String dingId = userInfoResult.getString("dingId");
            UserEntity userInDB = userService.getUserEntity(new UserEntity(dingId));
            String avatar = userInfoResult.getString("avatar");
            String name = SerializeUtil.unicodeToCn(userInfoResult.getString("name"));
//            userEntity.setName(name);
//            String name = userInfoResult.getString("name");
            if (!StringUtils.isEmpty(avatar) && !StringUtils.isEmpty(userInDB.getAvatar()) || (StringUtils.isEmpty(userInDB.getAvatar()) && !StringUtils.isEmpty(avatar))) {
                if (!StringUtils.isEmpty(userInDB.getAvatar())) {
                    if (!avatar.equals(userInDB.getAvatar())) {
                        needUpdate = true;
                        userInDB.setAvatar(avatar);
                    }
                } else {
                    needUpdate = true;
                    userInDB.setAvatar(avatar);
                }
            }
            if (!StringUtils.isEmpty(name) && !StringUtils.isEmpty(userInDB.getName()) || (StringUtils.isEmpty(userInDB.getName()) && !StringUtils.isEmpty(name))) {
                if (!StringUtils.isEmpty(userInDB.getAvatar())) {
                    if (!name.equals(userInDB.getName())) {
                        needUpdate = true;
                        userInDB.setName(name);
                    }
                } else {
                    needUpdate = true;
                    userInDB.setAvatar(avatar);
                }
            }
            if (needUpdate) {
                userInDB.setUpdateTime(new Date());
                userService.updateUser(userInDB);
            }
        }
        return userXCompanyInDB;
    }

    @Override
    public List<UserVO> getUserByCorpId(String corpId) {
        return userXCompanyMapper.getUserByCorpId(corpId, null);
    }


    @Override
    public List<UserVO> getUserByCorpId2(String corpId,Integer status) {
        return userXCompanyMapper.getUserByCorpId(corpId, status);
    }

    @Override
    public Integer getAdmin(String corpId) {
        UserXCompany userXCompany = userXCompanyMapper.getAdmin(corpId);
        if (userXCompany == null) {
            UserXCompany userXCompany1 = userXCompanyMapper.getOne(corpId);
            return userXCompany1.getUserId();
        }
        return userXCompany.getUserId();
    }

    @Override
    public void updateUserXCompany(UserXCompany userXCompany) {
        userXCompanyMapper.updateUserXCompany(userXCompany);
    }

    public static void main(String[] args) {
        String jsonStr = "{\"active\":true,\"avatar\":\"http://static.dingtalk.com/media/lADOyYXMMM0E2s0E1w_1239_1242.jpg\",\"department\":[59727174,43995749,36717562,61249357,32206341,61631059],\"dingId\":\"$:LWCP_v1:$VBnCXRdtIs/I0rSwC4JAdw==\",\"errcode\":0,\"errmsg\":\"ok\",\"isAdmin\":true,\"isBoss\":false,\"isHide\":false,\"isLeaderInDepts\":\"{59727174:false,43995749:false,36717562:false,61249357:false,32206341:false,61631059:false}\",\"isSenior\":false,\"jobnumber\":\"\",\"name\":\"杨泽洲\",\"orderInDepts\":\"{59727174:176399811495667509,43995749:8101582783557448,36717562:176399811472621512,61249357:176397206759646512,32206341:176399811472621512,61631059:176396713713646512}\",\"position\":\"会员\",\"unionid\":\"1TcbWAZRDoCAluVtniiRJqgiEiE\",\"userid\":\"yangzezhou\"}";
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        System.out.println(jsonObject.getString("department"));

    }
}
