package com.enterprise.mobile.web.job;

import com.enterprise.base.common.PetConstant;
import com.enterprise.base.common.RedisConstant;
import com.enterprise.base.entity.DSwitchEntity;
import com.enterprise.base.entity.PetFoodPlateDetailEntity;
import com.enterprise.base.entity.PetFoodPlateEntity;
import com.enterprise.base.entity.PetWeightEntity;
import com.enterprise.base.enums.PetFoodPlateGainEnum;
import com.enterprise.base.enums.StatusEnum;
import com.enterprise.base.enums.SwitchEnum;
import com.enterprise.base.vo.PetFoodConsumeVO;
import com.enterprise.mapper.pet.PetWeightMapper;
import com.enterprise.mapper.petFood.PetFoodPlateDetailMapper;
import com.enterprise.mapper.petFood.PetFoodPlateMapper;
import com.enterprise.service.redis.RedisService;
import com.enterprise.util.DateUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Description 宠物食物消耗定时任务
 * @Author zezhouyang
 * @Date 18/10/22 下午6:02
 */
@Component
public class PetFoodConsumeJob {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private PetFoodPlateMapper petFoodPlateMapper;

    @Resource
    private PetFoodPlateDetailMapper petFoodPlateDetailMapper;

    @Resource
    private PetWeightMapper petWeightMapper;

    @Resource
    private RedisService redisService;

    public static String DSWITCH_TYPE_KEY = RedisConstant.DSWITCH_TYPE_KEY;


    private Integer EVERY_SECONDS_CONSUME = PetConstant.EVERY_SECONDS_CONSUME_FEED;

    private Integer EVERY_GAIN_COUNT = PetConstant.EVERY_GAIN_COUNT;

