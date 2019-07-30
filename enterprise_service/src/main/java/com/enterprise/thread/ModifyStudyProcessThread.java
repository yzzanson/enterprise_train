package com.enterprise.thread;

import com.enterprise.base.common.SpringContextHolder;
import com.enterprise.base.entity.UserXLibraryEntity;
import com.enterprise.base.enums.StatusEnum;
import com.enterprise.mapper.questions.QuestionsMapper;
import com.enterprise.mapper.questions.UserXQuestionsMapper;
import com.enterprise.mapper.users.UserXLibraryMapper;
import com.enterprise.oa.OAMessageUtil;
import com.enterprise.util.QuestionUtil;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * SendMsgThread
 *
 * @author shisan
 * @create 2018-03-09 下午3:57
 **/
public class ModifyStudyProcessThread implements Runnable {

    private String MESSAGE_CONTENT = "此次问题id:%s,共更新%s条记录";

    private Integer libraryId;

    private Integer questionId;

    private Integer companyId;

    public ModifyStudyProcessThread(Integer companyId,Integer libraryId, Integer questionId) {
        this.companyId = companyId;
        this.libraryId = libraryId;
        this.questionId = questionId;
    }

    @Override
    public void run() {
        Integer totalUpdateCount = 0;
        UserXQuestionsMapper userXQuestionsMapper = SpringContextHolder.getBean(UserXQuestionsMapper.class);
        UserXLibraryMapper userXLibraryMapper = SpringContextHolder.getBean(UserXLibraryMapper.class);
        QuestionsMapper questionsMapper = SpringContextHolder.getBean(QuestionsMapper.class);
        //user_x_question状态为2则为重新学习的题
        Integer batchUpdateResult = userXQuestionsMapper.batchUpdateStatus(questionId, StatusEnum.HOLD_ON.getValue());
        if (batchUpdateResult > 0) {
            List<UserXLibraryEntity> userXLibraryEntityList = userXLibraryMapper.getByLibraryId(libraryId,StatusEnum.OK.getValue());
            for (UserXLibraryEntity userXLibraryEntity : userXLibraryEntityList) {
                Integer correctAnsweredQuestionNumber = userXQuestionsMapper.getTotalCorrectCountByLibraryId(companyId,libraryId, userXLibraryEntity.getUserId());
                Integer totoalCountInLibrary = questionsMapper.getTotalCountByLibraryId(libraryId);
                System.out.println("userid:"+userXLibraryEntity.getUserId()+"  所有题目数:" +totoalCountInLibrary +" 所有正确题目数:"+correctAnsweredQuestionNumber);

                //回答过的题进度不增加
                BigDecimal schedule  = new BigDecimal(QuestionUtil.getPercentDivision(correctAnsweredQuestionNumber, totoalCountInLibrary));
                System.out.println("userid:"+userXLibraryEntity.getUserId()+"  正确率:"+schedule);
                Integer updateResult = userXLibraryMapper.updateSchedule(new UserXLibraryEntity(userXLibraryEntity.getId(),schedule,new Date()));
                if(updateResult>0){
                    totalUpdateCount++;
                }
            }
        }
        String messageContnt = getMessageContent(questionId,totalUpdateCount);
        OAMessageUtil.sendTextMsgToDept(messageContnt);
    }

    private String getMessageContent(Integer questionId,Integer totalCount){
        StringBuffer sb = new StringBuffer();
        sb.append("问题id："+questionId+"\n");
        sb.append("更新数量："+totalCount+"\n");
        sb.append("充值时间："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"\n");
        return sb.toString();
    }


}
