package com.enterprise.service.bagTool.impl;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.*;
import com.enterprise.base.entity.*;
import com.enterprise.base.entity.bury.OASendBuryEntity;
import com.enterprise.base.enums.*;
import com.enterprise.base.enums.bury.OABuryEnum;
import com.enterprise.base.vo.BagToolRemainVO;
import com.enterprise.base.vo.MyPetVO;
import com.enterprise.base.vo.bagtool.*;
import com.enterprise.base.vo.dto.BagToolRateDTO;
import com.enterprise.cache.GetCache;
import com.enterprise.mapper.bagTool.*;
import com.enterprise.mapper.bury.OASendBuryMapper;
import com.enterprise.mapper.pet.MyPetMapper;
import com.enterprise.mapper.pet.PetDynamicMapper;
import com.enterprise.mapper.pet.PetWeightMapper;
import com.enterprise.mapper.users.UserConfigMapper;
import com.enterprise.mapper.users.UserMapper;
import com.enterprise.mapper.users.UserXCompanyMapper;
import com.enterprise.oa.OAMessageUtil;
import com.enterprise.service.bagTool.BagToolService;
import com.enterprise.service.isvTickets.IsvTicketsService;
import com.enterprise.service.redis.RedisService;
import com.enterprise.util.DateUtil;
import com.enterprise.util.oa.message.OAMessage;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.util.*;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/9/5 下午5:26
 */
@Service
public class BagToolServiceImpl implements BagToolService {

    private static Map<Integer, Integer> ratioMap = new HashMap<>();

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private BagToolPeopleMapper bagToolPeopleMapper;

    @Resource
    private BagToolMapper bagToolMapper;

    @Resource
    private BagToolDescMapper bagToolDescMapper;

    @Resource
    private BagToolRateMapper bagToolRateMapper;

    @Resource
    private BagToolEffectMapper bagToolEffectMapper;

//    @Resource
//    private UserXQuestionsMapper userXQuestionsMapper;

    @Resource
    private UserXCompanyMapper userXCompanyMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private MyPetMapper myPetMapper;

    @Resource
    private BagToolEffectQuestionsMapper bagToolEffectQuestionsMapper;

//    @Resource
//    private PetExperienceMapper petExperienceMapper;

    @Resource
    private IsvTicketsService isvTicketsService;

    @Resource
    private PetWeightMapper petWeightMapper;

    @Resource
    private PetDynamicMapper petDynamicMapper;

    @Resource
    private OASendBuryMapper oaSendBuryMapper;

    @Resource
    private UserConfigMapper userConfigMapper;

    @Resource
    private RedisService redisService;

    private Integer SEAL_COUNT = BagToolConstant.SEAL_COUNT;

    private final String MAIN_PAGE = GlobalConstant.getIndexUrl();

    private String QRCODE_PAGE = GlobalConstant.getQrCOdePage();

    private Integer GROWTH_LIQUID = BagToolConstant.GROWTH_LIQUID;

    private String BAGTOOL_KEY = RedisConstant.BAGTOOL_KEY;

    private static final Integer CORRECT_FOUR = BagToolGainTypeEnum.CORRECT_FOUR.getCount();
    private static final Integer CORRECT_TWELVE = BagToolGainTypeEnum.CORRECT_TWELVE.getCount();
    private static final Integer CORRECT_TWENTYFOUR = BagToolGainTypeEnum.CORRECT_TWENTYFOUR.getCount();

    @Override
    public List<BagToolPeopleVO> getUserToolList(Integer companyId, Integer userId) {
        List<BagToolVO> bigToolList = bagToolMapper.getAllTools();
        List<BagToolPeopleVO> resultList = new ArrayList<>();
        for (int i = 0; i < bigToolList.size(); i++) {
            BagToolVO bagToolVO = bigToolList.get(i);
            List<Integer> bagToolPeopleList = bagToolPeopleMapper.getToolIdByCompanyAndTool(companyId, bagToolVO.getId(), userId);
            Integer count = 0;
            if (CollectionUtils.isNotEmpty(bagToolPeopleList)) {
                count = bagToolPeopleList.size();
            }
            BagToolPeopleVO bagToolPeopelVO = new BagToolPeopleVO(bagToolVO.getId(), bagToolVO.getToolName(), bagToolVO.getDescript(), count);
            bagToolPeopelVO.setPeopleToolIdList(bagToolPeopleList);
            resultList.add(bagToolPeopelVO);
        }
        return resultList;
    }

    @Override
    public JSONObject getDescript() {
        BagToolDescVO bagToolDescVO = bagToolDescMapper.getOne();
        List<BagToolVO> bagToolList = bagToolMapper.getAllTools();
        bagToolDescVO.setToolList(bagToolList);
        return ResultJson.succResultJson(bagToolDescVO);
    }

