package com.enterprise.service.user.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.open.client.api.model.corp.CorpUserDetail;
import com.enterprise.base.bean.DingUserDetail;
import com.enterprise.base.common.*;
import com.enterprise.base.entity.*;
import com.enterprise.base.entity.bury.AppInviteBuryEntity;
import com.enterprise.base.entity.bury.OASendBuryEntity;
import com.enterprise.base.enums.StatusEnum;
import com.enterprise.base.enums.SubjectEnum;
import com.enterprise.base.enums.UserSourceEnum;
import com.enterprise.base.enums.bury.AppInviteEnum;
import com.enterprise.base.enums.bury.OABuryEnum;
import com.enterprise.base.vo.*;
import com.enterprise.base.vo.studyRemind.StudyRemindVO;
import com.enterprise.mapper.bury.AppInviteBuryMapper;
import com.enterprise.mapper.bury.OASendBuryMapper;
import com.enterprise.mapper.department.DepartmentMapper;
import com.enterprise.mapper.pet.MyPetMapper;
import com.enterprise.mapper.petFood.PetFoodDetailMapper;
import com.enterprise.mapper.petFood.PetFoodMapper;
import com.enterprise.mapper.questions.QuestionsLibraryMapper;
import com.enterprise.mapper.questions.UserXQuestionsMapper;
import com.enterprise.mapper.remind.StudyRemindMapper;
import com.enterprise.mapper.right.RightGroupMapper;
import com.enterprise.mapper.right.UserRightGroupMapper;
import com.enterprise.mapper.users.UserMapper;
import com.enterprise.mapper.users.UserXCompanyMapper;
import com.enterprise.mapper.users.UserXLibraryMapper;
import com.enterprise.oa.OAMessageUtil;
import com.enterprise.oa.WelcomeSingleThread;
import com.enterprise.service.isvTickets.IsvTicketsService;
import com.enterprise.service.remind.StudyRemindService;
import com.enterprise.service.user.UserService;
import com.enterprise.service.user.UserXDeptService;
import com.enterprise.util.AssertUtil;
import com.enterprise.util.DateUtil;
import com.enterprise.util.SerializeUtil;
import com.enterprise.util.dingtalk.DingHelper;
import com.enterprise.util.oa.message.OAMessage;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

/**
 * @Description UserServiceImpl
 * @Author shisan
 * @Date 2018/3/23 上午11:14
 */
