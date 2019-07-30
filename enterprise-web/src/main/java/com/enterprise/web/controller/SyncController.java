package com.enterprise.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.BaseController;
import com.enterprise.base.common.ResultJson;
import com.enterprise.base.entity.*;
import com.enterprise.mapper.questions.QuestionsLibraryMapper;
import com.enterprise.mapper.questions.QuestionsMapper;
import com.enterprise.mapper.questions.UserXQuestionsMapper;
import com.enterprise.mapper.users.UserXLibraryMapper;
import com.enterprise.service.department.DepartmentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description 同步更新修改过的表信息
 * @Author zezhouyang
 * @Date 18/6/21 上午11:17
 */
@RequestMapping("/sync")
@Controller
public class SyncController extends BaseController {

    @Resource
    private UserXLibraryMapper userXLibraryMapper;

    @Resource
    private QuestionsLibraryMapper questionLibraryMapper;

    @Resource
    private UserXQuestionsMapper userXQuestionMapper;

    @Resource
    private QuestionsMapper questionMapper;

    @Resource
    private DepartmentService departmentService;

    /**
     * userXLibrary表同步-新增companyId
     * */
    @RequestMapping("/syncUserXLibrary")
    @ResponseBody
    public JSONObject syncUserXLibrary(){
        Integer everyCount = 100;
        Integer totalCount = userXLibraryMapper.getTotalCountInDB();
        Integer loopCount = totalCount/everyCount;
        if( totalCount%everyCount>0){
            loopCount=loopCount+1;
        }
        int start = 0;
        Integer totalUpdateCount =0;
        for (int i = 0; i <= loopCount; i++) {
            List<UserXLibraryEntity> updateList = new ArrayList<>();
            start = i*everyCount;
            List<UserXLibraryEntity> userXLibraryList = userXLibraryMapper.getCountRecord(start,everyCount);
            for (int j = 0; j < userXLibraryList.size(); j++) {
                UserXLibraryEntity userXLibrary = userXLibraryList.get(j);
                Integer libraryId = userXLibrary.getLibraryId();
                if(userXLibrary.getCompanyId()==null){
                    QuestionsLibraryEntity questionLibraryEntity = questionLibraryMapper.getById(libraryId);
                    userXLibrary.setCompanyId(questionLibraryEntity.getCompanyId());
                    updateList.add(userXLibrary);
                }

            }
            if(updateList.size()>0) {
                totalUpdateCount+=updateList.size();
                userXLibraryMapper.batchUpdateNew(updateList);
            }
        }
        return ResultJson.succResultJson(totalUpdateCount);
    }



    /**
     * userXQuestions表同步-新增companyId
     * */
    @RequestMapping("/syncUserXQuestion")
    @ResponseBody
    public JSONObject syncUserXQuestion(){
        Integer everyCount = 100;
        Integer totalCount = userXQuestionMapper.getTotalCountInDB();
        Integer loopCount = totalCount/everyCount;
        if( totalCount%everyCount>0){
            loopCount=loopCount+1;
        }
        int start = 0;
        Integer totalUpdateCount =0;
        for (int i = 0; i <= loopCount; i++) {
            List<UserXQuestionsEntity> updateList = new ArrayList<>();
            start = i*everyCount;
            List<UserXQuestionsEntity> userXQuestionList = userXQuestionMapper.getCountRecord(start,everyCount);
            for (int j = 0; j < userXQuestionList.size(); j++) {
                UserXQuestionsEntity userXQuestion = userXQuestionList.get(j);
                Integer questionId = userXQuestion.getQuestionId();
                if(userXQuestion.getCompanyId()==null){
                    QuestionsEntity questionEntity = questionMapper.getById(questionId);
                    QuestionsLibraryEntity questionLibraryEntity = questionLibraryMapper.getById(questionEntity.getLibraryId());
                    userXQuestion.setCompanyId(questionLibraryEntity.getCompanyId());
                    updateList.add(userXQuestion);
                }
            }
            if(updateList.size()>0) {
                totalUpdateCount+=updateList.size();
                userXQuestionMapper.batchUpdate(updateList);
            }
        }
        return ResultJson.succResultJson(totalUpdateCount);
    }

    /**
     * 查看公司部门是否正确
     */
    @RequestMapping("/syncCompany.json")
    @ResponseBody
    public JSONObject syncCompany() throws Exception {
        return departmentService.syncCompany();
    }


    public static void main(String[] args) {
        System.out.println(4513/100);
    }
}
