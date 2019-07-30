package com.enterprise.isv.thread;

import com.enterprise.base.bean.DingCorpManager;
import com.enterprise.base.common.GlobalConstant;
import com.enterprise.base.common.SpringContextHolder;
import com.enterprise.base.entity.IsvTicketsEntity;
import com.enterprise.base.entity.UserXCompany;
import com.enterprise.base.entity.right.RightGroupEntity;
import com.enterprise.base.entity.right.RightResourceGroupEntity;
import com.enterprise.base.entity.right.RightResourceUrlEntity;
import com.enterprise.base.entity.right.UserRightGroupEntity;
import com.enterprise.base.enums.RoleEnum;
import com.enterprise.base.enums.StatusEnum;
import com.enterprise.oa.OAMessageUtil;
import com.enterprise.service.company.CompanyInfoService;
import com.enterprise.service.isvTickets.IsvTicketsService;
import com.enterprise.service.right.RightGroupService;
import com.enterprise.service.right.RightResourceGroupService;
import com.enterprise.service.right.RightResourceUrlService;
import com.enterprise.service.right.UserRightGroupService;
import com.enterprise.service.user.UserXCompanyService;
import com.enterprise.util.dingtalk.DingHelper;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/7/2 下午3:25
 */
public class SynchronizeRight implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(SynchronizePublicSingle.class);

    private String corpId;

    private String globalManageName = GlobalConstant.getManageRoleName();

    public SynchronizeRight(String corpId) {
        this.corpId = corpId;
    }

    private RightGroupService rightGroupService = SpringContextHolder.getBean(RightGroupService.class);
    private RightResourceGroupService rightResourceGroupService = SpringContextHolder.getBean(RightResourceGroupService.class);
    private RightResourceUrlService rightResourceUrlService = SpringContextHolder.getBean(RightResourceUrlService.class);
    private IsvTicketsService isvTicketsService = SpringContextHolder.getBean(IsvTicketsService.class);
    private UserXCompanyService userXCompanyService = SpringContextHolder.getBean(UserXCompanyService.class);
    private UserRightGroupService userRightGroupService = SpringContextHolder.getBean(UserRightGroupService.class);
    private CompanyInfoService companyInfoService = SpringContextHolder.getBean(CompanyInfoService.class);

    @Override
    public void run() {
        logger.info("开始同步权限表");
        //添加 超管角色不能被删除,所以。。
        IsvTicketsEntity isvTicketsEntity = isvTicketsService.getIsvTicketByCorpId(corpId);
        Integer companyId = isvTicketsEntity.getCompanyId();
        String companyName = companyInfoService.getCompanyNameById(companyId);
        String manageRoleName = RoleEnum.MANAGE.getDesc();
        List<RightGroupEntity> rightGroupList = rightGroupService.getByCompanyIdAndName(companyId, StatusEnum.OK.getValue(), manageRoleName);
        Integer rightManageGroupId = null;
        if (rightGroupList == null || (rightGroupList!=null && rightGroupList.size()==0)) {
            RightGroupEntity rightGroupEntity = new RightGroupEntity(companyId, manageRoleName, 1, 1, StatusEnum.OK.getValue(), new Date(), new Date());
            //初始化超管权限组用
            rightGroupService.saveOrUpdateRightGroupSingle(rightGroupEntity);
            rightManageGroupId = rightGroupEntity.getId();
        } else {
            rightManageGroupId = rightGroupList.get(0).getId();
        }
        List<RightResourceUrlEntity> manageResourceList = new ArrayList<>();
        if (!corpId.equals(GlobalConstant.getOperCorpId())) {
            //判断公司是否有管理员权限组
            //1.获取所有管理员该有的权限url
            manageResourceList = rightResourceUrlService.getResourceByOperate(0, 1,null);
        } else {
            manageResourceList = rightResourceUrlService.getResourceByOperate(null, 1,null);
        }
        //2.获取当前超管权限组拥有的资源id
        List<RightResourceGroupEntity> rightResourceGroupEntityList = rightResourceGroupService.getResourcesByCompanyAndGroup(new RightResourceGroupEntity(companyId, rightManageGroupId));
        Set<Integer> currentResourceSet = new HashSet<>();
        if (CollectionUtils.isNotEmpty(rightResourceGroupEntityList)) {
            for (int i = 0; i < rightResourceGroupEntityList.size(); i++) {
                Integer resourceId = rightResourceGroupEntityList.get(i).getRightResourceId();
                currentResourceSet.add(resourceId);
            }
        }
        List<RightResourceGroupEntity> addRightResourceGroupList = new ArrayList<>();
        List<RightResourceGroupEntity> updateRightResourceGroupList = new ArrayList<>();
        for (int i = 0; i < manageResourceList.size(); i++) {
            RightResourceUrlEntity manageResourceUrlInDB = manageResourceList.get(i);
            Integer resourceId = manageResourceUrlInDB.getId();
            if (!currentResourceSet.contains(resourceId)) {
                addRightResourceGroupList.add(new RightResourceGroupEntity(companyId, rightManageGroupId, resourceId, StatusEnum.OK.getValue(), new Date(), new Date()));
                currentResourceSet.add(resourceId);
            } else {
                RightResourceGroupEntity rightResourceGroupInDB = rightResourceGroupService.getByResourceIdAndCompany(new RightResourceGroupEntity(companyId, rightManageGroupId, resourceId));
                if (rightResourceGroupInDB != null && rightResourceGroupInDB.getStatus().equals(StatusEnum.DELETE.getValue())) {
                    updateRightResourceGroupList.add(new RightResourceGroupEntity());
                } else if (rightResourceGroupInDB != null && rightResourceGroupInDB.getStatus().equals(StatusEnum.OK.getValue())) {
                    continue;
                } else if (rightResourceGroupInDB == null) {
                    addRightResourceGroupList.add(new RightResourceGroupEntity(companyId, rightManageGroupId, resourceId, StatusEnum.OK.getValue(), new Date(), new Date()));
                }
            }
        }
        if (CollectionUtils.isNotEmpty(addRightResourceGroupList)) {
            rightResourceGroupService.batchInsert(addRightResourceGroupList);
        }
        if (CollectionUtils.isNotEmpty(updateRightResourceGroupList)) {
            rightResourceGroupService.batchUpdate(updateRightResourceGroupList);
        }
        List<UserRightGroupEntity> addUserRightGroupList = new ArrayList<>();
        List<UserRightGroupEntity> updateUserRightGroupList = new ArrayList<>();
        //获取所有的管理员,并将 用户_公司 表中加入
        StringBuffer sbf = new StringBuffer();
        sbf.append(companyName);
        List<DingCorpManager> dingCorpUserDetailList = DingHelper.getCorpManagerSimple2(corpId);
        for (int i = 0; i < dingCorpUserDetailList.size(); i++) {
            DingCorpManager dingCorpManager = dingCorpUserDetailList.get(i);
            String dingUserId = dingCorpManager.getUserid();
            UserXCompany userXCompany = userXCompanyService.getUserXCompany(new UserXCompany(corpId, dingUserId));
            if (userXCompany != null) {
                Integer userId = userXCompany.getUserId();
                logger.info("管理员"+i+ "-dingUserId:"+dingUserId +"-userId:"+userId);
                if (dingCorpManager.getSys_level().equals(new Long(1))) {
                    //超级管理员
                    if (userXCompany.getIsSuperManage() == null || (userXCompany.getIsSuperManage() != null && userXCompany.getIsSuperManage().equals(0))) {
                        // public UserXCompany(Integer id, String dingUserId, String corpId, Integer status, Integer isSuperManage) {
                        userXCompanyService.updateUserXCompany(new UserXCompany(userXCompany.getId(), userXCompany.getDingUserId(),corpId, StatusEnum.OK.getValue(), 1));
                    }
                }
                // public UserRightGroupEntity(Integer companyId,Integer userId,Integer rightGroupId, Integer status)
                UserRightGroupEntity usrRightGroupInDB = userRightGroupService.findUserRightGroupByGroupId(new UserRightGroupEntity(companyId, userId,rightManageGroupId, null ,new Date()));
                if (usrRightGroupInDB == null) {
                    addUserRightGroupList.add(new UserRightGroupEntity(companyId, userId,rightManageGroupId,StatusEnum.OK.getValue(),new Date(),new Date()));
                } else if (usrRightGroupInDB != null && usrRightGroupInDB.getStatus().equals(StatusEnum.DELETE.getValue())) {
                    updateUserRightGroupList.add(new UserRightGroupEntity(usrRightGroupInDB.getId(), rightManageGroupId, StatusEnum.OK.getValue(), new Date()));
                }
            }

            //保存超级管理员权限
        }
        if(CollectionUtils.isNotEmpty(addUserRightGroupList)){
            userRightGroupService.batchInsert(addUserRightGroupList);
            sbf.append("新增了").append(addUserRightGroupList.size()).append("用户权限");
        }
        if(CollectionUtils.isNotEmpty(updateUserRightGroupList)){
            userRightGroupService.batchUpdate(updateUserRightGroupList);
            sbf.append("更新了").append(updateUserRightGroupList.size()).append("用户权限");
        }
        if(CollectionUtils.isNotEmpty(addUserRightGroupList) || CollectionUtils.isNotEmpty(updateUserRightGroupList)) {
            OAMessageUtil.sendTextMsgToDept(sbf.toString());
        }
    }

    public static void main(String[] args) {
        Long ll = new Long(11);
        System.out.println(ll.equals(new Long(11)));
    }


}
