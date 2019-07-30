package com.enterprise.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.BaseController;
import com.enterprise.base.common.ResultJson;
import com.enterprise.base.entity.StudyRemindEntity;
import com.enterprise.service.remind.StudyRemindService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/4/23 下午2:25
 */
@Controller
@RequestMapping("/study_remind")
public class StudyRemindController extends BaseController{

    @Resource
    private StudyRemindService studyRemindService;

    @RequestMapping(value="/saveOrUpdate.json",method= RequestMethod.POST)
     @ResponseBody
     public JSONObject saveOrUpdate(Integer libraryId,String userids,String deptids) {
        return studyRemindService.saveOrUpdate(libraryId, deptids, userids);
    }

    /**
     * test-非线上接口
     * */
    @RequestMapping(value="/getSubDepartment.json",method= RequestMethod.GET)
    @ResponseBody
    public JSONObject getSubDepartment(Integer companyId,String deptids) {
        return ResultJson.succResultJson(studyRemindService.getSubDepartment(companyId,deptids));
    }



    /**
     * 获取所有学习情况
     * 总预览
     * */
    @RequestMapping("/getArrangements.json")
    @ResponseBody
    public JSONObject getArrangements(Integer libraryId) {
        return studyRemindService.getArrangements(libraryId);
    }


    /**
     * 获取所有学习情况
     * 详情
     * type 0/1 部门/成员
     * */
    @RequestMapping("/getArrangDetail.json")
    @ResponseBody
    public JSONObject getArrangDetail(Integer libraryId,Integer departmentId,Integer type) throws Exception {
        if(type==1 && departmentId==null){
            departmentId = 1;
        }
        return studyRemindService.getArrangDetail(libraryId, departmentId, type);
    }

    /**
     * 搜索
     * */
    @RequestMapping("/search.json")
    @ResponseBody
    public JSONObject search(Integer libraryId,String search) {
        return studyRemindService.search(libraryId, search);
    }




    /**
     * update测试
     * */
    @RequestMapping("/batchUpdate.json")
    @ResponseBody
    public JSONObject batchUpdate(String ids) {
        List<StudyRemindEntity> updateList = new ArrayList<>();
        if(StringUtils.isNotEmpty(ids)){
            String[] idArray = ids.split(",");
            for (int i = 0; i < idArray.length; i++) {
                Integer status = i%2;
                Integer id = Integer.valueOf(idArray[i]);
                updateList.add(new StudyRemindEntity(id,status,new Date()));
            }
        }
        Integer result = studyRemindService.batchUpdateStudyRemind(updateList);
        return ResultJson.succResultJson(result);
    }




}
