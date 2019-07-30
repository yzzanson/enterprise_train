package com.enterprise.service.question.impl;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.Page;
import com.enterprise.base.common.PageEntity;
import com.enterprise.base.common.ResultJson;
import com.enterprise.base.entity.QuestionLabelEntity;
import com.enterprise.base.enums.StatusEnum;
import com.enterprise.mapper.questions.QuestionLabelMapper;
import com.enterprise.mapper.questions.QuestionsMapper;
import com.enterprise.service.question.QuestionLabelService;
import com.enterprise.util.AssertUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/6/6 下午2:25
 */
@Service
public class QuestionLabelServiceImpl implements QuestionLabelService {

    @Resource
    private QuestionLabelMapper questionLabelMapper;

    @Resource
    private QuestionsMapper questionsMapper;

    @Override
    public Integer createOrUpdateQuestionLabel(QuestionLabelEntity questionLabelEntity) {
        if (questionLabelEntity.getId() == null || (questionLabelEntity.getId() != null && questionLabelEntity.getId().equals(0))) {
            questionLabelEntity.setCreateTime(new Date());
            questionLabelEntity.setUpdateTime(new Date());
            questionLabelEntity.setStatus(StatusEnum.OK.getValue());
            return questionLabelMapper.createQuestionLabel(questionLabelEntity);
        } else {
            if(questionLabelEntity.getStatus().equals(StatusEnum.DELETE.getValue())){
                //如果该标签已经被使用则不能移除
                Integer labelQuestionCount = questionsMapper.getQuestionCountByLabelId(questionLabelEntity.getId());
                AssertUtil.isTrue(labelQuestionCount<=0, "该标签已经被使用,无法删除");
                //删除所有具有该标签的题目的标签属性
                questionsMapper.removeLabels(Arrays.asList(questionLabelEntity.getId()));
            }
            questionLabelEntity.setUpdateTime(new Date());
            return questionLabelMapper.updateQuestionLabel(questionLabelEntity);
        }
    }

    @Override
    public JSONObject getQuestionLabelById(Integer id) {
        return ResultJson.succResultJson(questionLabelMapper.getQuestionLabelById(id));
    }

    @Override
    public JSONObject getQuestionLabelsByLibraryId(Integer libraryId, String name,PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPageNo(), pageEntity.getPageSize());
        PageInfo<QuestionLabelEntity> pageInfo = new PageInfo<>(questionLabelMapper.getQuestionLabelsByLibrary(libraryId,name));
        Map<String, Object> dataMap = Maps.newHashMap();
        dataMap.put("list", pageInfo.getList());
        dataMap.put("total", pageInfo.getTotal());
        dataMap.put("page", new Page(pageInfo.getPrePage(), pageEntity.getPageNo(), pageInfo.getNextPage(), pageInfo.getLastPage()));
        return ResultJson.succResultJson(dataMap);
    }

    @Override
    public Map<Integer, String> getLabelCollectionByLibraryId(Integer libraryId) {
        Map<Integer,String> libraryLabelMap = new HashMap<>();
        List<QuestionLabelEntity> questionLabelList = questionLabelMapper.getQuestionLabelsByLibraryId(libraryId);
        for(QuestionLabelEntity questionLabelEntity:questionLabelList){
            if(!libraryLabelMap.containsKey(questionLabelEntity.getId())){
                libraryLabelMap.put(questionLabelEntity.getId(),questionLabelEntity.getLabelName());
            }
        }
        return libraryLabelMap;
    }

    @Override
    public Map<String, Integer> getLabelCollectionByLibraryId2(Integer libraryId) {
        Map<String,Integer> libraryLabelMap = new HashMap<>();
        List<QuestionLabelEntity> questionLabelList = questionLabelMapper.getQuestionLabelsByLibraryId(libraryId);
        for(QuestionLabelEntity questionLabelEntity:questionLabelList){
            if(!libraryLabelMap.containsKey(questionLabelEntity.getLabelName())){
                libraryLabelMap.put(questionLabelEntity.getLabelName(),questionLabelEntity.getId());
            }
        }
        return libraryLabelMap;
    }

    @Override
    public List<QuestionLabelEntity> getQuestionLabelsByLibraryId(Integer libraryId) {
        return questionLabelMapper.getQuestionLabelsByLibraryId(libraryId);
    }

    @Override
    public String getLabelNameById(Integer id) {
        return questionLabelMapper.getLabelNameById(id);
    }

    @Override
    public Integer batchSafeDelete(String labelIds) {
        String[] labelIdArr =labelIds.split(",");
        List<Integer> questionLabelList = new ArrayList<>();
        for(String labelId:labelIdArr){
            questionLabelList.add(Integer.valueOf(labelId));
        }
        Integer reuslt = questionLabelMapper.batchSafeDelete(questionLabelList);
        if(reuslt>0){
            Integer questionUpdateReuslt = questionsMapper.removeLabels(questionLabelList);
            return reuslt;
        }
        return 0;
    }

    public static void main(String[] args) {
//        List<Integer> ss = new ArrayList<Integer>(1);
//        List<Integer> ss = Arrays.asList(1);
//        for (Integer sss:ss){
//            System.out.println(sss);
//        }
        Integer labelQuestionCount = 3;
        AssertUtil.isTrue(labelQuestionCount<=0, "该标签已经被使用,无法删除");
        System.out.println("sss");
    }
}
