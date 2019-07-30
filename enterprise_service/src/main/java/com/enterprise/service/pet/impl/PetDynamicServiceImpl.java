package com.enterprise.service.pet.impl;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.*;
import com.enterprise.base.entity.*;
import com.enterprise.base.enums.DynamicEnum;
import com.enterprise.base.enums.StatusEnum;
import com.enterprise.base.vo.PetDynamicVO;
import com.enterprise.mapper.bagTool.BagToolEffectMapper;
import com.enterprise.mapper.bagTool.BagToolEffectQuestionsMapper;
import com.enterprise.mapper.paperBall.PaperBallMapper;
import com.enterprise.mapper.pet.PetDynamicMapper;
import com.enterprise.mapper.pet.PetVisittMapper;
import com.enterprise.mapper.petFood.PetFoodPlateDetailMapper;
import com.enterprise.mapper.users.UserMapper;
import com.enterprise.mapper.users.UserXCompanyMapper;
import com.enterprise.service.pet.PetDynamicService;
import com.enterprise.util.AssertUtil;
import com.enterprise.util.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/10/26 下午4:04
 */
@Service
public class PetDynamicServiceImpl implements PetDynamicService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private PetDynamicMapper petDynamicMapper;

    @Resource
    private BagToolEffectMapper bagToolEffectMapper;

    @Resource
    private BagToolEffectQuestionsMapper bagToolEffectQuestionsMapper;

    @Resource
    private UserXCompanyMapper userXCompanyMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private PetFoodPlateDetailMapper petFoodPlateDetailMapper;

    @Resource
    private PaperBallMapper paperBallMapper;

    @Resource
    private PetVisittMapper petVisittMapper;

    private Integer EVERY_GAIN_COUNT = PetConstant.EVERY_GAIN_COUNT;

    @Override
    public JSONObject getPetDynamic(PageEntity pageEntity) {
        MobileLoginUser mobileLoginUser = MobileLoginUser.getUser();
        Integer companyId = mobileLoginUser.getCompanyID();
        Integer userId = mobileLoginUser.getUserID();
        PageHelper.startPage(pageEntity.getPageNo(), pageEntity.getPageSize());
        PageInfo<PetDynamicEntity> pageInfo = new PageInfo<>(petDynamicMapper.getPetDynamicList(companyId, userId));
        List<PetDynamicEntity> petDynamicList = pageInfo.getList();
        List<PetDynamicVO> resultPetDynamicVOList = new ArrayList<>();
        Integer unread = 0;
        for (int i = 0; i < petDynamicList.size(); i++) {
            PetDynamicEntity petDynamicEntity = petDynamicList.get(i);
            Integer activeId = petDynamicEntity.getActiveId();
            Integer dynamicId = petDynamicEntity.getDynamicId();
            //status 0不需消除 1正在作用 2已消除
            PetDynamicVO petDynamicVO = new PetDynamicVO();
            petDynamicVO.setActiveId(activeId);
            petDynamicVO.setId(petDynamicVO.getId());
            if (!activeId.equals(DynamicEnum.CUPID.getValue())) {
                petDynamicVO.setDynamicContent(petDynamicEntity.getDynamicContent());
            } else {
                //获取该丘比特答题数
                Integer effectCount = bagToolEffectQuestionsMapper.getToolEffectCount2(companyId, userId, dynamicId);
                Integer gainFoodCount = effectCount * EVERY_GAIN_COUNT;
                String dynamicContent = petDynamicEntity.getDynamicContent().replace("x", gainFoodCount + "");
                //String dynamicContent = String.format(petDynamicEntity.getDynamicContent(), gainFoodCount);
                petDynamicVO.setDynamicContent(dynamicContent);
            }
            if (activeId.equals(DynamicEnum.SEAL.getValue())
                    || activeId.equals(DynamicEnum.DECAY.getValue())
                    || activeId.equals(DynamicEnum.Dynamicpunch.getValue())
                    || activeId.equals(DynamicEnum.CUPID.getValue())) {
                BagToolEffectEntity bagToolEffectEntity = bagToolEffectMapper.getById(dynamicId);
                //作用状态为1
                if(bagToolEffectEntity.getStatus().equals(StatusEnum.OK.getValue())){
                    petDynamicVO.setStatus(bagToolEffectEntity.getStatus());
                    petDynamicVO.setEffectId(bagToolEffectEntity.getId());
                }else{
                    //消除状态为2
                    petDynamicVO.setStatus(StatusEnum.HOLD_ON.getValue());
                }
            } else {
                //其他不需要消除
                petDynamicVO.setStatus(StatusEnum.DELETE.getValue());
            }
            if(activeId.equals(DynamicEnum.SEAL.getValue())
                    || activeId.equals(DynamicEnum.DECAY.getValue())
                    || activeId.equals(DynamicEnum.Dynamicpunch.getValue())
                    || activeId.equals(DynamicEnum.CUPID.getValue())
                    || activeId.equals(DynamicEnum.GROWTH_LIQUID.getValue())) {
                BagToolEffectEntity bagToolEffectEntity = bagToolEffectMapper.getById(dynamicId);
                String dynamicUserName = userMapper.getNameById(bagToolEffectEntity.getUserId());
                petDynamicVO.setUserId(bagToolEffectEntity.getUserId());
                petDynamicVO.setUserName(dynamicUserName);
            }else if(activeId.equals(DynamicEnum.OTHER_CLEAN.getValue()) || activeId.equals(DynamicEnum.SELF_CLEAN.getValue())){
                PaperBallEntity paperBallEntity = paperBallMapper.getById(dynamicId);
                Integer dynamicUserId = paperBallEntity.getCleanUserId();
                String dynamicUserName = userMapper.getNameById(dynamicUserId);
                petDynamicVO.setUserId(dynamicUserId);
                petDynamicVO.setUserName(dynamicUserName);
            } else if(activeId.equals(DynamicEnum.OTHER_FEED.getValue()) || activeId.equals(DynamicEnum.SELF_CLEAN.getValue())){
                PetFoodPlateDetailEntity petFoodPlateDetailEntity = petFoodPlateDetailMapper.getById(dynamicId);
                Integer dynamicUserId = petFoodPlateDetailEntity.getFeedUserId();
                String dynamicUserName = userMapper.getNameById(dynamicUserId);
                petDynamicVO.setUserId(dynamicUserId);
                petDynamicVO.setUserName(dynamicUserName);
            }else if(activeId.equals(DynamicEnum.VISIT.getValue())){
                PetVisitEntity petVisitEntity = petVisittMapper.getById(dynamicId);
                Integer dynamicUserId = petVisitEntity.getUserId();
                String dynamicUserName = userMapper.getNameById(dynamicUserId);
                petDynamicVO.setUserId(dynamicUserId);
                petDynamicVO.setUserName(dynamicUserName);
            }
            String dateTime = DateUtil.getDisplayYMDHMS(petDynamicEntity.getCreateTime());
            petDynamicVO.setTime(dateTime);
            petDynamicVO.setIsRead(petDynamicEntity.getIsRead());
            resultPetDynamicVOList.add(petDynamicVO);
            if(unread!=1){
                if(petDynamicEntity.getIsRead().equals(0)){
                    unread =1;
                }
            }
        }
        Map<String, Object> dataMap = Maps.newHashMap();
        dataMap.put("unread",unread);
        dataMap.put("list", resultPetDynamicVOList);
        dataMap.put("total", pageInfo.getTotal());
        dataMap.put("page", new Page(pageInfo.getPrePage(), pageEntity.getPageNo(), pageInfo.getNextPage(), pageInfo.getLastPage()));
        return ResultJson.succResultJson(dataMap);
    }

    @Override
    public JSONObject updateMyPetDynamic() {
        //将当前所有的状态都更新
        MobileLoginUser mobileLoginUser = MobileLoginUser.getUser();
        Integer unreadCount = petDynamicMapper.getUnreadDynamic(mobileLoginUser.getCompanyID(), mobileLoginUser.getUserID());
        if(unreadCount<=0){
            return ResultJson.succResultJson(mobileLoginUser.getUserID());
        }
        Integer result = petDynamicMapper.updateMyDynamic(mobileLoginUser.getCompanyID(),mobileLoginUser.getUserID(),StatusEnum.OK.getValue());
        if(result>0){
            return ResultJson.succResultJson(mobileLoginUser.getUserID());
        }
        return ResultJson.errorResultJson(mobileLoginUser.getUserID());
    }

    @Override
    public JSONObject vistOther(Integer userId) {
        AssertUtil.notNull(userId, "未知的用户 : " + userId);
        try {
            MobileLoginUser mobileLoginUser = MobileLoginUser.getUser();
            Integer loginUserId = mobileLoginUser.getUserID();
            String corpId = mobileLoginUser.getCorpID();
            Integer companyId = mobileLoginUser.getCompanyID();
            if (loginUserId.equals(userId)) {
                return ResultJson.kickedutResultJson("不能访问自己");
            }
            UserXCompany userXCompany = userXCompanyMapper.getUserXCompany(new UserXCompany(corpId, userId));
            String visitUser = userMapper.getNameById(loginUserId);
            if (userXCompany == null) {
                return ResultJson.kickedutResultJson("不存在的用户");
            }
            //保存用户访问记录
            PetVisitEntity petVisitEntity = new PetVisitEntity(companyId, loginUserId, userId, new Date());
            Integer result1 = petVisittMapper.createPetVisit(petVisitEntity);
            //保存用户动态记录
            String contentModel = DynamicEnum.getDynamicEnum(DynamicEnum.VISIT.getValue()).getDynamic();
            contentModel = String.format(contentModel, visitUser);
            //public PetDynamicEntity(Integer companyId, Integer userId, Integer activeId, Integer dynamicId, String dynamicContent, Date createTime, Integer isRead, Integer status)
            PetDynamicEntity petDynamicEntity = new PetDynamicEntity(companyId, userId, DynamicEnum.VISIT.getValue(), petVisitEntity.getId(), contentModel, new Date(), StatusEnum.DELETE.getValue(), StatusEnum.OK.getValue());
            Integer result2 = petDynamicMapper.createPetDynamic(petDynamicEntity);
            if (result1 > 0 && result2 > 0) {
                return ResultJson.succResultJson(userId);
            }
            return ResultJson.errorResultJson("保存失败");
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResultJson.errorResultJson("出问题了!");
    }


    public static void main(String[] args) {
        String sttt = "杨一给你发射了爱神之箭~一方答题另一方获得相同猫粮哦！累计帮我赚得xg猫粮";
        sttt = sttt.replace("x",1111+"");
        System.out.println(sttt);
    }

}
