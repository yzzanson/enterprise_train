package com.enterprise.service.petFood.impl;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.GlobalConstant;
import com.enterprise.base.common.MobileLoginUser;
import com.enterprise.base.common.PetConstant;
import com.enterprise.base.common.ResultJson;
import com.enterprise.base.entity.*;
import com.enterprise.base.entity.bury.OASendBuryEntity;
import com.enterprise.base.entity.bury.PetRaiseBuryEntity;
import com.enterprise.base.enums.DynamicEnum;
import com.enterprise.base.enums.PetFoodGainEnum;
import com.enterprise.base.enums.PetFoodPlateGainEnum;
import com.enterprise.base.enums.StatusEnum;
import com.enterprise.base.enums.bury.OABuryEnum;
import com.enterprise.base.enums.bury.PetRaiseEnum;
import com.enterprise.base.exceptions.BusinessException;
import com.enterprise.base.vo.MyPetVO;
import com.enterprise.base.vo.PetFoodVO;
import com.enterprise.base.vo.UserPetVO;
import com.enterprise.dswitch.DSwitch;
import com.enterprise.mapper.bury.OASendBuryMapper;
import com.enterprise.mapper.bury.PetRaiseBuryMapper;
import com.enterprise.mapper.isvTickets.IsvTicketsMapper;
import com.enterprise.mapper.pet.MyPetMapper;
import com.enterprise.mapper.pet.PetDynamicMapper;
import com.enterprise.mapper.petFood.PetFoodDetailMapper;
import com.enterprise.mapper.petFood.PetFoodMapper;
import com.enterprise.mapper.petFood.PetFoodPlateDetailMapper;
import com.enterprise.mapper.petFood.PetFoodPlateMapper;
import com.enterprise.mapper.users.UserConfigMapper;
import com.enterprise.mapper.users.UserMapper;
import com.enterprise.mapper.users.UserXCompanyMapper;
import com.enterprise.oa.OAMessageUtil;
import com.enterprise.service.isvTickets.IsvTicketsService;
import com.enterprise.service.petFood.PetFoodService;
import com.enterprise.util.AssertUtil;
import com.enterprise.util.DateUtil;
import com.enterprise.util.oa.message.OAMessage;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/10/22 下午1:42
 */
@Service
public class PetFoodServiceImpl implements PetFoodService {

    @Resource
    private IsvTicketsMapper isvTicketsMapper;

    @Resource
    private IsvTicketsService isvTicketsService;

    @Resource
    private UserXCompanyMapper userXCompanyMapper;


    @Resource
    private MyPetMapper myPetMapper;

    @Resource
    private PetFoodMapper petFoodMapper;

    @Resource
    private PetFoodDetailMapper petFoodDetailMapper;

    @Resource
    private PetFoodPlateMapper petFoodPlateMapper;

    @Resource
    private PetFoodPlateDetailMapper petFoodPlateDetailMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private PetDynamicMapper petDynamicMapper;

    @Resource
    private PetRaiseBuryMapper petRaiseBuryMapper;


    @Resource
    private OASendBuryMapper oaSendBuryMapper;

    @Resource
    private UserConfigMapper userConfigMapper;


    //初始化的饲料数
    private Integer PET_INIT_FOOD_COUNT = PetConstant.INIT_COUNT;
    //每一次喂食数
    private Integer PET_FOOD_FEED_COUNT = PetConstant.EACH_FEED_COUNT;

    //投喂别人获取的猫粮数
    private Integer EACH_FEED_OTHER_COUNT = PetConstant.EVERY_GAIN_COUNT;
    //每x秒消耗1g猫粮
    private Integer EVERY_SECONDS_CONSUME_FEED = PetConstant.EVERY_SECONDS_CONSUME_FEED;

    //投食超额
    private String EXCEED_FEED = PetConstant.EXCEED_FEED;

    //猫粮不够
    private String NO_FOOD = PetConstant.NO_FOOD;

    private String FEED_MESSAGE = "主人,%s给我投食啦";

    private static final String MAIN_PAGE = GlobalConstant.getIndexUrl();

