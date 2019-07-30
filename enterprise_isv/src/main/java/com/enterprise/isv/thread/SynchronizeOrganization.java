package com.enterprise.isv.thread;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.DDConstant;
import com.enterprise.base.common.SpringContextHolder;
import com.enterprise.base.entity.DepartmentEntity;
import com.enterprise.base.entity.IsvTicketsEntity;
import com.enterprise.service.department.DepartmentService;
import com.enterprise.service.isvTickets.IsvTicketsService;
import com.enterprise.service.user.UserService;
import com.enterprise.util.dingtalk.DingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/4/18 下午3:40
 */
public class SynchronizeOrganization implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(SynchronizeOrganization.class);

    private String corpId;
    private IsvTicketsEntity isvTicketsEntity;

    public SynchronizeOrganization(String corpId, IsvTicketsEntity isvTicketsEntity) {
        this.corpId = corpId;
        this.isvTicketsEntity = isvTicketsEntity;
    }

    private IsvTicketsService isvTicketsService = SpringContextHolder.getBean(IsvTicketsService.class);
    private DepartmentService departmentService = SpringContextHolder.getBean(DepartmentService.class);
    private UserService  userService = SpringContextHolder.getBean(UserService.class);
//    private CompanyXLibraryService companyXLibraryService = SpringContextHolder.getBean(CompanyXLibraryService.class);

    @Override
    public void run() {
        try {
            long begin = System.currentTimeMillis();
            JSONArray dingDepts = new JSONArray();
            JSONArray userIds = null;
            try {
                dingDepts = DingHelper.getDGDepartmentList(corpId, DDConstant.RECURSION_DEPT);
            } catch (Exception e) {
                dingDepts = new JSONArray();
                logger.error(e.getMessage());
                JSONObject result = JSON.parseObject(e.getMessage());
                JSONObject object = null;
                if ("50004".equals(result.getString("errcode"))) {
                    // 请求的部门id不在授权范围内 获取权限内部门
                    JSONObject obj = DingHelper.getAuthedDept(corpId);
                    JSONArray deptIds = obj.getJSONObject("auth_org_scopes").getJSONArray("authed_dept");
                    if (deptIds != null && deptIds.size() > 0) {
                        for (int i = 0; i < deptIds.size(); i++) {
                            object = new JSONObject();
                            object.put("id", deptIds.get(i).toString());
                            //查出自己部门
                            JSONObject deptJsonObject = DingHelper.getDepartmentDetail(corpId, deptIds.getString(i));
                            dingDepts.add(deptJsonObject);
                            // 子部门 {"createDeptGroup":false,"name":"哈哈哈","id":62196779,"autoAddUser":false,"parentid":62268354}
                            dingDepts.addAll(DingHelper.getDepartmentList(corpId, object));
                        }
                    }
                    userIds = obj.getJSONObject("auth_org_scopes").getJSONArray("authed_user");
                }
            }
            IsvTicketsEntity isvTicketsEntity = isvTicketsService.getIsvTicketByCorpId(corpId);
            List<DepartmentEntity> departmentList =  departmentService.getDeptsByCompanyId(isvTicketsEntity.getCompanyId(),null);
            if(!dingDepts.isEmpty()) {
                departmentService.synchronize(dingDepts, departmentList, corpId);
                logger.info(corpId + "部门同步成功 >" + (System.currentTimeMillis() - begin) + "ms");

                userService.synchronize(dingDepts, corpId);
                logger.info(corpId + "用户同步结束 >" + (System.currentTimeMillis() - begin) + "ms");
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
