package com.enterprise.service.isvTickets.impl;

import com.enterprise.base.entity.IsvTicketsEntity;
import com.enterprise.base.entity.SuitesEntity;
import com.enterprise.base.enums.StatusEnum;
import com.enterprise.base.vo.TicketVO;
import com.enterprise.cache.GetCache;
import com.enterprise.mapper.isvTickets.IsvTicketsMapper;
import com.enterprise.service.isvTickets.IsvTicketsService;
import com.enterprise.service.suites.SuitesService;
import com.enterprise.util.AssertUtil;
import com.enterprise.util.dingtalk.AuthHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * IsvTicketsService
 *
 * @Author shisan
 * @Date 2018/3/26 上午10:19
 */
@Service
public class IsvTicketsServiceImpl implements IsvTicketsService {

    private static final Logger logger = LoggerFactory.getLogger(IsvTicketsServiceImpl.class);

    @Resource
    private IsvTicketsMapper isvTicketsMapper;

    @Resource
    private SuitesService suitesService;

    @Override
    public void createIsvTickets(IsvTicketsEntity isvTickets) {
        AssertUtil.notNull(isvTickets, "企业IsvTickets信息不能为空!");

        isvTicketsMapper.createIsvTickets(isvTickets);
    }

    @Override
    public void modifyIsvTickets(IsvTicketsEntity IsvTickets) {
        AssertUtil.notNull(IsvTickets, "IsvTickets 不能为空!");
        AssertUtil.notNull(IsvTickets.getId(), "IsvTickets的Id不能为空!");

        isvTicketsMapper.modifyIsvTickets(IsvTickets);
    }

    @Override
//    @GetCache(name="isvticket",value="corpId")
    public IsvTicketsEntity getIsvTicketByCorpId(String corpId) {
        IsvTicketsEntity isvTicketsEntityInDB = isvTicketsMapper.getIsvTicketByCorpId(corpId);
        if (isvTicketsEntityInDB == null) {
            return null;
        }
//        if (DateUtil.getMinutesBetweenTwoDate(new Date(), isvTicketsEntityInDB.getUpdateTime()) >= 90) {
//            isvRefreshOne(isvTicketsEntityInDB);
//        }
        return isvTicketsEntityInDB;
    }

    @Override
    @GetCache(name="isvticket",value="companyId")
    public IsvTicketsEntity getIsvTicketByCompanyId(Integer companyId) {
        AssertUtil.notNull(companyId, "companyId不能为空!");
        return isvTicketsMapper.getIsvTicketByCompanyId(companyId);
    }

    private String isvRefreshOne(IsvTicketsEntity isvTicket) {
        SuitesEntity suitesEntityInDB = suitesService.getSuitesentity(new SuitesEntity(StatusEnum.OK.getValue(), isvTicket.getSuiteId()));
        // 企业的ticket
        String corpAccessToken = AuthHelper.getCorpAccessToken(isvTicket.getCorpId(), suitesEntityInDB.getSuiteAccessToken(), isvTicket.getCorpPermanentCode());
        String corpTicket = AuthHelper.getCorpTicket(corpAccessToken);
        isvTicket.setCorpAccessToken(corpAccessToken);
        isvTicket.setCorpTicket(corpTicket);
        isvTicketsMapper.modifyIsvTickets(isvTicket);
        return corpAccessToken;
    }

    @Override
    public TicketVO getIsvTicketVOByCompanyId(Integer companyId) {
        return isvTicketsMapper.getIsvTicketVOByCompanyId(companyId);
    }

    public List<IsvTicketsEntity> seachRefreshIsvTickets() {
        return isvTicketsMapper.seachRefeshServiceTickets();
    }

    @Override
    public void refreshIsvTicket(IsvTicketsEntity entity) {
        AssertUtil.notNull(entity, "IsvTickets信息不能为空!");
        AssertUtil.notNull(entity.getSuiteId(), "套件Id不能为空!");
        isvRefreshOne(entity);
    }
}