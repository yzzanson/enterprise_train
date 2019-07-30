package com.enterprise.service.community.impl;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.*;
import com.enterprise.base.entity.CommunityInviteDetailEntity;
import com.enterprise.base.entity.CommunityInviteEntity;
import com.enterprise.base.entity.UserEntity;
import com.enterprise.base.entity.UserXCompany;
import com.enterprise.base.entity.right.RightGroupEntity;
import com.enterprise.base.vo.CommunityInviteVO;
import com.enterprise.base.vo.UserXLibraryScheduleVO;
import com.enterprise.mapper.community.CommunityInviteDetailMapper;
import com.enterprise.mapper.community.CommunityInviteMapper;
import com.enterprise.mapper.companyInfo.CompanyInfoMapper;
import com.enterprise.mapper.isvTickets.IsvTicketsMapper;
import com.enterprise.mapper.right.RightGroupMapper;
import com.enterprise.mapper.right.UserRightGroupMapper;
import com.enterprise.mapper.users.UserConfigMapper;
import com.enterprise.mapper.users.UserMapper;
import com.enterprise.mapper.users.UserXCompanyMapper;
import com.enterprise.mapper.users.UserXLibraryMapper;
import com.enterprise.oa.OAMessageUtil;
import com.enterprise.service.community.CommunityService;
import com.enterprise.util.DateUtil;
import com.enterprise.util.oa.message.OAMessage;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description
 * @Author zezhouyang
 * @Date 19/3/25 上午11:18
 */
@Service
public class CommunityServiceImpl implements CommunityService {

    @Resource
    private UserXLibraryMapper userXLibraryMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private RightGroupMapper rightGroupMapper;

    @Resource
    private UserRightGroupMapper userRightGroupMapper;

    @Resource
    private CommunityInviteMapper communityInviteMapper;

    @Resource
    private IsvTicketsMapper isvTicketMapper;

    @Resource
    private UserXCompanyMapper userXCompanyMapper;

    @Resource
    private UserConfigMapper userConfigMapper;

    @Resource
    private CompanyInfoMapper companyInfoMapper;

    @Resource
    private CommunityInviteDetailMapper communityInviteDetailMapper;

    private Integer userCount = 5;

    private final String INVITE_COMMUNITY = DDConstant.INVITE_COMMUNITY;

    private String QRCODE_PAGE = GlobalConstant.getQrCOdePage();


    @Override
    public JSONObject checkStudySchedule() {
        MobileLoginUser mobileLoginUser = MobileLoginUser.getUser();
        Integer companyId = mobileLoginUser.getCompanyID();
        List<UserXLibraryScheduleVO> userXLibraryList = userXLibraryMapper.getUserAchievePublicLibraryList(companyId);
        for (int i = 0; i < userXLibraryList.size(); i++) {
            UserXLibraryScheduleVO userXLibraryVO = userXLibraryList.get(i);
            Integer userId = userXLibraryVO.getUserId();
            UserEntity userEntity = userMapper.getUserById(userId);
            String libraryName = userXLibraryMapper.getUserAchievePublicLibrary(companyId,userId);
            userXLibraryVO.setLibraryName(libraryName);
            userXLibraryVO.setName(userEntity.getName());
            userXLibraryVO.setAvatar(userEntity.getAvatar());
        }
        if (userXLibraryList.size() >= userCount) {
            return ResultJson.succResultJson(userXLibraryList);
        } else {
            Integer diffCount = userCount - userXLibraryList.size();
            return ResultJson.errorResultJson(String.valueOf(diffCount), userXLibraryList);
        }
    }

    @Override
    public JSONObject inviteManage() {
        MobileLoginUser mobileLoginUser = MobileLoginUser.getUser();
        Integer companyId = mobileLoginUser.getCompanyID();
        Integer userId = mobileLoginUser.getUserID();
        RightGroupEntity rightGroupEntity = rightGroupMapper.getSuperManageRightGroup(companyId);
        if (rightGroupEntity == null) {
            return ResultJson.errorResultJson("不存在的权限组");
        }
        Integer rightGroupId = rightGroupEntity.getId();
        List<Integer> userRightGroupList = userRightGroupMapper.getUserRightGroupByGroupId(rightGroupId, companyId);
        Set<Integer> manageUserSet = new HashSet(userRightGroupList);
        if (manageUserSet.contains(userId)) {
            return ResultJson.succResultJson(userId);
        }
        return ResultJson.errorResultJson(userId);
    }

    @Override
    public JSONObject getManageList(PageEntity pageEntity) {
        MobileLoginUser mobileLoginUser = MobileLoginUser.getUser();
        Integer companyId = mobileLoginUser.getCompanyID();
        RightGroupEntity rightGroupEntity = rightGroupMapper.getSuperManageRightGroup(companyId);
        if (rightGroupEntity == null) {
            return ResultJson.errorResultJson("不存在的权限组");
        }
        List<CommunityInviteVO> resultList = new ArrayList<>();
        Integer rightGroupId = rightGroupEntity.getId();
        PageHelper.startPage(pageEntity.getPageNo(), pageEntity.getPageSize());
        PageInfo<Integer> pageInfo = new PageInfo<>(userRightGroupMapper.getUserRightGroupByGroupId(rightGroupId, companyId));
        List<Integer> userRightGroupList = pageInfo.getList();
        for (int i = 0; i < userRightGroupList.size(); i++) {
            Integer userId = userRightGroupList.get(i);
            UserEntity userEntity = userMapper.getUserById(userId);
            if(userEntity!=null) {
                CommunityInviteVO communityInviteVO = new CommunityInviteVO();
                Integer inviteTime = communityInviteMapper.getInviteCount(companyId, userId);
                CommunityInviteEntity invitedRecord = communityInviteMapper.getByCompanyIdAndInvitedUserId(companyId, userId, mobileLoginUser.getUserID());
                Integer isInvited = 0;
                if (inviteTime >= 3) {
                    isInvited = 1;
                } else if (invitedRecord != null) {
                    isInvited = 1;
                }

                communityInviteVO = new CommunityInviteVO(userId, userEntity.getName(), userEntity.getAvatar(), isInvited);
                resultList.add(communityInviteVO);
            }
        }
        Map<String, Object> dataMap = Maps.newHashMap();
        dataMap.put("list", resultList);
        dataMap.put("total", pageInfo.getTotal());
        dataMap.put("page", new Page(pageInfo.getPrePage(), pageEntity.getPageNo(), pageInfo.getNextPage(), pageInfo.getLastPage()));
        return ResultJson.succResultJson(dataMap);
    }

