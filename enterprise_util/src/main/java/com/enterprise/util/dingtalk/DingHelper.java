package com.enterprise.util.dingtalk;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.CorpRoleListRequest;
import com.dingtalk.api.request.CorpRoleSimplelistRequest;
import com.dingtalk.api.request.OapiUserUpdateRequest;
import com.dingtalk.api.response.CorpRoleListResponse;
import com.dingtalk.api.response.CorpRoleSimplelistResponse;
import com.dingtalk.api.response.OapiUserUpdateResponse;
import com.dingtalk.open.client.api.model.corp.CorpUserDetail;
import com.enterprise.base.bean.DingCorpManager;
import com.enterprise.base.bean.DingCorpUserDetail;
import com.enterprise.base.bean.DingUserDetail;
import com.enterprise.base.common.DDConstant;
import com.enterprise.base.common.SpringContextHolder;
import com.enterprise.base.entity.IsvTicketsEntity;
import com.enterprise.mapper.isvTickets.IsvTicketsMapper;
import com.enterprise.util.HttpUtil;
import com.taobao.api.ApiException;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anson on 18/3/26.
 */
public class DingHelper {

    private static Logger logger = LoggerFactory.getLogger(DingHelper.class);

    /**
     * 测试用
     */
    public static JSONArray getDepartmentList(String corpId, JSONObject object) throws Exception {
        object.put(DDConstant.ACCESS_TOKEN, getISVDBAccessToken(corpId));
        String url = JSONUtil.appendJsonParamsToUrl(DDConstant.GET_DEPARTMENT_LIST_URL, object);
        return HttpUtil.get(url).getJSONArray("department");
    }

    /**
     * 初始化部门用
     */
    public static JSONArray getDepartmentList(String corpId) throws Exception {
        JSONObject object = new JSONObject();
        object.put(DDConstant.ACCESS_TOKEN, getISVDBAccessToken(corpId));
        String url = JSONUtil.appendJsonParamsToUrl(DDConstant.GET_DEPARTMENT_LIST_URL, object);
        object = HttpUtil.get(url);
        return object.getJSONArray("department");
    }

    public static JSONArray getDGDepartmentList(String corpId, String isFetchChild) throws Exception {
        JSONObject object = new JSONObject();
        object.put(DDConstant.ACCESS_TOKEN, getISVDBAccessToken(corpId));
        object.put(DDConstant.IS_FETCH_CHILD, isFetchChild);
        String url = JSONUtil.appendJsonParamsToUrl(DDConstant.GET_DEPARTMENT_LIST_URL, object);
        object = HttpUtil.get(url);
        return object.getJSONArray("department");
    }


    public static JSONArray getDGDepartmentList2(String accessToekn, String isFetchChild) throws Exception {
        JSONObject object = new JSONObject();
        object.put(DDConstant.ACCESS_TOKEN, accessToekn);
        object.put(DDConstant.IS_FETCH_CHILD, isFetchChild);
        String url = JSONUtil.appendJsonParamsToUrl(DDConstant.GET_DEPARTMENT_LIST_URL, object);
        object = HttpUtil.get(url);
        return object.getJSONArray("department");
    }


    public static JSONObject getDGDepartmentList3(String accessToekn, String isFetchChild) throws Exception {
        JSONObject object = new JSONObject();
        object.put(DDConstant.ACCESS_TOKEN, accessToekn);
        object.put(DDConstant.IS_FETCH_CHILD, isFetchChild);
        String url = JSONUtil.appendJsonParamsToUrl(DDConstant.GET_DEPARTMENT_LIST_URL, object);
        object = HttpUtil.getNew(url);
        return object;
    }

    public static JSONObject getAuthedDept(String corpId) throws Exception {
        //TODO
        String url = DDConstant.OAPI_HOST + "/auth/scopes?access_token=" + getISVDBAccessToken(corpId);
        JSONObject object = HttpUtil.get(url);
        return object;
    }

