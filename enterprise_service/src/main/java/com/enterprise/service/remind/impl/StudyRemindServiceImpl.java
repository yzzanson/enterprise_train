package com.enterprise.service.remind.impl;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.DDConstant;
import com.enterprise.base.common.GlobalConstant;
import com.enterprise.base.common.LoginUser;
import com.enterprise.base.common.ResultJson;
import com.enterprise.base.entity.*;
import com.enterprise.base.enums.StatusEnum;
import com.enterprise.base.vo.UserVO;
import com.enterprise.base.vo.UserVO2;
import com.enterprise.base.vo.studyRemind.StudyRemindDeptVO;
import com.enterprise.base.vo.studyRemind.StudyRemindUserVO;
import com.enterprise.base.vo.studyRemind.StudyRemindVO;
import com.enterprise.mapper.companyInfo.CompanyInfoMapper;
import com.enterprise.mapper.department.DepartmentMapper;
import com.enterprise.mapper.isvTickets.IsvTicketsMapper;
import com.enterprise.mapper.questions.QuestionsLibraryMapper;
import com.enterprise.mapper.questions.QuestionsMapper;
import com.enterprise.mapper.remind.StudyRemindMapper;
import com.enterprise.mapper.users.UserMapper;
import com.enterprise.mapper.users.UserXCompanyMapper;
import com.enterprise.mapper.users.UserXDeptMapper;
import com.enterprise.mapper.users.UserXLibraryMapper;
import com.enterprise.oa.OAMessageUtil;
import com.enterprise.service.isvTickets.IsvTicketsService;
import com.enterprise.service.remind.StudyRemindService;
import com.enterprise.util.AssertUtil;
import com.enterprise.util.DateUtil;
import com.enterprise.util.oa.message.OAMessage;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/4/23 下午3:01
 */
@Service
public class StudyRemindServiceImpl implements StudyRemindService {

    @Resource
    private StudyRemindMapper studyRemindMapper;

    @Resource
    private DepartmentMapper departmentMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserXCompanyMapper userXCompanyMapper;

    @Resource
    private UserXDeptMapper userXDeptMapper;

    @Resource
    private UserXLibraryMapper userXLibraryMapper;

    @Resource
    private QuestionsLibraryMapper questionsLibraryMapper;

    @Resource
    private IsvTicketsService isvTicketsService;

    @Resource
    private IsvTicketsMapper isvTicketsMapper;

    @Resource
    private QuestionsMapper questionsMapper;

    private String ARRANGE_DEFAULT_MESSAGE = DDConstant.ARRANGE_STUDY_DEFAULT_MESSAGE;


    @Resource
    private CompanyInfoMapper companyInfoMapper;
    private String ARRANGE_DEFAULT_IMAGE = DDConstant.NEW_LIB;
    //二维码页面
    private String QRCODE_PAGE = GlobalConstant.getQrCOdePage();

    private static Logger logger = LoggerFactory.getLogger(StudyRemindServiceImpl.class);