    /**
     * @Param type 1/2 登陆/答题
     * type 1,2,3,4  每日登陆/累计4/累计12/累计24
     */
    @Override
    public BagDropVO dropBox(Integer type) {
        if (type < 0 || type > 5) {
            return new BagDropVO(null, StatusEnum.DELETE.getValue());
        }
        MobileLoginUser mobileLoginUser = MobileLoginUser.getUser();
        String currentDay = DateUtil.getCurrentDayStr();
        Integer result = 1;
        if (type == 1) {
            //首登是否掉落过
            result = bagToolPeopleMapper.checkIfGetTool(mobileLoginUser.getCompanyID(), BagToolGainTypeEnum.LOGIN.getValue(), mobileLoginUser.getUserID(), currentDay);
            if (result <= 0) {
                return new BagDropVO(BagToolGainTypeEnum.LOGIN.getValue(), StatusEnum.OK.getValue());
            } else {
                return new BagDropVO(BagToolGainTypeEnum.LOGIN.getValue(), StatusEnum.DELETE.getValue());
            }
        }
        return new BagDropVO(null, StatusEnum.DELETE.getValue());
    }

    @Override
    public JSONObject openBox(Integer type) {
        MobileLoginUser mobileLoginUser = MobileLoginUser.getUser();
        List<BagToolRateEntity> bagToolRateList = bagToolRateMapper.getByEventType(type);
        if (CollectionUtils.isEmpty(bagToolRateList)) {
            return ResultJson.errorResultJson("不存在的事件!");
        }
        List<BagToolRateDTO> ratoDTOList = new ArrayList<>();
        Integer start = 0;
        for (int i = 0; i < bagToolRateList.size(); i++) {
            BagToolRateEntity bagToolRate = bagToolRateList.get(i);
            Integer stageRate = bagToolRate.getRate();
            Integer startRange = start;
            Integer endRange = start + stageRate;
            start = endRange;
            BagToolRateDTO bagToolRateDTO = new BagToolRateDTO(type, bagToolRate.getToolId(), startRange, endRange);
            ratoDTOList.add(bagToolRateDTO);
        }
        //System.out.println(ratoDTOList.size());
        //计算概率

        Double rateNum = Math.random() * 100;
        for (int i = 0; i < ratoDTOList.size(); i++) {
            BagToolRateDTO bagToolRateDTO = ratoDTOList.get(i);
            if (bagToolRateDTO != null && !bagToolRateDTO.getStartRange().equals(bagToolRateDTO.getEndRange())) {
                if (rateNum >= bagToolRateDTO.getStartRange() && rateNum < bagToolRateDTO.getEndRange()) {
                    BagToolVO bagToolVO = getById(bagToolRateDTO.getToolId());
                    if (bagToolVO != null) {
                        Integer toolId = bagToolRateDTO.getToolId();
                        BagToolPeopleEntity bagToolPeopleEntity = new BagToolPeopleEntity(mobileLoginUser.getCompanyID(), mobileLoginUser.getUserID(), bagToolRateDTO.getToolId(), type, StatusEnum.OK.getValue(), new Date(), new Date());
                        System.out.println(bagToolPeopleEntity);
                        if (ratioMap.containsKey(toolId)) {
                            Integer toolHintCount = ratioMap.get(toolId);
                            ratioMap.put(toolId, ++toolHintCount);
                        } else {
                            ratioMap.put(toolId, 1);
                        }
                        Integer resultId = bagToolPeopleMapper.gainTool(bagToolPeopleEntity);
                        BagTreasureOpenVO bagTreasureOpenVO = new BagTreasureOpenVO(bagToolPeopleEntity.getId(), bagToolRateDTO.getToolId(), bagToolVO.getToolName(), bagToolVO.getDescript());
                        return ResultJson.succResultJson(bagTreasureOpenVO);
                    }
                }
            }
        }
        //printMapVale(ratioMap);
        return ResultJson.errorResultJson("error");
    }

