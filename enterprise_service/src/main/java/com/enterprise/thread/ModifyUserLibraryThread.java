package com.enterprise.thread;

import com.enterprise.base.common.SpringContextHolder;
import com.enterprise.base.entity.*;
import com.enterprise.base.enums.StatusEnum;
import com.enterprise.base.enums.SubjectEnum;
import com.enterprise.base.vo.studyRemind.StudyRemindVO;
import com.enterprise.mapper.companyXLibrary.CompanyXLibraryMapper;
import com.enterprise.mapper.department.DepartmentMapper;
import com.enterprise.mapper.isvTickets.IsvTicketsMapper;
import com.enterprise.mapper.questions.QuestionsLibraryMapper;
import com.enterprise.mapper.questions.QuestionsMapper;
import com.enterprise.mapper.questions.UserXQuestionsMapper;
import com.enterprise.mapper.remind.StudyRemindMapper;
import com.enterprise.mapper.users.UserXDeptMapper;
import com.enterprise.mapper.users.UserXLibraryMapper;
import com.enterprise.util.QuestionUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Description 进入系统时更新用户的学习进度
 * @Author zezhouyang
 * @Date 19/1/16 下午5:58
 */
public class ModifyUserLibraryThread implements Runnable {

    private Integer companyId;

    private Integer userId;

    public ModifyUserLibraryThread() {
    }

