package com.enterprise.mapper.isvTickets;

import com.enterprise.base.entity.IsvTicketsEntity;
import com.enterprise.base.vo.TicketVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by null on 2018-03-23 10:25:20
 */
public interface IsvTicketsMapper {
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
    IsvTicketsEntity getIsvTicketByCorpId(@Param("corpId") String corpId);

    /**
     * 根据企业corpId 获取isvTickets信息
     *
     * @param corpId 企业corpId
     * @author anson
     * @date 2018/5/4 上午10:01
     */
    Integer getCompanyIdByCorpId(@Param("corpId") String corpId);

    /**
     * 根据企业Id 获取isvTickets信息
     *
     * @param companyId 企业Id
     * @author shisan
     * @date 2018/3/27 下午2:05
     */
    IsvTicketsEntity getIsvTicketByCompanyId(@Param("companyId") Integer companyId);

    TicketVO getIsvTicketVOByCompanyId(@Param("companyId") Integer companyId);

    /**
     * 获取需要刷新的isvTickets
     *
     * @author shisan
     * @date 2018/3/26 下午2:03
     */
    List<IsvTicketsEntity> seachRefeshServiceTickets();

    /**
     * 获取所有的公司
     * */
    List<IsvTicketsEntity> getAllCompanys();

    /**
     * 获取没有拿到接入用户的企业
     * */
    List<IsvTicketsEntity> getNoAuthList();
}