    /**
     * 使用道具
     */
    @Override
    public JSONObject useTool(String toUserId, Integer toolPeopleId) {
        MobileLoginUser mobileLoginUser = MobileLoginUser.getUser();
        Integer companyId = mobileLoginUser.getCompanyID();
        Integer userId = mobileLoginUser.getUserID();
        UserXCompany userXCompany = userXCompanyMapper.getByCompanyIdANdDingUserId(mobileLoginUser.getCorpID(), toUserId);
        if (userXCompany == null) {
            return ResultJson.errorResultJson("不存在的用户");
        }
        Integer toolEffectUserId = userXCompany.getUserId();
        if (toolEffectUserId == null) {
            return ResultJson.errorResultJson("不存在的用户");
        }
        MyPetVO effectUserPetVO = myPetMapper.getMyPet(toolEffectUserId);
        if (effectUserPetVO == null) {
            return ResultJson.errorResultJson("Ta还没有领养小猫");
        }
        BagToolPeopleEntity bagToolPeopleEntity = bagToolPeopleMapper.getById(toolPeopleId);
        if (bagToolPeopleEntity == null) {
            return ResultJson.errorResultJson("不存在的道具!");
        }
        if (bagToolPeopleEntity != null && !bagToolPeopleEntity.getUserId().equals(userId)) {
            return ResultJson.errorResultJson("不是你的道具!");
        }
        Integer tooleId = bagToolPeopleEntity.getToolId();
        if (!(tooleId.equals(BagToolTypeEnum.RESURRECT.getValue()) || tooleId.equals(BagToolTypeEnum.RHESUS.getValue()) || tooleId.equals(BagToolTypeEnum.GROWTH_LIQUID.getValue())) && toolEffectUserId.equals(userId)) {
            return ResultJson.errorResultJson("不能作用于自己!");
        }
        //BagToolEffectEntity(Integer companyId, Integer toolId, Integer toolPeopleId, Integer userId,
        //Integer effectUserId, Integer status, Date createTime, Date updateTime)
        //首先判断是否丘比特,如果丘比特则先断裂之前的关系
        List<BagToolEffectEntity> startCupid = bagToolEffectMapper.getUserEffectedStatusSelf(companyId, userId, BagToolTypeEnum.CUPID.getValue());
        List<BagToolEffectEntity> endCupid = bagToolEffectMapper.getUserEffectedStatusOther(companyId, userId, BagToolTypeEnum.CUPID.getValue());

        List<BagToolEffectEntity> startOtherCupid = bagToolEffectMapper.getUserEffectedStatusSelf(companyId, userXCompany.getUserId(), BagToolTypeEnum.CUPID.getValue());
        List<BagToolEffectEntity> endOtherCupid = bagToolEffectMapper.getUserEffectedStatusOther(companyId, userXCompany.getUserId(), BagToolTypeEnum.CUPID.getValue());


        if (tooleId.equals(BagToolTypeEnum.CUPID.getValue())) {
            Integer updateCupidId = 0;
            if (CollectionUtils.isNotEmpty(startCupid) || CollectionUtils.isNotEmpty(endCupid) || CollectionUtils.isNotEmpty(startOtherCupid) || CollectionUtils.isNotEmpty(endOtherCupid)) {
                if (CollectionUtils.isNotEmpty(startCupid)) {
                    updateCupidId = startCupid.get(0).getId();
                } else if (CollectionUtils.isNotEmpty(endCupid)) {
                    updateCupidId = endCupid.get(0).getId();
                } else if (CollectionUtils.isNotEmpty(startOtherCupid)) {
                    updateCupidId = startOtherCupid.get(0).getId();
                } else if (CollectionUtils.isNotEmpty(endOtherCupid)) {
                    updateCupidId = endOtherCupid.get(0).getId();
                }
            }
            bagToolEffectMapper.dispelTool(updateCupidId, StatusEnum.DELETE.getValue());
        }


        BagToolEffectEntity bagToolEffectEntity = new BagToolEffectEntity(companyId, tooleId, toolPeopleId, userId, toolEffectUserId, StatusEnum.OK.getValue(), new Date(), new Date());
        //体重增加
        if (tooleId.equals(BagToolTypeEnum.GROWTH_LIQUID.getValue())) {
            PetWeightEntity petWeightEntity = petWeightMapper.getPetWeight(companyId, toolEffectUserId);
            if (petWeightEntity == null) {
                petWeightEntity = new PetWeightEntity(companyId, toolEffectUserId, GROWTH_LIQUID, new Date(), new Date());
                petWeightMapper.createPetWeight(petWeightEntity);
            } else {
                petWeightEntity.setWeight(petWeightEntity.getWeight() + GROWTH_LIQUID);
                petWeightMapper.updatePetWeight(petWeightEntity);
            }
        }
        //使用道具后保存记录
        Integer useTooleResult = bagToolEffectMapper.useTool(bagToolEffectEntity);

        //新增动态消息
        if (!tooleId.equals(BagToolTypeEnum.RHESUS.getValue()) && !tooleId.equals(BagToolTypeEnum.RESURRECT.getValue()) && !tooleId.equals(BagToolTypeEnum.MOSAIC.getValue())) {
            Integer result = saveDynamic(bagToolEffectEntity);
        }

        if (useTooleResult > 0) {
            //用户已有道具状态更新
            Integer updateResult = bagToolPeopleMapper.consumeTool(toolPeopleId, StatusEnum.HOLD_ON.getValue());
            if (updateResult < 0) {
                ResultJson.errorResultJson("道具状态更新失败");
            } else {
                String oaModel = bagToolMapper.getOaModelById(tooleId);
                if (StringUtils.isNotEmpty(oaModel)) {
                    String userName = userMapper.getNameById(userId);
                    String messageContent = String.format(oaModel, userName);
                    //发送OA消息给被作用的人
                    sendUseToolOAMessage(userId, toUserId, companyId, messageContent);
//                    OAMessageUtil.sendTextMessageWithStorage(companyId, toUserId, messageContent);
                }
                return ResultJson.succResultJson(toolEffectUserId);
            }
        }
        return ResultJson.errorResultJson("保存使用失败");
//
    }

    private void sendUseToolOAMessage(Integer userId, String dingUserId, Integer companyId, String content) {
        String corpId = isvTicketsService.getIsvTicketByCompanyId(companyId).getCorpId();
        UserXCompany userXCompany = userXCompanyMapper.getByCompanyIdANdDingUserId(corpId, dingUserId);
        UserConfigEntity userConfigEntity = userConfigMapper.findByUserId(userXCompany.getUserId());
        if (userConfigEntity == null || (userConfigEntity != null && userConfigEntity.getIsOa().equals(1))) {

            content += "\n" + DateUtil.getDate_Y_M_D_H_M_S();
            OAMessage oaMessage = new OAMessage();
            String mainPageBury = MAIN_PAGE + GlobalConstant.OA_BURY_TYPE + OABuryEnum.TOOL.getValue();
            String mainPage = String.format(mainPageBury, corpId);
            String mainPageQrcode = String.format(QRCODE_PAGE, URLEncoder.encode(mainPage));
            OAMessageUtil.sendOAMessageWithStroageV2(companyId, dingUserId, "", oaMessage.getSimpleOAMessage(mainPage, mainPageQrcode, content));

            //发送OA消息记录
            saveOASendBury(companyId, userId, OABuryEnum.TOOL.getValue(), new Date());
        }
    }

