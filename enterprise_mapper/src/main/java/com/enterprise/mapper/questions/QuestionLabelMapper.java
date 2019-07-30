package com.enterprise.mapper.questions;

import com.enterprise.base.entity.QuestionLabelEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by null on 2018-03-23 10:45:39
 */
public interface QuestionLabelMapper {

    Integer createQuestionLabel(QuestionLabelEntity questionLabelEntity);

    Integer updateQuestionLabel(QuestionLabelEntity questionLabelEntity);

    QuestionLabelEntity getQuestionLabelById(@Param("id") Integer id);

    /**
     * 获取题库的标签列表
     * */
    List<QuestionLabelEntity> getQuestionLabelsByLibrary(@Param("libraryId") Integer libraryId,@Param("name") String name);

    /**
     * 获取题库的标签列表
     * */
    List<QuestionLabelEntity> getQuestionLabelsByLibraryId(@Param("libraryId") Integer libraryId);

    String getLabelNameById(@Param("id") Integer id);

    /**
     * 批量删除
     * */
    Integer batchSafeDelete(@Param("labelIds") List<Integer> labelIds);


}