    /**
     * 将部门全部转化为员工
     * 如果安排了学习,则该员工拥有该题库的使用权,如果没有安排学习,该员工即失去了该题库的使用权
     */
    @Override
    @Transactional
    public JSONObject saveOrUpdate(Integer libraryId, String deptids, String userids) {
        try {
            boolean isContainAll = false;
            boolean isAllUser = false;
            if (StringUtils.isNotEmpty(deptids)) {
                String[] deptIdsArray = deptids.split(",");
                for (int i = 0; i < deptIdsArray.length; i++) {
                    String dingDeptId = departmentMapper.getDingDepartIdById(Integer.valueOf(deptIdsArray[i]));
                    if (dingDeptId.equals("1")) {
                        isAllUser = true;
                        isContainAll = true;
                        break;
                    }
                }
            }
            AssertUtil.notNull(libraryId, "题库id不能为空!");

            Integer totalCount = questionsMapper.getTotalCountByLibraryId(libraryId);
            AssertUtil.isTrue(totalCount>0, "此题库没有题目!");

            LoginUser loginUser = LoginUser.getUser();
            Integer companyId = loginUser.getCompanyID();
            String corpId = loginUser.getCorpID();
            if (StringUtils.isEmpty(deptids) && StringUtils.isEmpty(userids)) {
                userXLibraryMapper.batchDelete(new UserXLibraryEntity(companyId, StatusEnum.DELETE.getValue(), libraryId));
                studyRemindMapper.batchSafeDelete(companyId, libraryId, StatusEnum.DELETE.getValue(), new Date());
                return ResultJson.succResultJson("安排学习成功");
            }
            QuestionsLibraryEntity questionsLibraryEntity = questionsLibraryMapper.getById(libraryId);
            String libraryName = questionsLibraryEntity.getName();
            //先删除该公司该题库所有的学习提醒记录,再重新插入
            //获取所有当前发送过消息提醒的用户 -- 0 人员 1部门
            List<StudyRemindEntity> studyRemindUserIds = studyRemindMapper.getRemindUserIdList(companyId, libraryId, 0, null);
            List<StudyRemindEntity> studyRemindDeptIds = studyRemindMapper.getRemindUserIdList(companyId, libraryId, 1, null);
            if (CollectionUtils.isEmpty(studyRemindUserIds) && CollectionUtils.isEmpty(studyRemindDeptIds) && isAllUser) {
                isAllUser = true;
            } else {
                isAllUser = false;
            }

            //保存userXLibrary用
            //获取需要保存的部门及所有子部门
            Set<Integer> remindDeptIds = getRemindDepts(companyId, deptids);
            //获取所有参数传过来的需要提醒的数据
            List<UserXLibraryEntity> updateUserXLibraryList = getUpdateUserXLibraryList(companyId, libraryId, remindDeptIds, userids);

            //获取更新的数据
            Map<String, List<StudyRemindEntity>> updateUserIds = getAddOrUpdateUserIds(companyId, libraryId, studyRemindUserIds, userids);
            Map<String, List<StudyRemindEntity>> updateDeptIds = getAddOrUpdateDeptds(companyId, libraryId, studyRemindDeptIds, deptids);

            List<StudyRemindEntity> updateStudyRemindList = updateUserIds.get("update");
            List<StudyRemindEntity> addStudyRemindList = updateUserIds.get("add");

            List<StudyRemindEntity> updateStudyRemindDeptList = updateDeptIds.get("update");
            List<StudyRemindEntity> addStudyRemindDeptList = updateDeptIds.get("add");

            //studyRemind更新
            if (updateStudyRemindList != null) {
                for (int i = 0; i < updateStudyRemindList.size(); i++) {
                    StudyRemindEntity sr = updateStudyRemindList.get(i);
                    studyRemindMapper.updateStudyRemind(sr.getId(), sr.getStatus(), new Date());
                }
            }
            if (addStudyRemindList != null && addStudyRemindList.size() > 0) {
                studyRemindMapper.batchInsertStudyRemind(addStudyRemindList);
            }

            /**
             * 将学习提醒的部门信息更新入库
             * */
            if (updateStudyRemindDeptList != null && updateStudyRemindDeptList.size() > 0) {
                studyRemindMapper.batchUpdateStudyRemind(updateStudyRemindDeptList);
            }
            if (addStudyRemindDeptList != null && addStudyRemindDeptList.size() > 0) {
                studyRemindMapper.batchInsertStudyRemindDept(addStudyRemindDeptList);
            }

            StringBuffer sendUserBuffer = new StringBuffer(200);
            StringBuffer sendDeptBuffer = new StringBuffer(200);
//            String sendDeptDingId = "";
//            String sendUserDingId = "";
            if (CollectionUtils.isNotEmpty(addStudyRemindList)) {
                for (int i = 0; i < addStudyRemindList.size(); i++) {
                    Integer adduserId = addStudyRemindList.get(i).getUserId();
                    UserXCompany userXCompanyEntity = userXCompanyMapper.getDingIdByCorpIdAndUserId(corpId, adduserId);
                    String dingUserId = userXCompanyEntity.getDingUserId();
                    if (sendUserBuffer.length()==0) {
                        sendUserBuffer.append(dingUserId);
                    } else {
                        sendUserBuffer.append("|").append(dingUserId);
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(addStudyRemindDeptList)) {
                for (int i = 0; i < addStudyRemindDeptList.size(); i++) {
                    Integer deptId = addStudyRemindDeptList.get(i).getDeptId();
                    DepartmentEntity department = departmentMapper.getDepartmentById(deptId);
                    if (department != null) {
                        if (sendDeptBuffer.length()==0) {
                            sendDeptBuffer.append(department.getDingDeptId());
                        } else {
                            sendDeptBuffer.append("|").append(department.getDingDeptId());
                        }
                    }
                }
            }
            //获取所有提醒过的用户,先安排部分后,全选会出问题修复
            Set<Integer> userLibraryStudySet = new HashSet<>();
            List<Integer> userLibraryList = userXLibraryMapper.getUserByCorpIdAndLibraryId(companyId, corpId, libraryId);
            for (int i = 0; i < userLibraryList.size(); i++) {
                userLibraryStudySet.add(userLibraryList.get(i));
            }

            //先删除所有记录,对于存在题库的更新记录,不存在题库的加入题库
            userXLibraryMapper.batchDelete(new UserXLibraryEntity(companyId, StatusEnum.DELETE.getValue(), libraryId));

            if (CollectionUtils.isNotEmpty(updateUserXLibraryList)) {
                for (int i = 0; i < updateUserXLibraryList.size(); i++) {
                    UserXLibraryEntity userXLibraryUnknow = updateUserXLibraryList.get(i);
                    if (userXLibraryUnknow.getUserId() == null) {
                        continue;
                    }
                    UserXLibraryEntity uxl = userXLibraryMapper.getByUserIdAndLibraryId(companyId, userXLibraryUnknow.getUserId(), libraryId);
                    if (uxl == null) {
                        //Integer userId, Integer libraryId, Integer schedule, Date lastAnswerTime, Integer status, Date createTime, Date updateTime
                        userXLibraryMapper.createOrUpdateUserXLibrary(new UserXLibraryEntity(userXLibraryUnknow.getUserId(), libraryId, new BigDecimal(0), null, StatusEnum.OK.getValue(), new Date(), new Date(), companyId));
                    } else {
                        uxl.setStatus(1);
                        userXLibraryMapper.updateUserXLibrary(uxl);
                    }
                }
            }

//            StudyRemindConfVO studyRemindConfVO = studyRemindConfMapper.getByCompanyId(companyId);
            if (questionsLibraryEntity.getIsOA()==null || (questionsLibraryEntity.getIsOA()!=null && questionsLibraryEntity.getIsOA().equals(1))) {
                String content = String.format(ARRANGE_DEFAULT_MESSAGE, libraryName);
                String messageUrl = String.format(GlobalConstant.getChooseLibraryUrl(), corpId);
                String arena_pk_result_qrcode = String.format(QRCODE_PAGE, URLEncoder.encode(messageUrl));
                OAMessage oaMessage = new OAMessage().getOAMessageWithPic(messageUrl, arena_pk_result_qrcode, OAMessageUtil.getMessageContent(content), ARRANGE_DEFAULT_IMAGE);
                if (isAllUser) {
                    //10个人发送一次
                    StringBuffer dingUserBuffer = new StringBuffer(200);
//                    String dingUserIds = "";
                    List<UserVO> userList = userXCompanyMapper.getUserByCorpId(corpId, StatusEnum.OK.getValue());
                    Integer loopCount = userList.size() % 10 == 0 ? userList.size() / 10 : 1 + (userList.size() / 10);
                    for (int i = 0; i < loopCount; i++) {
                        Integer startIndex = i * 10;
                        Integer endIndex = 0;
                        if (loopCount <= 1) {
                            endIndex = userList.size();
                        } else {
                            if (i < loopCount - 1) {
                                endIndex = (i + 1) * 10;
                            } else {
                                endIndex = userList.size();
                            }
                        }
                        for (int j = startIndex; j < endIndex; j++) {
                            if (j == startIndex) {
                                dingUserBuffer.append(userList.get(j).getDingUserId());
                            } else {
                                dingUserBuffer.append("|").append(userList.get(j).getDingUserId());
                            }
                        }
                        logger.info("第" + i + "次发送全体OA消息");
                        OAMessageUtil.sendOAMessageWithStroage(loginUser.getCompanyID(), dingUserBuffer.toString(), content, oaMessage.getOAMessageWithPic(messageUrl, arena_pk_result_qrcode, OAMessageUtil.getMessageContent(content), ARRANGE_DEFAULT_IMAGE));
                    }
//                    OAMessageUtil.sendOAMessageWithStroageToALL(loginUser.getCompanyID(),content,new OAMessageAll().getOAMessageWithPic("oa",messageUrl, arena_pk_result_qrcode, dateTime,OAMessageUtil.getMessageContent(content), ARRANGE_DEFAULT_IMAGE));
                    //非全部但是选了全部
                } else if (isContainAll) {

                    StringBuffer dingUserBuffer = new StringBuffer(200);
                    List<UserVO> userList = userXCompanyMapper.getUserByCorpId(corpId, StatusEnum.OK.getValue());
                    Integer loopCount = userList.size() % 10 == 0 ? userList.size() / 10 : 1 + (userList.size() / 10);
                    for (int i = 0; i < loopCount; i++) {
                        Integer startIndex = i * 10;
                        Integer endIndex = 0;
                        if (loopCount <= 1) {
                            endIndex = userList.size();
                        } else {
                            if (i < loopCount - 1) {
                                endIndex = (i + 1) * 10;
                            } else {
                                endIndex = userList.size();
                            }
                        }
                        for (int j = startIndex; j < endIndex; j++) {
                            if (!userLibraryStudySet.contains(userList.get(j).getId())) {
                                if (dingUserBuffer.length()==0) {
                                    dingUserBuffer.append(userList.get(j).getDingUserId());
                                } else {
                                    dingUserBuffer.append("|").append(userList.get(j).getDingUserId());
                                }
                            }
                        }
                        if (dingUserBuffer.length()>0) {
                            OAMessageUtil.sendOAMessageWithStroage(loginUser.getCompanyID(), dingUserBuffer.toString(), content, oaMessage.getOAMessageWithPic(messageUrl, arena_pk_result_qrcode, OAMessageUtil.getMessageContent(content), ARRANGE_DEFAULT_IMAGE));
                        }
                    }

                } else {
                    OAMessageUtil.sendOAMessageWithStroage(loginUser.getCompanyID(), sendDeptBuffer.toString(), sendUserBuffer.toString(), content, oaMessage.getOAMessageWithPic(messageUrl, arena_pk_result_qrcode, OAMessageUtil.getMessageContent(content), ARRANGE_DEFAULT_IMAGE));
                }
            }
            return ResultJson.succResultJson("安排学习成功");
        } catch (Exception e) {
            return ResultJson.errorResultJson(e.getMessage());
        }

    }

    /**
     * @Param updateDepartmentIdSet  需要更新的部门列表(传入的部门id遍历获取其子部门)
     * @Param userIds                传入的用户id
     */
    private List<UserXLibraryEntity> getUpdateUserXLibraryList(Integer companyId, Integer libraryId, Set<Integer> updateDepartmentIdSet, String userIds) {
        Set<Integer> updateUserxLibraryIdSet = new HashSet<>();
        List<UserXLibraryEntity> allUpdateUserXLibraryList = new ArrayList<>();
        IsvTicketsEntity isvTicketEntity = isvTicketsService.getIsvTicketByCompanyId(companyId);
        if (CollectionUtils.isNotEmpty(updateDepartmentIdSet)) {
            Iterator departmentIdSetIter = updateDepartmentIdSet.iterator();
            while (departmentIdSetIter.hasNext()) {
                Integer departmentId = (Integer) departmentIdSetIter.next();
                List<UserXDeptEntity> userDeptList = userXDeptMapper.getUserInDept(isvTicketEntity.getCorpId(), departmentId, null);
                for (int i = 0; i < userDeptList.size(); i++) {
                    Integer userId = userDeptList.get(i).getUserId();
                    if (!updateUserxLibraryIdSet.contains(userId)) {
                        // public UserXLibraryEntity(Integer userId, Integer libraryId, Integer schedule, Date lastAnswerTime, Integer status, Date createTime, Date updateTime,Integer companyId)
                        updateUserxLibraryIdSet.add(userId);
                        UserXLibraryEntity userXLibraryEntity = new UserXLibraryEntity(userId, libraryId, null, null, 1, new Date(), new Date(), companyId);
                        allUpdateUserXLibraryList.add(userXLibraryEntity);
                    } else {
                        continue;
                    }
                }
            }
        }
        if (StringUtils.isNotEmpty(userIds)) {
            String[] userIdArray = userIds.split(",");
            for (int i = 0; i < userIdArray.length; i++) {
                Integer updateUserid = Integer.valueOf(userIdArray[i]);
                if (!updateUserxLibraryIdSet.contains(updateUserid)) {
                    // public UserXLibraryEntity(Integer userId, Integer libraryId, Integer schedule, Date lastAnswerTime, Integer status, Date createTime, Date updateTime,Integer companyId)
                    updateUserxLibraryIdSet.add(updateUserid);
                    UserXLibraryEntity userXLibraryEntity = new UserXLibraryEntity(updateUserid, libraryId, null, null, 1, new Date(), new Date(), companyId);
                    allUpdateUserXLibraryList.add(userXLibraryEntity);
                } else {
                    continue;
                }
            }
        }
        return allUpdateUserXLibraryList;
    }


    @Override
    public Set<Integer> getSubDepartment(Integer companyId, String deptids) {
        Set<Integer> subDeptIds = getRemindDepts(companyId, deptids);
        return subDeptIds;
    }

    //遍历获取该公司下的所有部门
    private Set<Integer> getRemindDepts(Integer companyId, String departmentIds) {
        Set<Integer> addRemindDeptIdSet = new HashSet<>();
        if (StringUtils.isEmpty(departmentIds)) {
            return addRemindDeptIdSet;
        } else {
            String[] departmentIdArray = departmentIds.split(",");
            for (int i = 0; i < departmentIdArray.length; i++) {
                Integer departmentId = Integer.valueOf(departmentIdArray[i]);
                DepartmentEntity deprmentInDB = departmentMapper.getDepartmentById(departmentId);
                if (deprmentInDB != null && deprmentInDB.getStatus() != null && deprmentInDB.getStatus().equals(1)) {
                    addRemindDeptIdSet.add(departmentId);
                    getChildDepartmentId(addRemindDeptIdSet, companyId, departmentId);
                } else {
                    continue;
                }
            }
        }
        return addRemindDeptIdSet;
    }

    private void getChildDepartmentId(Set<Integer> addRemindDeptIdSet, Integer companyId, Integer deparmtmentId) {
        DepartmentEntity initDepartment = departmentMapper.getDepartmentById(deparmtmentId);
        List<DepartmentEntity> childDepartmentList = departmentMapper.getDepartmentsByParentId(companyId, initDepartment.getDingDeptId());
        List<DepartmentEntity> childList = new ArrayList<>();
        for (int i = 0; i < childDepartmentList.size(); i++) {
            DepartmentEntity childDepartment = childDepartmentList.get(i);
            Integer childDepparmentId = childDepartment.getId();
            if (childDepartment.getParentId().equals(initDepartment.getDingDeptId())) {
                addRemindDeptIdSet.add(childDepparmentId);
                childList.add(childDepartment);
            }
        }
        for (DepartmentEntity childDeparmtnt : childList) {
            Integer deparmentId = childDeparmtnt.getId();
            List<DepartmentEntity> subChildDepartmentList = departmentMapper.getDepartmentsByParentId(companyId, childDeparmtnt.getDingDeptId());
            if (subChildDepartmentList.size() > 0) {
                for (int i = 0; i < subChildDepartmentList.size(); i++) {
                    DepartmentEntity depatemrntsd = subChildDepartmentList.get(i);
                    addRemindDeptIdSet.add(depatemrntsd.getId());
                }
            }
            getChildDepartmentId(addRemindDeptIdSet, companyId, deparmentId);
        }

    }


    //前面的是否包含后面的
    private static boolean isContains(String preString, String afterString) {
        String[] preStringArr = preString.split(",");
        String[] aftertringArr = afterString.split(",");
        for (int i = 0; i < preStringArr.length; i++) {
            for (int j = 0; j < aftertringArr.length; j++) {
                if (preStringArr[i].equals(aftertringArr[j])) {
                    return true;
                }
                continue;
            }
        }
        return false;
    }

    /**
     * @Param studyRemindList 已经提醒过的
     * @Param userIds         所有需要提醒的
     */
    private Map<String, List<StudyRemindEntity>> getAddOrUpdateUserIds(Integer companyId, Integer libraryId, List<StudyRemindEntity> studyRemindList, String userIds) {
        String[] useridArray = null;
        Set<Integer> needRemindUserIdSet = new HashSet<>();
        if (StringUtils.isNotEmpty(userIds)) {
            useridArray = userIds.split(",");
            for (int i = 0; i < useridArray.length; i++) {
                Integer userId = Integer.valueOf(useridArray[i]);
                needRemindUserIdSet.add(userId);
            }
        }

        Map<String, List<StudyRemindEntity>> resultMap = new HashMap<>();
        if (studyRemindList == null || studyRemindList.size() == 0) {
            if (useridArray == null || useridArray.length == 0) {
                resultMap.put("add", null);
                resultMap.put("update", null);
                return resultMap;
            }
            List<StudyRemindEntity> addList = new ArrayList<>();
            Iterator setIter = needRemindUserIdSet.iterator();
            while (setIter.hasNext()) {
                Integer userId = (Integer) setIter.next();
                addList.add(new StudyRemindEntity(companyId, libraryId, userId, StatusEnum.OK.getValue(), new Date(), new Date()));
            }
            resultMap.put("add", addList);
            resultMap.put("update", null);
            return resultMap;
        } else {
            List<StudyRemindEntity> addList = new ArrayList<>();
            List<StudyRemindEntity> updateList = new ArrayList<>();
            Map<Integer, Integer> addUpdatesMap = new HashMap<>();
            //学习过的更新,未学习过的插入
            for (StudyRemindEntity studyRemind : studyRemindList) {
                Integer studyRemindUserId = studyRemind.getUserId();
                if (needRemindUserIdSet.contains(studyRemindUserId)) {
                    studyRemind.setStatus(StatusEnum.OK.getValue());
                    studyRemind.setUpdateTime(new Date());
                    updateList.add(studyRemind);
                    addUpdatesMap.put(studyRemindUserId, studyRemindUserId);
                } else {
                    studyRemind.setStatus(StatusEnum.DELETE.getValue());
                    studyRemind.setUpdateTime(new Date());
                    updateList.add(studyRemind);
                    addUpdatesMap.put(studyRemindUserId, studyRemindUserId);
                }
            }
            Iterator<Integer> setIter = needRemindUserIdSet.iterator();
            while (setIter.hasNext()) {
                Integer userId = setIter.next();
                if (!addUpdatesMap.containsKey(userId)) {
                    StudyRemindEntity studyRemindEntity = new StudyRemindEntity(companyId, libraryId, userId, StatusEnum.OK.getValue(), new Date(), new Date());
                    addList.add(studyRemindEntity);
                }
            }
            resultMap.put("add", addList);
            resultMap.put("update", updateList);
            return resultMap;
        }
    }

    /**
     * @Param studyRemindList 已经提醒过的
     * @Param deptIds/needRemindUserIdSet 所有需要提醒的
     */
    private Map<String, List<StudyRemindEntity>> getAddOrUpdateDeptds(Integer companyId, Integer libraryId, List<StudyRemindEntity> studyRemindList, String deptIds) {
        Set<Integer> needRemindDeptIdSet = new HashSet<>();
        Map<String, List<StudyRemindEntity>> resultMap = new HashMap<>();
        String[] deptIdArray = null;
        if (StringUtils.isNotEmpty(deptIds)) {
            deptIdArray = deptIds.split(",");
            for (String deptId : deptIdArray) {
                needRemindDeptIdSet.add(Integer.valueOf(deptId.trim()));
            }
            if (studyRemindList == null || studyRemindList.size() == 0) {
                List<StudyRemindEntity> addList = new ArrayList<>();
                Iterator<Integer> setIter = needRemindDeptIdSet.iterator();
                while (setIter.hasNext()) {
                    //public StudyRemindEntity(Integer id, Integer companyId, Integer libraryId, Integer deptId, Integer userId, Integer status)
                    addList.add(new StudyRemindEntity(null, companyId, libraryId, setIter.next(), null, StatusEnum.OK.getValue()));
                }
                resultMap.put("add", addList);
                resultMap.put("update", null);
                return resultMap;
            } else {
                List<StudyRemindEntity> addList = new ArrayList<>();
                List<StudyRemindEntity> updateList = new ArrayList<>();
                Map<Integer, Integer> addUpdatesMap = new HashMap<>();
                //学习过的更新,未学习过的插入
                for (StudyRemindEntity studyRemind : studyRemindList) {//数据库中的
                    Integer studyRemindDeptId = studyRemind.getDeptId();
                    if (needRemindDeptIdSet.contains(studyRemindDeptId)) {//勾选提醒过的
                        studyRemind.setStatus(StatusEnum.OK.getValue());
                        studyRemind.setUpdateTime(new Date());
                        updateList.add(studyRemind);
                        addUpdatesMap.put(studyRemindDeptId, studyRemindDeptId);
                    } else {
                        studyRemind.setStatus(StatusEnum.DELETE.getValue());
                        studyRemind.setUpdateTime(new Date());
                        updateList.add(studyRemind);
                        addUpdatesMap.put(studyRemindDeptId, studyRemindDeptId);
                    }
                }
                Iterator<Integer> setIter = needRemindDeptIdSet.iterator();
                while (setIter.hasNext()) {
                    Integer deptId = setIter.next();
                    if (!addUpdatesMap.containsKey(deptId)) {
                        // addList.add(new StudyRemindEntity(null,companyId, libraryId, setIter.next(),null, StatusEnum.OK.getValue()));
                        StudyRemindEntity studyRemindEntity = new StudyRemindEntity(null, companyId, libraryId, deptId, null, StatusEnum.OK.getValue());
                        addList.add(studyRemindEntity);
                    }
                }
                resultMap.put("add", addList);
                resultMap.put("update", updateList);
                return resultMap;
            }
        } else {
            List<StudyRemindEntity> updateList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(studyRemindList)) {
                for (int i = 0; i < studyRemindList.size(); i++) {
                    StudyRemindEntity studyRemindEntity = studyRemindList.get(i);
                    studyRemindEntity.setStatus(0);
                    updateList.add(studyRemindEntity);
                }
            }
            resultMap.put("update", updateList);
        }
        return resultMap;
    }

    private Set<Integer> getUserIdsByDepts(String corpId, String deptIds, String userids) {
        Set<Integer> userIdSet = new HashSet<>();
        if (StringUtils.isEmpty(deptIds) && StringUtils.isEmpty(userids)) {
            return null;
        } else {
            if (StringUtils.isNotEmpty(deptIds)) {
                String[] deptIdArray = deptIds.split(",");
                for (int i = 0; i < deptIdArray.length; i++) {
                    Integer deptId = Integer.valueOf(deptIdArray[i]);
                    List<UserXDeptEntity> userDeptList = userXDeptMapper.getUserInDept(corpId, deptId, StatusEnum.OK.getValue());
                    for (UserXDeptEntity usxDept : userDeptList) {
                        userIdSet.add(usxDept.getUserId());
                    }
                }
            }
            if (StringUtils.isNotEmpty(userids)) {
                String[] userIdArray = userids.split(",");
                for (int i = 0; i < userIdArray.length; i++) {
                    Integer userId = Integer.valueOf(userIdArray[i]);
                    userIdSet.add(userId);
                }
            }
        }
        return userIdSet;
    }


    @Override
    public JSONObject getArrangements(Integer libraryId) {
        Integer companyId = LoginUser.getUser().getCompanyID();
        List<StudyRemindVO> resultList = new ArrayList<>();

        List<StudyRemindEntity> studyRemindDepartmentList = studyRemindMapper.getRemindUserIdList(companyId, libraryId, 1, StatusEnum.OK.getValue());
        List<StudyRemindEntity> studyRemindUserList = studyRemindMapper.getRemindUserIdList(companyId, libraryId, 0, StatusEnum.OK.getValue());

        if (CollectionUtils.isNotEmpty(studyRemindDepartmentList)) {
            for (StudyRemindEntity studyRemindDepartment : studyRemindDepartmentList) {
                Integer departmentId = studyRemindDepartment.getDeptId();
                String deptName = departmentMapper.getDeptNameById(companyId, departmentId);
                StudyRemindVO studyRemindVO = new StudyRemindVO();
                studyRemindVO.setDeptid(departmentId);
                studyRemindVO.setDeptname(deptName);
                resultList.add(studyRemindVO);
            }
        }

        if (CollectionUtils.isNotEmpty(studyRemindUserList)) {
            for (int i = 0; i < studyRemindUserList.size(); i++) {
                Integer userId = studyRemindUserList.get(i).getUserId();
                String userName = userMapper.getNameById(userId);
                StudyRemindVO studyRemindVO = new StudyRemindVO();
                studyRemindVO.setUserid(userId);
                studyRemindVO.setUsername(userName);
                resultList.add(studyRemindVO);
            }
        }
        CompanyInfoEntity companyInfoEntity = companyInfoMapper.getCompanyById(companyId);
        JSONObject resultJson = ResultJson.succResultJson(resultList);
        if(companyInfoEntity.getRefreshTime()!=null){
            resultJson.put("refresh_time",DateUtil.getDisplayYMDHMS(companyInfoEntity.getRefreshTime()));
        }else{
            resultJson.put("refresh_time",null);
        }
        return ResultJson.succResultJson(resultList);
    }

    @Override
    public JSONObject getArrangDetail(Integer libraryId, Integer departmentId, Integer type) throws Exception {
        Integer companyId = LoginUser.getUser().getCompanyID();
        logger.info("【getArrangDetail】当前的企业id:" + companyId);
        String corpId = LoginUser.getUser().getCorpID();
        Map<Integer, String> remindedDeptMap = new HashMap<>();
        //部门
        List<StudyRemindDeptVO> resultList = new ArrayList<>();
        List<StudyRemindUserVO> userRemindList = new ArrayList<>();

        List<DepartmentEntity> departmentList = departmentMapper.getDeptsByCompanyId(companyId, StatusEnum.OK.getValue());
//        String accessToken = isvTicketsMapper.getIsvTicketByCompanyId(companyId).getCorpAccessToken();
//        JSONObject jsonOjbect = DingHelper.getDGDepartmentList3(accessToken, DDConstant.RECURSION_DEPT);
//        boolean isNoAuthorized = false;
//        if (jsonOjbect.getInteger("errcode").equals(50004)) {
//            isNoAuthorized = true;
//        }

        List<StudyRemindEntity> studyRemindDepartmentList = studyRemindMapper.getRemindUserIdList(companyId, libraryId, 1, StatusEnum.OK.getValue());
        List<StudyRemindEntity> studyRemindUserList = studyRemindMapper.getRemindUserIdList(companyId, libraryId, 0, StatusEnum.OK.getValue());
        Set<Integer> studtRemindDepartmentSet = new HashSet<>();
        Set<Integer> studtRemindUserSet = new HashSet<>();
        for (int i = 0; i < studyRemindDepartmentList.size(); i++) {
            Integer remindDepartmentId = studyRemindDepartmentList.get(i).getDeptId();
            studtRemindDepartmentSet.add(remindDepartmentId);
        }
        for (int i = 0; i < studyRemindUserList.size(); i++) {
            Integer remindUserId = studyRemindUserList.get(i).getUserId();
            studtRemindUserSet.add(remindUserId);
        }

        //获取所有部门信息
        for (DepartmentEntity department : departmentList) {
            Integer departId = department.getId();
            String deptName = department.getName();
            StudyRemindDeptVO studyRemindVO = new StudyRemindDeptVO();
            studyRemindVO.setDeptId(departId);
            studyRemindVO.setName(deptName);
            Integer deptId = departmentMapper.getIdByCompanyIdAndDingDeptId(companyId, department.getParentId());
            studyRemindVO.setParentId(deptId);
            if (studtRemindDepartmentSet.contains(departId)) {
                studyRemindVO.setIsRemind(1);
            } else {
                studyRemindVO.setIsRemind(0);
            }
            resultList.add(studyRemindVO);
        }

        //获取所有用户的信息
        if (type == 1) {
            StudyRemindUserVO studyRemindUserVO = null;
            List<UserXDeptEntity> userXDeptList = userXDeptMapper.getUserInDept(corpId, departmentId, StatusEnum.OK.getValue());
            for (UserXDeptEntity userXDept : userXDeptList) {
                Integer userId = userXDept.getUserId();
                String nickName = userMapper.getNameById(userXDept.getUserId());
                studyRemindUserVO = new StudyRemindUserVO();
                studyRemindUserVO.setUserId(userId);
                studyRemindUserVO.setName(nickName);
                studyRemindUserVO.setDeptId(departmentId);
                if (studtRemindUserSet.contains(userId)) studyRemindUserVO.setIsRemind(1);
                else studyRemindUserVO.setIsRemind(0);
                userRemindList.add(studyRemindUserVO);
            }
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("department", resultList);
        if (type == 1) {
            resultMap.put("userlist", userRemindList);
        }
        return ResultJson.succResultJson(resultMap);
    }

    @Override
    public JSONObject search(Integer libraryId, String search) {
        Integer companyId = LoginUser.getUser().getCompanyID();
        String corpId = LoginUser.getUser().getCorpID();
        List<Integer> studyRemindUserIdList = studyRemindMapper.getRemindUserIds(companyId, libraryId, StatusEnum.OK.getValue());
        Set<Integer> userIdSet = new HashSet<>(studyRemindUserIdList);
        List<UserVO2> userList = userXCompanyMapper.getUserByName(corpId, search);
        for (int i = 0; i < userList.size(); i++) {
            UserVO2 userVO = userList.get(i);
            String deptids = userXDeptMapper.getUserDeptids(corpId, userVO.getId());
            if (userIdSet.contains(userVO.getId())) {
                userVO.setStatus(StatusEnum.OK.getValue());
            }
            userVO.setDeptIds(deptids);
        }
        return ResultJson.succResultJson(userList);
    }

    @Override
    public StudyRemindEntity getByCondition(Integer companyId, Integer libraryId, Integer userId) {
        return studyRemindMapper.getByCondition(companyId, libraryId, null, userId);
    }

    @Override
    public Integer batchInsertStudyRemind(List<StudyRemindEntity> studyRemindList) {
        return studyRemindMapper.batchInsertStudyRemind(studyRemindList);
    }

    @Override
    public Integer batchUpdateStudyRemind(List<StudyRemindEntity> studyRemindList) {
        return studyRemindMapper.batchUpdateStudyRemind(studyRemindList);
    }

    @Override
    public List<Integer> getRemindUserIds(Integer companyId, Integer libraryId, Integer status) {
        return studyRemindMapper.getRemindUserIds(companyId, libraryId, status);
    }


    public static void main(String[] args) {
//        OAMessageUtil.sendOAMessageWithStroageToALL(1, "sss", new OAMessageAll().getOAMessageWithPic("OA", "www.baidu.com", "www.baidu.com", OAMessageUtil.getMessageContent("sss"), "www.a.jpg"));
    }

}