    private void saveOASendBury(Integer companyId, Integer userId, Integer type, Date createTime) {
        OASendBuryEntity oaSendBuryEntity = new OASendBuryEntity(companyId, userId, type, createTime);
        oaSendBuryMapper.createOASendBury(oaSendBuryEntity);
    }

    private Integer saveDynamic(BagToolEffectEntity bagToolEffectEntity) {
        //判断是否有保存过动态
        PetDynamicEntity petDynamicEntity = petDynamicMapper.getPetDynamic(bagToolEffectEntity.getCompanyId(), bagToolEffectEntity.getEffectUserId(), bagToolEffectEntity.getToolId(), bagToolEffectEntity.getId());
        String userName = userMapper.getNameById(bagToolEffectEntity.getUserId());
        String contentModel = DynamicEnum.getDynamicEnum(bagToolEffectEntity.getToolId()).getDynamic();
        if (!bagToolEffectEntity.getToolId().equals(BagToolTypeEnum.GROWTH_LIQUID.getValue())) {
            if (bagToolEffectEntity.getToolId().equals(BagToolTypeEnum.CUPID.getValue())) {
                contentModel = String.format(contentModel, userName, "x");
            } else {
                contentModel = String.format(contentModel, userName);
            }
        } else {
            contentModel = String.format(contentModel, userName, GROWTH_LIQUID);
        }
        if (petDynamicEntity == null) {
            petDynamicEntity = new PetDynamicEntity(bagToolEffectEntity.getCompanyId(), bagToolEffectEntity.getEffectUserId(), bagToolEffectEntity.getToolId(), bagToolEffectEntity.getId(), contentModel, bagToolEffectEntity.getCreateTime(), StatusEnum.DELETE.getValue(), StatusEnum.OK.getValue());
            //public PetDynamicEntity(Integer companyId, Integer userId, Integer activeId, Integer dynamicId, String dynamicContent, Date createTime, Integer isRead, Integer status) {
            return petDynamicMapper.createPetDynamic(petDynamicEntity);
        }
        return 0;
    }


    /**
     * @Param toolId 要消除的道具效果
     * @Param count 使用道具数量
     */
    @Override
    public JSONObject elimateTool(Integer toolId, Integer count) {
        //获取我的回魂丹数量
        MobileLoginUser mobileLoginUser = MobileLoginUser.getUser();
        Integer companyId = mobileLoginUser.getCompanyID();
        Integer userId = mobileLoginUser.getUserID();
        if (toolId.equals(BagToolTypeEnum.RHESUS.getValue()) || toolId.equals(BagToolTypeEnum.RESURRECT.getValue()) || toolId.equals(BagToolTypeEnum.GROWTH_LIQUID.getValue())) {
            return ResultJson.errorResultJson("该效果不需消除!");
        }
        //回魂丹数量
        Integer resurrectCount = bagToolPeopleMapper.getToolCount(companyId, userId, BagToolTypeEnum.RESURRECT.getValue());
        if (resurrectCount <= 0) {
            return ResultJson.errorResultJson("回魂丹数量不足!");
        }
        //负面影响数
        List<BagToolEffectEntity> effectList = bagToolEffectMapper.getUserEffectedStatusOther(companyId, userId, toolId);
        if (CollectionUtils.isEmpty(effectList) && toolId.equals(BagToolTypeEnum.CUPID.getValue())) {
            effectList = bagToolEffectMapper.getUserEffectedStatusSelf(companyId, userId, toolId);
        }

        Integer totalEffectCount = effectList.size();
        if (effectList.size() <= 0) {
            return ResultJson.errorResultJson("不存在需要消除的负面效果!");
        }
        Integer relieveCount = 0;
        if (count > resurrectCount) {
            relieveCount = resurrectCount;
            if (relieveCount > totalEffectCount) {
                relieveCount = totalEffectCount;
            }
        } else {
            //使用数<实际回魂丹数
            if (count >= totalEffectCount) {
                relieveCount = totalEffectCount;
            } else {
                relieveCount = count;
            }
        }
        //当前回魂丹列表
        List<Integer> recurrectList = bagToolPeopleMapper.getToolIdByCompanyAndTool(companyId, BagToolTypeEnum.RESURRECT.getValue(), userId);
        List<BagToolEffectEntity> updateBagToolEffectList = new ArrayList<>();
        List<BagToolPeopleEntity> updateBagToolPeopleList = new ArrayList<>();
        for (int i = 0; i < relieveCount; i++) {
            Integer recurrectId = recurrectList.get(i);
            //负面影响
            BagToolEffectEntity relieveEntity = effectList.get(i);
            //public BagToolEffectEntity(Integer companyId, Integer toolId, Integer toolPeopleId, Integer userId, Integer effectUserId, Integer status, Date createTime, Date updateTime)
            //使用回魂丹
            BagToolEffectEntity bagToolEffect = new BagToolEffectEntity(companyId, BagToolTypeEnum.RESURRECT.getValue(), recurrectId, userId, userId, StatusEnum.OK.getValue(), new Date(), new Date());
            bagToolEffectMapper.useTool(bagToolEffect);
            //消除之前的作用 bagToolEffect
            Integer elimateId = bagToolEffect.getId();
            relieveEntity.setStatus(StatusEnum.DELETE.getValue());
            relieveEntity.setElimateId(elimateId);
            relieveEntity.setUpdateTime(new Date());
            updateBagToolEffectList.add(relieveEntity);
            //消除个人回魂丹记录 bagToolPeople
            BagToolPeopleEntity bagToolPeopleEntity = new BagToolPeopleEntity(recurrectId, StatusEnum.HOLD_ON.getValue(), new Date());
            updateBagToolPeopleList.add(bagToolPeopleEntity);
        }
        Integer result = 0;
        Integer result2 = 0;
        if (CollectionUtils.isNotEmpty(updateBagToolEffectList)) {
            result = bagToolEffectMapper.batchUpdate(updateBagToolEffectList);
        }
        if (CollectionUtils.isNotEmpty(updateBagToolEffectList)) {
            result2 = bagToolPeopleMapper.batchUpdate(updateBagToolPeopleList);
        }
        String elimateToolName = getById(toolId).getToolName();
        BagToolRelieveVO bagToolRelieveVO = new BagToolRelieveVO(result, elimateToolName);
        return ResultJson.finishResultJson(ResultCodeEnum.SUCCESS.getValue(), "消除了" + relieveCount + "个道具!", bagToolRelieveVO);
    }


