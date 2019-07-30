package com.enterprise.service.question.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.*;
import com.enterprise.base.entity.*;
import com.enterprise.base.enums.*;
import com.enterprise.base.exceptions.BusinessException;
import com.enterprise.base.vo.*;
import com.enterprise.mapper.companyInfo.CompanyInfoMapper;
import com.enterprise.mapper.companyXLibrary.CompanyXLibraryMapper;
import com.enterprise.mapper.isvTickets.IsvTicketsMapper;
import com.enterprise.mapper.markeyBuy.MarketBuyMapper;
import com.enterprise.mapper.questions.QuestionsLibraryMapper;
import com.enterprise.mapper.questions.QuestionsMapper;
import com.enterprise.mapper.questions.UserXQuestionsMapper;
import com.enterprise.mapper.users.UserMapper;
import com.enterprise.mapper.users.UserXDeptMapper;
import com.enterprise.mapper.users.UserXLibraryMapper;
import com.enterprise.oa.OAMessageUtil;
import com.enterprise.service.question.QuestionLabelService;
import com.enterprise.service.question.QuestionLibraryTitleService;
import com.enterprise.service.question.QuestionsLibraryService;
import com.enterprise.service.redis.RedisService;
import com.enterprise.util.AssertUtil;
import com.enterprise.util.DateUtil;
import com.enterprise.util.QuestionUtil;
import com.enterprise.util.excel.ExcelConstant;
import com.enterprise.util.excel.ExcelQuestionUtil;
import com.enterprise.util.excel.ExcelUtil;
import com.enterprise.util.excel.QuestionExcelDTO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.beans.Transient;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;

/**
 * CompanyInfoServiceImpl
 *
 * @author shisan
 * @create 2018-03-26 上午11:00
 **/
