package com.enterprise.service.grain.impl;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.MobileLoginUser;
import com.enterprise.base.common.Page;
import com.enterprise.base.common.PageEntity;
import com.enterprise.base.common.ResultJson;
import com.enterprise.base.entity.*;
import com.enterprise.base.vo.GrainActivityDetailVO;
import com.enterprise.base.vo.GrainActivityVO;
import com.enterprise.base.vo.GrainUserDonateDetailVO;
import com.enterprise.lock.RedisLockUtil;
import com.enterprise.mapper.companyInfo.CompanyInfoMapper;
import com.enterprise.mapper.grain.GrainActivityMapper;
import com.enterprise.mapper.grain.GrainBrandMapper;
import com.enterprise.mapper.grain.UserGrainActivityMapper;
import com.enterprise.mapper.pet.PetWeightMapper;
import com.enterprise.mapper.users.UserMapper;
import com.enterprise.mapper.users.UserXCompanyMapper;
import com.enterprise.service.grain.GrainService;
import com.enterprise.service.redis.RedisService;
import com.enterprise.util.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @Description
 * @Author zezhouyang
 * @Date 19/2/12 上午11:38
 */
@Service
public class GrainServiceImpl implements GrainService {

    @Resource
    private GrainActivityMapper grainActivityMapper;

    @Resource
    private PetWeightMapper petWeightMapper;

    @Resource
    private GrainBrandMapper grainBrandMapper;

    @Resource
    private UserGrainActivityMapper userGrainActivityMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private CompanyInfoMapper companyInfoMapper;

    @Resource
    private UserXCompanyMapper userXCompanyMapper;

    @Resource
    private RedisService redisService;

    private static final String ACTIVE_DONATE_KEY = "active_";

    private Long defaultExpire = 1000000L;

    @Override
    public JSONObject getGrainActivityList(PageEntity pageEntity) {
        MobileLoginUser mobileLoginUser = MobileLoginUser.getUser();
        Integer companyId = mobileLoginUser.getCompanyID();
        Integer userId = mobileLoginUser.getUserID();
        PetWeightEntity petWeightEntity = petWeightMapper.getPetWeight(companyId, userId);
        Integer petWeight = 0;
        if (petWeightEntity != null) {
            petWeight = petWeightEntity.getWeight();
        }
        PageHelper.startPage(pageEntity.getPageNo(), pageEntity.getPageSize());
        PageInfo<GrainActivityVO> pageInfo = new PageInfo<>(grainActivityMapper.getActivityList());
        List<GrainActivityVO> grainActivityList = pageInfo.getList();
        for (int i = 0; i < grainActivityList.size(); i++) {
            GrainActivityVO grainActivityVO = grainActivityList.get(i);
            Integer activityId = grainActivityVO.getId();
            Integer grainCost = grainActivityVO.getGrainCost();
            Integer needGrain = 0;
            if (petWeight >= grainCost) {
                needGrain = 0;
            } else {
                needGrain = grainCost - petWeight;
            }
            grainActivityVO.setNeedCost(needGrain);

            //计算状态
            Integer totalSponsorCount = userGrainActivityMapper.getSponsorCountByActivityId(activityId);
            Integer grainCount = grainActivityVO.getGrainCount() - totalSponsorCount;
            if (grainCount < 0) {
                grainCount = 0;
            }
            Integer grainStatus = 0;
            UserGrainActivityEntity userGrainActivityEntity = userGrainActivityMapper.getByCompanyAndUser(companyId, activityId, userId);
            if (userGrainActivityEntity != null) {
                grainStatus = 2;
            } else if (grainCount.equals(0)) {
                grainStatus = 3;
            } else {
                grainStatus = 1;
            }
            grainActivityVO.setStatus(grainStatus);

        }
        Map<String, Object> dataMap = Maps.newHashMap();
        dataMap.put("list", pageInfo.getList());
        dataMap.put("total", pageInfo.getTotal());
        dataMap.put("page", new Page(pageInfo.getPrePage(), pageEntity.getPageNo(), pageInfo.getNextPage(), pageInfo.getLastPage()));
        return ResultJson.succResultJson(dataMap);
    }