    /**
     * @Param toolId 要消除的道具效果
     * @Param count 使用道具数量
     */
    @Override
    public JSONObject elimateSingleTool(Integer effectId) {
        //获取我的回魂丹数量
        MobileLoginUser mobileLoginUser = MobileLoginUser.getUser();
        Integer companyId = mobileLoginUser.getCompanyID();
        Integer userId = mobileLoginUser.getUserID();
        BagToolEffectEntity bagToolEffectEntity = bagToolEffectMapper.getById(effectId);
        if (bagToolEffectEntity == null) {
            return ResultJson.errorResultJson("不存在的状态!");
        }
        //成长液、回魂丹、窜天炮不能被消除
        if (bagToolEffectEntity.getToolId().equals(BagToolTypeEnum.RHESUS.getValue()) || bagToolEffectEntity.getToolId().equals(BagToolTypeEnum.RESURRECT.getValue()) || bagToolEffectEntity.getToolId().equals(BagToolTypeEnum.GROWTH_LIQUID.getValue())) {
            return ResultJson.errorResultJson("该效果不需消除!");
        }
        //回魂丹数量
        Integer resurrectCount = bagToolPeopleMapper.getToolCount(companyId, userId, BagToolTypeEnum.RESURRECT.getValue());
        if (resurrectCount <= 0) {
            return ResultJson.errorResultJson("回魂丹数量不足!");
        }
        //负面影响数
        if (bagToolEffectEntity.getStatus().equals(StatusEnum.DELETE.getValue())) {
            return ResultJson.errorResultJson("该效果已经被消除!");
        }

        //当前回魂丹列表
        List<Integer> recurrectList = bagToolPeopleMapper.getToolIdByCompanyAndTool(companyId, BagToolTypeEnum.RESURRECT.getValue(), userId);
        Integer recurrectId = recurrectList.get(0);
        //使用回魂丹
        BagToolEffectEntity bagToolEffect = new BagToolEffectEntity(companyId, BagToolTypeEnum.RESURRECT.getValue(), recurrectId, userId, userId, StatusEnum.OK.getValue(), new Date(), new Date());
        bagToolEffectMapper.useTool(bagToolEffect);
        //消除之前的作用 bagToolEffect
        //Integer elimateId = bagToolEffect.getId();
        bagToolEffectEntity.setStatus(StatusEnum.DELETE.getValue());
        bagToolEffectEntity.setElimateId(bagToolEffect.getId());
        bagToolEffectEntity.setUpdateTime(new Date());
        List<BagToolEffectEntity> bagToolEffectUpdateList = new ArrayList<>();
        bagToolEffectUpdateList.add(bagToolEffectEntity);
        bagToolEffectMapper.batchUpdate(bagToolEffectUpdateList);
        //消除个人回魂丹记录 bagToolPeople
        BagToolPeopleEntity bagToolPeopleEntity = new BagToolPeopleEntity(recurrectId, StatusEnum.HOLD_ON.getValue(), new Date());
        List<BagToolPeopleEntity> bagToolPeopleUpdateList = new ArrayList<>();
        bagToolPeopleUpdateList.add(bagToolPeopleEntity);
        Integer result = bagToolPeopleMapper.batchUpdate(bagToolPeopleUpdateList);
        String elimateToolName = getById(bagToolEffectEntity.getToolId()).getToolName();
        BagToolRelieveVO bagToolRelieveVO = new BagToolRelieveVO(result, elimateToolName);
        return ResultJson.finishResultJson(ResultCodeEnum.SUCCESS.getValue(), "消除了1个道具!", bagToolRelieveVO);
    }

