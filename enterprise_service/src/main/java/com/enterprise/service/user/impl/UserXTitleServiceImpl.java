package com.enterprise.service.user.impl;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.ResultJson;
import com.enterprise.base.entity.QuestionLibraryTitleEntity;
import com.enterprise.base.entity.UserXTitleEntity;
import com.enterprise.base.enums.StatusEnum;
import com.enterprise.base.exceptions.BusinessException;
import com.enterprise.base.vo.UserCompanyLibraryVO;
import com.enterprise.base.vo.UserXLibraryVO2;
import com.enterprise.base.vo.UserXTitleLibraryVO;
import com.enterprise.base.vo.UserXTitleVO;
import com.enterprise.mapper.questions.QuestionLibraryTitleMapper;
import com.enterprise.mapper.questions.QuestionsMapper;
import com.enterprise.mapper.questions.UserXQuestionsMapper;
import com.enterprise.mapper.users.UserXLibraryMapper;
import com.enterprise.mapper.users.UserXTitleMapper;
import com.enterprise.service.user.UserXTitleService;
import com.enterprise.util.AssertUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/6/28 下午2:40
 */
@Service
public class UserXTitleServiceImpl implements UserXTitleService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private UserXTitleMapper userXTitleMapper;

    @Resource
    private UserXQuestionsMapper userXQuestionsMapper;

    @Resource
    private QuestionsMapper questionMapper;

    @Resource
    private QuestionLibraryTitleMapper questionLibraryTitleMapper;

    @Resource
    private UserXLibraryMapper userXLibraryMapper;

    @Override
    public Integer saveOrUpdateUserXTitle(UserXTitleEntity userXTitleEntity) {
        AssertUtil.notNull(userXTitleEntity.getCompanyId(), "未知的公司ID : " + userXTitleEntity.getCompanyId());
        AssertUtil.notNull(userXTitleEntity.getLibraryId(), "未知的题库ID : " + userXTitleEntity.getLibraryId());
        UserXTitleVO userXTitleVO = userXTitleMapper.findUserXTitleByCompanyAndLibrary(userXTitleEntity.getCompanyId(), userXTitleEntity.getLibraryId(), userXTitleEntity.getUserId(), null);
        Integer result = 0;
        if (userXTitleVO == null) {
            // public UserXTitleEntity(Integer companyId, Integer libraryId, Integer userId, Integer titleId, Integer chooseFlag, Integer status, Date createTime, Date updateTime) {
            result = userXTitleMapper.createUserXTitle(userXTitleEntity);
            return result;
        } else {
            return userXTitleMapper.updateUserXTitle(userXTitleEntity);
        }
    }

    @Override
    public List<UserXTitleVO> findUserXTitleListByCompany(Integer companyId, Integer userId) {
        //获取企业下完成度100%的题库
//        userXTitleMapper.batchDeleteChooseFlag(companyId,userId);
        List<UserXTitleEntity> updateList = new ArrayList<>();
        List<UserXTitleVO> userTitleList = new ArrayList<>();
        List<UserXLibraryVO2> userLibraryList = userXLibraryMapper.getAchieveUserLibrary(companyId,userId);
        for (int i = 0; i < userLibraryList.size(); i++) {
            UserXLibraryVO2 userXLibraryVO = userLibraryList.get(i);
            Integer libraryId = userXLibraryVO.getLibraryId();
            QuestionLibraryTitleEntity questionLibraryTitle = questionLibraryTitleMapper.getQuestionTitleByLibrary(libraryId);
            if(questionLibraryTitle!=null && questionLibraryTitle.getStatus().equals(1)) {
                UserXTitleVO userXTitleVO = userXTitleMapper.findUserXTitleByCompanyAndLibrary(companyId, libraryId, userId, null);
                if(userXTitleVO!=null){
                    UserXTitleVO userXTitle = new UserXTitleVO(userXTitleVO.getId(),userId,questionLibraryTitle.getTitle(),questionLibraryTitle.getType(),userXTitleVO.getChooseFlag());
                    updateList.add(new UserXTitleEntity(userXTitleVO.getId(),userXTitleVO.getChooseFlag(),StatusEnum.OK.getValue()));
                    userTitleList.add(userXTitle);
                }else{
                    UserXTitleEntity userXTitleEntity = new UserXTitleEntity(companyId,libraryId,userId,questionLibraryTitle.getId(),0,1,new Date(),new Date());
                    userXTitleMapper.createUserXTitle(userXTitleEntity);
                    UserXTitleVO userXTitle = new UserXTitleVO(userXTitleEntity.getId(),userId,questionLibraryTitle.getTitle(),questionLibraryTitle.getType(),0);
                    userTitleList.add(userXTitle);
                }
            }
        }
        if(CollectionUtils.isNotEmpty(updateList)){
            userXTitleMapper.batchDeleteChooseFlag(companyId,userId);
            userXTitleMapper.batchUpdate(updateList);
        }
        return userTitleList;
    }

    /**
     * 先批量更新,后更新
     */
    @Override
    public JSONObject wearTitle(Integer companyId, Integer userId, Integer id) throws BusinessException {
        AssertUtil.notNull(id, "未知的头衔id : " + id);
        userXTitleMapper.batchDeleteChooseFlag(companyId, userId);
        Integer result = userXTitleMapper.updateUserXTitle(new UserXTitleEntity(id, StatusEnum.OK.getValue(), StatusEnum.OK.getValue()));
        if (result > 0) {
            return ResultJson.succResultJson(id);
        }
        return ResultJson.errorResultJson("" + id);
    }

    @Override
    public String findUseWearTitleByCompany(Integer companyId, Integer userId) {
        return userXTitleMapper.findUseWearTitleByCompany(companyId, userId);
    }

    @Override
    public UserXTitleVO findUserWearTitleByCompany(Integer companyId, Integer userId) {
        return userXTitleMapper.findUserWearTitleByCompany(companyId, userId);
    }

    /**
     * 修复用户头衔
     */
    @Override
    public JSONObject updateUserTitle(Integer libraryId) {
        QuestionLibraryTitleEntity questionLibraryTitleInDB = questionLibraryTitleMapper.getQuestionTitleByLibrary(libraryId);
        if (questionLibraryTitleInDB != null && questionLibraryTitleInDB.getStatus().equals(StatusEnum.OK.getValue())) {
            Integer totalUpdateCount = 0;
            Integer totoalCountInLibrary = questionMapper.getTotalCountByLibraryId(libraryId);
            List<UserCompanyLibraryVO> userLibraryList = userXQuestionsMapper.getAnsweredUserListByLibraryId(libraryId);
            for (int i = 0; i < userLibraryList.size(); i++) {
                UserCompanyLibraryVO userCompanyLibraryVO = userLibraryList.get(i);
                Integer companyId = userCompanyLibraryVO.getCompanyId();
                Integer userId = userCompanyLibraryVO.getUserId();
                Integer correctAnsweredQuestionNumber = userXQuestionsMapper.getUserCorrectQuestionsByLibraryId(companyId, userId, libraryId);
                if (correctAnsweredQuestionNumber >= totoalCountInLibrary) {
                    UserXTitleVO userXTitleVO = userXTitleMapper.findUserXTitleByCompanyAndLibrary(companyId, libraryId, userId, null);
                    if (userXTitleVO == null) {
                        // public UserXTitleEntity(Integer companyId, Integer libraryId, Integer userId, Integer titleId, Integer chooseFlag, Integer status, Date createTime, Date updateTime)
                        Integer result = userXTitleMapper.createUserXTitle(new UserXTitleEntity(companyId, libraryId, userId, questionLibraryTitleInDB.getId(), 0, StatusEnum.OK.getValue(), new Date(), new Date()));
                        totalUpdateCount++;
                    }
                }
            }
            return ResultJson.errorResultJson("共更新了" + totalUpdateCount + "个记录");
        } else {
            return ResultJson.errorResultJson("题库没有头衔");
        }
    }

    /**
     * 遍历所有的用户头衔,去掉完成度不够的头衔
     */
    @Override
    public JSONObject clearTitle() {
        List<UserXTitleLibraryVO> userTitleList = userXTitleMapper.getAllList(StatusEnum.OK.getValue());
        Integer totalCount = 0;
        for (int i = 0; i < userTitleList.size(); i++) {
            UserXTitleLibraryVO userXLibraryVO = userTitleList.get(i);
            Integer companyId = userXLibraryVO.getCompanyId();
            Integer userId = userXLibraryVO.getUserId();
            Integer libraryId = userXLibraryVO.getLibraryId();
            Integer totoalCountInLibrary = questionMapper.getTotalCountByLibraryId(libraryId);
            Integer correctAnsweredQuestionNumber = userXQuestionsMapper.getUserCorrectQuestionsByLibraryId(companyId, userId, libraryId);
            if (correctAnsweredQuestionNumber < totoalCountInLibrary) {
                userXTitleMapper.updateUserXTitle(new UserXTitleEntity(userXLibraryVO.getId(), null, StatusEnum.DELETE.getValue()));
                totalCount++;
            }
        }
        return ResultJson.succResultJson(totalCount);
    }


}