    @Override
    public JSONObject getGrainActivityDetail(Integer activityId) {
        MobileLoginUser mobileLoginUser = MobileLoginUser.getUser();
        Integer companyId = mobileLoginUser.getCompanyID();
        Integer userId = mobileLoginUser.getUserID();
        GrainActivityDetailVO grainActivityDetailVO = new GrainActivityDetailVO();
        GrainActivityEntity grainActivityEntity = grainActivityMapper.getById(activityId);
        try {
            Integer brandId = grainActivityEntity.getGrainBrandId();
            BeanUtils.copyProperties(grainActivityDetailVO, grainActivityEntity);
            GrainBrandEntity grainBrandEntity = grainBrandMapper.getById(brandId);
            grainActivityDetailVO.setGrainBrand(grainBrandEntity.getGrainBrand());
            grainActivityDetailVO.setGrainSponsor(grainBrandEntity.getGrainSponsor());
            grainActivityDetailVO.setGrainLogo(grainActivityEntity.getGrainDetailLogo());
            //计算剩余数量
            Integer totalSponsorCount = userGrainActivityMapper.getSponsorCountByActivityId(activityId);
            Integer grainCount = grainActivityEntity.getGrainCount() - totalSponsorCount;
            if (grainCount < 0) {
                grainCount = 0;
            }
            Integer grainStatus = 0;
            UserGrainActivityEntity userGrainActivityEntity = userGrainActivityMapper.getByCompanyAndUser(companyId, activityId, userId);
            if (userGrainActivityEntity != null) {
                grainStatus = 2;
            } else if (grainCount.equals(0)) {
                grainStatus = 3;
            } else {
                grainStatus = 1;
            }
            grainActivityDetailVO.setGrainCount(grainCount);
            grainActivityDetailVO.setStatus(grainStatus);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return ResultJson.succResultJson(grainActivityDetailVO);
    }


    @Override
    public JSONObject getCertificateDetail(Integer activityId) {
        MobileLoginUser mobileLoginUser = MobileLoginUser.getUser();
        Integer companyId = mobileLoginUser.getCompanyID();
        Integer userId = mobileLoginUser.getUserID();
        UserGrainActivityEntity userGrainActivityEntity = userGrainActivityMapper.getByCompanyAndUser(companyId, activityId, userId);
        GrainActivityEntity grainActivityEntity = grainActivityMapper.getById(activityId);
        if (grainActivityEntity == null) {
            return ResultJson.errorResultJson("不存在的活动");
        }
        if (userGrainActivityEntity == null) {
            return ResultJson.errorResultJson("未获得证书");
        }
        GrainBrandEntity grainBrandEntity = grainBrandMapper.getById(grainActivityEntity.getGrainBrandId());
        String dateTime = DateUtil.getYearMonthDay(userGrainActivityEntity.getCreateTime());
//        String grainCost = grainActivityEntity.getGrainCost()/1000 +"Kg";
//        String grainExplain = String.format(grainActivityEntity.getCertificateContent(), dateTime, grainCost, grainActivityEntity.getDonatedBase());
        String grainExplain = String.format(grainActivityEntity.getCertificateContent(), dateTime);
        GrainUserDonateDetailVO grainUserDonateDetailVO = new GrainUserDonateDetailVO(userGrainActivityEntity.getId(), activityId, mobileLoginUser.getDingName(), mobileLoginUser.getAvatar(), grainExplain, userGrainActivityEntity.getCertificateNo(), grainBrandEntity.getGrainSponsor(), grainActivityEntity.getGrainCost());
        //Integer id, String name, String avatar, String grainExplain, String certificateNo, String grainSponsor, Integer grainCost, Integer activityId
        return ResultJson.succResultJson(grainUserDonateDetailVO);
    }

    @Override
    public JSONObject getCertificateDetailNoLogin(Integer id) {
        UserGrainActivityEntity userGrainActivityEntity = userGrainActivityMapper.getById(id);
        Integer activityId = userGrainActivityEntity.getActivityId();
        Integer userId = userGrainActivityEntity.getUserId();
        GrainActivityEntity grainActivityEntity = grainActivityMapper.getById(activityId);
        if (grainActivityEntity == null) {
            return ResultJson.errorResultJson("不存在的活动");
        }
        GrainBrandEntity grainBrandEntity = grainBrandMapper.getById(grainActivityEntity.getGrainBrandId());
        String dateTime = DateUtil.getYearMonthDay(userGrainActivityEntity.getCreateTime());
        String grainExplain = String.format(grainActivityEntity.getCertificateContent(), dateTime);
        UserEntity userEntity = userMapper.getUserById(userId);
        GrainUserDonateDetailVO grainUserDonateDetailVO = new GrainUserDonateDetailVO(userGrainActivityEntity.getId(), activityId, userEntity.getName(), userEntity.getAvatar(), grainExplain, userGrainActivityEntity.getCertificateNo(), grainBrandEntity.getGrainSponsor(), grainActivityEntity.getGrainCost());
        //Integer id, String name, String avatar, String grainExplain, String certificateNo, String grainSponsor, Integer grainCost, Integer activityId
        return ResultJson.succResultJson(grainUserDonateDetailVO);
    }

    @Override
    public JSONObject getCertificateList() {
        MobileLoginUser mobileLoginUser = MobileLoginUser.getUser();
        Integer companyId = mobileLoginUser.getCompanyID();
        Integer userId = mobileLoginUser.getUserID();
        List<UserGrainActivityEntity> userGrainActivityList = userGrainActivityMapper.getListByUser(companyId, userId);
        List<GrainUserDonateDetailVO> resultList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(userGrainActivityList)) {
            for (int i = 0; i < userGrainActivityList.size(); i++) {
                UserGrainActivityEntity userGrainActivity = userGrainActivityList.get(i);
                Integer activityId = userGrainActivity.getActivityId();
                GrainActivityEntity grainActivityEntity = grainActivityMapper.getById(activityId);
                if (grainActivityEntity == null) {
                    continue;
                }
                GrainBrandEntity grainBrandEntity = grainBrandMapper.getById(grainActivityEntity.getGrainBrandId());
                String dateTime = DateUtil.getYearMonthDay(userGrainActivity.getCreateTime());
//                String grainCost = grainActivityEntity.getGrainCost()/1000 +"Kg";
//                String grainExplain = String.format(grainActivityEntity.getCertificateContent(), dateTime, grainCost, grainActivityEntity.getDonatedBase());
                String grainExplain = String.format(grainActivityEntity.getCertificateContent(), dateTime);
                GrainUserDonateDetailVO grainUserDonateDetailVO = new GrainUserDonateDetailVO(userGrainActivity.getId(), activityId, mobileLoginUser.getDingName(), mobileLoginUser.getAvatar(), grainExplain, userGrainActivity.getCertificateNo(), grainBrandEntity.getGrainSponsor(), grainActivityEntity.getGrainCost());
                //Integer id, Integer activityId, String name, String avatar, String grainExplain, String certificateNo, String grainSponsor, Integer grainCost
                resultList.add(grainUserDonateDetailVO);
            }
        }
        return ResultJson.succResultJson(resultList);
    }

