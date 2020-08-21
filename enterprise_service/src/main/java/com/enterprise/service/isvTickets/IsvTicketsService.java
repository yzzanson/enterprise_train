package com.enterprise.service.isvTickets;

import com.enterprise.base.entity.IsvTicketsEntity;
import com.enterprise.base.vo.TicketVO;

import java.util.List;

/**
 * IsvTicketsService
 *
 * @Author shisan
 * @Date 2018/3/26 上午10:19
 */
public interface IsvTicketsService {

    /**
     * 新增IsvTicketsEntity
     *
     * @param isvTickets 新增内容
     * @author shisan
     * @date 2017/11/30 下午3:00
     */
    void createIsvTickets(IsvTicketsEntity isvTickets);

    /**
     * 修改 IsvTicketsEntity
     *
     * @param isvTickets 修改内容
     * @author shisan
     * @date 2017/11/30 下午3:08
     */
    void modifyIsvTickets(IsvTicketsEntity isvTickets);

    /**
     * 根据企业corpId 获取isvTickets信息
     *
     * @param corpId 企业corpId
     * @author shisan
     * @date 2018/3/26 上午10:10
     */
    IsvTicketsEntity getIsvTicketByCorpId(String corpId);

    /**
     * 根据企业Id 获取isvTickets信息
     *
     * @param companyId 企业Id
     * @author shisan
     * @date 2018/3/27 下午2:05
     */
    IsvTicketsEntity getIsvTicketByCompanyId(Integer companyId);

    /**
     * 根据企业Id 获取isvTickets信息
     *
     * @param companyId 企业Id
     * @author shisan
     * @date 2018/3/27 下午2:05
     */
    TicketVO getIsvTicketVOByCompanyId(Integer companyId);

    /**
     * 获取需要刷新的isvTickets
     *
     * @author shisan
     * @date 2018/3/26 下午2:03
     */
    List<IsvTicketsEntity> seachRefreshIsvTickets();

    /**
     * 根据条件刷新的isvTickets
     *
     * @param entity isvtickets 信息
     * @author shisan
     * @date 2018/3/26 下午2:08
     */
    IsvTicketsEntity refreshIsvTicket(IsvTicketsEntity entity);

}