package com.enterprise.service.marketBuy.impl;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.GlobalConstant;
import com.enterprise.base.common.LoginUser;
import com.enterprise.base.common.ResultJson;
import com.enterprise.base.entity.IsvTicketsEntity;
import com.enterprise.base.entity.MarketBuyEntity;
import com.enterprise.base.enums.StatusEnum;
import com.enterprise.base.vo.MaketBuyVO;
import com.enterprise.mapper.companyInfo.CompanyInfoMapper;
import com.enterprise.mapper.isvTickets.IsvTicketsMapper;
import com.enterprise.mapper.markeyBuy.MarketBuyMapper;
import com.enterprise.oa.OAMessageUtil;
import com.enterprise.service.isvTickets.IsvTicketsService;
import com.enterprise.service.marketBuy.MarketBuyService;
import com.enterprise.service.redis.RedisService;
import com.enterprise.util.DateUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by liam on 2018-07-02 10:16:48
 */
@Service
public class MarketBuyServiceImpl implements MarketBuyService {

    private static final Logger logger = LoggerFactory.getLogger(MarketBuyServiceImpl.class);

    @Resource
    private MarketBuyMapper marketBuyMapper;
    @Resource
    private IsvTicketsService isvTicketsService;

    @Resource
    private IsvTicketsMapper isvTicketMapper;
    @Resource
    private CompanyInfoMapper companyInfoMapper;

    @Resource
    private RedisService redisService;

    private static final Integer DEFAULT_MAX_FREE = 100;

    private static final String DEFAULT_VERSION = GlobalConstant.DEFAULT_VERSION;

    private static final Integer DEFAULT_FREE_TIME = GlobalConstant.DEFAULT_FREE_TIME;

