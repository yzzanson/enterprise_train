package com.enterprise.service.question.impl;

import com.enterprise.base.entity.IsvTicketsEntity;
import com.enterprise.base.entity.UserEntity;
import com.enterprise.base.entity.WeekRankEntity;
import com.enterprise.base.vo.UserAnswerRankSimpleNewVO;
import com.enterprise.mapper.bagTool.BagToolEffectMapper;
import com.enterprise.mapper.department.DepartmentMapper;
import com.enterprise.mapper.rank.WeekRankMapper;
import com.enterprise.mapper.users.UserMapper;
import com.enterprise.service.isvTickets.IsvTicketsService;
import com.enterprise.service.question.WeekRankService;
import com.enterprise.service.user.UserXDeptService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/9/5 上午11:07
 */
@Service
public class WeekRankServiceImpl implements WeekRankService {

    @Resource
    private WeekRankMapper weekRankMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserXDeptService userXDeptService;

    @Resource
    private IsvTicketsService isvTicketsService;

    @Resource
    private DepartmentMapper departmentMapper;

    @Resource
    private BagToolEffectMapper bagToolEffectMapper;

    @Override
    public Integer batchInsert(List<WeekRankEntity> weekRankList) {
        return weekRankMapper.batchInsert(weekRankList);
    }

    @Override
    public Integer singleDelete(Integer companyId,String date) {
        return weekRankMapper.singleDelete(companyId,date);
    }

    @Override
    public List<UserAnswerRankSimpleNewVO> getWeekRankByCompanyId(Integer companyId,String date) {

        List<WeekRankEntity> weekRankList =  weekRankMapper.getWeekRankByCompanyId(companyId, date);
        IsvTicketsEntity isvTicketsEntity = isvTicketsService.getIsvTicketByCompanyId(companyId);
        List<UserAnswerRankSimpleNewVO> reusltList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(weekRankList)) {
            for (WeekRankEntity weekRank : weekRankList) {
                Integer userId = weekRank.getUserId();
                UserEntity userEntity = userMapper.getUserById(userId);
                Integer departmentId = userXDeptService.getDepartmentId(isvTicketsEntity.getCorpId(), userId);
                String departmentName = departmentMapper.getDeptNameById(companyId, departmentId);
                UserAnswerRankSimpleNewVO userAnswerRankSimpleVO = new UserAnswerRankSimpleNewVO(userId,userEntity.getName(),userEntity.getAvatar(),weekRank.getRank(),departmentName);
                if(weekRank.getTitle()!=null) {
                    userAnswerRankSimpleVO.setTitle(weekRank.getTitle());
                }
                if(weekRank.getTitleType()!=null) {
                    userAnswerRankSimpleVO.setTitleType(weekRank.getTitleType());
                }
                userAnswerRankSimpleVO.setWeight(weekRank.getWeight());
                reusltList.add(userAnswerRankSimpleVO);
            }
        }
        return reusltList;
    }
}
