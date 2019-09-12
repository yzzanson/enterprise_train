package com.enterprise.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.open.client.api.model.corp.Department;
import com.enterprise.base.common.BaseController;
import com.enterprise.base.common.ResultJson;
import com.enterprise.base.entity.DepartmentEntity;
import com.enterprise.base.entity.IsvTicketsEntity;
import com.enterprise.mapper.department.DepartmentMapper;
import com.enterprise.mapper.isvTickets.IsvTicketsMapper;
import com.enterprise.service.department.DepartmentService;
import com.enterprise.service.department.DepartmentSimpleService;
import com.enterprise.service.question.UserXLibraryService;
import com.enterprise.util.dingtalk.DingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/10/12 上午10:47
 */
@Controller
@RequestMapping("/sync")
public class SyncController extends BaseController{

    @Resource
    private IsvTicketsMapper isvTicketsMapper;

    @Resource
    private DepartmentMapper departmentMapper;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private DepartmentService departmentService;

    @Resource
    private UserXLibraryService userXLibraryService;

    @Resource
    private DepartmentSimpleService departmentSimpleService;

    /**
     * 查看公司部门是否正确
     */
    @RequestMapping("/checkCompany.json")
    @ResponseBody
    public JSONObject checkCompany() {
        List<IsvTicketsEntity> isvTicketsList = isvTicketsMapper.getAllCompanys();
        List<Integer> companyIdList = new ArrayList<>();
        for (int i = 0; i < isvTicketsList.size(); i++) {
            IsvTicketsEntity isvTicket = isvTicketsList.get(i);
            String corpId = isvTicket.getCorpId();
            Integer companyId = isvTicket.getCompanyId();
            try {
                JSONArray departmentArray = DingHelper.getDGDepartmentList(corpId, "1");
                if(departmentArray.size()>0){
                    Department department  = JSONObject.parseObject(String.valueOf(departmentArray.get(0)), Department.class);
                    Integer id = department.getId().intValue();
                    String name = department.getName();
                    DepartmentEntity departmentEntity = departmentMapper.getByCompanyIdAndDingDeptId(companyId, id);
                    if(departmentEntity==null){
                        companyIdList.add(companyId);
                        continue;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        logger.info("===============");
        logger.info(companyIdList.toString());
        return ResultJson.succResultJson(companyIdList);
    }



    /**
     * 查看公司部门是否正确
     */
    @RequestMapping("/syncCompany.json")
    @ResponseBody
    public JSONObject syncCompany(Integer companyId) throws Exception {
        return departmentService.syncCompany(companyId);
    }

    /**
     * 查看公司部门是否正确
     */
    @RequestMapping("/syncRight.json")
    @ResponseBody
    public JSONObject syncRight(Integer companyId) {
        return departmentService.syncRightSingle(companyId);
    }


    /**
     * 同步所有答题时间
     */
    @RequestMapping("/updateFinishTime.json")
    @ResponseBody
    public JSONObject updateFinishTime() {
        return userXLibraryService.updateFinishTime2();
    }

    /**
     * 获取所有部门
     */
    @RequestMapping("/getAllDepartment.json")
    @ResponseBody
    public JSONObject getAllDepartment() {
        return departmentSimpleService.getDepartments();
    }


}