    /**
     * type 0/1 自己/别人
     * 获取某个用户的宠物状态
     * <p/>
     * 自己的宠物不显示川窜天炮
     */
    @Override
    public JSONObject getUserPet(Integer userId, Integer type) {
        if (userId.equals(MobileLoginUser.getUser().getUserID())) {
            type = 0;
        }
        Integer companyId = MobileLoginUser.getUser().getCompanyID();
        MyPetVO myPetVO = myPetMapper.getMyPet(userId);
        if (myPetVO == null) {
            return ResultJson.errorResultJson("");
        }
        //都不显示窜天炮
        List<BagToolEffectUserVO> effectUserList = bagToolEffectMapper.getUserEffectedTool(companyId, userId, 0);
        List<BagToolVO> bagToolList = bagToolMapper.getAllTools();
        String tip = "";
        Map<String, Object> resultMap = new HashMap<>();
        for (BagToolEffectUserVO bagToolEffect : effectUserList) {
            Integer toolId = bagToolEffect.getToolId();
            BagToolVO bagToolVO = getById(toolId);
            bagToolEffect.setToolName(bagToolVO.getToolName());
            //获取作用人
            String lastEffectUser = bagToolEffectMapper.getLastToolUser(companyId, userId, toolId);
            Integer effectUserCount = bagToolEffectMapper.getToolUserCount(companyId, userId, toolId);


            String explain = "";
            if (bagToolVO.getId().equals(1)) {
                //获取剩余的题
                Integer totalRemainCount = bagToolEffect.getCount() * SEAL_COUNT;
                //获取被作用的effectId
                List<BagToolEffectEntity> bagToolEffectList = bagToolEffectMapper.getUserEffectedStatusOther(companyId, userId, BagToolTypeEnum.SEAL.getValue());
                Integer totalAnswerCount = 0;
                if (CollectionUtils.isNotEmpty(bagToolEffectList)) {
                    for (int i = 0; i < bagToolEffectList.size(); i++) {
                        BagToolEffectEntity bagToolEffecti = bagToolEffectList.get(i);
                        Integer subCount = bagToolEffectQuestionsMapper.getToolEffectCount(companyId, userId, bagToolEffecti.getId());
                        totalAnswerCount += subCount;
                    }
                }
                totalRemainCount = totalRemainCount - totalAnswerCount;

                if (effectUserCount > 1) {
                    lastEffectUser = lastEffectUser + "...等" + effectUserCount + "人";
//                    explain = String.format(bagToolVO.getExlpainShow(), lastEffectUser, petName);
                    explain = String.format(bagToolVO.getExlpainShow(), lastEffectUser);
                } else {
//                    explain = String.format(bagToolVO.getExlpainShow(), lastEffectUser, petName);
                    explain = String.format(bagToolVO.getExlpainShow(), lastEffectUser);
                }
                if (type == 0) {
                    tip = String.format(bagToolVO.getTip(), totalRemainCount);
                }
            } else if (bagToolVO.getId().equals(2)) {
                if (effectUserCount > 1) {
                    lastEffectUser = lastEffectUser + "...等" + effectUserCount + "人";
//                    explain = String.format(bagToolVO.getExlpainShow(), petName, lastEffectUser);
                    explain = String.format(bagToolVO.getExlpainShow(), lastEffectUser);
                } else {
//                    explain = String.format(bagToolVO.getExlpainShow(), petName, lastEffectUser);
                    explain = String.format(bagToolVO.getExlpainShow(), lastEffectUser);
                }
                if (type == 0) {
                    tip = bagToolVO.getTip();
                }
            } else if (bagToolVO.getId().equals(3)) {
                if (effectUserCount > 1) {
                    lastEffectUser = lastEffectUser + "...等" + effectUserCount + "人";
//                    explain = String.format(bagToolVO.getExlpainShow(), petName, lastEffectUser);
                    explain = String.format(bagToolVO.getExlpainShow(), lastEffectUser);
                } else {
//                    explain = String.format(bagToolVO.getExlpainShow(), petName, lastEffectUser);
                    explain = String.format(bagToolVO.getExlpainShow(), lastEffectUser);
                }
                if (type == 0) {
                    tip = bagToolVO.getTip();
                }
            } else if (bagToolVO.getId().equals(4)) {
                //与谁连了
                List<BagToolEffectEntity> startEffectList = bagToolEffectMapper.getUserEffectedStatusOther(companyId, userId, BagToolTypeEnum.CUPID.getValue());
                List<BagToolEffectEntity> endEffectList = bagToolEffectMapper.getUserEffectedStatusSelf(companyId, userId, BagToolTypeEnum.CUPID.getValue());

                //获取作用后答题数
                Integer effectId = 0;
                Integer connectUserId = null;
                if (CollectionUtils.isNotEmpty(startEffectList)) {
                    effectId = startEffectList.get(0).getId();
                    if (userId.equals(startEffectList.get(0).getEffectUserId())) {
                        connectUserId = startEffectList.get(0).getUserId();
                    } else {
                        connectUserId = startEffectList.get(0).getEffectUserId();
                    }
                } else if (CollectionUtils.isNotEmpty(endEffectList)) {
                    effectId = endEffectList.get(0).getId();
                    if (userId.equals(endEffectList.get(0).getEffectUserId())) {
                        connectUserId = endEffectList.get(0).getUserId();
                    } else {
                        connectUserId = endEffectList.get(0).getEffectUserId();
                    }
                }
                Integer answerCount = bagToolEffectQuestionsMapper.getToolEffectCount2(companyId, userId, effectId);
                lastEffectUser = userMapper.getNameById(connectUserId);
                explain = String.format(bagToolVO.getExlpainShow(), lastEffectUser);
                if (type == 0) {
                    tip = String.format(bagToolVO.getTip(), lastEffectUser, answerCount);
                }
            } else if (bagToolVO.getId().equals(5)) {
                if (effectUserCount > 1) {
                    lastEffectUser = lastEffectUser + "...等" + effectUserCount + "人";
                    explain = String.format(bagToolVO.getExlpainShow(), lastEffectUser);
                } else {
                    explain = String.format(bagToolVO.getExlpainShow(), lastEffectUser);
                }
                if (type == 0) {
                    tip = String.format(bagToolVO.getTip());
                }
            }
            bagToolEffect.setExplain(explain);
            if (type == 0) {
                bagToolEffect.setTip(tip);
            }
        }
        //获取宠物状态
        if (myPetVO != null && myPetVO.getLevel() >= 0) {
            String uname = userMapper.getNameById(userId);
//            PetExperienceEntity petExperienceEntity = petExperienceMapper.getByLevel(myPetVO.getLevel());
//            myPetVO.setPhysicalValueHigh(petExperienceEntity.getMaxPhysicalValue());
//            myPetVO.setLevelExperienceValue(petExperienceEntity.getExperienceHigh());
            myPetVO.setName(uname);
            PetWeightEntity petWeightEntity = petWeightMapper.getPetWeight(companyId, userId);
            if (petWeightEntity != null) {
                myPetVO.setWeight(petWeightEntity.getWeight());
            } else {
                myPetVO.setWeight(0);
            }
        }
        resultMap.put("effectList", effectUserList);
        resultMap.put("toolList", bagToolList);
        resultMap.put("pet", myPetVO);
        return ResultJson.succResultJson(resultMap);
    }

