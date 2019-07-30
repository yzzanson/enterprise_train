package com.enterprise.service.question;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.PageEntity;
import com.enterprise.base.entity.QuestionLabelEntity;

import java.util.List;
import java.util.Map;

/**
 * @Description UserService
 * @Author anson
 * @Date 2018/3/26 下午14:47
 */
public interface QuestionLabelService {

    Integer createOrUpdateQuestionLabel(QuestionLabelEntity questionLabelEntity);

    JSONObject getQuestionLabelById(Integer id);

    JSONObject getQuestionLabelsByLibraryId(Integer libraryId, String name,PageEntity pageEntity);

    Map<Integer,String> getLabelCollectionByLibraryId(Integer libraryId);

    Map<String,Integer> getLabelCollectionByLibraryId2(Integer libraryId);

    List<QuestionLabelEntity> getQuestionLabelsByLibraryId(Integer libraryId);

    String getLabelNameById(Integer id);

    Integer batchSafeDelete(String labelIds);
}