    private static final String INDEX_PAGE = GlobalConstant.getUserIndexUrl();

    private String QRCODE_PAGE = GlobalConstant.getQrCOdePage();

    /**
     * 初始化,每个企业,有宠物的用户获取100g猫粮
     */
    @Override
    public JSONObject initPetFood() {
        List<IsvTicketsEntity> isvTicketsList = isvTicketsMapper.getAllCompanys();
        Integer totalCount = 0;
        for (int i = 0; i < isvTicketsList.size(); i++) {
            String corpId = isvTicketsList.get(i).getCorpId();
            Integer companyId = isvTicketsList.get(i).getCompanyId();
            List<UserPetVO> userVOList = userXCompanyMapper.getUserRaisePet(corpId);
            List<PetFoodEntity> petFoodList = new ArrayList<>();
            List<PetFoodDetailEntity> petFoodDetailList = new ArrayList<>();
            for (int j = 0; j < userVOList.size(); j++) {
                Integer userId = userVOList.get(j).getUserId();
                PetFoodEntity petFoodEntity = new PetFoodEntity(companyId, userId, PET_INIT_FOOD_COUNT, new Date(), new Date());
                PetFoodDetailEntity petFoodDetailEntity = new PetFoodDetailEntity(companyId, userId, PetFoodGainEnum.INIT.getValue(), PET_INIT_FOOD_COUNT, new Date(), new Date());
                petFoodList.add(petFoodEntity);
                petFoodDetailList.add(petFoodDetailEntity);
            }
            if (CollectionUtils.isNotEmpty(petFoodList)) {
                totalCount++;
                petFoodMapper.batchInsert(petFoodList);
            }
            if (CollectionUtils.isNotEmpty(petFoodDetailList)) {
                petFoodDetailMapper.batchInsert(petFoodDetailList);
            }
        }
        return ResultJson.succResultJson(totalCount);
    }

    /**
     * 喂养宠物
     */
    @Override
    @DSwitch(type="1")
    public JSONObject feedPet(MobileLoginUser loginUser) throws BusinessException {
        Integer companyId = loginUser.getCompanyID();
        Integer userId = loginUser.getUserID();
        MyPetVO myPetVO = myPetMapper.getMyPet(userId);
        AssertUtil.notNull(myPetVO, "用户未初始化宠物");
        PetFoodEntity petFoodEntity = petFoodMapper.getPetFood(companyId, userId);
        AssertUtil.notNull(petFoodEntity, "用户未初始化猫粮");
        //宠物食物减少
        AssertUtil.isTrue(petFoodEntity.getFoodCount() >= PET_FOOD_FEED_COUNT, "当前猫粮不够!");
        Integer feedCount = petFoodEntity.getFoodCount() - PET_FOOD_FEED_COUNT;
        Date createDate = new Date();
        Integer foodCountUpdate = petFoodMapper.updatePetFood(new PetFoodEntity(petFoodEntity.getId(), feedCount, createDate));
        Integer foodDetailInsert = petFoodDetailMapper.createPetFoodDetail(new PetFoodDetailEntity(companyId, userId, PetFoodGainEnum.FEED.getValue(), -PET_FOOD_FEED_COUNT, createDate, createDate));
        AssertUtil.isTrue(foodCountUpdate > 0 && foodDetailInsert > 0, "更新猫粮失败");
        //宠物盘中食物增加
        PetFoodPlateEntity petFoodPlateEntity = petFoodPlateMapper.getPeteFoodPlateByUser(companyId, userId);
        if (petFoodPlateEntity == null) {
            //下一次消耗饲料时间
            Date nextUpdateTime = DateUtil.getSecondLaterDate(createDate, EVERY_SECONDS_CONSUME_FEED);
            petFoodPlateEntity = new PetFoodPlateEntity(companyId, userId, PET_FOOD_FEED_COUNT, createDate, createDate);
            Integer foodPlateInsert = petFoodPlateMapper.createPetFoodPlate(petFoodPlateEntity);
            Integer selfFeedPlateInsert = petFoodPlateDetailMapper.createPetFoodPlateDetail(new PetFoodPlateDetailEntity(companyId, userId, userId, PET_FOOD_FEED_COUNT, PetFoodPlateGainEnum.SELF_FEED.getValue(), StatusEnum.OK.getValue(), nextUpdateTime, createDate, createDate));
            AssertUtil.isTrue(foodPlateInsert > 0 && selfFeedPlateInsert > 0, "更新猫粮盘失败");
        } else {
            Date nextUpdateTime = DateUtil.getSecondLaterDate(createDate, EVERY_SECONDS_CONSUME_FEED);
            Integer petFoodCount = petFoodPlateEntity.getFoodCount() + PET_FOOD_FEED_COUNT;
            petFoodPlateEntity.setFoodCount(petFoodCount);
            Integer foodPlateUpdate = petFoodPlateMapper.updatePetFoodPlate(petFoodPlateEntity);
            Integer selfFeedPlateInsert = petFoodPlateDetailMapper.createPetFoodPlateDetail(new PetFoodPlateDetailEntity(companyId, userId, userId, PET_FOOD_FEED_COUNT, PetFoodPlateGainEnum.SELF_FEED.getValue(), StatusEnum.OK.getValue(), nextUpdateTime, createDate, createDate));
            AssertUtil.isTrue(foodPlateUpdate > 0 && selfFeedPlateInsert > 0, "更新猫粮盘失败");
        }
        //保存埋点
        saveRaisePetBury(companyId,userId, PetRaiseEnum.FEEDSELF.getValue(),createDate);
        return ResultJson.succResultJson("喂食成功");
    }

