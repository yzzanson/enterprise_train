package com.enterprise.service.question.impl;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.Page;
import com.enterprise.base.common.PageEntity;
import com.enterprise.base.common.ResultJson;
import com.enterprise.base.entity.QuestionsFeedbackEntity;
import com.enterprise.base.vo.QuestionFeedbackVO;
import com.enterprise.mapper.questions.QuestionsFeedbackMapper;
import com.enterprise.mapper.users.UserMapper;
import com.enterprise.service.question.QuestionsFeedbackService;
import com.enterprise.util.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by anson on 18/3/28.
 */
@Service
public class QuestionsFeedbackServiceImpl implements QuestionsFeedbackService {

    @Resource
    private QuestionsFeedbackMapper questionsFeedbackMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    public Integer createOrUpdateQuestionFeedback(QuestionsFeedbackEntity questionsFeedbackEntity) {
        return questionsFeedbackMapper.createOrUpdateQuestionFeedback(questionsFeedbackEntity);
    }

    @Override
    public Integer doUpdate(Integer id, Integer userId,Integer status) {
        return questionsFeedbackMapper.doUpdate(id,userId,status);
    }

    @Override
    public JSONObject findFeedbackList(Integer questionId, PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPageNo(), pageEntity.getPageSize());
//        List<QuestionsFeedbackEntity> questionsFeedbackList = questionsFeedbackMapper.findFeedbackList(questionId);
        PageInfo<QuestionsFeedbackEntity> pageInfo = new PageInfo<>(questionsFeedbackMapper.findFeedbackList(questionId));
        List<QuestionFeedbackVO> questionFeedbackVOList = new ArrayList<>();
        for (QuestionsFeedbackEntity questionsFeedbackEntity : pageInfo.getList()){
            QuestionFeedbackVO questionFeedbackVO = new QuestionFeedbackVO();
            try {
                BeanUtils.copyProperties(questionFeedbackVO, questionsFeedbackEntity);
                String userName = userMapper.getNameById(questionsFeedbackEntity.getUserId());
                questionFeedbackVO.setUserName(userName);
                questionFeedbackVO.setFeedbackTime(DateUtil.getDateYMD(questionsFeedbackEntity.getCreateTime()));
                String readState="未读";
                String handleState = "处理它";
                if(questionsFeedbackEntity.getStatus().equals(2)){
                    readState = "已读";
                }else if(questionsFeedbackEntity.getStatus().equals(3)){
                    readState = "已读";
                    handleState = "已处理";
                }
                questionFeedbackVO.setReadState(readState);
                questionFeedbackVO.setHandleState(handleState);
                questionFeedbackVOList.add(questionFeedbackVO);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        Map<String, Object> dataMap = Maps.newHashMap();
        dataMap.put("list", questionFeedbackVOList);
        dataMap.put("total", pageInfo.getTotal());
        dataMap.put("page", new Page(pageInfo.getPrePage(), pageEntity.getPageNo(), pageInfo.getNextPage(), pageInfo.getLastPage()));
        return ResultJson.succResultJson(dataMap);
    }
}
