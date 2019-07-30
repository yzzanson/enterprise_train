package com.enterprise.mapper.questions;

import com.enterprise.base.entity.QuestionLibraryTitleEntity;
import org.apache.ibatis.annotations.Param;

/**
 * Created by null on 2018-03-23 10:45:39
 */
public interface QuestionLibraryTitleMapper {

    Integer createQuestionLibraryTitle(QuestionLibraryTitleEntity questionLibraryTitleEntity);

    Integer updateQuestionLibraryTitle(QuestionLibraryTitleEntity questionLibraryTitleEntity);

    QuestionLibraryTitleEntity getQuestionTitleByLibrary(@Param("libraryId") Integer libraryId);

}