    @Override
    public JSONObject grainDonate(Integer activityId) {
        MobileLoginUser mobileLoginUser = MobileLoginUser.getUser();
        Integer companyId = mobileLoginUser.getCompanyID();
        Integer userId = mobileLoginUser.getUserID();
        //判断爱心粮是否够
        GrainActivityEntity grainActivityEntity = grainActivityMapper.getById(activityId);
        if (grainActivityEntity == null) {
            return ResultJson.errorResultJson("不存在的活动");
        }
        Integer grainCost = grainActivityEntity.getGrainCost();
        PetWeightEntity petWeightEntity = petWeightMapper.getPetWeight(companyId, userId);
        if (petWeightEntity == null) {
            return ResultJson.errorResultJson("宠物体重未初始化");
        }
        Integer petWeight = petWeightEntity.getWeight();
        if (petWeight < grainCost) {
            return ResultJson.errorResultJson("爱心值不够");
        }

        //分布式锁
//        System.out.println("开始获得锁");
        RedisLockUtil redisLockUtil = new RedisLockUtil(defaultExpire);
        redisLockUtil.lock(ACTIVE_DONATE_KEY + activityId, String.valueOf(activityId), null);
//        System.out.println("正常执行");
        try {
            UserGrainActivityEntity userGrainActivityEntity = userGrainActivityMapper.getByCompanyAndUser(companyId, activityId, userId);
            if (userGrainActivityEntity != null) {
                return ResultJson.errorResultJson("您已经参加过捐助");
            }
            // synchronized (this){
            Integer sponsorCount = userGrainActivityMapper.getSponsorCountByActivityId(activityId);
            if (sponsorCount == null) {
                sponsorCount = 0;
            }
            Integer totalCount = grainActivityEntity.getGrainCount();
            if (sponsorCount >= totalCount) {
                return ResultJson.errorResultJson("已领完");
            }
            //宠物体重减少
            petWeight = petWeight - grainCost;
            petWeightEntity.setWeight(petWeight);
            petWeightEntity.setUpdateTime(new Date());
            petWeightMapper.updatePetWeight(petWeightEntity);
            StringBuffer certNoBuf = new StringBuffer();
            //插入用户活动
            String certNo = String.format("%06d", ++sponsorCount);
            certNoBuf.append("NO.").append(grainActivityEntity.getCertificate()).append(certNo);
            String userCertNo = certNoBuf.toString();
            //public UserGrainActivityEntity(Integer companyId, Integer activityId, Integer userId, Integer grainCost, String certificateNo, Date createTime) {
            UserGrainActivityEntity userGrainActivityEntityForSave = new UserGrainActivityEntity(companyId, activityId, userId, grainCost, userCertNo, new Date());
            Integer result = userGrainActivityMapper.createUserGrainActivity(userGrainActivityEntityForSave);


            if (result > 0) {
                return ResultJson.succResultJson(grainCost);
            }
            return ResultJson.errorResultJson(activityId);
        } finally {
            redisLockUtil.unlock(ACTIVE_DONATE_KEY + activityId);
        }
        // }
    }