    /**
     * @Param evenType 事件 1,2,3,4,5,6/登陆,答对4题,答对12题,答对24题,combo3,combo5
     * @Param count 触发次数
     */
    @Override
    public JSONObject bagDropTest(Integer eventType, Integer count) {
        List<BagToolRateEntity> bagToolRateList = bagToolRateMapper.getByEventType(eventType);
        if (CollectionUtils.isEmpty(bagToolRateList)) {
            return ResultJson.errorResultJson("不存在的事件!");
        }
        List<BagToolRateDTO> ratoDTOList = new ArrayList<>();
        Integer start = 0;
        for (int i = 0; i < bagToolRateList.size(); i++) {
            BagToolRateEntity bagToolRate = bagToolRateList.get(i);
            Integer stageRate = bagToolRate.getRate();
            Integer startRange = start;
            Integer endRange = start + stageRate;
            start = endRange;
            BagToolRateDTO bagToolRateDTO = new BagToolRateDTO(eventType, bagToolRate.getToolId(), startRange, endRange);
            ratoDTOList.add(bagToolRateDTO);
        }
        //System.out.println(ratoDTOList.size());
        //计算概率
        Map<Integer, Integer> resultMap = new HashMap<>();
        for (int j = 0; j < count; j++) {
            Integer hintToolId = 0;
            Double rateNum = Math.random() * 100;
            for (int i = 0; i < ratoDTOList.size(); i++) {
                BagToolRateDTO bagToolRateDTO = ratoDTOList.get(i);
                if (bagToolRateDTO != null && !bagToolRateDTO.getStartRange().equals(bagToolRateDTO.getEndRange())) {
                    if (rateNum >= bagToolRateDTO.getStartRange() && rateNum < bagToolRateDTO.getEndRange()) {
                        BagToolVO bagToolVO = getById(bagToolRateDTO.getToolId());
                        if (bagToolVO != null) {
                            hintToolId = bagToolRateDTO.getToolId();

                        }
                    }
                }
            }
            if (resultMap.containsKey(hintToolId)) {
                Integer hintCount = resultMap.get(hintToolId);
                resultMap.put(hintToolId, ++hintCount);
            } else {
                resultMap.put(hintToolId, 1);
            }
        }

        List<BagToolResult> resultList = new ArrayList<>();
        Iterator<Integer> mapItor = resultMap.keySet().iterator();
        String eventName = BagToolGainTypeEnum.getAgentStatusEnum(eventType).getChiDesc();
        while (mapItor.hasNext()) {
            Integer toolId = mapItor.next();
            BagToolVO bagToolVO = getById(toolId);
            Integer hintCount = resultMap.get(toolId);
            BagToolResult bagToolResult = new BagToolResult(toolId, bagToolVO.getToolName(), hintCount);
            resultList.add(bagToolResult);
        }
        return ResultJson.succResultJson(eventName, resultList);
    }