@Service
public class UserServiceImpl implements UserService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private RightGroupMapper rightGroupMapper;

    @Resource
    private UserMapper usersMapper;

    @Resource
    private MyPetMapper myPetMapper;

    @Resource
    private UserXQuestionsMapper userXQuestionsMapper;

    @Resource
    private UserXCompanyMapper userXCompanyMapper;

    @Resource
    private UserXDeptService userXDeptService;

    @Resource
    private DepartmentMapper departmentMapper;

    @Resource
    private IsvTicketsService isvTicketsService;

    @Resource
    private UserRightGroupMapper userRightGroupMapper;

    @Resource
    private QuestionsLibraryMapper questionsLibraryMapper;

    @Resource
    private UserXLibraryMapper userXLibraryMapper;

    @Resource
    private StudyRemindMapper studyRemindMapper;

    @Resource
    private StudyRemindService studyRemindService;

    @Resource
    private AppInviteBuryMapper appInviteBuryMapper;

    @Resource
    private OASendBuryMapper oaSendBuryMapper;

    @Resource
    private PetFoodMapper petFoodMapper;

    @Resource
    private PetFoodDetailMapper petFoodDetailMapper;

    //初始化的饲料数
    private Integer PET_INIT_FOOD_COUNT = PetConstant.INIT_COUNT;

    @Override
    public UserEntity getUserById(Integer id) {
        return usersMapper.getUserById(id);
    }

    private static final String MOBILE_INDEX_PAGE = GlobalConstant.getIndexUrl();

    private String QRCODE_PAGE = GlobalConstant.getQrCOdePage();

    private String WELCOME_WORD = "今天我来到这个世界,陪你成长";

    private String INVITE_MESSAGE = PetFoodConstant.INVITE_MESSAGE;

    private static final String MAIN_PAGE = GlobalConstant.getIndexUrl();


    /**
     * 将钉钉中的组织架构人员同步到本地
     */
    @Override
    public void synchronize(JSONArray dingDepts, String corpId) throws Exception {
        List<DingUserVO> insertData = new ArrayList<>(); // 需要插入的新用户
        List<DingUserVO> updateData = new ArrayList<>(); // 需要更新的用户

        Integer companyId = isvTicketsService.getIsvTicketByCorpId(corpId).getCompanyId();
        List<UserVO> userInDBList = userXCompanyMapper.getUserByCorpId(corpId, null);
        Set<String> dingIdSet = new HashSet<>();
        for (int i = 0; i < userInDBList.size(); i++) {
            String dingId = userInDBList.get(i).getDingId();
            if (StringUtils.isNotEmpty(dingId)) {
                dingIdSet.add(dingId);
            }
        }

        JSONObject object = null;
        if (dingDepts != null && dingDepts.size() > 0) {
            for (int i = 0; i < dingDepts.size(); i++) {
                object = (JSONObject) dingDepts.get(i);
                String deptId = object.getString("id");//钉钉的部门id
                DepartmentEntity department = departmentMapper.getByCompanyIdAndDingDeptId(companyId, Integer.valueOf(deptId));
                if (department == null) {
                    throw new RuntimeException("钉钉部门ID " + deptId + " 同步异常");
                }
                List<DingUserDetail> corpUserList = DingHelper.getMembersByCorpIdAndDeptId2(corpId, Long.valueOf(deptId));
                // TODO 部门入口 修改 @shisan
                getCompareUsers(insertData, updateData, corpUserList, dingIdSet, corpId, department.getDingDeptId());
            }
        }

        //开始更新
        Date curr = new Date();
        synchronized (this) {
            //新增用户
            if (insertData != null && insertData.size() > 0) {
                String deptName = "";
                for (DingUserVO dingUserVO : insertData) {
                    Integer userId = null;
                    UserXCompany userXCompany = userXCompanyMapper.getUserXCompany(new UserXCompany(dingUserVO.getCorpId(), dingUserVO.getDingUserId()));
                    if (userXCompany != null) {
                        userId = userXCompany.getUserId();
                    } else {
                        UserEntity userEntity = null;
                        if (StringUtils.isNotEmpty(dingUserVO.getDingId())) {
                            userEntity = usersMapper.getUserByDingId(dingUserVO.getDingId());
                        } else if (StringUtils.isNotEmpty(dingUserVO.getUnionId())) {
                            userEntity = usersMapper.getUserByUnionId(dingUserVO.getUnionId());
                        }
                        if (userEntity != null) {
                            userId = userEntity.getId();
                        }
                    }
                    DepartmentEntity department = departmentMapper.getByCompanyIdAndDingDeptId(companyId, dingUserVO.getDeptId());
                    UserXDeptEntity userXDeptEntity = userXDeptService.getUserXDeptByCorpIdAndDeptId(new UserXDeptEntity(department.getId(), dingUserVO.getCorpId(), dingUserVO.getDingUserId()));
                    deptName = department.getName();
                    UserEntity uu = null;
                    if (userId == null) {
                        dingUserVO.setCreateTime(curr);
                        dingUserVO.setUpdateTime(curr);
                        uu = new UserEntity();
                        BeanUtils.copyProperties(uu, dingUserVO);
                        try {
                            usersMapper.createOrUpdateUser(uu);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        userId = uu.getId();
                    }

                    if (userId != null && userXCompany == null) {
                        userXCompany = new UserXCompany();
                        userXCompany.setSource(UserSourceEnum.BACKSTAGE.getValue());
                        userXCompany.setStatus(StatusEnum.OK.getValue());
                        userXCompany.setCorpId(dingUserVO.getCorpId());
                        userXCompany.setDingUserId(dingUserVO.getDingUserId());
                        userXCompany.setIsAdmin(dingUserVO.getIsAdmin());
                        userXCompany.setUserId(userId);
                        userXCompanyMapper.createUserXCompany(userXCompany);
                    }

                    if (userId != null && userXDeptEntity == null) {
                        userXDeptEntity = new UserXDeptEntity();
                        userXDeptEntity.setDeptName(deptName);
                        userXDeptEntity.setUserId(userId);
                        userXDeptEntity.setCorpId(dingUserVO.getCorpId());
                        userXDeptEntity.setDingUserId(dingUserVO.getDingUserId());
                        userXDeptEntity.setDeptId(department.getId());
                        userXDeptEntity.setStatus(StatusEnum.OK.getValue());
                        userXDeptEntity.setCreateTime(curr);
                        userXDeptEntity.setUpdateTime(curr);
                        userXDeptService.createUserXDept(userXDeptEntity);
                    }
                }
                logger.info("部门:" + deptName + "_新增了" + insertData.size() + "个用户!");
            }
        }

        if (updateData != null && updateData.size() > 0) {
            for (DingUserVO dingUserVO : updateData) {
                try {
                    UserEntity userEntity = null;
                    if (StringUtils.isNotEmpty(dingUserVO.getDingId())) {
                        userEntity = usersMapper.getUserByDingId(dingUserVO.getDingId());
                    } else if (StringUtils.isNotEmpty(dingUserVO.getUnionId())) {
                        userEntity = usersMapper.getUserByUnionId(dingUserVO.getUnionId());
                    }
                    DepartmentEntity department = departmentMapper.getByCompanyIdAndDingDeptId(companyId, dingUserVO.getDeptId());
                    String deptName = department.getName();
                    if (userEntity != null) {
                        String dingName = SerializeUtil.unicodeToCn(dingUserVO.getName());
                        userEntity.setName(dingName);
                        userEntity.setAvatar(dingUserVO.getAvatar());
                        userEntity.setUpdateTime(curr);
                        usersMapper.updateUser(userEntity);

                        UserXCompany userXCompany = userXCompanyMapper.getUserXCompany(new UserXCompany(dingUserVO.getCorpId(), dingUserVO.getDingUserId()));
                        if (userXCompany == null) {
                            userXCompany = new UserXCompany();
                        }
//                        userXCompany.setIsBoss(dingUserVO.getIsBoss());
                        userXCompany.setIsAdmin(dingUserVO.getIsAdmin());
                        userXCompany.setUserId(userEntity.getId());
                        userXCompany.setDingUserId(dingUserVO.getDingUserId());
                        userXCompany.setCorpId(dingUserVO.getCorpId());
                        userXCompany.setStatus(StatusEnum.OK.getValue());
                        userXCompany.setUpdateTime(curr);
                        if (userXCompany.getId() != null && userXCompany.getId() > 0) {
                            userXCompanyMapper.updateUserXCompany(userXCompany);
                        } else {
                            userXCompany.setSource(UserSourceEnum.BACKSTAGE.getValue());
                            userXCompanyMapper.createUserXCompany(userXCompany);
                        }

                        UserXDeptEntity userXDeptEntity = userXDeptService.getUserXDeptByCorpIdAndDeptId(new UserXDeptEntity(department.getId(), dingUserVO.getCorpId(), dingUserVO.getDingUserId()));
                        if (userXDeptEntity == null) {
                            userXDeptEntity = new UserXDeptEntity();
                        }
                        userXDeptEntity.setDingUserId(dingUserVO.getDingUserId());
                        userXDeptEntity.setStatus(StatusEnum.OK.getValue());
                        userXDeptEntity.setCorpId(dingUserVO.getCorpId());
                        userXDeptEntity.setDeptId(department.getId());
                        userXDeptEntity.setUpdateTime(curr);
                        userXDeptEntity.setUserId(userEntity.getId());
                        userXDeptEntity.setDeptName(deptName);
                        userXDeptService.createUserXDept(userXDeptEntity);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            logger.info("修改了" + insertData.size() + "个用户!");
        }
    }

    /**
     * userlist 部门下的用户信息
     * <p/>
     * corpUserList -- 钉钉接口返回的部门下用户信息
     * dingIdSet -- user_x_company表中的ding_user_id userDingIdSet
     */
    private void getCompareUsers(List<DingUserVO> insertList, List<DingUserVO> updateList, List<DingUserDetail> corpUserList, Set<String> dingIdSet, String corpId, Integer deptId) {
        DingUserDetail dingUserDetail = null;
        Integer companyId = isvTicketsService.getIsvTicketByCorpId(corpId).getCompanyId();
        Integer deptIdInDB = departmentMapper.getIdByCompanyIdAndDingDeptId(companyId, deptId);

        if (corpUserList != null && corpUserList.size() > 0) {//钉钉返回
            for (int i = 0; i < corpUserList.size(); i++) {
                dingUserDetail = corpUserList.get(i);
                boolean noExist = true;
                Integer userId = null;
                if (dingIdSet.contains(dingUserDetail.getDingId())) {
                    UserXCompany userXCompanyInDB = userXCompanyMapper.getByCompanyIdANdDingUserId(corpId, dingUserDetail.getUserid());
                    UserXDeptEntity userXDeptEntity = userXDeptService.getUserXDeptByCorpIdAndDeptId(new UserXDeptEntity(deptIdInDB, corpId, dingUserDetail.getUserid()));
                    if (userXDeptEntity != null) {
                        userId = userXCompanyInDB.getUserId();
                        noExist = false;
                    } else {
                        noExist = true;
                    }
                } else {
                    noExist = true;
                }
                //用户部门不存在
                if (noExist) {
                    addNewUsers(insertList, dingUserDetail, corpId, deptId);
                } else {//用户部门存在
                    addUpdatUsers(updateList, dingUserDetail, userId, corpId, deptId);
                }
            }
        }
    }

    //用户或者 用户部门不存在
    private void addNewUsers(List<DingUserVO> result, DingUserDetail corpUserDetail, String corpId, Integer deptId) {
        DingUserVO users = new DingUserVO();
        users.setDingId(corpUserDetail.getDingId());
//        String name = SerializeUtil.unicodeToCn(corpUserDetail.getName());
        users.setName(corpUserDetail.getName());
        users.setAvatar(corpUserDetail.getAvatar());
        users.setSource(0);
        users.setStatus(StatusEnum.OK.getValue());
        users.setCorpId(corpId);
        users.setDeptId(deptId);
        users.setDingUserId(corpUserDetail.getUserid());
        users.setUnionId(corpUserDetail.getUnionid());
//        if (corpUserDetail.getIsAdmin() != null && corpUserDetail.getIsAdmin()) users.setIsAdmin(1);
//        else users.setIsAdmin(0);
        if (corpUserDetail.getIsBoss() != null && corpUserDetail.getIsBoss()) users.setIsBoss(1);
        else users.setIsBoss(0);
        result.add(users);
    }

    //用户或者用户部门存在
    private void addUpdatUsers(List<DingUserVO> result, DingUserDetail corpUserDetail, Integer userId, String corpId, Integer deptId) {
        DingUserVO users = new DingUserVO();
        users.setId(userId);
        users.setDingId(corpUserDetail.getDingId());
        users.setDingUserId(corpUserDetail.getUserid());
        users.setAvatar(corpUserDetail.getAvatar());
//        String name = SerializeUtil.unicodeToCn(corpUserDetail.getName());
        users.setName(corpUserDetail.getName());
        users.setUpdateTime(new Date());
        users.setCorpId(corpId);
        users.setDeptId(deptId);
        users.setUnionId(corpUserDetail.getUnionid());
//        if (corpUserDetail.getIsAdmin() != null && corpUserDetail.getIsAdmin()) users.setIsAdmin(1);
//        else users.setIsAdmin(0);
        if (corpUserDetail.getIsBoss() != null && corpUserDetail.getIsBoss()) users.setIsBoss(1);
        else users.setIsBoss(0);
        result.add(users);
    }

    @Override
    public void createOrUpdateUser(UserEntity userEntity) {
        AssertUtil.notNull(userEntity, "用户信息不能为空!");
        usersMapper.createOrUpdateUser(userEntity);
    }

    @Override
    public void updateUser(UserEntity userEntity) {
        usersMapper.updateUser(userEntity);
    }

    @Override
    public UserEntity findUser(UserEntity userEntity) {
        return usersMapper.findUser(userEntity);
    }

    @Override
    public UserEntity getUserEntity(UserEntity userEntity) {
        AssertUtil.notNull(userEntity, "用户信息不能为空!");
        AssertUtil.isTrue(userEntity.getId() != null || userEntity.getDingId() != null, "用户Id和dingId不能同时为空!");
        return usersMapper.getUserEntity(userEntity);
    }

    @Override
    public JSONObject isNewbie() {
        MobileLoginUser mobileLoginUser = MobileLoginUser.getUser();
        logger.info("isNewbie:" + mobileLoginUser.toString());
        //判断是否领养过宠物和答过题
        MyPetVO myPetVO = myPetMapper.getMyPet(mobileLoginUser.getUserID());
        Integer reuslt = userXQuestionsMapper.isUserAnswered(mobileLoginUser.getUserID());
        UserNewbieVO userNewBieVO = new UserNewbieVO();
        userNewBieVO.setUserId(mobileLoginUser.getUserID());
        if (myPetVO != null) userNewBieVO.setIsChoosePet(1);
        else userNewBieVO.setIsChoosePet(0);
        if (reuslt <= 0) userNewBieVO.setIsAnswered(0);
        else userNewBieVO.setIsAnswered(1);
        return ResultJson.succResultJson(userNewBieVO);
    }

    @Override
    public Integer getNewUserByCompanyIdAndDate(Integer companyId, String date) {
        return userXCompanyMapper.getNewUserByCompanyIdAndDate(companyId, date);
    }

    @Override
    public JSONObject getUserInfo(Integer userId, String corpId) {
        UserEntity userEntity = usersMapper.getUserById(userId);
        MyPetVO myPetVO = myPetMapper.getMyPet(userId);
//        PetExperienceEntity petExperienceEntity = petExperienceMapper.getByLevel(myPetVO.getLevel());
        Integer deptId = userXDeptService.getDepartmentId(corpId, userId);
        String deptName = "";
        if (deptId != null && deptId > 0) {
            deptName = departmentMapper.getDeptNameById(MobileLoginUser.getUser().getCompanyID(), deptId);
        }
        UserInfoVO userInfoVO = new UserInfoVO(userEntity.getId(), deptName, userEntity.getName(), userEntity.getAvatar(), null, null, null, null);
        return ResultJson.succResultJson(userInfoVO);
    }

    /**
     * dingUserId公司内id   0/1/2  新增/修改/删除
     */
    @Override
    public synchronized Integer createOrModifyCallBackDepartment(String corpId, String dingUserId, Integer type) {
        if (type != 2) {
            CorpUserDetail corpUserDetail = DingHelper.getMemberDetail(corpId, dingUserId);
            //部门dingid列表
            if (corpUserDetail != null) {
                List<Long> deptidArray = corpUserDetail.getDepartment();
                String dingId = corpUserDetail.getDingId().trim();
                UserEntity userEntity = usersMapper.getUserByDingId(dingId);
                Set<Integer> userDepartmentIdSet = new HashSet<>();
                Boolean isNewUser = false;
                Boolean isCorpNewUser = false;
                Integer userId = null;
                if (userEntity != null) {
                    //首先更新用户信息
                    isNewUser = false;
                    userId = userEntity.getId();
                    try {
                        usersMapper.updateUser(new UserEntity(userEntity.getId(), corpUserDetail.getName(), corpUserDetail.getAvatar(), new Date()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    //public UserEntity(String name, String dingId, String avatar, Integer status, Integer source, Date createTime, Date updateTime) {
                    isNewUser = true;
                    userEntity = new UserEntity(corpUserDetail.getName(), dingId, corpUserDetail.getAvatar(), StatusEnum.OK.getValue(), UserSourceEnum.NEIXUN.getValue(), new Date(), new Date());
                    try {
                        usersMapper.createOrUpdateUser(userEntity);
                        userId = userEntity.getId();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Integer companyId = isvTicketsService.getIsvTicketByCorpId(corpId).getCompanyId();
                UserXCompany userXCompanyInDB = userXCompanyMapper.getDingIdByCorpIdAndUserId(corpId, userId);
                if (userXCompanyInDB == null) {
                    isCorpNewUser = true;
                    logger.info("userXCompany1——corpUserDetail" + corpUserDetail.toString());
                    Integer isAdmin = corpUserDetail.getIsAdmin() != null && corpUserDetail.getIsAdmin() ? 1 : 0;
                    Integer isBoss = corpUserDetail.getIsBoss() != null && corpUserDetail.getIsBoss() ? 1 : 0;
                    UserXCompany userXCompany = new UserXCompany(corpId, dingUserId, userId, isAdmin, isBoss, StatusEnum.OK.getValue(), new Date(), new Date(), UserSourceEnum.BACKSTAGE.getValue());
                    logger.info("userXCompany1" + userXCompany.toString());
                    userXCompanyMapper.createUserXCompany(userXCompany);
                    userXCompanyMapper.updateUserXCompany(userXCompany);
                } else if (userXCompanyInDB.getStatus().equals(StatusEnum.DELETE.getValue())) {
                    userXCompanyMapper.updateUserXCompany(new UserXCompany(userXCompanyInDB.getId(), StatusEnum.OK.getValue(), new Date()));
                } else if (!userXCompanyInDB.getDingUserId().equals(dingUserId)) {
                    //(Integer id, String corpId, Integer status, Date updateTime, String dingUserId)
                    userXCompanyMapper.updateUserXCompany(new UserXCompany(userXCompanyInDB.getId(), corpId, StatusEnum.OK.getValue(), new Date(), dingUserId));
                }
                Map<Integer, Object> existsUserXDept = new HashMap<>();
                Map<String, Object> newUserXDept = new HashMap<>();
                List<UserXDeptEntity> userDeptList = userXDeptService.getUserDepts(corpId, userId, 0);
                Set<Integer> existUserDeptIdSet = new HashSet<>();
                for (int i = 0; i < userDeptList.size(); i++) {
                    //主键id
                    existUserDeptIdSet.add(userDeptList.get(i).getDeptId());
                }

                for (int i = 0; i < deptidArray.size(); i++) {
                    String deingDeptIdSource = String.valueOf(deptidArray.get(i)).trim();//钉钉部门id
                    Integer departmentId = departmentMapper.getIdByCompanyIdAndDingDeptId(companyId, Integer.valueOf(deingDeptIdSource));
                    if (CollectionUtils.isNotEmpty(existUserDeptIdSet) && existUserDeptIdSet.contains(departmentId)) {
                        //当前存在的用户部门
                        existsUserXDept.put(departmentId, departmentId);
                    } else {
                        //不存在的用户部门
                        newUserXDept.put(deingDeptIdSource, deingDeptIdSource);
                    }
                }
                // TODO @shisan
                //先将用户的部门全更新为空
                userXDeptService.batchSafeDelte(new UserXDeptEntity(corpId, userId, StatusEnum.DELETE.getValue()));
                Iterator itor = existsUserXDept.keySet().iterator();

                //获取用户的部门
                Set<Integer> userDeptIdSet = new HashSet<>();
                String updateDeptNames = "";
                while (itor.hasNext()) {
                    Integer deptId = new Integer(itor.next().toString());
                    String departmentName = departmentMapper.getDeptNameById(companyId, deptId);
                    if (!userDeptIdSet.contains(deptId)) {
                        userDeptIdSet.add(deptId);
                        UserXDeptEntity userXDeptInDB = userXDeptService.getByUserIdAndCorpIdAndDeptId(new UserXDeptEntity(corpId, userId, deptId, null));
                        userXDeptService.createUserXDept(new UserXDeptEntity(userXDeptInDB.getId(), deptId, departmentName, dingUserId, StatusEnum.OK.getValue(), new Date()));
                        if (updateDeptNames == null || updateDeptNames.length() == 0) updateDeptNames += departmentName;
                        else updateDeptNames += "," + updateDeptNames;
                    }
                }
                String addDeptNames = "";
                Iterator newitor = newUserXDept.keySet().iterator();
                while (newitor.hasNext()) {
                    IsvTicketsEntity isvTicket = isvTicketsService.getIsvTicketByCorpId(corpId);
                    //dingDeptId
                    Integer deptId = new Integer(newitor.next().toString().trim());
                    DepartmentEntity departmentInDB = departmentMapper.getByCompanyIdAndDingDeptId(isvTicket.getCompanyId(), deptId);
                    if (CollectionUtils.isEmpty(userDeptIdSet) || (CollectionUtils.isNotEmpty(userDeptIdSet) && !userDeptIdSet.contains(departmentInDB.getId()))) {
                        // TODO 部门入库 修改 @shisan
                        userDeptIdSet.add(departmentInDB.getId());
                        userXDeptService.createUserXDept(new UserXDeptEntity(corpId, departmentInDB.getId(), departmentInDB.getName(), dingUserId, userId, StatusEnum.OK.getValue(), new Date(), new Date()));
                        if (addDeptNames == null || addDeptNames.length() == 0)
                            addDeptNames += departmentInDB.getName();
                        else addDeptNames += "," + departmentInDB.getName();
                    }
                }

                if (newUserXDept != null && newUserXDept.size() > 0) {
                    logger.info("用户" + corpUserDetail.getName() + "新增了部门:" + addDeptNames);
                }
                if (existsUserXDept != null && existsUserXDept.size() > 0) {
                    logger.info("用户" + corpUserDetail.getName() + "更新了部门:" + addDeptNames);
                }


                userDeptList = userXDeptService.getUserDepts(corpId, userId, 1);
                //重新获取用户部门列表
                for (int i = 0; i < userDeptList.size(); i++) {
                    Integer userDeptId = userDeptList.get(i).getDeptId();
                    userDepartmentIdSet.add(userDeptId);
                }

                //初始化企业题库
                List<QuestionsLibraryEntity> enterpriseQuestionsLibraryList = questionsLibraryMapper.findQuestionLibrary(new QuestionsLibraryEntity(companyId, SubjectEnum.ENTERPRISE.getValue(), 1, null));
                //2
                Set<Integer> enterpriseQuestionLibraryAdd = new HashSet<>();
                for (int i = 0; i < enterpriseQuestionsLibraryList.size(); i++) {
                    QuestionsLibraryEntity enterpriseQuestionLibraryEntity = enterpriseQuestionsLibraryList.get(i);
                    Integer libraryId = enterpriseQuestionLibraryEntity.getId();
                    List<StudyRemindVO> studyRemindVOList = studyRemindMapper.getListByCompanyIdAndLibraryIdAndType(companyId, libraryId, 0);
                    StringBuffer publicDefaultDepartIdBuffer = new StringBuffer(200);
                    if (CollectionUtils.isNotEmpty(studyRemindVOList)) {
                        for (int j = 0; j < studyRemindVOList.size(); j++) {
                            if (publicDefaultDepartIdBuffer.length() == 0) {
                                publicDefaultDepartIdBuffer.append(studyRemindVOList.get(j).getDeptid());
                            } else {
                                publicDefaultDepartIdBuffer.append(",").append(studyRemindVOList.get(j).getDeptid());
                            }
                        }
                        Set<Integer> publicDefaultDepartmentIdSet = studyRemindService.getSubDepartment(companyId, publicDefaultDepartIdBuffer.toString());
                        Iterator<Integer> usedepartmentItor = userDepartmentIdSet.iterator();
                        while (usedepartmentItor.hasNext()) {
                            Integer userdepartmentId = usedepartmentItor.next();
                            if (publicDefaultDepartmentIdSet.contains(userdepartmentId)) {
                                if (!enterpriseQuestionLibraryAdd.contains(libraryId)) {
                                    enterpriseQuestionLibraryAdd.add(libraryId);
                                }
                                break;
                            } else {
                                continue;
                            }
                        }
                    } else {
                        continue;
                    }
                }

//            List<UserXLibraryEntity> publicAddUserXLibraryList = new ArrayList<>();
//            List<UserXLibraryEntity> publicUpdateUserXLibraryList = new ArrayList<>();

                List<UserXLibraryEntity> enterpriseAddUserXLibraryList = new ArrayList<>();
                List<UserXLibraryEntity> enterpriseUpdateUserXLibraryList = new ArrayList<>();

                if (CollectionUtils.isNotEmpty(enterpriseQuestionLibraryAdd)) {
                    Iterator<Integer> enterpriseQuestionIdItor = enterpriseQuestionLibraryAdd.iterator();
                    while (enterpriseQuestionIdItor.hasNext()) {
                        Integer enterpriseQuestionLibraryId = enterpriseQuestionIdItor.next();
                        UserXLibraryEntity userXLibrary = userXLibraryMapper.getByUserIdAndLibraryId(companyId, userId, enterpriseQuestionLibraryId);
                        if (userXLibrary == null) {
                            //public UserXLibraryEntity(Integer userId, Integer libraryId, Integer schedule, Date lastAnswerTime, Integer status, Date createTime, Date updateTime,Integer companyId)
                            enterpriseAddUserXLibraryList.add(new UserXLibraryEntity(userId, enterpriseQuestionLibraryId, new BigDecimal(0), null, StatusEnum.OK.getValue(), new Date(), new Date(), companyId));
                        } else if (userXLibrary != null && userXLibrary.getStatus().equals(0)) {
                            enterpriseUpdateUserXLibraryList.add(new UserXLibraryEntity(userXLibrary.getId(), companyId, userId, StatusEnum.OK.getValue()));
                        }
                    }
                }

                if (CollectionUtils.isNotEmpty(enterpriseAddUserXLibraryList)) {
                    userXLibraryMapper.batchInsertuserxlibrary(enterpriseAddUserXLibraryList);
                }
                if (CollectionUtils.isNotEmpty(enterpriseUpdateUserXLibraryList)) {
                    userXLibraryMapper.batchUpdateNew(enterpriseUpdateUserXLibraryList);
                }

                if (isCorpNewUser) {
                    //更新该用户在公司的饲料数
//                    MyPetVO myPetVO = myPetMapper.getMyPet(userId);
//                    if (myPetVO != null) {
//                        Boolean isExist = true;
//                        PetFoodDetailEntity petFoodDetailEntity = petFoodDetailMapper.getByCompanyAndUserId(companyId, userId);
//                        if (petFoodDetailEntity == null) {
//                            petFoodDetailEntity = new PetFoodDetailEntity(companyId, userId, PetFoodGainEnum.INIT.getValue(), PET_INIT_FOOD_COUNT, new Date(), new Date());
//                            petFoodDetailMapper.createPetFoodDetail(petFoodDetailEntity);
//                            isExist = false;
//                        }
//                        PetFoodEntity petFoodEntity = petFoodMapper.getPetFood(companyId, userId);
//                        if (petFoodEntity == null) {
//                            petFoodEntity = new PetFoodEntity(companyId, userId, PET_INIT_FOOD_COUNT, new Date(), new Date());
//                            petFoodMapper.createPetFood(petFoodEntity);
//                        } else if (petFoodEntity != null && !isExist) {
//                            Integer foodCount = petFoodEntity.getFoodCount() + PET_INIT_FOOD_COUNT;
//                            petFoodEntity.setFoodCount(foodCount);
//                            petFoodMapper.updatePetFood(petFoodEntity);
//                        }
//                    }

                    new Thread(new WelcomeSingleThread(corpUserDetail.getUserid(), corpId, companyId)).start();
                    saveOASendBury(companyId, userId, OABuryEnum.ENTER.getValue(), new Date());
                }
            }
        } else {
            //更新用户公司和用户
            UserXCompany userXCompany = userXCompanyMapper.getByCompanyIdANdDingUserId(corpId, dingUserId);
            if (userXCompany != null) {
                userXCompanyMapper.updateUserXCompany(new UserXCompany(userXCompany.getId(), StatusEnum.DELETE.getValue(), new Date()));
                userXDeptService.batchSafeDelte(new UserXDeptEntity(corpId, userXCompany.getUserId(), StatusEnum.DELETE.getValue()));
            }
        }
        return type;
    }

    @Override
    public JSONObject invite(Integer userId) {
        MobileLoginUser mobileLoginUser = MobileLoginUser.getUser();
        Integer companyId = mobileLoginUser.getCompanyID();
        Integer mUserId = mobileLoginUser.getUserID();
        IsvTicketsEntity isvTicketsEntity = isvTicketsService.getIsvTicketByCompanyId(companyId);
        String dingName = usersMapper.getNameById(mUserId);
        String praiseTitle = String.format(INVITE_MESSAGE, dingName);
        praiseTitle += "\n" + DateUtil.getDate_Y_M_D_H_M_S();
//                LinkOAMessage linkOAMessage = new LinkOAMessage();
        OAMessage oaMessage = new OAMessage();
        String mainPageBury = MAIN_PAGE + GlobalConstant.OA_BURY_TYPE + OABuryEnum.INVITE.getValue();
        String mainPage = String.format(mainPageBury, isvTicketsEntity.getCorpId());
        String mainPageQrcode = String.format(QRCODE_PAGE, URLEncoder.encode(mainPage));
        UserXCompany userXCompany = userXCompanyMapper.getDingIdByCorpIdAndUserId(isvTicketsEntity.getCorpId(), userId);
        OAMessageUtil.sendOAMessageWithStroageV2(companyId, userXCompany.getDingUserId(), "", oaMessage.getSimpleOAMessage(mainPage, mainPageQrcode, praiseTitle));
        saveRaisePetBury(companyId, mUserId, AppInviteEnum.INVITE.getValue(), new Date());
        saveOASendBury(companyId, mUserId, OABuryEnum.INVITE.getValue(), new Date());
        return ResultJson.succResultJson(userId);
    }

    private void saveRaisePetBury(Integer companyId, Integer userId, Integer type, Date createTime) {
        AppInviteBuryEntity petRaiseBuryEntity = new AppInviteBuryEntity(companyId, userId, type, createTime);
        appInviteBuryMapper.createAppInviteBury(petRaiseBuryEntity);
    }

    private void saveOASendBury(Integer companyId, Integer userId, Integer type, Date createTime) {
        OASendBuryEntity oaSendBuryEntity = new OASendBuryEntity(companyId, userId, type, createTime);
        oaSendBuryMapper.createOASendBury(oaSendBuryEntity);
    }


    private static String ustartToCn(final String str) {
        StringBuilder sb = new StringBuilder().append("0x")
                .append(str.substring(2, 6));
        Integer codeInteger = Integer.decode(sb.toString());
        int code = codeInteger.intValue();
        char c = (char) code;
        return String.valueOf(c);
    }


    public static void main(String[] args) throws UnsupportedEncodingException {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("result", ResultCodeEnum.SUCCESS.getValue().toString());
//        jsonObject.put("msg", "ok");
//        jsonObject.put("data", 12);
//        System.out.println(jsonObject.toString());
        String userName = "\uD83C\uDDE8\uD83C\uDDF3陈栖文   ♻️『近视防控-孝感™』";
        // String sss =  new String("\uD83C\uDDE8\uD83C\uDDF3陈栖文   ♻️『近视防控-孝感™".getBytes(),"utf-8");

        // System.out.println(sss);

    }


}