    //判断下次喂食时间,当前时间-下次消耗时间<0 不减少
    //           120>当前时间-下次消耗时间>0 减少一次
    //               当前时间-下次消耗时间>240 减少二次
    //pet_plate_detail --> next_feed_time下一次投喂时间
    public void work() {

        String key = DSWITCH_TYPE_KEY + SwitchEnum.CONSUME_FOOD.getValue();
        DSwitchEntity objectFromRedis = (DSwitchEntity) redisService.getSerializeObj(key);
        if(objectFromRedis!=null && objectFromRedis.getStatus()!=null && objectFromRedis.getStatus().equals(0)){
            return;
        }

        try {
            Date currentTime = new Date();
            List<PetFoodPlateEntity> petFoodPlateList = petFoodPlateMapper.getAllRetainFoodList();
            if (CollectionUtils.isNotEmpty(petFoodPlateList)) {
                for (int i = 0; i < petFoodPlateList.size(); i++) {
                    PetFoodPlateEntity petFoodPlate = petFoodPlateList.get(i);
                    Integer companyId = petFoodPlate.getCompanyId();
                    Integer userId = petFoodPlate.getUserId();
                    //获取当前最早的一次喂食时间-且未被消耗光
                    List<PetFoodPlateDetailEntity> petFoodPlateDetailList = petFoodPlateDetailMapper.getFeedList(companyId, userId);
                    if(CollectionUtils.isNotEmpty(petFoodPlateDetailList)) {
                        //当前需要消耗的数量
                        //获取第一次更新时间
                        Date updateTime = null;
                        Date updateEndTime = null;
                        Integer plateFoodCount = petFoodPlate.getFoodCount();//盘中剩余
                        PetFoodPlateDetailEntity petFoodPlateDetail = petFoodPlateDetailList.get(0);//喂食
                        Integer plateDetailId = petFoodPlateDetail.getId();
                        //该喂食被消耗的数量
                        PetFoodPlateDetailEntity petFoodConsumeDetail = petFoodPlateDetailMapper.getPlateConsumeRecord(companyId, userId, plateDetailId, null);
                        if (petFoodConsumeDetail == null) {
                            //logger.info("A:"+userId+"消耗了1g饲料");
                            updateTime = new Date();
                            //public PetFoodPlateDetailEntity(Integer companyId, Integer userId, Integer feedUserId, Integer foodCount, Integer type, Integer status, Integer consumePlateId, Date nextPlanConsumeTime, Date createTime, Date updateTime)
                            updateEndTime = DateUtil.getSecondLaterDate(updateTime, EVERY_SECONDS_CONSUME);
                            petFoodConsumeDetail = new PetFoodPlateDetailEntity(companyId, userId, null, 1, PetFoodPlateGainEnum.AUTO_CONSUME.getValue(), StatusEnum.OK.getValue(), plateDetailId, updateEndTime, new Date(), new Date());
                            petFoodPlateDetailMapper.createPetFoodPlateDetail(petFoodConsumeDetail);
                            plateFoodCount = plateFoodCount - 1;
                            petFoodPlate.setFoodCount(plateFoodCount);
                            petFoodPlateMapper.updatePetFoodPlate(petFoodPlate);
                            //增加体重
                            updatePetWeight(companyId, userId, 1);
                        } else {
                            //logger.info("B:"+userId+"消耗了1g饲料");
                            updateEndTime = DateUtil.getSecondLaterDate(petFoodConsumeDetail.getNextPlanConsumeTime(), EVERY_SECONDS_CONSUME);
                            Integer consumedCount = petFoodConsumeDetail.getFoodCount() + 1;
                            if (consumedCount.equals(petFoodPlateDetail.getFoodCount())) {
                                petFoodPlateDetail.setStatus(StatusEnum.DELETE.getValue());
                                petFoodPlateDetail.setUpdateTime(new Date());
                                petFoodPlateDetailMapper.updatePetFoodDetail(petFoodPlateDetail);

                                petFoodConsumeDetail.setStatus(StatusEnum.DELETE.getValue());
                                petFoodConsumeDetail.setNextPlanConsumeTime(updateEndTime);
                                petFoodConsumeDetail.setFoodCount(consumedCount);
                                petFoodConsumeDetail.setUpdateTime(new Date());
                                petFoodPlateDetailMapper.updatePetFoodDetail(petFoodConsumeDetail);
                            } else {
                                petFoodConsumeDetail.setNextPlanConsumeTime(updateEndTime);
                                petFoodConsumeDetail.setFoodCount(consumedCount);
                                petFoodConsumeDetail.setUpdateTime(new Date());
                                petFoodPlateDetailMapper.updatePetFoodDetail(petFoodConsumeDetail);
                            }
                            plateFoodCount = plateFoodCount - 1;
                            petFoodPlate.setFoodCount(plateFoodCount);
                            petFoodPlateMapper.updatePetFoodPlate(petFoodPlate);
                            updatePetWeight(companyId, userId, 1);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @Param petFoodPlateFeedDetailId 喂养id
     * @Param petFoodConsumeDetailId 消耗id
     * @Param shouldConsumeTime 下一次更新消耗时间
     * @Param countDownTime 当前更新次数
     */
    private PetFoodConsumeVO consumePlateFood(Integer companyId, Integer userId, Date shouldConsumeTime, Integer petFoodPlateFeedDetailId, Integer petFoodConsumeDetailId, Integer countDownTime) {
        PetFoodPlateEntity petFoodPlate = petFoodPlateMapper.getPeteFoodPlateByUser(companyId, userId);
        //喂养该次的总数量
        PetFoodPlateDetailEntity petFoodPlateFeedEntity = petFoodPlateDetailMapper.getById(petFoodPlateFeedDetailId);
        //喂养被消耗的数量
        PetFoodPlateDetailEntity petFoodPlateDetailEntity = petFoodPlateDetailMapper.getById(petFoodConsumeDetailId);
        //盘中剩余
        Integer petFoodLeft = petFoodPlate.getFoodCount();
        Integer consumeTime = 0;
        Integer result = 0;
        //应该减少次数多于剩余食物量则消耗剩余食物量,否则消耗应该减少量
        if (countDownTime > petFoodLeft) {
            consumeTime = petFoodLeft;
        } else {
            consumeTime = countDownTime;
        }
        //上次主人自己投喂剩余的可消耗数量
        Integer leftFeedCount = petFoodPlateFeedEntity.getFoodCount() - petFoodPlateDetailEntity.getFoodCount();
        if (consumeTime > leftFeedCount) {
            consumeTime = leftFeedCount;
        }
        petFoodLeft = petFoodLeft - consumeTime;
        petFoodPlate.setFoodCount(petFoodLeft);
        if (petFoodConsumeDetailId != null) {
            Integer consumeCount = petFoodPlateDetailEntity.getFoodCount() + consumeTime;
            if (petFoodPlateFeedEntity.getFoodCount().equals(consumeCount)) {
                petFoodPlateFeedEntity.setStatus(StatusEnum.DELETE.getValue());
                Date updateTime = DateUtil.getSecondLaterDate(petFoodPlateFeedEntity.getUpdateTime(), consumeTime * EVERY_SECONDS_CONSUME);
                petFoodPlateFeedEntity.setUpdateTime(updateTime);
                petFoodPlateDetailMapper.updatePetFoodDetail(petFoodPlateFeedEntity);
            }
            petFoodPlateDetailEntity.setFoodCount(consumeCount);
            petFoodPlateDetailEntity.setNextPlanConsumeTime(shouldConsumeTime);
            petFoodPlateDetailMapper.updatePetFoodDetail(petFoodPlateDetailEntity);
            petFoodPlateMapper.updatePetFoodPlate(petFoodPlate);
        }
        if (countDownTime > consumeTime) {
            result = 1;
        }
        PetFoodConsumeVO petFoodConsumeVO = new PetFoodConsumeVO();
        petFoodConsumeVO.setConsumeTime(consumeTime);
        petFoodConsumeVO.setResult(result);
        //宠物体重增加
        updatePetWeight(companyId, userId, consumeTime);
        return petFoodConsumeVO;
    }

    /**
     * 消耗其他人的投喂
     */
    private Integer consumeOtherFeed(Integer companyId, Integer userId, PetFoodConsumeVO consumeReuslt, Integer leftConsumeCount) {
        List<PetFoodPlateDetailEntity> petFoodPlateDetailList = petFoodPlateDetailMapper.getOtherFeedList(companyId, userId);
        if (CollectionUtils.isEmpty(petFoodPlateDetailList)) {
            return 0;
        }
        Date nextConsumeTime = consumeReuslt.getNextConsumeTime();
        Integer consumeCount = 0;
        for (int i = 0; i < petFoodPlateDetailList.size(); i++) {
            PetFoodPlateDetailEntity petFoodPlateDetail = petFoodPlateDetailList.get(i);
            //是否有消耗次数,从最早的开始消耗
            PetFoodPlateDetailEntity petFoodPlateConsumeDetail = petFoodPlateDetailMapper.getPlateConsumeRecord(companyId, userId,petFoodPlateDetail.getId(), StatusEnum.OK.getValue());
            if (petFoodPlateConsumeDetail != null) {
                //当前投喂消耗的饲料量
                Integer petFoodOtherConsumeCount = petFoodPlateConsumeDetail.getFoodCount();
                //别人投喂的饲料量
                Integer petFoodOtherFeedCount = petFoodPlateDetail.getFoodCount();
                //当前投喂还可以消耗的饲料量
                Integer leftCanConsumeCount = petFoodOtherFeedCount - petFoodOtherConsumeCount;
                if (leftConsumeCount >= consumeCount + leftCanConsumeCount) {
                    consumeCount = consumeCount + leftCanConsumeCount;
                    //把当前的消耗完
                    petFoodPlateConsumeDetail.setUpdateTime(new Date());
                    nextConsumeTime = DateUtil.getSecondLaterDate(consumeReuslt.getNextConsumeTime(), leftCanConsumeCount * EVERY_SECONDS_CONSUME);
                    consumeReuslt.setNextConsumeTime(nextConsumeTime);
                    petFoodPlateConsumeDetail.setFoodCount(petFoodOtherConsumeCount + leftCanConsumeCount);
                    petFoodPlateConsumeDetail.setNextPlanConsumeTime(nextConsumeTime);
                    petFoodPlateConsumeDetail.setStatus(StatusEnum.DELETE.getValue());
                    petFoodPlateDetailMapper.updatePetFoodDetail(petFoodPlateConsumeDetail);
                    PetFoodPlateEntity petFoodPlateEntity = petFoodPlateMapper.getPeteFoodPlateByUser(companyId, userId);
                    //更新盘中食物剩余
                    Integer leftPlateCount = petFoodPlateEntity.getFoodCount();
                    leftPlateCount = leftPlateCount - leftCanConsumeCount;
                    petFoodPlateEntity.setFoodCount(leftPlateCount);
                    petFoodPlateEntity.setUpdateTime(new Date());
                    petFoodPlateMapper.updatePetFoodPlate(petFoodPlateEntity);
                    //更新宠物体重
                    updatePetWeight(companyId, userId, leftCanConsumeCount);

                    petFoodPlateDetail.setStatus(StatusEnum.DELETE.getValue());
                    petFoodPlateDetailMapper.updatePetFoodDetail(petFoodPlateDetail);

                    if (leftConsumeCount == consumeCount + leftCanConsumeCount) {
                        return 0;
                    }
                } else {
                    Integer curConsumeCount = leftConsumeCount - consumeCount;
                    consumeCount = leftConsumeCount;
                    if (curConsumeCount > 0) {
                        Integer foodCount = petFoodPlateConsumeDetail.getFoodCount();
                        foodCount = foodCount + curConsumeCount;
                        petFoodPlateConsumeDetail.setUpdateTime(new Date());
                        nextConsumeTime = DateUtil.getSecondLaterDate(consumeReuslt.getNextConsumeTime(), leftCanConsumeCount * EVERY_SECONDS_CONSUME);
                        consumeReuslt.setNextConsumeTime(nextConsumeTime);
                        petFoodPlateConsumeDetail.setFoodCount(foodCount);
                        petFoodPlateConsumeDetail.setNextPlanConsumeTime(nextConsumeTime);
                        petFoodPlateConsumeDetail.setStatus(StatusEnum.DELETE.getValue());
                        petFoodPlateDetailMapper.updatePetFoodDetail(petFoodPlateConsumeDetail);

                        if (curConsumeCount.equals(EVERY_GAIN_COUNT)) {
                            petFoodPlateDetail.setStatus(StatusEnum.DELETE.getValue());
                            petFoodPlateDetailMapper.updatePetFoodDetail(petFoodPlateDetail);
                        }

                        //更新盘中食物剩余
                        PetFoodPlateEntity petFoodPlateEntity = petFoodPlateMapper.getPeteFoodPlateByUser(companyId, userId);
                        Integer leftPlateCount = petFoodPlateEntity.getFoodCount();
                        leftPlateCount = leftPlateCount - curConsumeCount;
                        petFoodPlateEntity.setFoodCount(leftPlateCount);
                        petFoodPlateEntity.setUpdateTime(new Date());
                        petFoodPlateMapper.updatePetFoodPlate(petFoodPlateEntity);
                        updatePetWeight(companyId, userId, curConsumeCount);
                        return 0;
                    }
                }
            } else {
                //别人投喂的饲料量
                Integer petFoodOtherFeedCount = petFoodPlateDetail.getFoodCount();
                Integer curConsumeCount = 0;
                //当前投喂还可以消耗的饲料量
                if (leftConsumeCount >= consumeCount + EVERY_GAIN_COUNT) {
                    curConsumeCount = EVERY_GAIN_COUNT;
                    consumeCount = consumeCount + EVERY_GAIN_COUNT;
                    //public PetFoodPlateDetailEntity(Integer companyId, Integer userId, Integer feedUserId, Integer foodCount, Integer type, Date createTime, Date updateTime)
                    PetFoodPlateDetailEntity petFoodPlateConsumeNewDetail = new PetFoodPlateDetailEntity(companyId, userId, null, EVERY_GAIN_COUNT, StatusEnum.OK.getValue(), new Date(), new Date());
                    petFoodPlateConsumeNewDetail.setConsumePlateId(petFoodPlateDetail.getId());
                    petFoodPlateDetailMapper.createPetFoodPlateDetail(petFoodPlateConsumeNewDetail);

                    petFoodPlateDetail.setStatus(StatusEnum.DELETE.getValue());
                    petFoodPlateDetailMapper.updatePetFoodDetail(petFoodPlateDetail);

                    nextConsumeTime = DateUtil.getSecondLaterDate(consumeReuslt.getNextConsumeTime(), EVERY_GAIN_COUNT * EVERY_SECONDS_CONSUME);
                    consumeReuslt.setNextConsumeTime(nextConsumeTime);
                    updatePetWeight(companyId, userId, curConsumeCount);
                } else {
                    curConsumeCount = leftConsumeCount - consumeCount;
                    PetFoodPlateDetailEntity petFoodPlateConsumeNewDetail = new PetFoodPlateDetailEntity(companyId, userId, null, EVERY_GAIN_COUNT, StatusEnum.OK.getValue(), new Date(), new Date());
                    petFoodPlateConsumeNewDetail.setConsumePlateId(petFoodPlateDetail.getId());
                    petFoodPlateDetailMapper.createPetFoodPlateDetail(petFoodPlateConsumeNewDetail);
                    nextConsumeTime = DateUtil.getSecondLaterDate(consumeReuslt.getNextConsumeTime(), curConsumeCount * EVERY_SECONDS_CONSUME);
                    consumeReuslt.setNextConsumeTime(nextConsumeTime);

                    if (curConsumeCount.equals(EVERY_GAIN_COUNT)) {
                        petFoodPlateDetail.setStatus(StatusEnum.DELETE.getValue());
                        petFoodPlateDetailMapper.updatePetFoodDetail(petFoodPlateDetail);
                    }

                    updatePetWeight(companyId, userId, curConsumeCount);
                }

            }
        }
        return 1;
    }


    private void updatePetWeight(Integer companyId, Integer userId, Integer consumeTime) {
        PetWeightEntity petWeightEntity = petWeightMapper.getPetWeight(companyId, userId);
        if (petWeightEntity != null) {
            Integer petWeightCount = petWeightEntity.getWeight() + consumeTime;
            petWeightEntity.setWeight(petWeightCount);
            petWeightMapper.updatePetWeight(petWeightEntity);
        } else {
            petWeightEntity = new PetWeightEntity(companyId, userId, consumeTime, new Date(), new Date());
            petWeightMapper.createPetWeight(petWeightEntity);
        }

    }

}
