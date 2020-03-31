package com.enterprise.service.paperBall.impl;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.GlobalConstant;
import com.enterprise.base.common.MobileLoginUser;
import com.enterprise.base.common.PetFoodConstant;
import com.enterprise.base.common.ResultJson;
import com.enterprise.base.entity.*;
import com.enterprise.base.entity.bury.OASendBuryEntity;
import com.enterprise.base.entity.bury.PetRaiseBuryEntity;
import com.enterprise.base.enums.DynamicEnum;
import com.enterprise.base.enums.PetFoodGainEnum;
import com.enterprise.base.enums.StatusEnum;
import com.enterprise.base.enums.bury.OABuryEnum;
import com.enterprise.base.enums.bury.PetRaiseEnum;
import com.enterprise.base.vo.PaperBallVO;
import com.enterprise.base.vo.UserPetVO;
import com.enterprise.mapper.bury.OASendBuryMapper;
import com.enterprise.mapper.bury.PetRaiseBuryMapper;
import com.enterprise.mapper.paperBall.PaperBallMapper;
import com.enterprise.mapper.pet.PetDynamicMapper;
import com.enterprise.mapper.petFood.PetFoodDetailMapper;
import com.enterprise.mapper.petFood.PetFoodMapper;
import com.enterprise.mapper.users.UserConfigMapper;
import com.enterprise.mapper.users.UserMapper;
import com.enterprise.mapper.users.UserXCompanyMapper;
import com.enterprise.oa.OAMessageUtil;
import com.enterprise.service.isvTickets.IsvTicketsService;
import com.enterprise.service.paperBall.PaperBallService;
import com.enterprise.util.DateUtil;
import com.enterprise.util.oa.message.OAMessage;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @Description 纸团逻辑
 * @Author zezhouyang
 * @Date 18/10/23 上午10:55
 */