    @Override
    public JSONObject getUserTotalData() {
        Integer companyCount = companyInfoMapper.getAllCompanys().size();
        Integer validateUserCount = userXCompanyMapper.getTotalActiveUserCount(null);
        Map<String, Integer> resultMap = new HashMap<>();
        String resultData = String.valueOf(validateUserCount);
        StringBuffer userCountBuf = new StringBuffer();
        userCountBuf.append(resultData.substring(0,resultData.length()-1)).append(new Random().nextInt(10)).append(resultData.substring(resultData.length()-1,resultData.length()));
        resultMap.put("grainCount", companyCount);
        resultMap.put("userCount", Integer.valueOf(userCountBuf.toString()));
        return ResultJson.succResultJson(resultMap);
    }

    @Override
    public JSONObject grainDonateRelease(Integer activityId) {
        String key = ACTIVE_DONATE_KEY + activityId;
        if(redisService.getnx(key)!=null){
            redisService.releasenx(key);
        }
        return ResultJson.succResultJson(activityId);
    }

    public static void main(String[] args) {
//        String str = String.format("%06d", 123);
//        System.out.println(str);
        String  resultData = "4867";
//        String s1 = resultData.substring(0,resultData.length()-1);
//        String s2 = resultData.substring(resultData.length()-1,resultData.length());
//        Integer result = new Random().nextInt(10);
        StringBuffer userCountBuf = new StringBuffer();
        userCountBuf.append(resultData.substring(0,resultData.length()-1)).append(new Random().nextInt(10)).append(resultData.substring(resultData.length()-1,resultData.length()));
        System.out.println(userCountBuf.toString());
    }

}