    /**
     * 加入redis 缓存
     * 获取数据库的AccessToken
     *
     * @param corpId
     * @return
     * @throws Exception
     */
    public static String getISVDBAccessToken(String corpId) throws Exception {
        IsvTicketsMapper isvTicketsMapper = SpringContextHolder.getBean(IsvTicketsMapper.class);
        IsvTicketsEntity isv = isvTicketsMapper.getIsvTicketByCorpId(corpId);
        logger.info("t.getAccessToken() > " + isv.getCorpAccessToken());
        return isv.getCorpAccessToken();
    }

    /**
     * 获取部门详情
     */
    public static JSONObject getDepartmentDetail(String corpId, String deptId) {
        try {
            JSONObject object = new JSONObject();
            object.put(DDConstant.ACCESS_TOKEN, getISVDBAccessToken(corpId));
//            object.put(DDC、onstant.ACCESS_TOKEN, "c298f93de9c03d39ba51dfd1c0796851");
            if (deptId != null) {
                object.put("id", String.valueOf(deptId));
            }
            String url = JSONUtil.appendJsonParamsToUrl(DDConstant.GET_DEPARTMENT_DETAIL, object);
            object.clear();
            object = HttpUtil.get(url);
            return object;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取下级部门
     */
    public static JSONObject getSubDepartmentList(String corpId, String deptId) {
        try {
            JSONObject object = new JSONObject();
            object.put(DDConstant.ACCESS_TOKEN, getISVDBAccessToken(corpId));
            if (deptId != null) {
                object.put("id", String.valueOf(deptId));
            }
            String url = JSONUtil.appendJsonParamsToUrl(DDConstant.GET_DEPARTMENT_LIST_URL, object);
            object.clear();
            object = HttpUtil.get(url);
            return object;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取在某个部门下的用户信息
     */
    public static List<CorpUserDetail> getMembersByCorpIdAndDeptId(String corpId, Long deptId) {
        JSONObject object = new JSONObject();
        try {
            object.put(DDConstant.ACCESS_TOKEN, getISVDBAccessToken(corpId));
            if (deptId != null) {
                object.put("department_id", String.valueOf(deptId));
            }
            String url = JSONUtil.appendJsonParamsToUrl(DDConstant.GET_USER_LIST_URL, object);
            JSONObject response = HttpUtil.get(url);
            Integer errcode = response.getInteger("errcode");
            if (errcode > 0) {
                return null;
            }
            List<CorpUserDetail> userLisgt = JSONUtil.getListObj((String) (response.get("userlist").toString()), CorpUserDetail.class);
            return userLisgt;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取在某个部门下的用户信息
     */
    public static List<DingUserDetail> getMembersByCorpIdAndDeptId2(String corpId, Long deptId) {
        JSONObject object = new JSONObject();
        try {
            object.put(DDConstant.ACCESS_TOKEN, getISVDBAccessToken(corpId));
            if (deptId != null) {
                object.put("department_id", String.valueOf(deptId));
            }
            String url = JSONUtil.appendJsonParamsToUrl(DDConstant.GET_USER_LIST_URL, object);
            JSONObject response = HttpUtil.get(url);
            Integer errcode = response.getInteger("errcode");
            if (errcode > 0) {
                return null;
            }
            List<DingUserDetail> userLisgt = JSONUtil.getListObj((String) (response.get("userlist").toString()), DingUserDetail.class);
            return userLisgt;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取成员详情
     */
    public static CorpUserDetail getMemberDetail(String corpId, String userId) {
        try {
            JSONObject object = new JSONObject();
            object.put(DDConstant.ACCESS_TOKEN, getISVDBAccessToken(corpId));
            object.put("userid", userId);
            String memberDetailUrl = JSONUtil.appendJsonParamsToUrl(DDConstant.GET_MEMBER_DETAIL_URL, object);
            JSONObject response = null;
            response = HttpUtil.get(memberDetailUrl);
            Integer errcode = response.getInteger("errcode");
            if (errcode > 0) {
                return null;
            }
            CorpUserDetail corpUserDetail = JSON.parseObject(response.toJSONString(), CorpUserDetail.class);
            logger.info(corpUserDetail.toString());
            return corpUserDetail;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取成员详情
     */
    public static CorpUserDetail getMemberDetail2(String userId, String accessToken) {
        try {
            JSONObject object = new JSONObject();
            object.put(DDConstant.ACCESS_TOKEN, accessToken);
            object.put("userid", userId);
            String memberDetailUrl = JSONUtil.appendJsonParamsToUrl(DDConstant.GET_MEMBER_DETAIL_URL, object);
            JSONObject response = null;

            response = HttpUtil.get(memberDetailUrl);
            CorpUserDetail corpUserDetail = JSON.parseObject(response.toJSONString(), CorpUserDetail.class);
            logger.info(corpUserDetail.toString());
            return corpUserDetail;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取成员详情
     */
    public static Integer getOrgUserCount(String corpId, String onlyActive) {
        JSONObject object = new JSONObject();
        try {
            object.put(DDConstant.ACCESS_TOKEN, getISVDBAccessToken(corpId));

            if (onlyActive != null) {
                object.put("onlyActive", String.valueOf(onlyActive));
            }
            String url = JSONUtil.appendJsonParamsToUrl(DDConstant.GET_ORG_USER_COUNT, object);
            //https://oapi.dingtalk.com/user/get_org_user_count?access_token=%s&onlyActive=%s
            JSONObject response = null;
            response = HttpUtil.get(url);
            logger.info(response.toJSONString());
            Integer count = JSON.parseObject(response.toJSONString()).getInteger("count");
            return count;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取企业管理员列表
     */
    public static List<DingCorpUserDetail> getCorpManager(String corpId) {
        JSONObject object = new JSONObject();
        try {
            object.put(DDConstant.ACCESS_TOKEN, getISVDBAccessToken(corpId));
            String url = JSONUtil.appendJsonParamsToUrl(DDConstant.GET_CORP_MANAGE, object);
            //https://oapi.dingtalk.com/user/get_org_user_count?access_token=%s&onlyActive=%s
            JSONObject response = null;
            response = HttpUtil.get(url);
            logger.info(response.toJSONString());
            List<DingCorpUserDetail> dingCorpUserDeailList = new ArrayList<>();
            List<DingCorpManager> adminUserList = JSONUtil.getListObj((String) (response.get("admin_list").toString()), DingCorpManager.class);
            for (int i = 0; i < adminUserList.size(); i++) {
                DingCorpManager admin = adminUserList.get(i);
                DingCorpUserDetail dingCorpUserDetail = new DingCorpUserDetail();
                dingCorpUserDetail.setUserid(admin.getUserid());
                dingCorpUserDetail.setSysLevel(admin.getSys_level());
                CorpUserDetail corpUserDetail = getMemberDetail(corpId, admin.getUserid());
                BeanUtils.copyProperties(dingCorpUserDetail, corpUserDetail);
                dingCorpUserDeailList.add(dingCorpUserDetail);
            }
            return dingCorpUserDeailList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取企业管理员列表
     */
    public static List<String> getCorpManagerSimple(String corpId) {
        JSONObject object = new JSONObject();
        try {
            object.put(DDConstant.ACCESS_TOKEN, getISVDBAccessToken(corpId));
            String url = JSONUtil.appendJsonParamsToUrl(DDConstant.GET_CORP_MANAGE, object);
            //https://oapi.dingtalk.com/user/get_org_user_count?access_token=%s&onlyActive=%s
            JSONObject response = null;
            response = HttpUtil.get(url);
            logger.info(response.toJSONString());
            List<String> dingCorpUserDingIdList = new ArrayList<>();
            List<DingCorpManager> adminUserList = JSONUtil.getListObj((String) (response.get("admin_list").toString()), DingCorpManager.class);
            for (int i = 0; i < adminUserList.size(); i++) {
                DingCorpManager admin = adminUserList.get(i);
                dingCorpUserDingIdList.add(admin.getUserid());
            }
            return dingCorpUserDingIdList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取企业管理员列表
     */
    public static List<DingCorpManager> getCorpManagerSimple2(String corpId) {
        JSONObject object = new JSONObject();
        try {
            object.put(DDConstant.ACCESS_TOKEN, getISVDBAccessToken(corpId));
            String url = JSONUtil.appendJsonParamsToUrl(DDConstant.GET_CORP_MANAGE, object);
            //https://oapi.dingtalk.com/user/get_org_user_count?access_token=%s&onlyActive=%s
            JSONObject response = null;
            response = HttpUtil.get(url);
            logger.info(response.toJSONString());
            List<String> dingCorpUserDingIdList = new ArrayList<>();
            List<DingCorpManager> adminUserList = JSONUtil.getListObj((String) (response.get("admin_list").toString()), DingCorpManager.class);
            logger.info("获取公司管理员列表:");
            logger.info(response.get("admin_list").toString());
            return adminUserList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取企业管理员列表
     */
    public static List<DingCorpUserDetail> getCorpManager2(String accesstoken) {
        JSONObject object = new JSONObject();
        try {
            object.put(DDConstant.ACCESS_TOKEN, accesstoken);
            String url = JSONUtil.appendJsonParamsToUrl(DDConstant.GET_CORP_MANAGE, object);
            //https://oapi.dingtalk.com/user/get_org_user_count?access_token=%s&onlyActive=%s
            JSONObject response = null;
            response = HttpUtil.get(url);
            logger.info(response.toJSONString());
            List<DingCorpUserDetail> dingCorpUserDeailList = new ArrayList<>();
            List<DingCorpManager> adminUserList = JSONUtil.getListObj((String) (response.get("admin_list").toString()), DingCorpManager.class);
            for (int i = 0; i < adminUserList.size(); i++) {
                DingCorpManager admin = adminUserList.get(i);
                DingCorpUserDetail dingCorpUserDetail = new DingCorpUserDetail();
                dingCorpUserDetail.setUserid(admin.getUserid());
                dingCorpUserDetail.setSysLevel(admin.getSys_level());
                CorpUserDetail corpUserDetail = getMemberDetail2(admin.getUserid(), accesstoken);
                BeanUtils.copyProperties(dingCorpUserDetail, corpUserDetail);
                dingCorpUserDeailList.add(dingCorpUserDetail);
            }
            return dingCorpUserDeailList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String getCorpRoleList(String accessToken) {
        DingTalkClient client = new DefaultDingTalkClient("https://eco.taobao.com/router/rest");
        CorpRoleListRequest req = new CorpRoleListRequest();
        req.setSize(20L);
        req.setOffset(0L);
        CorpRoleListResponse rsp = null;
        try {
            rsp = client.execute(req, accessToken);
            List<CorpRoleListResponse.RoleGroups> roleGroupList = rsp.getResult().getList();
            System.out.println("=============body==============");
            System.out.println(rsp.getBody());
            System.out.println("=============body==============");
            System.out.println("=============list==============");
            for (int i = 0; i < roleGroupList.size(); i++) {
                System.out.println(roleGroupList.get(i).getGroupName());
                if (roleGroupList.get(i).getRoles() != null) {
                    System.out.println(roleGroupList.get(i).getRoles().toString());
                }
            }
            System.out.println("=============list==============");
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getRoleUserList(String accessToken, Long roleId) {
        DingTalkClient client = new DefaultDingTalkClient("https://eco.taobao.com/router/rest");
        CorpRoleSimplelistRequest req = new CorpRoleSimplelistRequest();
        req.setRoleId(roleId);
        req.setSize(20L);
        req.setOffset(0L);
        CorpRoleSimplelistResponse rsp = null;
        try {
            rsp = client.execute(req, accessToken);
            System.out.println(rsp.getBody());
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) {
//        JSONObject object = new JSONObject();
//        object.put(DDConstant.ACCESS_TOKEN, "44b37306fd683a598ab3cd195dbb9366");
//        object.put("department_id", "1");
//        String url = JSONUtil.appendJsonParamsToUrl(DDConstant.GET_USER_LIST_URL, object);
//        JSONObject response = HttpUtil.get(url);
//        List<CorpUserDetail> userLisgt = JSONUtil.getListObj((String) (response.get("userlist").toString()), CorpUserDetail.class);
//        for (int i = 0; i <userLisgt.size() ; i++) {
//            System.out.println(userLisgt.get(i).toString());
//        }
//        JSONObject jsonObject = getDepartmentDetail("dingf115ac3395a05876", "64497441");
//        DepartmentDetail department = JSONObject.parseObject(jsonObject.toJSONString(), DepartmentDetail.class);
//        System.out.println(ToStringBuilder.reflectionToString(department));


//        List<DingCorpUserDetail> dingCorpuserDetailList =  ("ding0d0be1f4df9f899a35c2f4657eb6378f");
//        for (int i = 0; i < dingCorpuserDetailList.size(); i++) {
//            System.out.println(dingCorpuserDetailList.get(i));
//        }

//        List<DingCorpUserDetail> dingCorpuserDetailList =  getCorpManager2("4f1519a6425639eeb334ed27e281a068");
//        for (int i = 0; i < dingCorpuserDetailList.size(); i++) {
//            System.out.println("'"+dingCorpuserDetailList.get(i).getUserid()+"," + dingCorpuserDetailList.get(i).getName()+"'");
//            System.out.println(",");
//        }

//        CorpUserDetail corpUserDetail = getMemberDetail2("0132245720853374","f0b84c75e8663769b161ca0a4f74b00d");
//        System.out.println(corpUserDetail.toString());
//        List<DingUserDetail> dingCorpuserDetailList =  getMembersByCorpIdAndDeptId2("d772399c396a3ff49d9f4e3497a62fb2", 1L);
//        for (int i = 0; i < dingCorpuserDetailList.size(); i++) {
//            System.out.println("'"+dingCorpuserDetailList.get(i).toString()+"'");
//            System.out.println(",");
//        }
//        System.out.println(dingCorpuserDetailList.size());

//        try {
//            JSONArray jsonArray = getDGDepartmentList2("4b5807f16dc5372a9615f6655b65a80e", "true");
//            List<Department> departmentList = JSONArray.parseArray(jsonArray.toJSONString(), Department.class);
//            System.out.println(departmentList.size());
//            for (int i = 0; i < departmentList.size(); i++) {
//                System.out.println("==================");
//                System.out.print(departmentList.get(i).getId() + ",");
//                System.out.println(departmentList.get(i).getName());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        String url = DDConstant.OAPI_HOST + "/auth/scopes?access_token=0a8c6796b2a93559b905585b968e6d9e";
//        JSONObject object = HttpUtil.get(url);
//        System.out.println(object.toJSONString());

        //获取角色列表
//        getCorpRoleList("872c6d5579e53c6489f58250eb63ad74");
        //获取角色包含人
//        getRoleUserList("872c6d5579e53c6489f58250eb63ad74",151319775L);


        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/update");
        OapiUserUpdateRequest request = new OapiUserUpdateRequest();
        request.setUserid("0217321951697739");
//        request.setPosition(null);
        String accessToken = "ee75f742879f35e593054614121d0243";

        try {
            OapiUserUpdateResponse response = client.execute(request, accessToken);
            System.out.println(response.getErrcode());
            System.out.println(response.getErrmsg());
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }
}