@Service
public class PaperBallServiceImpl implements PaperBallService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private PaperBallMapper paperBallMapper;

    @Resource
    private UserXCompanyMapper userXCompanyMapper;

    @Resource
    private IsvTicketsService isvTicketsService;

    @Resource
    private PetFoodMapper petFoodMapper;

    @Resource
    private PetFoodDetailMapper petFoodDetailMapper;

    @Resource
    private PetDynamicMapper petDynamicMapper;

    @Resource
    private PetRaiseBuryMapper petRaiseBuryMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private OASendBuryMapper oaSendBuryMapper;

    @Resource
    private UserConfigMapper userConfigMapper;

    private String CLEAN_MESSAGE = PetFoodConstant.CLEAN_MESSAGE;

    private static final String MAIN_PAGE = GlobalConstant.getIndexUrl();

    private static final String INDEX_PAGE = GlobalConstant.getUserIndexUrl();

    private String QRCODE_PAGE = GlobalConstant.getQrCOdePage();

    /**
     * 生成纸团
     */
    @Override
    public Integer genPaperBall(Integer companyId) {
        IsvTicketsEntity isvTicketsEntity = isvTicketsService.getIsvTicketByCompanyId(companyId);
        if (isvTicketsEntity == null) {
            return 0;
        }
        String corpId = isvTicketsEntity.getCorpId();
        while (true) {
//            List<UserVO> userXCompanyList = userXCompanyMapper.getUserByCorpId(corpId, StatusEnum.OK.getValue());
            List<UserPetVO> userXCompanyList = userXCompanyMapper.getUserRaisePet(corpId);
            if (userXCompanyList.size() <= 0) {
                return 0;
            }
            //如果该公司全部人员的球的数量都=3则退出,既没有人球数量<3
            Integer totalCount = 0;
            for (int i = 0; i < userXCompanyList.size(); i++) {
                Integer userId = userXCompanyList.get(i).getUserId();
                Integer existPaperBallCount = paperBallMapper.getExistPaperBallCount(companyId, userId);
                if (existPaperBallCount >= 3) {
                    totalCount++;
                    if (i == userXCompanyList.size() - 1 && totalCount.equals(userXCompanyList.size())) {
                        return 0;
                    } else {
                        continue;
                    }
                } else {
                    break;
                }
            }
            Integer totalUserCount = userXCompanyList.size();
            Integer randomCount = new Random().nextInt(totalUserCount);
            //随机选中的用户
            UserPetVO chooseUserVO = userXCompanyList.get(randomCount);
            Integer chooseUserId = chooseUserVO.getUserId();
            Integer existPaperBallCount = paperBallMapper.getExistPaperBallCount(companyId, chooseUserId);
            if (existPaperBallCount >= 3) {
                continue;
            } else {
                //获取当前未获取到的纸团列表
                Integer ballType = 0;
                List<Integer> paperBallType = paperBallMapper.getUserNotGainPaperBallList(companyId, chooseUserId);
                if (CollectionUtils.isNotEmpty(paperBallType)) {
                    Integer chooseType = new Random().nextInt(paperBallType.size());
                    ballType = paperBallType.get(chooseType);
                }
                PaperBallEntity paperBallEntity = new PaperBallEntity(companyId, chooseUserId, ballType, null, new Date(), null, null, StatusEnum.OK.getValue());
                paperBallMapper.createPaperBall(paperBallEntity);
                return 1;
            }
        }
    }

    @Override
    public List<PaperBallEntity> getExpiredPaper() {
        return paperBallMapper.getExpiredPaper();
    }

    @Override
    public Integer batchUpdate(List<PaperBallEntity> paperBallList) {
        return paperBallMapper.batchUpdate(paperBallList);
    }


    @Override
    public JSONObject cleanBallTest(Integer companyId, Integer userId, Integer ballId) {
        try {
            PaperBallEntity paperBallEntity = paperBallMapper.getById(ballId);
            if (paperBallEntity != null && paperBallEntity.getStatus().equals(StatusEnum.DELETE.getValue())) {
                return ResultJson.kickedutResultJson("你来晚了!");
            }
            String dateTime = DateUtil.getCurrentDayStr();
            //获取是谁的球
            Integer ballUserId = paperBallEntity.getUserId();
            //查看当前是否还有清扫次数
            Integer cleanCount = paperBallMapper.getCleanCount(companyId, ballUserId, userId, dateTime);
            if (cleanCount != null && cleanCount >= 3) {
                return ResultJson.errorResultJson("没有次数了!");
            }
            // public PaperBallEntity(Integer id, Integer cleanUserId, Date cleanTime, Integer status) {
            Integer result = 1;
            if (result > 0) {
                Integer foodCount = genFood(companyId, userId);
                //sendOA(companyId, userId, ballId);
                //saveDynamic(ballId, foodCount);
                //saveRaisePetBury(companyId, userId, PetRaiseEnum.CLEAN.getValue(), new Date());
                //saveOASendBury(companyId, userId, OABuryEnum.CLEAN.getValue(), new Date());
                return ResultJson.succResultJson(foodCount);
            }
            //产生食物,发送OA消息
            return ResultJson.errorResultJson(ballId);
        }catch (Exception e){
            return ResultJson.succResultJson("小错误");
        }
    }

    @Override
    public JSONObject cleanBall(Integer ballId) {
        PaperBallEntity paperBallEntity = paperBallMapper.getById(ballId);
        if (paperBallEntity != null && paperBallEntity.getStatus().equals(StatusEnum.DELETE.getValue())) {
            return ResultJson.kickedutResultJson("你来晚了!");
        }
        MobileLoginUser mobileLoginUser = MobileLoginUser.getUser();
        Integer companyId = mobileLoginUser.getCompanyID();
        Integer userId = mobileLoginUser.getUserID();
        String dateTime = DateUtil.getCurrentDayStr();
        //获取是谁的球
        Integer ballUserId = paperBallEntity.getUserId();
        //查看当前是否还有清扫次数
        Integer cleanCount = paperBallMapper.getCleanCount(companyId, ballUserId, userId, dateTime);
        if (cleanCount != null && cleanCount >= 3) {
            return ResultJson.errorResultJson("没有次数了!");
        }
        // public PaperBallEntity(Integer id, Integer cleanUserId, Date cleanTime, Integer status) {
        Integer result = paperBallMapper.updatePaperBall(new PaperBallEntity(ballId, userId, new Date(), StatusEnum.DELETE.getValue()));
        if (result > 0) {
            Integer foodCount = genFood(companyId, userId);
            sendOA(companyId, userId, ballId);
            saveDynamic(ballId, foodCount);
            saveRaisePetBury(companyId, userId, PetRaiseEnum.CLEAN.getValue(), new Date());
            saveOASendBury(companyId, userId, OABuryEnum.CLEAN.getValue(), new Date());
            return ResultJson.succResultJson(foodCount);
        }
        //产生食物,发送OA消息
        return ResultJson.errorResultJson(ballId);
    }

    /**
     * 所有未消除的纸团类型
     * 纸团目前有3中类型
     */
    @Override
    public JSONObject setUncleanBallType() {
        Integer totalUpdateCount = 0;
        List<PaperBallEntity> allPaperBallUpdateList = new ArrayList<>();
        List<PaperBallEntity> paperBallGroupList = paperBallMapper.getExistPaperBallGroup();
        for (int i = 0; i < paperBallGroupList.size(); i++) {
            PaperBallEntity paperBallUser = paperBallGroupList.get(i);
            Integer companyId = paperBallUser.getCompanyId();
            Integer userId = paperBallUser.getUserId();
            List<PaperBallEntity> userPaperBallList = paperBallMapper.getActiveBallList(companyId, userId);
            for (int j = 0; j < userPaperBallList.size(); j++) {
                Integer ballType = j + 1;
                PaperBallEntity paperBallEntity = userPaperBallList.get(j);
                paperBallEntity.setBallType(ballType);
                allPaperBallUpdateList.add(paperBallEntity);
                totalUpdateCount++;
            }
        }
        Integer reuslt = paperBallMapper.batchUpdateType(allPaperBallUpdateList);
        return ResultJson.succResultJson(totalUpdateCount);
    }

    @Override
    public JSONObject getUserPaperBall() {
        List<PaperBallEntity> paperBallList = paperBallMapper.getActiveBallListAll();
        for (int i = 0; i < paperBallList.size(); i++) {
            PaperBallEntity paperBallEntitiy = paperBallList.get(i);
            IsvTicketsEntity isvEntity = isvTicketsService.getIsvTicketByCompanyId(paperBallEntitiy.getCompanyId());
            List<Integer> userList = new ArrayList<>(paperBallEntitiy.getUserId());
            List<Integer> userIdList = userXCompanyMapper.getRemainUserIdList2(isvEntity.getCorpId(), userList);
            Integer cuserId = userIdList.get(new Random().nextInt(userIdList.size()));
            String sss = paperBallEntitiy.getCompanyId() + "," + cuserId + "," + paperBallEntitiy.getId();
            System.out.println(sss);
        }
        return null;
    }

    @Override
    public JSONObject getFeedUser() {
        List<PetFoodEntity> petFoodList = petFoodMapper.getPetFoodList();
        for (int i = 0; i < petFoodList.size(); i++) {
            PetFoodEntity petFoodEntity = petFoodList.get(i);
            List<Integer> userList = new ArrayList<>(petFoodEntity.getUserId());
            IsvTicketsEntity isvTicketsEntity = isvTicketsService.getIsvTicketByCompanyId(petFoodEntity.getCompanyId());
            List<Integer> userIdList = userXCompanyMapper.getRemainUserIdList2(isvTicketsEntity.getCorpId(), userList);
            Integer cuserId = userIdList.get(new Random().nextInt(userIdList.size()));
            String sss = isvTicketsEntity.getCompanyId() + "," + petFoodEntity.getUserId() + "," + cuserId;
            System.out.println(sss);
        }
        return null;
    }


    private Integer saveDynamic(Integer ballId, Integer foodCount) {
        PaperBallEntity paperBallEntity = paperBallMapper.getById(ballId);
        Boolean selfClean = false;
        Integer activeId = DynamicEnum.OTHER_CLEAN.getValue();
        if (paperBallEntity.getUserId().equals(paperBallEntity.getCleanUserId())) {
            activeId = DynamicEnum.SELF_CLEAN.getValue();
            selfClean = true;
        }
        //判断是否有保存过动态
        PetDynamicEntity petDynamicEntity = petDynamicMapper.getPetDynamic(paperBallEntity.getCompanyId(), paperBallEntity.getUserId(), activeId, ballId);
        if (petDynamicEntity != null) {
            return 0;
        }
        String userName = userMapper.getNameById(paperBallEntity.getCleanUserId());
        String contentModel = DynamicEnum.getDynamicEnum(activeId).getDynamic();
        if (selfClean) {
            contentModel = String.format(contentModel, foodCount);
        } else {
            contentModel = String.format(contentModel, userName);
        }
        if (petDynamicEntity == null) {
            petDynamicEntity = new PetDynamicEntity(paperBallEntity.getCompanyId(), paperBallEntity.getUserId(), activeId, ballId, contentModel, paperBallEntity.getCleanTime(), StatusEnum.DELETE.getValue(), StatusEnum.OK.getValue());
            return petDynamicMapper.createPetDynamic(petDynamicEntity);
        }
        return 0;
    }

    private void saveRaisePetBury(Integer companyId, Integer userId, Integer type, Date createTime) {
        PetRaiseBuryEntity petRaiseBuryEntity = new PetRaiseBuryEntity(companyId, userId, type, createTime);
        petRaiseBuryMapper.createPetRaiseBury(petRaiseBuryEntity);
    }

    private void saveOASendBury(Integer companyId, Integer userId, Integer type, Date createTime) {
        OASendBuryEntity oaSendBuryEntity = new OASendBuryEntity(companyId, userId, type, createTime);
        oaSendBuryMapper.createOASendBury(oaSendBuryEntity);
    }

    @Override
    public JSONObject getActiveBallList(Integer userId) {
        MobileLoginUser mobileLoginUser = MobileLoginUser.getUser();
        Integer companyId = mobileLoginUser.getCompanyID();
//        Integer userId = mobileLoginUser.getUserID();
        List<PaperBallEntity> paperBallList = paperBallMapper.getActiveBallList(companyId, userId);
        PaperBallVO paperBallVO = new PaperBallVO();
        paperBallVO.setTotalCount(paperBallList.size());
        List<PaperBallVO.PaperBallDetailVO> paperBallDetailList = new ArrayList<>();
        if (paperBallList.size() > 0) {
            for (int i = 0; i < paperBallList.size(); i++) {
                PaperBallVO outClass = new PaperBallVO();
                PaperBallVO.PaperBallDetailVO paperBallDetailVO = outClass.new PaperBallDetailVO();
                paperBallDetailVO.setId(paperBallList.get(i).getId());
                paperBallDetailVO.setCreateTime(paperBallList.get(i).getCreateTime());
                paperBallDetailList.add(paperBallDetailVO);
            }
        }
        paperBallVO.setPaperBallList(paperBallDetailList);
        return ResultJson.succResultJson(paperBallVO);
    }

    private Integer genFood(Integer companyId, Integer userId) {
        PetFoodEntity petFoodEntity = petFoodMapper.getPetFood(companyId, userId);
        Integer genFoodCount = (int) (Math.random() * 6 + 5);
        Integer remainFoodCount = petFoodEntity.getFoodCount() + genFoodCount;
        petFoodEntity.setFoodCount(remainFoodCount);

        //Integer companyId, Integer userId, Integer type, Integer foodCount, Date createTime, Date updateTime
        PetFoodDetailEntity petFoodDetailEntity = new PetFoodDetailEntity(companyId, userId, PetFoodGainEnum.CLEAN.getValue(), genFoodCount, new Date(), new Date());
        petFoodMapper.updatePetFood(petFoodEntity);
        petFoodDetailMapper.createPetFoodDetail(petFoodDetailEntity);
        return genFoodCount;
    }

    private void sendOA(Integer companyId, Integer userId, Integer ballId) {
        UserConfigEntity userConfigEntity = userConfigMapper.findByUserId(userId);
        if(userConfigEntity==null || (userConfigEntity!=null && userConfigEntity.getIsOa().equals(1))) {
            PaperBallEntity paperBallEntity = paperBallMapper.getById(ballId);
            if (userId.equals(paperBallEntity.getUserId())) {
                return;
            }
            IsvTicketsEntity isvTicketsEntity = isvTicketsService.getIsvTicketByCompanyId(companyId);
            String dingName = userMapper.getNameById(userId);
            String praiseTitle = String.format(CLEAN_MESSAGE, dingName);
            praiseTitle += "\n" + DateUtil.getDate_Y_M_D_H_M_S();
//                LinkOAMessage linkOAMessage = new LinkOAMessage();
            OAMessage oaMessage = new OAMessage();

            String mainPageBury = MAIN_PAGE + GlobalConstant.OA_BURY_TYPE + OABuryEnum.CLEAN.getValue();
//        String mainPageBury = INDEX_PAGE+"/"+userId+"/1"+GlobalConstant.OA_BURY_TYPE + OABuryEnum.CLEAN.getValue();
            String mainPage = String.format(mainPageBury, isvTicketsEntity.getCorpId());
            String mainPageQrcode = String.format(QRCODE_PAGE, URLEncoder.encode(mainPage));
            UserXCompany userXCompany = userXCompanyMapper.getDingIdByCorpIdAndUserId(isvTicketsEntity.getCorpId(), paperBallEntity.getUserId());
//                OAMessageUtil.sendOAMessageWithStroage(mobileLoginUser.getCompanyID(), userXCompany.getDingUserId(), "", oaMessage.getSimpleOAMessage(mainPage, mainPageQrcode, praiseTitle));
            OAMessageUtil.sendOAMessageWithStroageV2(companyId, userXCompany.getDingUserId(), "", oaMessage.getSimpleOAMessage(mainPage, mainPageQrcode, praiseTitle));
        }
    }

    public static void main(String[] args) {
    }

}
