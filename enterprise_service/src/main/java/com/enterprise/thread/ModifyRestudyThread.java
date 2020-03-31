package com.enterprise.thread;

import com.enterprise.base.common.SpringContextHolder;
import com.enterprise.base.entity.UserXLibraryEntity;
import com.enterprise.base.entity.UserXQuestionsEntity;
import com.enterprise.mapper.questions.QuestionsMapper;
import com.enterprise.mapper.questions.UserXQuestionsMapper;
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
public class ModifyRestudyThread implements Runnable {

    private Integer libraryId;

    private List<UserXQuestionsEntity> userXQuestionList;

    public ModifyRestudyThread() {
    }

    public ModifyRestudyThread(Integer libraryId, List<UserXQuestionsEntity> userXQuestionList) {
        this.libraryId = libraryId;
        this.userXQuestionList = userXQuestionList;
    }

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void run() {
        //将该用户下的所有公司的题库进度重算
        UserXLibraryMapper userXLibraryMapper = SpringContextHolder.getBean(UserXLibraryMapper.class);
        UserXQuestionsMapper userXQuestionsMapper = SpringContextHolder.getBean(UserXQuestionsMapper.class);
        QuestionsMapper questionsMapper = SpringContextHolder.getBean(QuestionsMapper.class);

        List<UserXLibraryEntity> updateList = new ArrayList<>();
        Set<Integer> updatePublicLibraryIdSet = new HashSet<>();

        //将所有用户题库学习进度回答问题重算
        for (int i = 0; i < userXQuestionList.size(); i++) {
            UserXQuestionsEntity userXQuestionEntity = userXQuestionList.get(i);
            Integer companyId = userXQuestionEntity.getCompanyId();
            Integer userId = userXQuestionEntity.getUserId();
            UserXLibraryEntity userXLibraryInDB = userXLibraryMapper.getByUserIdAndLibraryId(companyId, userId, libraryId);
            if (userXLibraryInDB != null && !updatePublicLibraryIdSet.contains(userXLibraryInDB.getId())) {
                Integer correctAnsweredQuestionNumber = userXQuestionsMapper.getUserCorrectQuestionsByLibraryId(companyId, userId, libraryId);
                Integer totoalCountInLibrary = questionsMapper.getTotalCountByLibraryId(libraryId);
                BigDecimal schedule = new BigDecimal(0);
                if (totoalCountInLibrary > 0) {
                    schedule = new BigDecimal(QuestionUtil.getPercentDivision(correctAnsweredQuestionNumber, totoalCountInLibrary));
                }
//                if(!userXLibraryInDB.getSchedule().equals(100)){
                userXLibraryInDB.setFinishTime(null);
//                }
                userXLibraryInDB.setAnswerCount(correctAnsweredQuestionNumber);
                userXLibraryInDB.setSchedule(schedule);
                userXLibraryInDB.setUpdateTime(new Date());
                updateList.add(userXLibraryInDB);
                updatePublicLibraryIdSet.add(userXLibraryInDB.getId());
            }
        }

        //更新数据库
        if (CollectionUtils.isNotEmpty(updateList)) {
            userXLibraryMapper.batchUpdateNew(updateList);
        }
        logger.info("题库" + libraryId + "重新学习更新了" + updateList.size() + "条数据!");
    }
}