    /**
     * isRelieve  0已解除 1可以解除 2没解除按钮
     * 我被作用的道具列表
     */
    @Override
    public JSONObject getMyEffectedTool(PageEntity pageEntity) {
        MobileLoginUser mobileLoginUser = MobileLoginUser.getUser();
        PageHelper.startPage(pageEntity.getPageNo(), pageEntity.getPageSize());
        PageInfo<BagToolEffectEntity> pageInfo = new PageInfo<>(bagToolEffectMapper.getEffectedToolList(mobileLoginUser.getCompanyID(), mobileLoginUser.getUserID()));
        List<BagToolEffectUserVO2> bagToolEffectUserVOList = new ArrayList<>();
        for (BagToolEffectEntity bagToolEffectInDB : pageInfo.getList()) {
            BagToolEffectUserVO2 bagToolEffectUserVO = new BagToolEffectUserVO2();
            Integer toolId = bagToolEffectInDB.getToolId();
            String useToolPeopleName = userMapper.getNameById(bagToolEffectInDB.getUserId());
            BagToolVO bagToolVO = getById(toolId);
            String explainShow = String.format(bagToolVO.getExlpainShow(), useToolPeopleName);
            bagToolEffectUserVO.setId(bagToolEffectInDB.getId());
            bagToolEffectUserVO.setToolId(toolId);
            bagToolEffectUserVO.setToolName(bagToolVO.getToolName());
            bagToolEffectUserVO.setExplainShow(explainShow);
            bagToolEffectUserVOList.add(bagToolEffectUserVO);
            if (toolId.equals(BagToolTypeEnum.RHESUS)) {
                bagToolEffectUserVO.setIsRelieve(StatusEnum.HOLD_ON.getValue());
            } else {
                bagToolEffectUserVO.setIsRelieve(bagToolEffectInDB.getStatus());
            }

        }

        Map<String, Object> dataMap = Maps.newHashMap();
        dataMap.put("list", bagToolEffectUserVOList);
        dataMap.put("total", pageInfo.getTotal());
        dataMap.put("page", new Page(pageInfo.getPrePage(), pageEntity.getPageNo(), pageInfo.getNextPage(), pageInfo.getLastPage()));
        return ResultJson.succResultJson(dataMap);
    }

    @Override
    public JSONObject getToolAnswerCount(Integer toolEffectId) {
        BagToolEffectEntity bagToolEffectEntity = bagToolEffectMapper.getById(toolEffectId);
        if (bagToolEffectEntity == null) {
            return ResultJson.errorResultJson("不存在的记录");
        }
        Integer toolId = bagToolEffectEntity.getToolId();
        BagToolVO bagToolVO = getById(toolId);
        if (!(toolId.equals(BagToolTypeEnum.SEAL.getValue()) || toolId.equals(BagToolTypeEnum.DECAY.getValue()))) {
            return ResultJson.errorResultJson("该道具不需要消除");
        }
        //使用道具人
        String useToolPeople = userMapper.getNameById(bagToolEffectEntity.getUserId());
        //获取当前还需答题数
        Integer totalNeedAnswerCount = 0;
        if (toolId.equals(BagToolTypeEnum.SEAL.getValue())) {
            totalNeedAnswerCount = BagToolConstant.SEAL_COUNT;
        }
        if (toolId.equals(BagToolTypeEnum.DECAY.getValue())) {
            totalNeedAnswerCount = BagToolConstant.DECAY_COUNT;
        }
        Integer bagToolEffectAnswerCount = bagToolEffectQuestionsMapper.getToolEffectCount(bagToolEffectEntity.getCompanyId(), bagToolEffectEntity.getEffectUserId(), toolEffectId);
        Integer remainCount = totalNeedAnswerCount - bagToolEffectAnswerCount;
        BagToolRemainVO bagToolRemainVO = new BagToolRemainVO(toolEffectId, toolId, bagToolVO.getToolName(), useToolPeople, totalNeedAnswerCount, bagToolEffectAnswerCount, remainCount);
        return ResultJson.succResultJson(bagToolRemainVO);
    }


    private class BagToolResult {

        private Integer id;

        private String toolName;

        private Integer count;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getToolName() {
            return toolName;
        }

        public void setToolName(String toolName) {
            this.toolName = toolName;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }


        public BagToolResult(Integer id, String toolName, Integer count) {
            this.id = id;
            this.toolName = toolName;
            this.count = count;
        }

    }

    private void printMapVale(Map<Integer, Integer> paramMap) {
        StringBuffer sbf = new StringBuffer();
        Iterator mapKeyItor = paramMap.keySet().iterator();
        while (mapKeyItor.hasNext()) {
            Integer keyVal = (Integer) mapKeyItor.next();
            if (sbf.length() != 0) {
                sbf.append(",");
            }
            sbf.append(keyVal).append(":").append(paramMap.get(keyVal));
        }
        System.out.println(sbf.toString());
    }


    @GetCache(name = "bagtool", value = "id")
    public BagToolVO getById(Integer id) {
        return bagToolMapper.getById(id);
    }

    public static void main(String[] args) {
//        Integer tooleId = 1;
//        if (!(tooleId.equals(BagToolTypeEnum.RESURRECT.getV  alue()) || tooleId.equals(BagToolTypeEnum.RHESUS.getValue()))) {
//            System.out.println("fail");
//        } else {
//            System.out.println("success");
//        }
    }

}
