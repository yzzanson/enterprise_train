package com.enterprise.service.question;

import com.enterprise.base.entity.QuestionLibraryTitleEntity;

/**
 * @Description UserService
 * @Author anson
 * @Date 2018/3/26 下午14:47
 */
public interface QuestionLibraryTitleService {

    Integer createOrUpdateQuestionLibraryTitle(QuestionLibraryTitleEntity questionLibraryTitleEntity);

    QuestionLibraryTitleEntity getQuestionTitleByLibrary(Integer libraryId);

}

