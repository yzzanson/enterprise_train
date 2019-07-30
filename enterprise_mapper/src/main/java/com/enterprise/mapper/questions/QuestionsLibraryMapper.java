package com.enterprise.mapper.questions;

import com.enterprise.base.entity.QuestionsLibraryEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by null on 2018-03-23 10:46:42
 */
public interface QuestionsLibraryMapper {

    Integer createOrUpdateQuestionLibrary(QuestionsLibraryEntity questionsLibraryEntity);

    Integer updateQuestionLibrary(QuestionsLibraryEntity questionsLibraryEntity);

    List<QuestionsLibraryEntity> findQuestionLibrary(QuestionsLibraryEntity questionsLibraryEntity);

    List<QuestionsLibraryEntity> findQuestionLibrary2(QuestionsLibraryEntity questionsLibraryEntity);

    /**
     * 查找企业题库,包括被分配的官方题库
     * */
    List<QuestionsLibraryEntity> findCompanyQuestionLibrary(QuestionsLibraryEntity questionsLibraryEntity);

    Integer useQuestionLibrary(@Param("id") Integer id);

    QuestionsLibraryEntity findById(@Param("id") Integer id);

    Integer copyQuestionLibrary(QuestionsLibraryEntity questionsLibraryEntity);

    Integer batchDelete(@Param("ids") List<Integer> ids);

    List<String> getLabelList(@Param("subject") Integer subject,@Param("companyId") Integer companyId);

    QuestionsLibraryEntity getById(@Param("id") Integer id);

    String getLibraryNameById(@Param("id") Integer id);

    /**
     * 获取默认的题库
     * */
    QuestionsLibraryEntity getDefaultQuestionLibrary(@Param("companyId") Integer companyId,@Param("parentId") Integer parentId,@Param("isDefault")  Integer isDefault);

    List<QuestionsLibraryEntity> getDefaultQuestionLibraryNoAssigned();

    Integer updateUseCount(@Param("id") Integer id,@Param("useCount") Integer useCount);

    Integer addUseCount(@Param("id") Integer id,@Param("useCount") Integer useCount);

    /**
     * 获取当前企业题库数目
     * */
    Integer getQuesitonLibraryCount(@Param("companyId") Integer companyId,@Param("subject") Integer subject);
}