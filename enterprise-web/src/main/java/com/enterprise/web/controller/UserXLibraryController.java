package com.enterprise.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.BaseController;
import com.enterprise.base.common.PageEntity;
import com.enterprise.service.question.UserXLibraryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by anson on 18/3/27.
 */
@Controller
@RequestMapping("/userx_library")
public class UserXLibraryController  extends BaseController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private UserXLibraryService userXLibraryService;

    /**
     * 企业题库-查看完成度
     * */
    @RequestMapping("/getCompletion.json")
    @ResponseBody
    public JSONObject getCompletion(Integer libraryId,PageEntity pageEntity) {
        return userXLibraryService.findUserStudySchedule(libraryId,pageEntity);
    }

}