    public ModifyUserLibraryThread(Integer companyId, Integer userId) {
        this.companyId = companyId;
        this.userId = userId;
    }

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void run() {
        //将该用户下的所有公司的题库进度重算
        UserXLibraryMapper userXLibraryMapper = SpringContextHolder.getBean(UserXLibraryMapper.class);
        CompanyXLibraryMapper companyXLibraryMapper = SpringContextHolder.getBean(CompanyXLibraryMapper.class);
        QuestionsLibraryMapper questionsLibraryMapper = SpringContextHolder.getBean(QuestionsLibraryMapper.class);
        UserXQuestionsMapper userXQuestionsMapper = SpringContextHolder.getBean(UserXQuestionsMapper.class);
        QuestionsMapper questionsMapper = SpringContextHolder.getBean(QuestionsMapper.class);
        UserXDeptMapper userXDeptMapper = SpringContextHolder.getBean(UserXDeptMapper.class);
        IsvTicketsMapper isvTicketMapper = SpringContextHolder.getBean(IsvTicketsMapper.class);
        StudyRemindMapper studyRemindMapper = SpringContextHolder.getBean(StudyRemindMapper.class);

        List<UserXLibraryEntity> updateList = new ArrayList<>();
        List<UserXLibraryEntity> insertList = new ArrayList<>();

        //将所有企业题库重新计算
        Set<Integer> publicLibraryIdSet = new HashSet<>();
        Set<Integer> updatePublicLibraryIdSet = new HashSet<>();

        Set<Integer> userDeptIdSet = new HashSet<>();
        String corpId = isvTicketMapper.getIsvTicketByCompanyId(companyId).getCorpId();
        //获取用户的部门
        List<UserXDeptEntity> userDepts = userXDeptMapper.getUserDepts(corpId,userId,1);
        for (int i = 0; i < userDepts.size(); i++) {
            userDeptIdSet.add(userDepts.get(i).getDeptId());
        }

        //将所有默认官方题库重算
        List<Integer> userXLibraryList = userXLibraryMapper.getByCompanyIdAndUserId(companyId, userId, null);
        Set<Integer> userLibrarySet = new HashSet(Arrays.asList(userXLibraryMapper));

        List<QuestionsLibraryEntity> enterpriseQuestionLibraryList = questionsLibraryMapper.findQuestionLibrary2(new QuestionsLibraryEntity(StatusEnum.OK.getValue(), SubjectEnum.ENTERPRISE.getValue(), "", companyId));
        for (int i = 0; i < enterpriseQuestionLibraryList.size(); i++) {
            Integer libraryId = enterpriseQuestionLibraryList.get(i).getId();
            if(userLibrarySet.contains(libraryId)){continue;}
            //获取该题库的所有部门
            List<StudyRemindVO> studyRemindDeptList = studyRemindMapper.getListByCompanyIdAndLibraryIdAndType(companyId,libraryId,0);
            if(CollectionUtils.isNotEmpty(studyRemindDeptList)) {
                Set<Integer> remindDeptIds = getApplyDepartmentIdSet(companyId, studyRemindDeptList);
                for (Integer userDeptId : userDeptIdSet) {
                    if (remindDeptIds.contains(userDeptId)) {
                        userLibrarySet.add(libraryId);
                        UserXLibraryEntity userXLibraryEntity = new UserXLibraryEntity(companyId, userId, libraryId, StatusEnum.OK.getValue(), new Date());
                        insertList.add(userXLibraryEntity);
                        break;
                    }
                }
            }
            StudyRemindEntity studyRemindEntity = studyRemindMapper.getByCondition(companyId,libraryId,null,userId);
            if(studyRemindEntity!=null && studyRemindEntity.getStatus().equals(1) && !userLibrarySet.contains(libraryId)){
                UserXLibraryEntity userXLibraryEntity = new UserXLibraryEntity(companyId, userId, libraryId, StatusEnum.OK.getValue(), new Date());
                insertList.add(userXLibraryEntity);
            }else if(studyRemindEntity!=null && studyRemindEntity.getStatus().equals(1) && userLibrarySet.contains(libraryId)){
                UserXLibraryEntity userXLibraryEntity = userXLibraryMapper.getByUserIdAndLibraryId(companyId, userId, libraryId);
                userXLibraryEntity.setStatus(1);
                updateList.add(userXLibraryEntity);
            }
        }


        //全选企业的默认官方题库和选中该企业的默认官方题库
        List<Integer> publicLibraryList = companyXLibraryMapper.getDefaultPublicLibrary(companyId);
        for (int i = 0; i < publicLibraryList.size(); i++) {
            Integer id = publicLibraryList.get(i);
            publicLibraryIdSet.add(id);
        }
        //user_x_library是否有,官方题库删除的增加,否则
        for (int i = 0; i < userXLibraryList.size(); i++) {
            Integer libraryId = userXLibraryList.get(i);
            QuestionsLibraryEntity questionLibraryEntity = questionsLibraryMapper.getById(libraryId);
            if(questionLibraryEntity!=null) {
                logger.info(questionLibraryEntity.toString());
                Integer subject = questionLibraryEntity.getSubject();
                Integer status = questionLibraryEntity.getStatus();
                if (subject.equals(SubjectEnum.ENTERPRISE.getValue()) && status.equals(StatusEnum.OK.getValue())) {
                    updateList.add(new UserXLibraryEntity(companyId, userId, libraryId, StatusEnum.OK.getValue(), new Date()));
                } else if (subject.equals(SubjectEnum.PUBLIC.getValue())) {
                    if (publicLibraryIdSet.contains(libraryId)) {
                        updateList.add(new UserXLibraryEntity(companyId, userId, libraryId, StatusEnum.OK.getValue(), new Date()));
                        updatePublicLibraryIdSet.add(libraryId);
                    } else {
                        updateList.add(new UserXLibraryEntity(companyId, userId, libraryId, StatusEnum.DELETE.getValue(), new Date()));
                        updatePublicLibraryIdSet.add(libraryId);
                    }
                }
            }
        }

        for (int i = 0; i < publicLibraryList.size(); i++) {
            Integer libraryId = publicLibraryList.get(i);
            if (!updatePublicLibraryIdSet.contains(libraryId)) {

                UserXLibraryEntity userXLibraryEntity = new UserXLibraryEntity(companyId, userId, libraryId, StatusEnum.OK.getValue(), new Date());
                insertList.add(userXLibraryEntity);
            } else {
                continue;
            }

        }

        List<UserXLibraryEntity> insertLibraryList = new ArrayList<>();
        List<UserXLibraryEntity> updateLibraryList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(updateList)) {
            for (int i = 0; i < updateList.size(); i++) {
                UserXLibraryEntity userXLibraryEntity = updateList.get(i);
                Integer comapnyId = userXLibraryEntity.getCompanyId();
                Integer userId = userXLibraryEntity.getUserId();
                Integer libraryId = userXLibraryEntity.getLibraryId();
                UserXLibraryEntity userXLibraryInDB = userXLibraryMapper.getByUserIdAndLibraryId(comapnyId, userId, libraryId);
                //没删除的重新计算学习进度
                if (!userXLibraryEntity.getStatus().equals(StatusEnum.DELETE.getValue())) {
                    Integer correctAnsweredQuestionNumber = userXQuestionsMapper.getUserCorrectQuestionsByLibraryId(companyId, userId, libraryId);
                    Integer totoalCountInLibrary = questionsMapper.getTotalCountByLibraryId(libraryId);
                    BigDecimal schedule = new BigDecimal(0);
                    if (totoalCountInLibrary > 0) {
                        schedule = new BigDecimal(QuestionUtil.getPercentDivision(correctAnsweredQuestionNumber, totoalCountInLibrary));
                        userXLibraryInDB.setAnswerCount(correctAnsweredQuestionNumber);
                        userXLibraryInDB.setSchedule(schedule);
                        userXLibraryInDB.setFinishTime(null);
                        updateLibraryList.add(userXLibraryInDB);
                    }
                } else {
                    userXLibraryInDB.setStatus(StatusEnum.DELETE.getValue());
                    userXLibraryInDB.setFinishTime(null);
                    updateLibraryList.add(userXLibraryInDB);
                }
            }
            userXLibraryMapper.batchUpdateNew(updateLibraryList);
        }

