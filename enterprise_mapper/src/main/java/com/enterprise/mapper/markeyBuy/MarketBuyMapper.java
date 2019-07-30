package com.enterprise.mapper.markeyBuy;

import com.enterprise.base.entity.MarketBuyEntity;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by liam on 2018-07-02 10:16:48
 */
public interface MarketBuyMapper{

    BigDecimal isBuy(@Param("corpId") String corpId);

    List<MarketBuyEntity> getByCorpId(@Param("corpId") String corpId,@Param("status") Integer status);

    Integer createMarketBuy(MarketBuyEntity marketBuyEntity);

    Integer batchUpdate(@Param("marketBuyList") List<MarketBuyEntity> marketBuyList);

    MarketBuyEntity getByCorpIdAndStatus(@Param("corpId") String corpId,@Param("status") Integer status);

    List<String> getMarketVersionList();
}