    private void saveRaisePetBury(Integer companyId,Integer userId,Integer type,Date createTime){
        PetRaiseBuryEntity petRaiseBuryEntity = new PetRaiseBuryEntity(companyId,userId,type,createTime);
        petRaiseBuryMapper.createPetRaiseBury(petRaiseBuryEntity);
    }

    /**
     * 当前的猫粮和剩余的猫粮
     */
    @Override
    public JSONObject getPetFood(Integer userId) {
        Integer companyId = MobileLoginUser.getUser().getCompanyID();

        PetFoodEntity petFoodEntity = petFoodMapper.getPetFood(companyId, userId);
        PetFoodPlateEntity petFoodPlateEntity = petFoodPlateMapper.getPeteFoodPlateByUser(companyId, userId);
        PetFoodVO petFoodVO = new PetFoodVO();
        petFoodVO.setUserId(userId);
        if (petFoodEntity != null) {
            petFoodVO.setPetFoodCount(petFoodEntity.getFoodCount());
        }else{
            Integer petFoodCount = 0;
            PetFoodDetailEntity petFoodDetailEntity =  petFoodDetailMapper.getByCompanyAndUserId(companyId, userId);
            if(petFoodDetailEntity==null) {
                petFoodDetailEntity = new PetFoodDetailEntity(companyId, userId, PetFoodGainEnum.INIT.getValue(), PET_INIT_FOOD_COUNT, new Date(), new Date());
                petFoodDetailMapper.createPetFoodDetail(petFoodDetailEntity);
                petFoodCount = PET_INIT_FOOD_COUNT;
            }else{
                petFoodCount = petFoodDetailMapper.getUserPetFoodCount(companyId, userId);
            }
            //如果没有则查询并初始化食物
            petFoodEntity = new PetFoodEntity(companyId,userId,petFoodCount,new Date(),new Date());
            petFoodMapper.createPetFood(petFoodEntity);
            petFoodVO.setPetFoodCount(petFoodCount);
        }
        if (petFoodPlateEntity != null) {
            petFoodVO.setPetFoodPlateCount(petFoodPlateEntity.getFoodCount());
            if (petFoodPlateEntity.getFoodCount() != null && petFoodPlateEntity.getFoodCount() > 0) {
                Integer plateCount = petFoodPlateEntity.getFoodCount();
                Integer timeRemain = petFoodPlateEntity.getFoodCount() * EVERY_SECONDS_CONSUME_FEED / 60;
                Integer hour = timeRemain / 60;
                Integer minute = 0;
                if (hour > 0) {
                    minute = timeRemain - hour * 60;
                } else {
                    minute = timeRemain;
                }
                petFoodVO.setRemainHourTime(hour);
                petFoodVO.setRemainMinuteTime(minute);
            }
        }else{
            petFoodVO.setPetFoodPlateCount(0);
        }
        return ResultJson.succResultJson(petFoodVO);
    }