        Integer updateCount = updateLibraryList.size();

        updateLibraryList.clear();
        if (CollectionUtils.isNotEmpty(insertList)) {
            BigDecimal schedule = new BigDecimal(0);
            for (int i = 0; i < insertList.size(); i++) {
                UserXLibraryEntity userXLibraryEntity = insertList.get(i);
                Integer comapnyId = userXLibraryEntity.getCompanyId();
                Integer userId = userXLibraryEntity.getUserId();
                Integer libraryId = userXLibraryEntity.getLibraryId();
                UserXLibraryEntity userXLibraryInDB = userXLibraryMapper.getByUserIdAndLibraryId(comapnyId, userId, libraryId);
                Integer correctAnsweredQuestionNumber = userXQuestionsMapper.getUserCorrectQuestionsByLibraryId(companyId, userId, libraryId);
                Integer totoalCountInLibrary = questionsMapper.getTotalCountByLibraryId(libraryId);
                schedule = new BigDecimal(QuestionUtil.getPercentDivision(correctAnsweredQuestionNumber, totoalCountInLibrary));
                if (userXLibraryInDB != null) {
                    userXLibraryInDB.setSchedule(schedule);
                    userXLibraryInDB.setStatus(StatusEnum.OK.getValue());
                    userXLibraryInDB.setAnswerCount(correctAnsweredQuestionNumber);
                    userXLibraryInDB.setFinishTime(null);
                    updateLibraryList.add(userXLibraryInDB);
                } else {
                    //public UserXLibraryEntity(Integer companyId, Integer userId, Integer libraryId,
                    // BigDecimal schedule, Integer answerCount, Date lastAnswerTime, Integer isUpdate,
                    // Integer status, Date createTime, Date updateTime)
                    UserXLibraryEntity userXLibraryForSave = new UserXLibraryEntity(comapnyId, userId, libraryId, schedule, correctAnsweredQuestionNumber, null, null, StatusEnum.OK.getValue(), new Date(), new Date());
                    insertLibraryList.add(userXLibraryForSave);
                }
            }
            if (CollectionUtils.isNotEmpty(updateLibraryList)) {
                userXLibraryMapper.batchUpdateNew(updateLibraryList);
            }
            if (CollectionUtils.isNotEmpty(insertLibraryList)) {
                userXLibraryMapper.batchInsertuserxlibrary(insertLibraryList);
            }
        }
        String message = "companyId:" + companyId + ",userId" + userId + "更新了" + updateCount + "条记录,插入了" + insertLibraryList.size() + "条记录";
    }

    private Set<Integer> getApplyDepartmentIdSet(Integer companyId,List<StudyRemindVO> studyRemindDeptList){
        Set<Integer> departmentIdSet = new HashSet<>();
        DepartmentMapper departmentMapper = SpringContextHolder.getBean(DepartmentMapper.class);
        for (int i = 0; i < studyRemindDeptList.size(); i++) {
            Integer departmentId = studyRemindDeptList.get(i).getDeptid();
            DepartmentEntity deprmentInDB = departmentMapper.getDepartmentById(departmentId);
            if (deprmentInDB != null && deprmentInDB.getStatus() != null && deprmentInDB.getStatus().equals(1)) {
                departmentIdSet.add(departmentId);
                getChildDepartmentId(departmentIdSet, companyId, departmentId);
            } else {
                continue;
            }
        }
        return departmentIdSet;
    }


    private void getChildDepartmentId(Set<Integer> addRemindDeptIdSet, Integer companyId, Integer deparmtmentId) {
        DepartmentMapper departmentMapper = SpringContextHolder.getBean(DepartmentMapper.class);
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

    public static void main(String[] args) {
        for (int i = 0; i < 10 ; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.println("i:"+i+",j"+j);
                if(i==j){
                    break;
                }
            }
        }
    }

}