    /**
     * 付费购买记录
     *
     * @param plainTextJson
     */
    @Override
    public void add(JSONObject plainTextJson) {
//		{
//					"EventType": "market_buy",
//						"SuiteKey": "suited6db0pze8yao1b1y",
//						"buyCorpId": "dingxxxxxxxx",
//						"goodsCode": "FW_GOODS-xxxxxxxx",
//						"itemCode": "1c5f70cf04c437fb9aa1b20xxxxxxxx",
//						"itemName": "按照范围收费规格0-300",
//						"subQuantity": 1（订购的具体人数）,
//					"maxOfPeople": 300,
//						"minOfPeople": 0,
//						"orderId": 308356401xxxxxxxx,
//						"paidtime": 1474535702000,
//						"serviceStopTime": 1477065600000,
//						"payFee":147600,
//						"orderCreateSource":"DRP",
//						"nominalPayFee":147600,
//						"discountFee":600,
//						"discount":0.06,
//						"distributorCorpId":"ding9f50b15bccd16741",
//						"distributorCorpName":"测试企业"
//				}
        try {

//			MarketBuy marketBuy =  JSON.parseObject(plainTextJson.toJSONString(),new TypeReference<MarketBuy>(){});
            MarketBuyEntity marketBuy = parse2MarketBuy(plainTextJson);
            String corpId = marketBuy.getBuyCorpId();


            List<MarketBuyEntity> list = marketBuyMapper.getByCorpId(corpId, StatusEnum.OK.getValue());
            if (list != null && list.size() > 0) {
                for (MarketBuyEntity b : list) {
                    b.setStatus(StatusEnum.HOLD_ON.getValue());
                }
                marketBuyMapper.batchUpdate(list);
            }

            logger.info("最终:" + marketBuy.toString());
            marketBuyMapper.createMarketBuy(marketBuy);

            // 跟新isvtickets  购买字段
            if (marketBuy.getPayFee() != null && marketBuy.getPayFee().compareTo(new BigDecimal(0)) == 1) {
                IsvTicketsEntity isvTickets = new IsvTicketsEntity();
                isvTickets = isvTicketMapper.getIsvTicketByCorpId(corpId);
                if (isvTickets != null) {
                    isvTickets.setIsBuy(StatusEnum.OK.getValue());
                    isvTicketsService.modifyIsvTickets(isvTickets);
                    OAMessageUtil.sendTextMsgToDept("购买成功 > " + marketBuy.getBuyCorpId() + ":" + marketBuy.getPayFee());
                    //DingMsgUtil.sendMsg("购买成功 > "+isvTickets.getCorpName()+":"+marketBuy.getPayFee());
                } else {
                    OAMessageUtil.sendTextMsgToDept("有人购买,但找不到isvtickets接入记录,可能购买回调记录比授权快" + plainTextJson.toJSONString());
                    //DingMsgUtil.sendMsg("有人购买,但找不到isvtickets接入记录,可能购买回调记录比授权快"+plainTextJson.toJSONString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            //DingMsgUtil.sendMsg("保存购买记录失败,请立刻手动添加 " + plainTextJson.toJSONString());
        }
    }

    @Override
    public boolean isBuy(String corpId) {
        BigDecimal sum = marketBuyMapper.isBuy(corpId);
        if (sum != null && sum.doubleValue() > 0) {
            return true;
        }
        return false;
    }

    //status 0正常 1过期
    @Override
    public JSONObject getMarketVersionMessage() {
        LoginUser loginUser = LoginUser.getUser();
        logger.info("获取版本号用户信息:"+loginUser.toString());
        Integer modelType = GlobalConstant.modelType;//1测试 2正式
        String corpId = loginUser.getCorpID();
        Integer companyId = loginUser.getCompanyID();
        String companyName = companyInfoMapper.getCompanyNameById(companyId);
        if (modelType == 2) {
            MaketBuyVO marketVO = new MaketBuyVO();
            if (companyId <= DEFAULT_MAX_FREE) {
                //MaketBuyVO(String corpId, String corpName, String version, Integer freeDay,Integer status)
                marketVO = new MaketBuyVO(corpId, companyName, DEFAULT_VERSION, DEFAULT_FREE_TIME, StatusEnum.DELETE.getValue());
                return ResultJson.succResultJson(marketVO);
            }
            String versionName = "";
            IsvTicketsEntity isvTicketsEntity = isvTicketsService.getIsvTicketByCompanyId(companyId);
            List<MarketBuyEntity> marketBuyList = marketBuyMapper.getByCorpId(isvTicketsEntity.getCorpId(), StatusEnum.OK.getValue());
            if (CollectionUtils.isNotEmpty(marketBuyList)) {
                MarketBuyEntity marketBuyEntity = marketBuyList.get(0);
                Long diffDate = DateUtil.getDiffDays(marketBuyEntity.getServiceStopTime(), new Date());
                if(diffDate<0){
                    marketVO = new MaketBuyVO(corpId, companyName, marketBuyEntity.getItemName(), diffDate.intValue(), StatusEnum.OK.getValue());
                    return ResultJson.errorResultJson(marketVO);
                }
                marketVO = new MaketBuyVO(corpId, companyName, marketBuyEntity.getItemName(),  diffDate.intValue(), StatusEnum.DELETE.getValue());
                return ResultJson.succResultJson(marketVO);
            } else {
                versionName = "未知版本";
                marketVO = new MaketBuyVO(corpId, companyName, versionName,  0, StatusEnum.DELETE.getValue());
                return ResultJson.errorResultJson(marketVO);
            }
        } else {
            MaketBuyVO marketVO = new MaketBuyVO(corpId, companyName, DEFAULT_VERSION, DEFAULT_FREE_TIME, StatusEnum.DELETE.getValue());
            return ResultJson.succResultJson(marketVO);
        }
    }

    @Override
    public List<String> getMarketVesion() {
        return marketBuyMapper.getMarketVersionList();
    }

    @Override
    public MarketBuyEntity getByCorpId(String corpId) {
        return marketBuyMapper.getByCorpIdAndStatus(corpId, StatusEnum.OK.getValue());
    }

    private MarketBuyEntity parse2MarketBuy(JSONObject plainTextJson) {
        MarketBuyEntity marketBuy = new MarketBuyEntity();
        marketBuy.setSuiteKey(plainTextJson.getString("SuiteKey"));
        marketBuy.setBuyCorpId(plainTextJson.getString("buyCorpId"));
        System.out.println("buyCorpId > " + plainTextJson.getString("buyCorpId"));
        marketBuy.setGoodsCode(plainTextJson.getString("goodsCode"));
        marketBuy.setItemCode(plainTextJson.getString("itemCode"));
        marketBuy.setItemName(plainTextJson.getString("itemName"));
        if (StringUtils.isNotBlank(plainTextJson.getString("subQuantity")))
            marketBuy.setSubQuantity(plainTextJson.getInteger("subQuantity"));
        if (StringUtils.isNotBlank(plainTextJson.getString("maxOfPeople")))
            marketBuy.setMaxOfPeople(plainTextJson.getInteger("maxOfPeople"));
        if (StringUtils.isNotBlank(plainTextJson.getString("minOfPeople")))
            marketBuy.setMinOfPeople(plainTextJson.getInteger("minOfPeople"));
        marketBuy.setOrderId(plainTextJson.getString("orderId"));
        marketBuy.setPaidTime(plainTextJson.getDate("paidtime"));
        marketBuy.setServiceStopTime(plainTextJson.getDate("serviceStopTime"));
        marketBuy.setPayFee(plainTextJson.getBigDecimal("payFee"));

        System.out.println("payFee > " + plainTextJson.getBigDecimal("payFee"));
        marketBuy.setOrderCreateSource(plainTextJson.getString("orderCreateSource"));
        marketBuy.setNominalPayFee(plainTextJson.getBigDecimal("nominalPayFee"));
        marketBuy.setDiscountFee(plainTextJson.getBigDecimal("discountFee"));
        marketBuy.setDiscount(plainTextJson.getBigDecimal("discount"));
        marketBuy.setDistributorCorpId(plainTextJson.getString("distributorCorpId"));
        marketBuy.setDistributorCorpName(plainTextJson.getString("distributorCorpName"));
        marketBuy.setStatus(StatusEnum.OK.getValue());
        return marketBuy;
    }



    public static void main(String[] args) {
//        String str = "{\"EventType\": \"market_buy\", \"SuiteKey\": \"suited6db0pze8yao1b1y\", \"buyCorpId\": \"dingxxxxxxxx\", \"goodsCode\": \"FW_GOODS-xxxxxxxx\", \"itemCode\": \"1c5f70cf04c437fb9aa1b20xxxxxxxx\", \"itemName\": \"按照范围收费规格0-300\", \"subQuantity\": 1, \"maxOfPeople\": 300, \"minOfPeople\": 0,\"orderId\": \"308356401xxxxxxxx\", \"paidtime\": 1474535702000, \"serviceStopTime\": 1477065600000, \"payFee\":147600, \"orderCreateSource\":\"DRP\", \"nominalPayFee\":147600, \"discountFee\":600, \"discount\":0.06, \"distributorCorpId\":\"ding9f50b15bccd16741\", \"distributorCorpName\":\"测试企业\"}";
//
//        JSONObject s = JSONObject.parseObject(str);
//        System.out.println(s.toJSONString());
//        System.out.println("buyCorpId > " + s.getString("buyCorpId"));
//
//        MarketBuyEntity marketBuy = JSON.parseObject(str, new TypeReference<MarketBuyEntity>() {
//        });
//        System.out.println(marketBuy.getMaxOfPeople());
//        Date date = DateUtil.getDate("2019-01-28 00:00:00");
//        long diffDate = DateUtil.getDiffDays(date, new Date());
//        System.out.println(diffDate);
//
        long time = 1548691200000L;
        Date date = new Date(time);
        System.out.println(DateUtil.getDisplayYMDHMS(date));


    }
}