    /**
     * 喂养其他的宠物
     */
    @Override
    @DSwitch(type="2")
    public JSONObject feedOtherPet(Integer userId) throws BusinessException {
        MobileLoginUser mobileLoginUser = MobileLoginUser.getUser();
        Integer sUserId = mobileLoginUser.getUserID();
        Integer companyId = mobileLoginUser.getCompanyID();
        if (userId.equals(sUserId)) {
            return ResultJson.kickedutResultJson("不能给自己投食");
        }
        PetFoodEntity petFoodEntity = petFoodMapper.getPetFood(companyId, sUserId);
        PetFoodEntity petFoodUserEntity = petFoodMapper.getPetFood(companyId, userId);
        if(petFoodUserEntity==null){
            Integer petFoodCount = 0;
            PetFoodDetailEntity petFoodDetailEntity =  petFoodDetailMapper.getByCompanyAndUserId(companyId, userId);
            if(petFoodDetailEntity==null) {
                petFoodDetailEntity = new PetFoodDetailEntity(companyId, userId, PetFoodGainEnum.INIT.getValue(), PET_INIT_FOOD_COUNT, new Date(), new Date());
                petFoodDetailMapper.createPetFoodDetail(petFoodDetailEntity);
                petFoodCount = PET_INIT_FOOD_COUNT;
            }else{
                petFoodCount = petFoodDetailMapper.getUserPetFoodCount(companyId, userId);
            }
            //如果没有则查询并初始化食物
            petFoodEntity = new PetFoodEntity(companyId,userId,petFoodCount,new Date(),new Date());
            petFoodMapper.createPetFood(petFoodEntity);
        }


        if (petFoodEntity.getFoodCount() < EACH_FEED_OTHER_COUNT) {
            return ResultJson.kickedutResultJson(NO_FOOD);
        }
        //给该同事投喂猫粮次数
        //Integer getFeedOtherCount(@Param("companyId") Integer companyId, @Param("userId") Integer userId, @Param("feedUserId") Integer feedUserId, @Param("type") Integer type,@Param("time") String time)
        String dayStr = DateUtil.getDateYMD(new Date());
        Integer feedCount = petFoodPlateDetailMapper.getFeedOtherCount(companyId, userId, sUserId,PetFoodPlateGainEnum.OTHER_FEED.getValue(), dayStr);
        if (feedCount >= 3) {
            return ResultJson.kickedutResultJson(EXCEED_FEED);
        }
        //自己猫粮袋减少10g 对方猫粮盆增加10g
        Integer foodRemainCount = petFoodEntity.getFoodCount() - EACH_FEED_OTHER_COUNT;
        Integer foodCountUpdate = petFoodMapper.updatePetFood(new PetFoodEntity(petFoodEntity.getId(), foodRemainCount, new Date()));
        Integer foodDetailInsert = petFoodDetailMapper.createPetFoodDetail(new PetFoodDetailEntity(companyId, sUserId, PetFoodGainEnum.FEEDOTHER.getValue(), -EACH_FEED_OTHER_COUNT, new Date(), new Date()));
        Date createDate = new Date();


        //判断是否有值
        PetFoodPlateEntity feedUserPlate = petFoodPlateMapper.getPeteFoodPlateByUser(companyId, userId);
        if (feedUserPlate == null) {
            //Integer companyId, Integer userId, Integer foodCount, Date createTime, Date updateTime
            Integer createPlate = petFoodPlateMapper.createPetFoodPlate(new PetFoodPlateEntity(companyId, userId, EACH_FEED_OTHER_COUNT, createDate, createDate));
            //PetFoodPlateDetailEntity(Integer companyId, Integer userId, Integer feedUserId, Integer foodCount, Integer type, Date nextPlanConsumeTime, Date createTime, Date updateTime)
            Date nextUpdateTime = DateUtil.getSecondLaterDate(new Date(), EVERY_SECONDS_CONSUME_FEED);
            PetFoodPlateDetailEntity foodPlateDetailEntity = new PetFoodPlateDetailEntity(companyId, userId, sUserId, EACH_FEED_OTHER_COUNT, PetFoodPlateGainEnum.OTHER_FEED.getValue(), StatusEnum.OK.getValue(), nextUpdateTime, createDate, createDate);
            Integer foodDetailFeedInsert = petFoodPlateDetailMapper.createPetFoodPlateDetail(foodPlateDetailEntity);
            if (createPlate > 0 && foodDetailFeedInsert > 0) {
                sendOA(companyId, userId, sUserId);
                saveDynamic(foodPlateDetailEntity);
                saveRaisePetBury(companyId, userId, PetRaiseEnum.FEEDOTHER.getValue(), createDate);
                saveOASendBury(companyId, userId, OABuryEnum.FEED.getValue(), new Date());
                return ResultJson.succResultJson("投喂成功");
            } else {
                return ResultJson.succResultJson("投喂失败");
            }
        } else {
            Integer remainCount = feedUserPlate.getFoodCount() + EACH_FEED_OTHER_COUNT;
            Integer updatePlate = petFoodPlateMapper.updatePetFoodPlate(new PetFoodPlateEntity(feedUserPlate.getId(), remainCount, createDate));
            Date nextUpdateTime = DateUtil.getSecondLaterDate(new Date(), EVERY_SECONDS_CONSUME_FEED);
            //获取上次的消费时间
            if (feedUserPlate.getFoodCount() > 0) {
                PetFoodPlateDetailEntity petFoodPlateDetailEntity = petFoodPlateDetailMapper.getLatestConsumeRecord(companyId, userId, PetFoodPlateGainEnum.AUTO_CONSUME.getValue());
                if (petFoodPlateDetailEntity == null) {
                    nextUpdateTime = DateUtil.getSecondLaterDate(createDate, EVERY_SECONDS_CONSUME_FEED);
                } else {
                    nextUpdateTime = DateUtil.getSecondLaterDate(petFoodPlateDetailEntity.getNextPlanConsumeTime(), EVERY_SECONDS_CONSUME_FEED);
                }
            }
            PetFoodPlateDetailEntity petFoodPlateDetailEntity = new PetFoodPlateDetailEntity(companyId, userId, sUserId, EACH_FEED_OTHER_COUNT, PetFoodPlateGainEnum.OTHER_FEED.getValue(), StatusEnum.OK.getValue(), nextUpdateTime, createDate, createDate);
            Integer foodDetailFeedInsert = petFoodPlateDetailMapper.createPetFoodPlateDetail(petFoodPlateDetailEntity);
            if (foodDetailFeedInsert > 0) {
                saveDynamic(petFoodPlateDetailEntity);
                sendOA(companyId, userId, sUserId);
                saveRaisePetBury(companyId, userId, PetRaiseEnum.FEEDOTHER.getValue(), createDate);
                saveOASendBury(companyId, userId, OABuryEnum.FEED.getValue(), new Date());
                return ResultJson.succResultJson("投喂成功");
            } else {
                return ResultJson.succResultJson("投喂失败");
            }
        }
    }