    @Override
    public JSONObject sendInvite(Integer userId) {
        MobileLoginUser mobileLoginUser = MobileLoginUser.getUser();
        Integer companyId = mobileLoginUser.getCompanyID();
        Integer loginUserID = mobileLoginUser.getUserID();
        Integer inviteTime = communityInviteMapper.getInviteCount(companyId, userId);
        CommunityInviteEntity invitedRecord = communityInviteMapper.getByCompanyIdAndInvitedUserId(companyId, userId, loginUserID);
        if (inviteTime >= 3) {
            return ResultJson.errorResultJson("超过次数!");
        }
        if(invitedRecord!=null){
            return ResultJson.errorResultJson("您已经邀请过了!");
        }
        //Integer companyId, Integer userId, Integer inviteUser, Date createTime
        CommunityInviteEntity communityInviteEntity = new CommunityInviteEntity(companyId, userId, loginUserID, new Date());
        if (communityInviteMapper.createCommunityInvite(communityInviteEntity) > 0) {
            //发送OA消息
            sendInviteOAMessage(userId,loginUserID,companyId);
            return ResultJson.succResultJson(userId);
        }
        return ResultJson.errorResultJson(userId);
    }

    @Override
    public JSONObject saveInvite(String name, String phoneNum,Integer companyId,Integer userId) {
        ReentrantLock lock = new ReentrantLock();
        //(Integer companyId, Integer userId, String userName, String phoneNum, Date createTime)
        CommunityInviteDetailEntity communityInviteDetail = new CommunityInviteDetailEntity(companyId,userId,name,phoneNum,new Date());
        if(communityInviteDetailMapper.getByCompanyId(companyId)!=null){
            return ResultJson.errorResultJson("亲，您的企业已经有其他管理员提交了～");
        }
        lock.lock();
        Integer result = communityInviteDetailMapper.createCommunityInviteDetail(communityInviteDetail);
        lock.unlock();
        if(result>0){
            return ResultJson.succResultJson(userId);
        }
        return ResultJson.errorResultJson(userId);
    }

    @Override
    public JSONObject checkIsInvite() {
        MobileLoginUser mobileLoginUser = MobileLoginUser.getUser();
        CommunityInviteDetailEntity communityDetailEntity = communityInviteDetailMapper.getByCompanyId(mobileLoginUser.getCompanyID());
        if(communityDetailEntity!=null){
            return ResultJson.succResultJson(mobileLoginUser.getCompanyID());
        }
        return ResultJson.errorResultJson(mobileLoginUser.getCompanyID());
    }

    /**
     * 发送OA消息
     * //TODO 主页
     *
     * @Param userId接收人
     * @Param loginUserId发送方
     */
    private void sendInviteOAMessage(Integer userId, Integer loginUserId,Integer companyId) {
        String corpId = isvTicketMapper.getIsvTicketByCompanyId(companyId).getCorpId();
        UserXCompany userXCompany = userXCompanyMapper.getDingIdByCorpIdAndUserId(corpId, userId);
//        UserConfigEntity userConfigEntity = userConfigMapper.findByUserId(userXCompany.getUserId());
//        if (userConfigEntity == null || (userConfigEntity != null && userConfigEntity.getIsOa().equals(1))) {
        String sendUserName = userMapper.getNameById(loginUserId);
        String companyName = companyInfoMapper.getCompanyNameById(companyId);
        String content = String.format(INVITE_COMMUNITY, sendUserName);
        content += "\n" + DateUtil.getDate_Y_M_D_H_M_S();
        OAMessage oaMessage = new OAMessage();
//        String mainPageBury = GlobalConstant.getCommunityPage() + GlobalConstant.OA_BURY_TYPE;
        String mainPageBury = GlobalConstant.getCommunityPage();
        //corpId companyId userId
        String mainPage = String.format(mainPageBury, corpId ,userId,companyId, companyName);
        String mainPageQrcode = String.format(QRCODE_PAGE, URLEncoder.encode(mainPage));
        OAMessageUtil.sendOAMessageWithStroage(companyId, userXCompany.getDingUserId(), "", oaMessage.getSimpleOAMessage(mainPage, mainPageQrcode, content));
        //发送OA消息记录
//        saveOASendBury(companyId, userId, OABuryEnum.TOOL.getValue(), new Date());
//        }
    }

    public static void main(String[] args) {
        String mainPageBury = "https://neixun.forwe.store/web/test/exam-app/?corp=%s#/sign-up?userId=%s&companyId=%s&companyName=%s";
        String mainPage = String.format(mainPageBury, "ding0980998" ,23,2, "杭州捷盈科技");
        System.out.println(URLEncoder.encode(mainPage));
    }


}
