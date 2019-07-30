package com.enterprise.service.question.impl;

import com.enterprise.base.common.RedisConstant;
import com.enterprise.base.entity.QuestionLibraryTitleEntity;
import com.enterprise.base.enums.StatusEnum;
import com.enterprise.mapper.questions.QuestionLibraryTitleMapper;
import com.enterprise.service.question.QuestionLibraryTitleService;
import com.enterprise.service.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/6/6 下午2:25
 */
@Service
public class QuestionLibraryTitleServiceImpl implements QuestionLibraryTitleService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private QuestionLibraryTitleMapper questionLibraryTitleMapper;

    @Resource
    private RedisService redisService;

    private static String QUESTION_LIBRARY_TITLE_KEY = RedisConstant.QUESTION_LIBRARY_TITLE_KEY;

    private Long expireTime = RedisConstant.EXPIRE_TIME_COMMON;

    @Override
    public Integer createOrUpdateQuestionLibraryTitle(QuestionLibraryTitleEntity questionLibraryTitleEntity) {
        if (questionLibraryTitleEntity.getId() == null) {
            QuestionLibraryTitleEntity questionLibraryTitleInDB =  questionLibraryTitleMapper.getQuestionTitleByLibrary(questionLibraryTitleEntity.getLibraryId());
            questionLibraryTitleEntity.setStatus(1);
            if(questionLibraryTitleInDB==null && StringUtil.isNotEmpty(questionLibraryTitleEntity.getTitle())){

                Integer result = questionLibraryTitleMapper.createQuestionLibraryTitle(questionLibraryTitleEntity);
                String key = QUESTION_LIBRARY_TITLE_KEY + "id_" + questionLibraryTitleEntity.getLibraryId();
                redisService.setSerialize(key, expireTime ,questionLibraryTitleEntity);
                return result;
            }else{
                if(StringUtil.isEmpty(questionLibraryTitleEntity.getTitle())){
                    questionLibraryTitleInDB.setStatus(StatusEnum.DELETE.getValue());
                }else{
                    questionLibraryTitleInDB.setStatus(StatusEnum.OK.getValue());
                }
                questionLibraryTitleInDB.setTitle(questionLibraryTitleEntity.getTitle());
                questionLibraryTitleInDB.setUpdateTime(new Date());
                Integer result = questionLibraryTitleMapper.updateQuestionLibraryTitle(questionLibraryTitleInDB);
                questionLibraryTitleInDB =  questionLibraryTitleMapper.getQuestionTitleByLibrary(questionLibraryTitleEntity.getLibraryId());
                String key = QUESTION_LIBRARY_TITLE_KEY + "id_" + questionLibraryTitleEntity.getLibraryId();
                redisService.setSerialize(key, expireTime,questionLibraryTitleInDB);
                return result;
            }
        } else {
            if(StringUtil.isEmpty(questionLibraryTitleEntity.getTitle())){
                questionLibraryTitleEntity.setStatus(StatusEnum.DELETE.getValue());
            }
            questionLibraryTitleEntity.setTitle(questionLibraryTitleEntity.getTitle());
            questionLibraryTitleEntity.setUpdateTime(new Date());

            Integer result =questionLibraryTitleMapper.updateQuestionLibraryTitle(questionLibraryTitleEntity);
            QuestionLibraryTitleEntity questionLibraryTitleInDB =  questionLibraryTitleMapper.getQuestionTitleByLibrary(questionLibraryTitleEntity.getLibraryId());
            String key = QUESTION_LIBRARY_TITLE_KEY + "id_" + questionLibraryTitleEntity.getId();
            redisService.setSerialize(key, expireTime,questionLibraryTitleInDB);
            return result;
        }
    }


    @Override
//    @GetCache(name="questionlibrarytitle",value="id")
    public QuestionLibraryTitleEntity getQuestionTitleByLibrary(Integer id) {
        String key = QUESTION_LIBRARY_TITLE_KEY + "id_" + id;
        QuestionLibraryTitleEntity result = (QuestionLibraryTitleEntity) redisService.getSerializeObj(key);
        if(result!=null){
            return result;
        }
        result = questionLibraryTitleMapper.getQuestionTitleByLibrary(id);
        redisService.setSerialize(key,expireTime,result);
        return result;
    }
}