    /**
     * @Param sUserId 我
     * @Param sUserId 喂养对象
     * 喂养其他的宠物
     */
    @Override
    public JSONObject feedOtherPetTest(Integer companyId,Integer sUserId,Integer otherUserId) {
        try {
            if (otherUserId.equals(sUserId)) {
                return ResultJson.kickedutResultJson("不能给自己投食");
            }
            PetFoodEntity petFoodEntity = petFoodMapper.getPetFood(companyId, sUserId);
            if (petFoodEntity.getFoodCount() < EACH_FEED_OTHER_COUNT) {
                return ResultJson.kickedutResultJson(NO_FOOD);
            }
            //给该同事投喂猫粮次数
            String dayStr = DateUtil.getDateYMD(new Date());
            Integer feedCount = petFoodPlateDetailMapper.getFeedOtherCount(companyId, otherUserId, sUserId, PetFoodPlateGainEnum.OTHER_FEED.getValue(), dayStr);
            if (feedCount >= 3) {
                return ResultJson.kickedutResultJson(EXCEED_FEED);
            }
            //自己猫粮袋减少10g 对方猫粮盆增加10g
            Integer foodRemainCount = petFoodEntity.getFoodCount() - EACH_FEED_OTHER_COUNT;
            Integer foodCountUpdate = 1;
            Integer foodDetailInsert = 1;
            Date createDate = new Date();
            //判断是否有值
            PetFoodPlateEntity feedUserPlate = petFoodPlateMapper.getPeteFoodPlateByUser(companyId, otherUserId);
            if (feedUserPlate == null) {
                //Integer companyId, Integer userId, Integer foodCount, Date createTime, Date updateTime
                Integer createPlate = petFoodPlateMapper.createPetFoodPlate(new PetFoodPlateEntity(companyId, otherUserId, EACH_FEED_OTHER_COUNT, createDate, createDate));
                //PetFoodPlateDetailEntity(Integer companyId, Integer userId, Integer feedUserId, Integer foodCount, Integer type, Date nextPlanConsumeTime, Date createTime, Date updateTime)
                Date nextUpdateTime = DateUtil.getSecondLaterDate(new Date(), EVERY_SECONDS_CONSUME_FEED);
                PetFoodPlateDetailEntity foodPlateDetailEntity = new PetFoodPlateDetailEntity(companyId, otherUserId, sUserId, EACH_FEED_OTHER_COUNT, PetFoodPlateGainEnum.OTHER_FEED.getValue(), StatusEnum.OK.getValue(), nextUpdateTime, createDate, createDate);
                Integer foodDetailFeedInsert = petFoodPlateDetailMapper.createPetFoodPlateDetail(foodPlateDetailEntity);
                if (createPlate > 0 && foodDetailFeedInsert > 0) {
                    return ResultJson.succResultJson("投喂成功");
                } else {
                    return ResultJson.succResultJson("投喂失败");
                }
            } else {
                Integer remainCount = feedUserPlate.getFoodCount() + EACH_FEED_OTHER_COUNT;
                Integer updatePlate = 1;
                Date nextUpdateTime = DateUtil.getSecondLaterDate(new Date(), EVERY_SECONDS_CONSUME_FEED);
                //获取上次的消费时间
                if (feedUserPlate.getFoodCount() > 0) {
                    PetFoodPlateDetailEntity petFoodPlateDetailEntity = petFoodPlateDetailMapper.getLatestConsumeRecord(companyId, otherUserId, PetFoodPlateGainEnum.AUTO_CONSUME.getValue());
                    if (petFoodPlateDetailEntity == null) {
                        nextUpdateTime = DateUtil.getSecondLaterDate(createDate, EVERY_SECONDS_CONSUME_FEED);
                    } else {
                        nextUpdateTime = DateUtil.getSecondLaterDate(petFoodPlateDetailEntity.getNextPlanConsumeTime(), EVERY_SECONDS_CONSUME_FEED);
                    }
                }
                PetFoodPlateDetailEntity petFoodPlateDetailEntity = new PetFoodPlateDetailEntity(companyId, otherUserId, sUserId, EACH_FEED_OTHER_COUNT, PetFoodPlateGainEnum.OTHER_FEED.getValue(), StatusEnum.OK.getValue(), nextUpdateTime, createDate, createDate);
                Integer foodDetailFeedInsert = 1;
                if (foodDetailFeedInsert > 0) {
                    return ResultJson.succResultJson("投喂成功");
                } else {
                    return ResultJson.succResultJson("投喂失败");
                }
            }
        }catch (Exception e){
            return ResultJson.succResultJson("小错误");
        }
    }