@Service
public class QuestionLibraryServiceImpl implements QuestionsLibraryService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private QuestionsLibraryMapper questionsLibraryMapper;

    @Resource
    private QuestionsMapper questionsMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserXLibraryMapper userXLibraryMapper;

    @Resource
    private CompanyInfoMapper companyInfoMapper;

    @Resource
    private QuestionLabelService questionLabelService;

    @Resource
    private CompanyXLibraryMapper companyXLibraryMapper;

    @Resource
    private QuestionLibraryTitleService questionLibraryTitleService;

    @Resource
    private RedisService redisService;

    @Resource
    private UserXDeptMapper userXDeptMapper;

    @Resource
    private IsvTicketsMapper isvTickestMapper;

    @Resource
    private UserXQuestionsMapper userXQuestionMapper;

    @Resource
    private MarketBuyMapper marketBuyMapper;


    private static final Integer PUBLIC_SUBJECT = 0;

    private static final String OPER_CORPID = GlobalConstant.getOperCorpId();

    private static String QUESTION_LIBRARY_KEY = RedisConstant.QUESTION_LIBRARY_KEY;

    private Long expireTime = RedisConstant.EXPIRE_TIME_COMMON;

    @Override
    public Integer createOrUpdateQuestionLibrary(QuestionsLibraryEntity questionsLibraryEntity) {
        //题库id为空
        if (questionsLibraryEntity.getId() == null || (questionsLibraryEntity.getId() != null && questionsLibraryEntity.getId().equals(0))) {
            questionsLibraryEntity.setUseCount(0);
            Integer result = questionsLibraryMapper.createOrUpdateQuestionLibrary(questionsLibraryEntity);
            String key = QUESTION_LIBRARY_KEY + "id_" + questionsLibraryEntity.getId();
            redisService.setSerialize(key, expireTime, questionsLibraryEntity);
            if (result > 0 && StringUtils.isNotEmpty(questionsLibraryEntity.getTitle())) {
                Integer result1 = questionLibraryTitleService.createOrUpdateQuestionLibraryTitle(new QuestionLibraryTitleEntity(questionsLibraryEntity.getCompanyId(), questionsLibraryEntity.getId(), questionsLibraryEntity.getTitle(), questionsLibraryEntity.getTitleType(), new Date()));
                return result1;
            }
            return result;
            //题库id不为空
        } else {
            //非官方题库
            if (questionsLibraryEntity.getSubject() != null && !questionsLibraryEntity.getSubject().equals(2)) {
                questionsLibraryEntity.setDefaultFlag(null);
            }
            questionsLibraryEntity.setUpdateTime(new Date());
            Integer result = 0;
            if (questionsLibraryEntity.getCompanyLibraryId() != null) {
                result = companyXLibraryMapper.modifyCompanyLibrary(new CompanyXLibraryEntity(questionsLibraryEntity.getCompanyLibraryId(), questionsLibraryEntity.getStatus()));
            } else {
                result = questionsLibraryMapper.updateQuestionLibrary(questionsLibraryEntity);
            }
            if (result >= 0) {
                QuestionsLibraryEntity questionsLibraryEntityInDB = questionsLibraryMapper.getById(questionsLibraryEntity.getId());
                String key = QUESTION_LIBRARY_KEY + "id_" + questionsLibraryEntity.getId();
                redisService.setSerialize(key, expireTime, questionsLibraryEntityInDB);
                if (StringUtils.isNotEmpty(questionsLibraryEntity.getTitle())) {
                    return questionLibraryTitleService.createOrUpdateQuestionLibraryTitle(new QuestionLibraryTitleEntity(questionsLibraryEntity.getCompanyId(), questionsLibraryEntity.getId(), questionsLibraryEntity.getTitle(), questionsLibraryEntity.getTitleType(), new Date()));
                } else {
                    return questionLibraryTitleService.createOrUpdateQuestionLibraryTitle(new QuestionLibraryTitleEntity(questionsLibraryEntity.getCompanyId(), questionsLibraryEntity.getId(), "", questionsLibraryEntity.getTitleType(), new Date()));
                }
            }
            return result;
        }
    }

    @Override
    public JSONObject checkLibraryCount(){
        String limitCount = "无限量";
        LoginUser loginUser = LoginUser.getUser();
        String corpId = loginUser.getCorpID();
        Integer companyId = isvTickestMapper.getCompanyIdByCorpId(corpId);
        String versionName = "";
        QuestionLibraryLimitVO questionLibraryLimitVO = null;
        List<MarketBuyEntity> marketBuyList = marketBuyMapper.getByCorpId(corpId, StatusEnum.OK.getValue());
        if (CollectionUtils.isNotEmpty(marketBuyList)) {
            versionName = marketBuyList.get(0).getItemName();
        }
        Integer libraryCount = questionsLibraryMapper.getQuesitonLibraryCount(companyId,QuestionLibraryTypeEnum.PRIVATE.getValue());
//        if(corpId.equals(DDConstant.OPERATOR_CORP_ID) || corpId.equals(DDConstant.OPERATOR_CORP_ID2) || corpId.equals(DDConstant.OPERATOR_CORP_ID3)){
//            questionLibraryLimitVO = new QuestionLibraryLimitVO("免费版",libraryCount,limitCount);
//            return ResultJson.succResultJson(questionLibraryLimitVO);
//        }
        if(StringUtils.isNotEmpty(versionName)){
            MarketVersionEnum marketVersionEnum = MarketVersionEnum.getAgentStatusEnum(versionName);
            Integer libraryLimitCount = marketVersionEnum.getValue();
            if(libraryLimitCount.equals(0)){
                questionLibraryLimitVO = new QuestionLibraryLimitVO(versionName,libraryCount,limitCount);
                return ResultJson.succResultJson(questionLibraryLimitVO);
            }
            if(libraryLimitCount>0 && libraryCount>=libraryLimitCount){
                questionLibraryLimitVO = new QuestionLibraryLimitVO(versionName,libraryCount,String.valueOf(libraryLimitCount));
                return ResultJson.errorResultJson(questionLibraryLimitVO);
            }
            questionLibraryLimitVO = new QuestionLibraryLimitVO(versionName,libraryCount,String.valueOf(libraryLimitCount));
            return ResultJson.succResultJson(questionLibraryLimitVO);
        }else{
            //老的种子用户 不限量
            questionLibraryLimitVO = new QuestionLibraryLimitVO(versionName,libraryCount,limitCount);
            return ResultJson.succResultJson(questionLibraryLimitVO);
        }
    }

    /**
     * 企业题库内只显示企业题库
     * 0/1/2 公共/企业/官方
     */
    @Override
    public JSONObject findQuestionsLibrary(QuestionsLibraryEntity questionsLibraryEntity, PageEntity pageEntity) {
//        Integer roleId = LoginUser.getUser().getRole();
//        if (!roleId.equals(RoleEnum.OPER)) {
//            questionsLibraryEntity.setCompanyId(questionsLibraryEntity.getCompanyId());
//        }
        String corpId = LoginUser.getUser().getCorpID();
        if (questionsLibraryEntity.getSubject().equals(0)) {
            questionsLibraryEntity.setCompanyId(null);
        } else if (questionsLibraryEntity.getSubject().equals(1)) {
            questionsLibraryEntity.setSubject(1);
            questionsLibraryEntity.setCompanyId(LoginUser.getUser().getCompanyID());
        } else if (questionsLibraryEntity.getSubject().equals(2)) {
            questionsLibraryEntity.setSubject(2);
            questionsLibraryEntity.setCompanyId(null);
        }
        if (StringUtils.isNotEmpty(questionsLibraryEntity.getLabel())) {
            List<String> labelList = new ArrayList<>();
            String[] label = questionsLibraryEntity.getLabel().split(",");
            for (int i = 0; i < label.length; i++) {
                labelList.add(label[i]);
            }
            questionsLibraryEntity.setLabelList(labelList);
        }
        questionsLibraryEntity.setStatus(1);
        PageHelper.startPage(pageEntity.getPageNo(), pageEntity.getPageSize());
        PageInfo<QuestionsLibraryEntity> pageInfo = null;
        //模板
        if (questionsLibraryEntity.getSubject().equals(0)) {
            if (corpId.equals(OPER_CORPID)) {
                pageInfo = new PageInfo<>(questionsLibraryMapper.findQuestionLibrary(questionsLibraryEntity));
            } else {
                questionsLibraryEntity.setStatus(1);
                pageInfo = new PageInfo<>(questionsLibraryMapper.findQuestionLibrary2(questionsLibraryEntity));
            }
            //企业题库
        } else if (questionsLibraryEntity.getSubject().equals(1)) {
            pageInfo = new PageInfo<>(questionsLibraryMapper.findQuestionLibrary2(questionsLibraryEntity));
            //官方
        } else {
            pageInfo = new PageInfo<>(questionsLibraryMapper.findQuestionLibrary(questionsLibraryEntity));
        }

        List<QuestionLibraryVO> questionLibraryVOList = new ArrayList<>();
        for (QuestionsLibraryEntity questionsLibraryInDB : pageInfo.getList()) {
            QuestionLibraryVO questionLibraryVO = new QuestionLibraryVO();
            try {
                BeanUtils.copyProperties(questionLibraryVO, questionsLibraryInDB);
                UserEntity userEntity = userMapper.getUserById(questionsLibraryInDB.getOperator());
                AssertUtil.notNull(userEntity, "不存在的用户信息!");
                questionLibraryVO.setCreator(userEntity.getName());
                questionLibraryVO.setCreateTime(DateUtil.getDisplayYMDHMS(questionsLibraryInDB.getCreateTime()));
                questionLibraryVO.setUpdateTime(DateUtil.getDisplayYMDHMS(questionsLibraryInDB.getUpdateTime()));
                Integer questionCount = questionsMapper.getTotalCountByLibraryId(questionLibraryVO.getId());
                questionLibraryVO.setQuestionCount(questionCount);
                //企业题库
                //学习人数 完成度
                if (questionsLibraryEntity.getSubject().equals(SubjectEnum.ENTERPRISE.getValue())) {
                    Integer studyCount = userXLibraryMapper.getStudyCountByLibraryId(questionsLibraryEntity.getCompanyId(), questionsLibraryInDB.getId());
                    questionLibraryVO.setUseCount(null);
                    String companyName = companyInfoMapper.getCompanyNameById(questionsLibraryInDB.getCompanyId());
                    questionLibraryVO.setCompanyName(companyName);
                    if (studyCount == null) {
                        studyCount = 0;
                    }
                    questionLibraryVO.setStudyCount(studyCount);
                } else if (questionsLibraryEntity.getSubject().equals(SubjectEnum.MODEL.getValue())) {
                    questionLibraryVO.setStudyCount(null);
                    if (questionLibraryVO.getUseCount() == null)
                        questionLibraryVO.setUseCount(0);
                } else if (questionsLibraryEntity.getSubject().equals(SubjectEnum.PUBLIC.getValue())) {
                    if (questionLibraryVO.getUseCount() == null || questionLibraryVO.getUseCount() == 0) {
                        Integer companyCount = companyXLibraryMapper.findCompanyLibraryCount(questionsLibraryInDB.getId());
                        companyCount = companyCount == null ? 0 : companyCount;
                        questionLibraryVO.setUseCount(companyCount);
                    }
                }
                if (questionLibraryVO.getCompanyLibraryId() == 0) {
                    questionLibraryVO.setCompanyLibraryId(null);
                }
                QuestionLibraryTitleEntity questionLibraryTitleEntity = questionLibraryTitleService.getQuestionTitleByLibrary(questionsLibraryInDB.getId());
                if (questionLibraryTitleEntity != null) {
                    questionLibraryVO.setTitle(questionLibraryTitleEntity.getTitle());
                    questionLibraryVO.setTitleType(questionLibraryTitleEntity.getType());
                }

                questionLibraryVOList.add(questionLibraryVO);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        Map<String, Object> dataMap = Maps.newHashMap();
        dataMap.put("list", questionLibraryVOList);
        dataMap.put("total", pageInfo.getTotal());
        dataMap.put("page", new Page(pageInfo.getPrePage(), pageEntity.getPageNo(), pageInfo.getNextPage(), pageInfo.getLastPage()));
        JSONObject resultJson = ResultJson.succResultJson(dataMap);
        logger.info(resultJson.toJSONString());
        return resultJson;
    }

    @Override
    public JSONObject getLibraryDetail(Integer id) {
        QuestionsLibraryEntity questionsLibraryEntity = questionsLibraryMapper.getById(id);
        if (questionsLibraryEntity != null) {
            QuestionLibraryDetailVO questionLibraryDetailVO = new QuestionLibraryDetailVO();
            try {
                BeanUtils.copyProperties(questionLibraryDetailVO, questionsLibraryEntity);
                QuestionLibraryTitleEntity questionLibraryTitleEntity = questionLibraryTitleService.getQuestionTitleByLibrary(questionLibraryDetailVO.getId());
                if (questionLibraryTitleEntity != null) {
                    questionLibraryDetailVO.setTitle(questionLibraryTitleEntity.getTitle());
                    questionLibraryDetailVO.setTitleType(questionLibraryTitleEntity.getType());
                }
                if(questionsLibraryEntity.getOptionSortType()==null){
                    questionLibraryDetailVO.setOptionSortType(1);
                }
                return ResultJson.succResultJson(questionLibraryDetailVO);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return ResultJson.errorResultJson(id);
    }

    @Override
    public JSONObject useLibrary(String libraryIds) {
        //将父节点useCount置1,之后新增企业题库,并将该父题库下的题拷贝到子题库中
        LoginUser loginUser = LoginUser.getUser();
        String[] libraryIdArray = libraryIds.split(",");
        Integer flag = 0;
        for (int i = 0; i < libraryIdArray.length; i++) {
            Integer libraryId = Integer.valueOf(libraryIdArray[i]);
            QuestionsLibraryEntity sourcesLibraryEntity = questionsLibraryMapper.findById(libraryId);
            if (sourcesLibraryEntity.getSubject() != null && !sourcesLibraryEntity.getSubject().equals(QuestionLibraryTypeEnum.PUBLIC.getValue())) {
                logger.error("非官方题库不能被使用!");
                continue;
            }
            if (sourcesLibraryEntity.getStatus().equals(StatusEnum.HOLD_ON.getValue())) {
                flag = 2;
                logger.error("停用状态下的题库不能使用!");
                continue;
            }
            QuestionsLibraryEntity destQuestionLibrary = new QuestionsLibraryEntity(loginUser.getCompanyID(), libraryId, loginUser.getUserID());
            questionsLibraryMapper.copyQuestionLibrary(destQuestionLibrary);
            //复制父题库的标签,老标签id,新标签id
            Integer destQuestionLibraryId = destQuestionLibrary.getId();
            Map<Integer, Integer> questionLabelMap = copyQuestionLabel(libraryId, destQuestionLibraryId);
            Integer useCount = null;
            if (sourcesLibraryEntity.getUseCount() == null) {
                useCount = 1;
            } else {
                useCount = sourcesLibraryEntity.getUseCount() + 1;
            }
            questionsLibraryMapper.updateUseCount(libraryId, useCount);

            logger.info("新建的libraryid" + destQuestionLibrary.getId());
            List<QuestionsEntity> questionAllList = questionsMapper.getAllFromLibrary(libraryId);
            for (int j = 0; j < questionAllList.size(); j++) {
                QuestionsEntity questionEntity = questionAllList.get(j);
                questionEntity.setLibraryId(destQuestionLibraryId);
                questionEntity.setOperator(loginUser.getUserID());
                questionEntity.setParentId(questionEntity.getId());
                if (questionEntity.getLabelId() != null) {
                    questionEntity.setLabelId(questionLabelMap.get(questionEntity.getLabelId()));
                }
            }

//                Integer insertQuestionsResult = questionsMapper.insertQuestionsFromOtherLibrary(libraryId, destQuestionLibrary.getId(), loginUser.getUserID());
            Integer insertQuestionsResult = questionsMapper.batchInsertQuestions(questionAllList);
            if (insertQuestionsResult <= 0) {
                logger.error(String.format("复制题目时插入失败,id:%s!", libraryId));
            }
        }
        if (libraryIdArray.length == 1 && flag.equals(2)) {
            return ResultJson.succResultJson("停用状态的题库不能使用!");
        }
        return ResultJson.succResultJson("使用题库成功!");
    }

    private Map<Integer, Integer> copyQuestionLabel(Integer libraryId, Integer destLibraryId) {
        LoginUser loginUser = LoginUser.getUser();
        Map<Integer, Integer> resultMap = new HashMap<>();
        List<QuestionLabelEntity> questionLabelList = questionLabelService.getQuestionLabelsByLibraryId(libraryId);
        for (int i = 0; i < questionLabelList.size(); i++) {
            QuestionLabelEntity questionLabel = questionLabelList.get(i);
            QuestionLabelEntity useQuestionLabel = new QuestionLabelEntity(loginUser.getCompanyID(), destLibraryId, loginUser.getUserID(), questionLabel.getLabelName(), StatusEnum.OK.getValue(), new Date(), new Date());
            questionLabelService.createOrUpdateQuestionLabel(useQuestionLabel);
            resultMap.put(questionLabelList.get(i).getId(), useQuestionLabel.getId());
        }
        return resultMap;
    }


    @Override
    public JSONObject batchDelete(String libraryIds) {
        try {
            String[] idArray = libraryIds.split(",");
            List<Integer> idList = new ArrayList<>();
            for (int i = 0; i < idArray.length; i++) {
                if (StringUtils.isNotEmpty(idArray[i])) {
                    idList.add(Integer.valueOf(idArray[i].trim()));
                }
            }
            Integer result = questionsLibraryMapper.batchDelete(idList);
            if (result > 0) {
                return ResultJson.succResultJson(libraryIds);
            }
            return ResultJson.errorResultJson("更新失败");
        } catch (Exception e) {
            return ResultJson.errorResultJson("更新失败:" + e.getMessage());
        }
    }

    @Override
    public JSONObject batchDelete2(String libraryIds) {
        try {
            List<Integer> questionLibraryIdList = new ArrayList<>();
            List<CompanyXLibraryEntity> companyXLibraryList = new ArrayList<>();
            String[] libraryIdArr = libraryIds.split(",");
            for (int i = 0; i < libraryIdArr.length; i++) {
                String[] complexLibraryId = libraryIdArr[i].split("\\|");
                if (complexLibraryId.length <= 1) {
                    continue;
                }
                String libraryId = complexLibraryId[0];
                String companyLibraryId = complexLibraryId[1];
                if (StringUtils.isNotEmpty(companyLibraryId) && !companyLibraryId.equals("null")) {
                    CompanyXLibraryEntity companyXLibraryEntity = new CompanyXLibraryEntity();
                    companyXLibraryEntity.setId(Integer.valueOf(companyLibraryId));
                    companyXLibraryEntity.setStatus(StatusEnum.DELETE.getValue());
                    companyXLibraryList.add(companyXLibraryEntity);
                } else {
                    questionLibraryIdList.add(Integer.valueOf(libraryId));
                }
            }
            Integer reuslt1 = 0;
            Integer reuslt2 = 0;
            if (CollectionUtils.isNotEmpty(companyXLibraryList)) {
                reuslt1 = companyXLibraryMapper.batchupdate(companyXLibraryList);
            }
            if (CollectionUtils.isNotEmpty(questionLibraryIdList)) {
                reuslt2 = questionsLibraryMapper.batchDelete(questionLibraryIdList);
            }
            if (reuslt1 > 0 || reuslt2 > 0) {
                return ResultJson.succResultJson(libraryIds);
            }
            return ResultJson.errorResultJson("更新失败");
        } catch (Exception e) {
            return ResultJson.errorResultJson("更新失败:" + e.getMessage());
        }
    }

    /**
     * 继承其他的题库
     */
    @Override
    @Transient
    public Integer inheritQuestionLibrary(List<QuestionsLibraryEntity> addDefaultQuestionLibrary) {
        Integer result = 0;
        for (int i = 0; i < addDefaultQuestionLibrary.size(); i++) {
            QuestionsLibraryEntity questionsLibraryEntity = addDefaultQuestionLibrary.get(i);
            Integer createLibraryResult = questionsLibraryMapper.createOrUpdateQuestionLibrary(questionsLibraryEntity);
            if (createLibraryResult > 0 && questionsLibraryEntity.getId() > 0) {
                Integer insertQuestionsResult = questionsMapper.insertQuestionsFromOtherLibrary(questionsLibraryEntity.getParentId(), questionsLibraryEntity.getId(), questionsLibraryEntity.getOperator());
                if (insertQuestionsResult <= 0) {
                    logger.error(String.format("复制题目时插入失败,id:%s!", questionsLibraryEntity.getId()));
                } else {
                    result++;
                    QuestionsLibraryEntity parentQuestionLibrary = getById(questionsLibraryEntity.getParentId());
                    Integer useCount = null;
                    if (parentQuestionLibrary.getUseCount() == null) {
                        useCount = 1;
                    } else {
                        useCount = parentQuestionLibrary.getUseCount() + 1;
                    }
                    //父题库使用次数+1
                    questionsLibraryMapper.updateUseCount(questionsLibraryEntity.getParentId(), useCount);
                }
            }
        }
        return result;
    }

    @Override
    public JSONObject getLabelList(Integer subject, PageEntity pageEntity) {
        Integer companyId = null;
        if (subject != 0) {
            companyId = LoginUser.getUser().getCompanyID();
        }
        PageHelper.startPage(pageEntity.getPageNo(), pageEntity.getPageSize());
        PageInfo<String> pageInfo = new PageInfo<>(questionsLibraryMapper.getLabelList(subject, companyId));
        Map<String, Object> dataMap = Maps.newHashMap();
        dataMap.put("list", pageInfo.getList());
        dataMap.put("total", pageInfo.getTotal());
        dataMap.put("page", new Page(pageInfo.getPrePage(), pageEntity.getPageNo(), pageInfo.getNextPage(), pageInfo.getLastPage()));
        return ResultJson.succResultJson(dataMap);
    }

    @Override
    public List<QuestionsLibraryEntity> getQuestionLibrarys(QuestionsLibraryEntity questionsLibraryEntity) {
        return questionsLibraryMapper.findQuestionLibrary(questionsLibraryEntity);
    }

    @Override
    public QuestionsLibraryEntity getDefaultQuestionLibrary(Integer companyId, Integer parentId, Integer isDefault) {
        return questionsLibraryMapper.getDefaultQuestionLibrary(companyId, parentId, isDefault);
    }

    @Override
    public List<QuestionsLibraryEntity> getDefaultQuestionLibraryNoAssigned() {
        return questionsLibraryMapper.getDefaultQuestionLibraryNoAssigned();
    }


    //    @GetCache(name="questionlibrary",value="id")
    @Override
    public QuestionsLibraryEntity getById(Integer id) {
        String redisKey = QUESTION_LIBRARY_KEY + "id_" + id;
        //获取从redis中查询到的对象
        Object objectFromRedis = redisService.getSerializeObj(redisKey);
        //如果查询到了
        if (null != objectFromRedis) {
            return (QuestionsLibraryEntity) objectFromRedis;
        }
        try {
            objectFromRedis = questionsLibraryMapper.getById(id);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if (objectFromRedis != null) {
            redisService.setSerialize(redisKey, expireTime, objectFromRedis);
        }
        return (QuestionsLibraryEntity) objectFromRedis;
    }

    @Override
    public void exportQuestionLibrary(HttpServletResponse response, Integer libraryId) {
        try {
//            Map<Integer,String> questionLabelMap = questionLabelService.getLabelCollectionByLibraryId(libraryId);
            String libraryName = questionsLibraryMapper.getLibraryNameById(libraryId);
            AssertUtil.isTrue(libraryName != null, "不存在的题库id");
            List<QuestionVO1> questionsList = questionsMapper.getQuestionDetailList(libraryId);
            List<QuestionExcelDTO> questionExcelList = new ArrayList<>();
            for (int i = 0; i < questionsList.size(); i++) {
                QuestionVO1 questionVO = questionsList.get(i);
                QuestionExcelDTO questionExcelDTO = new QuestionExcelDTO();
                BeanUtils.copyProperties(questionExcelDTO, questionVO);
                questionExcelDTO.setLabel(questionVO.getLabelName());
                if (questionVO.getType().equals(QuestionTypeEnum.COMPLETION.getType())) {
                    String answer = questionVO.getAnswer();
                    JSONArray jsonArray = JSONArray.parseArray(answer);
                    //["春天|春","夏天|夏","秋天|秋","冬天|冬"]
                    String answer1 = QuestionUtil.parseJsonToString(jsonArray);
                    //[春天|春],[夏天|夏],[秋天|秋],[冬天|冬]
                    questionExcelDTO.setAnswer(answer1);
                } else if (questionVO.getType().equals(QuestionTypeEnum.MULTI_CHOICE.getType())) {
                    String answer = questionVO.getAnswer();
                    answer = answer.replaceAll(",", "|");
                    questionExcelDTO.setAnswer(answer);
                    JSONObject optionsJson = JSONObject.parseObject(questionVO.getOptions());
                    parseJsonToQuestionDTO(optionsJson, questionExcelDTO);
                } else {
                    JSONObject optionsJson = JSONObject.parseObject(questionVO.getOptions());
                    parseJsonToQuestionDTO(optionsJson, questionExcelDTO);
                }
                String questionType = QuestionTypeEnum.getQuestionTypeEnum(questionVO.getType()).getChineseDesc();
                questionExcelDTO.setQuestionType(questionType);
                questionExcelList.add(questionExcelDTO);
            }
            ExcelUtil.downloadExcelFile(libraryName, ExcelConstant.getHeadMap(), questionExcelList, response);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void batchInsertQuestionLabel(Integer companyId, Integer libraryId, List<QuestionExcelDTO> resultList) {
        LoginUser loginUser = LoginUser.getUser();
        Map<String, Integer> questionLabelMap = questionLabelService.getLabelCollectionByLibraryId2(libraryId);
        Integer addCount = 0;
        for (int i = 0; i < resultList.size(); i++) {
            QuestionExcelDTO questionExcelDTO = resultList.get(i);
            String questionLabel = questionExcelDTO.getLabel();
            if (StringUtils.isNotEmpty(questionLabel)) {
                questionLabel = questionLabel.trim();
                if (!questionLabelMap.containsKey(questionLabel)) {
                    QuestionLabelEntity questionLabelEntity = new QuestionLabelEntity(companyId, libraryId, loginUser.getUserID(), questionLabel, StatusEnum.OK.getValue(), new Date(), new Date());
                    Integer addResult = questionLabelService.createOrUpdateQuestionLabel(questionLabelEntity);
                    if (addResult > 0) {
                        addCount++;
                        questionLabelMap.put(questionLabel, questionLabelEntity.getId());
                    } else {
                        logger.error(("题库" + libraryId + "创建标签" + questionLabel + "失败"));
                        throw new BusinessException("题库" + libraryId + "创建标" + questionLabel + "签失败");
                    }
                }
            }
        }
        logger.info(libraryId + "共新增" + addCount + "个标签!");
    }

    @Override
    public void addLibraryUseCount(Integer id, Integer addCount) {
        questionsLibraryMapper.addUseCount(id, addCount);
    }

    @Override
    public JSONObject getArrangerdCompany(String search, Integer libraryId, PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPageNo(), pageEntity.getPageSize());
        PageInfo<CompanyLibraryVO> pageInfo = new PageInfo<>(companyInfoMapper.findCompanyList(search));
        //公司是否被全选
        Boolean isAllChoose = false;
        CompanyXLibraryEntity companyXLibraryTop = companyXLibraryMapper.findCompanyXLibrary(0, libraryId);
        if (companyXLibraryTop != null && companyXLibraryTop.getStatus().equals(StatusEnum.OK.getValue())) {
            isAllChoose = true;
        }
        List<CompanyLibraryVO> companyLibraryList = pageInfo.getList();
        for (CompanyLibraryVO companyLibraryVO : companyLibraryList) {
            Integer companyId = companyLibraryVO.getCompanyId();
            CompanyXLibraryEntity companyXLibraryInDB = companyXLibraryMapper.findCompanyXLibrary(companyId, libraryId);
            if (isAllChoose) {
                companyLibraryVO.setIsArranged(1);
            } else {
                if (companyXLibraryInDB != null && companyXLibraryInDB.getStatus().equals(StatusEnum.OK.getValue())) {
                    companyLibraryVO.setIsArranged(1);
                } else {
                    companyLibraryVO.setIsArranged(0);
                }
            }
        }
        Map<String, Object> dataMap = Maps.newHashMap();
        dataMap.put("list", companyLibraryList);
        dataMap.put("total", pageInfo.getTotal());
        dataMap.put("page", new Page(pageInfo.getPrePage(), pageEntity.getPageNo(), pageInfo.getNextPage(), pageInfo.getLastPage()));
        return ResultJson.succResultJson(dataMap);
    }

    /**
     * 默认官方题库应该不可以取消分配企业 确认
     * 默认官方题库,企业可以删除,删除后列表上的勾去掉,可以重新安排
     */
    @Override
    public JSONObject arrangeOfficialLibrary(String companyIds, Integer libraryId) {
        String[] companyIdArr = companyIds.split(",");
//        QuestionsLibraryEntity questionsLibraryEntity = questionsLibraryMapper.getById(libraryId);
        //是否为默认题库, 》默认题库则只能新增不能减少 》非默认题库可以新增与减少
        //Integer defaultFlag = questionsLibraryEntity.getDefaultFlag();
        List<CompanyXLibraryEntity> addCompanyLibrary = new ArrayList<>();
        List<CompanyXLibraryEntity> updateCompanyLibrary = new ArrayList<>();
//        List<CompanyXLibraryEntity> allCompanyList = companyXLibraryMapper.getAllCompanyByLibraryId(libraryId);
        List<Integer> arrangedCompanyList = companyXLibraryMapper.getCompanysByLibraryId(libraryId, StatusEnum.OK.getValue());
        List<Integer> deletedCompanyList = companyXLibraryMapper.getCompanysByLibraryId(libraryId, StatusEnum.DELETE.getValue());
        Set arrangedCompanySet = new HashSet(arrangedCompanyList);
        Set deletedCompanySet = new HashSet(deletedCompanyList);
//        Boolean isDefaultLibrary = false;
        //先将所有的公司全删了
        companyXLibraryMapper.batchSafeDelete(libraryId, StatusEnum.DELETE.getValue());
        for (int i = 0; i < companyIdArr.length; i++) {
            Integer companyId = Integer.valueOf(companyIdArr[i]);
            if (arrangedCompanySet.contains(companyId) || deletedCompanySet.contains(companyId)) {
                CompanyXLibraryEntity compXLibrary = companyXLibraryMapper.findCompanyXLibrary(companyId, libraryId);
                compXLibrary.setStatus(StatusEnum.OK.getValue());
                updateCompanyLibrary.add(compXLibrary);
            } else {
                CompanyXLibraryEntity comXLibrary = new CompanyXLibraryEntity(companyId, libraryId, 0, StatusEnum.OK.getValue(), new Date(), new Date());
                addCompanyLibrary.add(comXLibrary);
            }
//            }
        }
        //是否同步所有公司的所有员工
        if (CollectionUtils.isNotEmpty(updateCompanyLibrary)) {
            companyXLibraryMapper.batchupdate(updateCompanyLibrary);
        }
        if (CollectionUtils.isNotEmpty(addCompanyLibrary)) {
//                new Thread(new SynchronizePublicLibrary(libraryId)).start();
            companyXLibraryMapper.batchInsert(addCompanyLibrary);
        }
        //更新企业数
        Integer companyCount = companyXLibraryMapper.findCompanyLibraryCount(libraryId);
        companyCount = companyCount == null ? 0 : companyCount;
        questionsLibraryMapper.updateUseCount(libraryId, companyCount);
        return ResultJson.succResultJson(libraryId);
    }

    /**
     * 学院完成度
     */
    @Override
    public JSONObject getLibraryCompletion(Integer libraryId) {
        LoginUser loginUser = LoginUser.getUser();
        Integer companyId = loginUser.getCompanyID();
        Integer totalStudyCount = userXLibraryMapper.getStudyCountByLibraryId(companyId, libraryId);
        List<UserXLibraryEntity> finishUserList = userXLibraryMapper.getStudyCountByLibraryIdAndSchedule(companyId, libraryId, 1);
        Integer finishUserCount = finishUserList.size();
        Integer noFinishUserCount = totalStudyCount - finishUserList.size();
        Integer finishPercent = QuestionUtil.getDivision(finishUserCount, totalStudyCount);
        Integer noFinishPercent = 100 - finishPercent;
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("totalCount", totalStudyCount);
        resultMap.put("finishCount", finishUserCount);
        resultMap.put("nofinishCount", noFinishUserCount);
        resultMap.put("finishPercent", finishPercent);
        resultMap.put("nofinishPercent", noFinishPercent);
        return ResultJson.succResultJson(resultMap);
    }

    /**
     * 学员完成度列表
     * 用户一次答题正确/总共答题用户人数
     * 0未完成、1完成
     */
    @Override
    public JSONObject getLibraryCompletionDetail(Integer libraryId, Integer type, PageEntity pageEntity) {
        LoginUser loginUser = LoginUser.getUser();
        Integer companyId = loginUser.getCompanyID();
        IsvTicketsEntity isvTicketEntity = isvTickestMapper.getIsvTicketByCompanyId(companyId);
        PageHelper.startPage(pageEntity.getPageNo(), pageEntity.getPageSize());
        PageInfo<UserXLibraryEntity> finishUserPage = new PageInfo<>(userXLibraryMapper.getStudyCountByLibraryIdAndSchedule(companyId, libraryId, type));
        List<UserXLibraryEntity> finishUserList = finishUserPage.getList();
        List<UserXLibraryCountVO> resultList = new ArrayList<>();
        for (int i = 0; i < finishUserList.size(); i++) {
            UserXLibraryEntity userXLibraryEntity = finishUserList.get(i);
            Integer userId = userXLibraryEntity.getUserId();
            Integer totalCount = questionsMapper.getTotalCountByLibraryId(libraryId);
            Integer answerCount = userXLibraryMapper.getCorrectCount(companyId, userId, libraryId);
            String accuracy = QuestionUtil.getPercentDivision(answerCount, totalCount);
            String name = userMapper.getNameById(userId);
            String deptName = userXDeptMapper.getUserDeptName(isvTicketEntity.getCorpId(), userId);
            String schedule = userXLibraryEntity.getSchedule() + "";
            if(type==1){
                schedule = "100%";
            }
            UserXLibraryCountVO userXLibraryCountVO = new UserXLibraryCountVO(null, name, deptName, schedule, accuracy, userXLibraryEntity.getFinishTime());
            resultList.add(userXLibraryCountVO);
        }
        Map<String, Object> dataMap = Maps.newHashMap();
        dataMap.put("list", resultList);
        dataMap.put("total", finishUserPage.getTotal());
        dataMap.put("page", new Page(finishUserPage.getPrePage(), pageEntity.getPageNo(), finishUserPage.getNextPage(), finishUserPage.getLastPage()));
        return ResultJson.succResultJson(dataMap);
    }

    /**
     * 题库正确率=用户答题一次正确/答题总人数
     */
    @Override
    public JSONObject getLibraryAccuracy(Integer libraryId, PageEntity pageEntity) {
        LoginUser loginUser = LoginUser.getUser();
        Integer companyId = loginUser.getCompanyID();
        IsvTicketsEntity isvTicketEntity = isvTickestMapper.getIsvTicketByCompanyId(companyId);
        PageHelper.startPage(pageEntity.getPageNo(), pageEntity.getPageSize());
        PageInfo<QuestionVO> quesitonPage = new PageInfo<>(questionsMapper.getQuestionList(libraryId, null, null));
        List<QuestionVO> questionVOList = quesitonPage.getList();
        List<QuestionLibraryAccuracyVO> resultList = new ArrayList<>();
        for (int i = 0; i < questionVOList.size(); i++) {
            QuestionVO questionVO = questionVOList.get(i);
            Integer questionId = questionVO.getId();
            Integer answerCount = userXQuestionMapper.getCorrectCount(companyId, questionId);
            Integer totalCount = userXQuestionMapper.getQuestionAnswerUserCount(companyId, questionId);
            String accuracy = totalCount == 0 ? "0" : QuestionUtil.getPercentDivision(answerCount, totalCount);
            accuracy = accuracy + "%";
            Integer allAnswerCount = userXQuestionMapper.getQuestionAnswerCount(companyId, questionId);
            //答错人数
            Integer wrongCount = totalCount - answerCount;
            List<UserVO3> wrongUserList = userXQuestionMapper.getWrongAnswerList(companyId, questionId);
            for (int j = 0; j < wrongUserList.size(); j++) {
                UserVO3 userVO3 = wrongUserList.get(j);
                Integer userId = userVO3.getId();
                String name = userMapper.getNameById(userId);
                String deptName = userXDeptMapper.getUserDeptName(isvTicketEntity.getCorpId(), userId);
                userVO3.setName(name);
                userVO3.setDeptName(deptName);
            }
            QuestionLibraryAccuracyVO questionLibraryAccuracyVO = new QuestionLibraryAccuracyVO(questionId, questionVO.getDescription(), allAnswerCount, accuracy, wrongCount);
            questionLibraryAccuracyVO.setWrongUserList(wrongUserList);
            resultList.add(questionLibraryAccuracyVO);
        }
        Map<String, Object> dataMap = Maps.newHashMap();
        dataMap.put("list", resultList);
        dataMap.put("total", quesitonPage.getTotal());
        dataMap.put("page", new Page(quesitonPage.getPrePage(), pageEntity.getPageNo(), quesitonPage.getNextPage(), quesitonPage.getLastPage()));
        return ResultJson.succResultJson(dataMap);
    }

    @Override
    public void exportLibraryCompletionDetail(Integer libraryId) {
        String[] sheetArray = ExcelConstant.getMultiCompletionSheetNameArr();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        LoginUser loginUser = LoginUser.getUser();
        Integer companyId = loginUser.getCompanyID();
        String libraryName = questionsLibraryMapper.getLibraryNameById(libraryId);
        IsvTicketsEntity isvTicketEntity = isvTickestMapper.getIsvTicketByCompanyId(companyId);
//        PageInfo<UserXLibraryEntity> finishUserPage = new PageInfo<>(userXLibraryMapper.getStudyCountByLibraryIdAndSchedule(companyId, libraryId, type));
        List<UserXLibraryEntity> nofinishUserList = userXLibraryMapper.getStudyCountByLibraryIdAndSchedule(companyId, libraryId, 0);
        List<UserXLibraryEntity> finishUserList = userXLibraryMapper.getStudyCountByLibraryIdAndSchedule(companyId, libraryId, 1);
        List<Object> resultList = new ArrayList<>();
        Map<String,List> mixResultMap = new HashMap<>();
        List<UserXLibraryCountVO> finishList = new ArrayList();
        if (CollectionUtils.isNotEmpty(finishUserList)) {
            for (int i = 0; i < finishUserList.size(); i++) {
                UserXLibraryEntity userXLibraryEntity = finishUserList.get(i);
                Integer userId = userXLibraryEntity.getUserId();
                Integer totalCount = questionsMapper.getTotalCountByLibraryId(libraryId);
                Integer answerCount = userXLibraryMapper.getCorrectCount(companyId, userId, libraryId);
                String accuracy = QuestionUtil.getPercentDivision(answerCount, totalCount);
                accuracy = accuracy + "%";
                String schedule = userXLibraryEntity.getSchedule() + "%";
                if(userXLibraryEntity.getSchedule().compareTo(new BigDecimal(100))==0){
                    schedule = "100%";
                }
                String name = userMapper.getNameById(userId);
                String deptName = userXDeptMapper.getUserDeptName(isvTicketEntity.getCorpId(), userId);
                if (StringUtils.isEmpty(deptName)) {
                    deptName = "未知部门";
                }
                UserXLibraryCountVO userXLibraryCountVO = new UserXLibraryCountVO(name, deptName, schedule, accuracy, userXLibraryEntity.getFinishTime());
                finishList.add(userXLibraryCountVO);
            }

        }
        mixResultMap.put(sheetArray[0],finishList);
        resultList.add(finishList);

        List<UserXLibraryCountVO> nofinishList = new ArrayList();
        if (CollectionUtils.isNotEmpty(nofinishUserList)) {
            for (int i = 0; i < nofinishUserList.size(); i++) {
                UserXLibraryEntity userXLibraryEntity = nofinishUserList.get(i);
                Integer userId = userXLibraryEntity.getUserId();
                String schedule = userXLibraryEntity.getSchedule() + "%";
                String name = userMapper.getNameById(userId);
                String deptName = userXDeptMapper.getUserDeptName(isvTicketEntity.getCorpId(), userId);
                if (StringUtils.isEmpty(deptName)) {
                    deptName = "未知部门";
                }
                UserXLibraryCountVO userXLibraryCountVO = new UserXLibraryCountVO(name, deptName, schedule, DateUtil.getDisplayYMDHMS(userXLibraryEntity.getUpdateTime()));
                nofinishList.add(userXLibraryCountVO);
            }

        }
        resultList.add(nofinishList);
        mixResultMap.put(sheetArray[1], nofinishList);

        //ExcelUtil.downloadExcelFile(libraryName + "题库正确率", ExcelConstant.getAccuracyHeadMap(), resultList, response);
        ExcelUtil.downloadExcelMulti(libraryName + "题库正确率", ExcelConstant.getMultiCompletionSheetNameArr(), ExcelConstant.getMultiCompletionTableTitleArr(), ExcelConstant.getHeadTableColumnArr(), ExcelConstant.getTableColumnNameArr(),mixResultMap,response );

    }

    @Override
    public void exportLibraryAccuracy(Integer libraryId) {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        LoginUser loginUser = LoginUser.getUser();
        Integer companyId = loginUser.getCompanyID();
        String libraryName = questionsLibraryMapper.getLibraryNameById(libraryId);
        IsvTicketsEntity isvTicketEntity = isvTickestMapper.getIsvTicketByCompanyId(companyId);
        List<QuestionVO> questionVOList = questionsMapper.getQuestionList(libraryId, null, null);
        List<QuestionLibraryAccuracyVO> resultList = new ArrayList<>();
        for (int i = 0; i < questionVOList.size(); i++) {
            QuestionVO questionVO = questionVOList.get(i);
            Integer questionId = questionVO.getId();
            Integer answerCount = userXQuestionMapper.getCorrectCount(companyId, questionId);
            Integer totalCount = userXQuestionMapper.getQuestionAnswerUserCount(companyId, questionId);
            String accuracy = totalCount == 0 ? "0" : QuestionUtil.getPercentDivision(answerCount, totalCount);
            accuracy = accuracy + "%";
            Integer allAnswerCount = userXQuestionMapper.getQuestionAnswerCount(companyId, questionId);
            //答错人数
            Integer wrongCount = totalCount - answerCount;
            List<UserVO3> wrongUserList = userXQuestionMapper.getWrongAnswerList(companyId, questionId);
            for (int j = 0; j < wrongUserList.size(); j++) {
                UserVO3 userVO3 = wrongUserList.get(j);
                Integer userId = userVO3.getId();
                String name = userMapper.getNameById(userId);
                String deptName = userXDeptMapper.getUserDeptName(isvTicketEntity.getCorpId(), userId);
                userVO3.setName(name);
                userVO3.setDeptName(deptName);
            }
            QuestionLibraryAccuracyVO questionLibraryAccuracyVO = new QuestionLibraryAccuracyVO(questionId, questionVO.getDescription(), allAnswerCount, accuracy, wrongCount);
            resultList.add(questionLibraryAccuracyVO);
        }
        ExcelUtil.downloadExcelFile(libraryName + "题库正确率", ExcelConstant.getAccuracyHeadMap(), resultList, response);
    }


    @Override
    @Transient
    public JSONObject importQuestionLibrary(Integer libraryId, String filePath, String sheetName, Integer startRow, String label) {
        try {
            Integer userId = 1;
            //标签 第一个为钉钉 其他的为商业
            List<QuestionExcelDTO> resultList = null;
            if (filePath.endsWith("xlsx")) {
                resultList = ExcelQuestionUtil.importXSSF(new File(filePath));
            } else {
                resultList = ExcelQuestionUtil.importHSSF(new File(filePath));
            }
//            List<QuestionExcelDTO> resultList = new ExcelUtil(QuestionExcelDTO.class).importExcel(sheetName, startRow, filePath);
            if (resultList != null && resultList.size() > 0) {
//                QuestionsLibraryEntity questionsLibraryEntity = new QuestionsLibraryEntity(1, sheetName, 0, PUBLIC_SUBJECT, label, userId, StatusEnum.OK.getValue(), null, new Date(), new Date(), 1);
//                Integer result = createOrUpdateQuestionLibrary(questionsLibraryEntity);
//                if (result > 0) {
                List<QuestionsEntity> questionsList = getQuestionsListFromSource(userId, libraryId, resultList);
                //批量导入题目
                Integer questionInsertResult = questionsMapper.batchInsertQuestions(questionsList);
                String oaMessage = sheetName + "成功插入" + questionInsertResult + "条数据,列表共有" + questionsList.size() + "条数据";
                logger.info(oaMessage);
                OAMessageUtil.sendTextMsgToDept(oaMessage);
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<QuestionsEntity> getQuestionsListFromSource(Integer userId, Integer libraryId, List<QuestionExcelDTO> resultList) {
        List<QuestionsEntity> questionsList = new ArrayList<>();
        try {
            Integer companyId = null;
            QuestionsLibraryEntity questionsLibraryEntity = getById(libraryId);
            companyId = questionsLibraryEntity.getCompanyId();
            batchInsertQuestionLabel(companyId, libraryId, resultList);
            Map<String, Integer> questionLabelMap = questionLabelService.getLabelCollectionByLibraryId2(libraryId);
//            Integer userId = LoginUser.getUser().getUserID();
            for (int i = 0; i < resultList.size(); i++) {
                QuestionExcelDTO questionExcelDTO = resultList.get(i);
                QuestionsEntity questionsEntity = new QuestionsEntity();
                BeanUtils.copyProperties(questionsEntity, questionExcelDTO);
                if (StringUtils.isEmpty(questionExcelDTO.getAnswer()) || StringUtils.isEmpty(questionExcelDTO.getDescription())) {
                    logger.info("{第" + i + "题:}" + questionExcelDTO.toString() + "{题目有误!}");
                    continue;
                }
                if (StringUtils.isNotEmpty(questionExcelDTO.getLabel())) {
                    questionsEntity.setLabelId(questionLabelMap.get(questionExcelDTO.getLabel()));
                }
                questionsEntity.setLibraryId(libraryId);
                questionsEntity.setOperator(userId);
                questionsEntity.setOptions(parseChoiceToJson(questionExcelDTO));
                questionsEntity.setStatus(StatusEnum.OK.getValue());
                questionsEntity.setCreateTime(new Date());
                questionsEntity.setUpdateTime(new Date());
                questionsList.add(questionsEntity);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return questionsList;
    }

    private static String parseChoiceToJson(QuestionExcelDTO QuestionDto) {
        JSONObject jsonObject = new JSONObject();
        if (StringUtils.isNotEmpty(QuestionDto.getOptionA())) jsonObject.put("A", QuestionDto.getOptionA());
        else jsonObject.put("A", "");
        if (StringUtils.isNotEmpty(QuestionDto.getOptionB())) jsonObject.put("B", QuestionDto.getOptionB());
        else jsonObject.put("B", "");
        if (StringUtils.isNotEmpty(QuestionDto.getOptionC())) jsonObject.put("C", QuestionDto.getOptionC());
        else jsonObject.put("C", "");
        if (StringUtils.isNotEmpty(QuestionDto.getOptionD())) jsonObject.put("D", QuestionDto.getOptionD());
        else jsonObject.put("D", "");
        return jsonObject.toJSONString();
    }

    private static QuestionExcelDTO parseJsonToQuestionDTO(JSONObject questionJson, QuestionExcelDTO questionExcelDTO) {
        String optionA = questionJson.getString("A");
        questionExcelDTO.setOptionA(optionA);
        String optionB = questionJson.getString("B");
        questionExcelDTO.setOptionB(optionB);
        if (StringUtils.isNotEmpty(questionJson.getString("C"))) {
            String optionC = questionJson.getString("C");
            questionExcelDTO.setOptionC(optionC);
        }
        if (StringUtils.isNotEmpty(questionJson.getString("D"))) {
            String optionD = questionJson.getString("D");
            questionExcelDTO.setOptionD(optionD);
        }

        return questionExcelDTO;
    }

    public static void main(String[] args) {
//        QuestionsLibraryEntity questionsLibrary = new QuestionsLibraryEntity();
//        questionsLibrary.setName("sss");
//        questionsLibrary.setId(1);
//        questionsLibrary.setCreateTime(new Date());
//        questionsLibrary.setLabel("bbbbbb");
//        questionsLibrary.setOperator(5);
//        questionsLibrary.setSubject(0);
//
//        QuestionLibraryVO questionLibraryVO = new QuestionLibraryVO();
//        try {
//            BeanUtils.copyProperties(questionLibraryVO,questionsLibrary);
//            System.out.println(questionLibraryVO.toString());
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("result", ResultCodeEnum.SUCCESS.getValue().toString());
//        jsonObject.put("msg", "ok");
//        jsonObject.put("data", "12,13,14");
//        System.out.println(jsonObject.toJSONString());

//        String answer = "A,B,C";
//        String answer1 = answer.replaceAll(",", "|");
//        System.out.println(answer1);

//        BigDecimal big = new BigDecimal(100.00);
//        if(big.compareTo(new BigDecimal(100))==0){
//            System.out.println(1);
//        }else{
//            System.out.println(2);
//        }

        String str= "100";
        str+="%";
        System.out.println(str);

//        String answer = questionVO.getAnswer();
//        String answer1 = answer.replaceAll(",","|");

    }

}
