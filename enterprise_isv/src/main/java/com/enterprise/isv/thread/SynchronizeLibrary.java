package com.enterprise.isv.thread;

import com.enterprise.base.common.SpringContextHolder;
import com.enterprise.base.entity.IsvTicketsEntity;
import com.enterprise.base.entity.QuestionsLibraryEntity;
import com.enterprise.base.entity.StudyRemindEntity;
import com.enterprise.base.entity.UserXLibraryEntity;
import com.enterprise.base.enums.StatusEnum;
import com.enterprise.base.enums.SubjectEnum;
import com.enterprise.base.vo.UserVO;
import com.enterprise.service.isvTickets.IsvTicketsService;
import com.enterprise.service.question.QuestionsLibraryService;
import com.enterprise.service.question.UserXLibraryService;
import com.enterprise.service.remind.StudyRemindService;
import com.enterprise.service.user.UserXCompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/4/18 下午3:40
 */
public class SynchronizeLibrary implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(SynchronizeLibrary.class);

    private String corpId;

    public SynchronizeLibrary(String corpId) {
        this.corpId = corpId;
    }

    private IsvTicketsService isvTicketsService = SpringContextHolder.getBean(IsvTicketsService.class);
    private UserXCompanyService userXCompanyService = SpringContextHolder.getBean(UserXCompanyService.class);
    private UserXLibraryService userXLibraryService = SpringContextHolder.getBean(UserXLibraryService.class);
    private QuestionsLibraryService questionsLibraryService = SpringContextHolder.getBean(QuestionsLibraryService.class);
    private StudyRemindService studyRemindService = SpringContextHolder.getBean(StudyRemindService.class);

    @Override
    public void run() {
        try {
            long begin = System.currentTimeMillis();
            IsvTicketsEntity isvTicketsEntity = isvTicketsService.getIsvTicketByCorpId(corpId);
            Integer companyId = isvTicketsEntity.getCompanyId();
//            logger.info(corpId + "用户同步结束 >" + (System.currentTimeMillis() - begin) + "ms");
            //先查看公司是否有这几套题库
            //Integer companyId, Integer subject, Integer status, Integer defaultFlag
            Integer adminUserId = userXCompanyService.getAdmin(corpId);
            List<QuestionsLibraryEntity> addDefaultQuestionLibrary = new ArrayList<>();
            List<StudyRemindEntity> addStudyRemind = new ArrayList<>();
            List<StudyRemindEntity> updateStudyRemind = new ArrayList<>();
            //模板题库type=0
            List<QuestionsLibraryEntity> questionsLibraryList = questionsLibraryService.getQuestionLibrarys(new QuestionsLibraryEntity(null, SubjectEnum.MODEL.getValue(), 1, 1));
            for(QuestionsLibraryEntity questionsLibraryEntity : questionsLibraryList){
                QuestionsLibraryEntity defaultQuestionLibraryInDB = questionsLibraryService.getDefaultQuestionLibrary(companyId, questionsLibraryEntity.getId(), 1);
                if(defaultQuestionLibraryInDB==null){
                    defaultQuestionLibraryInDB = new QuestionsLibraryEntity(companyId,questionsLibraryEntity.getName(),0,1,null,adminUserId, StatusEnum.OK.getValue(),questionsLibraryEntity.getId(),new Date(),new Date(),1,questionsLibraryEntity.getSortType());
                    addDefaultQuestionLibrary.add(defaultQuestionLibraryInDB);
                }
            }
            if(addDefaultQuestionLibrary!=null){
                Integer inheritReuslt = questionsLibraryService.inheritQuestionLibrary(addDefaultQuestionLibrary);
                logger.info("同步了"+inheritReuslt+"个题库!");
            }
            //企业题库type=1
            List<QuestionsLibraryEntity> companyDefaultQuestionsLibraryList = questionsLibraryService.getQuestionLibrarys(new QuestionsLibraryEntity(companyId, 1, "", 1));
            List<UserVO> userXCompanyList = userXCompanyService.getUserByCorpId(corpId);
            List<UserXLibraryEntity> addUserXLibraryList = new ArrayList<>();
            for (UserVO userVO:userXCompanyList){
                Integer userId = userVO.getId();
                for(QuestionsLibraryEntity questionsLibraryEntity : companyDefaultQuestionsLibraryList){
                    Integer libraryId = questionsLibraryEntity.getId();
                    UserXLibraryEntity userXLibraryEntity = userXLibraryService.getByUserIdAndLibraryId(companyId,userId, libraryId);
                    if(userXLibraryEntity==null){
                        userXLibraryEntity = new UserXLibraryEntity(userId,libraryId,new BigDecimal(0),null,StatusEnum.OK.getValue(),new Date(),new Date(),companyId);
                        addUserXLibraryList.add(userXLibraryEntity);
                    }
                    StudyRemindEntity studyRemindEntity = studyRemindService.getByCondition(companyId, libraryId, userId);
                    if(studyRemindEntity==null){
                        //  public StudyRemindEntity(Integer companyId, Integer libraryId, Integer userId, Integer status, Date createTime, Date updateTime)
                        addStudyRemind.add(new StudyRemindEntity(companyId,libraryId,userId,StatusEnum.OK.getValue(),new Date(),new Date()));
                    }else if(studyRemindEntity.getStatus()!=null && studyRemindEntity.getStatus().equals(StatusEnum.DELETE.getValue())){
                        updateStudyRemind.add(new StudyRemindEntity(studyRemindEntity.getId(),StatusEnum.OK.getValue(),new Date()));
                    }
                }
            }
            if(addUserXLibraryList.size()>0){
                Integer addUserXLibraryResult = userXLibraryService.batchInsertUserXLibrary(addUserXLibraryList);
                logger.info("同步了"+addUserXLibraryResult+"个用户题库!");
            }else{
                logger.info("没有需要同步的用户题库!");
            }
            if(addStudyRemind.size()>0){
                Integer result = studyRemindService.batchInsertStudyRemind(addStudyRemind);
                logger.info("学习提醒新增了"+result+"条数据!");
            }
            if(updateStudyRemind.size()>0){
                Integer result = studyRemindService.batchUpdateStudyRemind(updateStudyRemind);
                logger.info("学习提醒更新了"+result+"条数据!");
            }
            logger.info(corpId+"用户同步结束 >" + (System.currentTimeMillis() - begin) + "ms");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