    /**
     * userId被投喂人
     * sUserId投喂人
     */
    private void sendOA(Integer companyId, Integer userId, Integer sUserId) {
        UserConfigEntity userConfigEntity = userConfigMapper.findByUserId(userId);
        if(userConfigEntity==null || (userConfigEntity!=null && userConfigEntity.getIsOa().equals(1))) {
            IsvTicketsEntity isvTicketsEntity = isvTicketsService.getIsvTicketByCompanyId(companyId);
            String dingName = userMapper.getNameById(sUserId);
            String feedTitle = String.format(FEED_MESSAGE, dingName);
            feedTitle += "\n" + DateUtil.getDate_Y_M_D_H_M_S();
            OAMessage oaMessage = new OAMessage();
            String mainPageBury = MAIN_PAGE + GlobalConstant.OA_BURY_TYPE + OABuryEnum.FEED.getValue();
//        String mainPageBury = INDEX_PAGE+"/"+userId+"/1"+GlobalConstant.OA_BURY_TYPE + OABuryEnum.FEED.getValue();
            //String mainPageBury = MAIN_PAGE+GlobalConstant.OA_BURY_TYPE + OABuryEnum.FEED.getValue();
            String mainPage = String.format(mainPageBury, isvTicketsEntity.getCorpId());
            String mainPageQrcode = String.format(QRCODE_PAGE, URLEncoder.encode(mainPage));
            UserXCompany userXCompany = userXCompanyMapper.getDingIdByCorpIdAndUserId(isvTicketsEntity.getCorpId(), userId);
            OAMessageUtil.sendOAMessageWithStroageV2(companyId, userXCompany.getDingUserId(), "", oaMessage.getSimpleOAMessage(mainPage, mainPageQrcode, feedTitle));
        }
    }

