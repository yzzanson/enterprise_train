package com.enterprise.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.BaseController;
import com.enterprise.base.common.LoginUser;
import com.enterprise.base.common.PageEntity;
import com.enterprise.base.common.ResultJson;
import com.enterprise.base.entity.right.RightGroupEntity;
import com.enterprise.service.right.RightGroupService;
import com.enterprise.util.AssertUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/7/3 下午2:58
 */
@Controller
@RequestMapping("/right_group")
public class RightGroupController extends BaseController{

    @Resource
    private RightGroupService rightGroupService;

    /**
     * 创建权限组
     * */
    @RequestMapping(value="/createOrUpdateGroup.json",method= RequestMethod.POST)
    @ResponseBody
    public JSONObject createOrUpdateGroup(RightGroupEntity rightGroupEntity) {
        if(StringUtils.isNotEmpty(rightGroupEntity.getName())){
            AssertUtil.isTrue(rightGroupEntity.getName().length()<10,"角色名长度过长");
        }
        Integer result = rightGroupService.saveOrUpdateRightGroup(rightGroupEntity);
        if(result>0){
            return ResultJson.succResultJson(rightGroupEntity);
        }else{
           return ResultJson.errorResultJson(rightGroupEntity);
        }
    }


    /**
     * 获取权限组列表
     * */
    @RequestMapping(value="/getRightGroups.json",method= RequestMethod.GET)
    @ResponseBody
    public JSONObject getRightGroups(PageEntity pageEntity) {
        return rightGroupService.getRightGroups(LoginUser.getUser().getCompanyID(),pageEntity);
    }

    /**
     * 查询某个权限组下某个用户
     * */
    @RequestMapping(value="/findUserRightGroup.json",method= RequestMethod.GET)
    @ResponseBody
    public JSONObject findUserRightGroup(Integer rightGroupId,String name) {
        AssertUtil.isTrue(name != null, "搜索条件不能为空");
        return rightGroupService.findUserRightGroup(rightGroupId, name);
    }


    /**
     * 获取部门列表,某个部门下用户是否有权限组权限
     * */
    @RequestMapping(value="/getDepartmentGroup.json",method= RequestMethod.GET)
    @ResponseBody
    public JSONObject getDepartmentGroup(Integer deptId,Integer rightGroupId,String name)  throws Exception{
        return rightGroupService.getDepartmentGroup(deptId, rightGroupId, name);
    }

    /**
     * 获取部门列表,某个部门下用户是否有权限组权限
     * */
    @RequestMapping(value="/getGroupUser.json",method= RequestMethod.GET)
    @ResponseBody
    public JSONObject getGroupUser(Integer rightGroupId) {
        return rightGroupService.getGroupUser(rightGroupId);
    }


    /**
     * 获取部门列表,某个部门下用户是否有权限组权限
     * */
    @RequestMapping(value="/getManageUser.json",method= RequestMethod.GET)
    @ResponseBody
    public JSONObject getManageUser() {
        return rightGroupService.getManageUser();
    }



    public static void main(String[] args) {
        String str= "sssssssssss";
        AssertUtil.isTrue(str.length()<10,"角色名长度过长");
    }

}
