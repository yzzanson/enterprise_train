package com.enterprise.service.marketBuy;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.entity.MarketBuyEntity;

import java.util.List;

/**
 * Created by liam on 2018-07-02 10:16:48
 */
public interface MarketBuyService {

    void add(JSONObject plainTextJson);

    boolean isBuy(String corpId);

    JSONObject getMarketVersionMessage();

    List<String> getMarketVesion();

    MarketBuyEntity getByCorpId(String corpId);
}