    private Integer saveDynamic(PetFoodPlateDetailEntity petFoodPlateDetailEntity) {
        PetDynamicEntity petDynamicEntity = petDynamicMapper.getPetDynamic(petFoodPlateDetailEntity.getCompanyId(), petFoodPlateDetailEntity.getFeedUserId(), DynamicEnum.OTHER_FEED.getValue(), petFoodPlateDetailEntity.getId());
        String userName = userMapper.getNameById(petFoodPlateDetailEntity.getFeedUserId());
        String contentModel = DynamicEnum.getDynamicEnum(DynamicEnum.OTHER_FEED.getValue()).getDynamic();
        if (petDynamicEntity == null) {
            contentModel = String.format(contentModel, userName, EACH_FEED_OTHER_COUNT);
            petDynamicEntity = new PetDynamicEntity(petFoodPlateDetailEntity.getCompanyId(), petFoodPlateDetailEntity.getUserId(), DynamicEnum.OTHER_FEED.getValue(), petFoodPlateDetailEntity.getId(), contentModel, petFoodPlateDetailEntity.getCreateTime(), StatusEnum.DELETE.getValue(),StatusEnum.OK.getValue());
            return petDynamicMapper.createPetDynamic(petDynamicEntity);
        }
        return 0;
    }

    private void saveOASendBury(Integer companyId,Integer userId,Integer type,Date createTime){
        OASendBuryEntity oaSendBuryEntity = new OASendBuryEntity(companyId,userId,type,createTime);
        oaSendBuryMapper.createOASendBury(oaSendBuryEntity);
    }

    public static void main(String[] args) {
//        Integer timeRemain = 10 * 120 / 60;
//        Integer hour = timeRemain / 60;
//        Integer minute = 0;
//        if (hour > 0) {
//            minute = timeRemain - hour * 60;
//        } else {
//            minute = timeRemain;
//        }
//        System.out.println(hour);
//        System.out.println(minute);
        Integer PET_FOOD_FEED_COUNT = 120;
        AssertUtil.isTrue(120 >= PET_FOOD_FEED_COUNT, "当前猫粮不够!");
        System.out.println("");

        String mainPageBury = INDEX_PAGE+"/"+1231+"/1"+GlobalConstant.OA_BURY_TYPE + OABuryEnum.FEED.getValue();
        System.out.println(mainPageBury);
    }
}
