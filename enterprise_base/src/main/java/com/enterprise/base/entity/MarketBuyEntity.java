package com.enterprise.base.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/12/28 下午5:51
 */
public class MarketBuyEntity {

    private Integer id;

    //购买应用的suiteKey
    private String suiteKey;

    //购买应用企业corpid
    private String buyCorpId;

    //购买商品码
    private String goodsCode;

    //购买商品规格码
    private String itemCode;

    //商品规格名称
    private String itemName;

    //订购具体人数
    private Integer subQuantity;

    //购买的商品规格能服务的最多企业人数
    private Integer maxOfPeople;

    //购买的商品规格能服务的最少企业人数
    private Integer minOfPeople;

    //订单id
    private String orderId;

    //下单时间
    private Date paidTime;

    //该订单的服务到期时间
    private Date serviceStopTime;

    //订单支付费用，以分为单位
    private BigDecimal payFee;

    //订单创建来源，如果来自钉钉分销系统，则值为"DRP"
    private String orderCreateSource;

    //钉钉分销系统提单价，以分为单位
    private BigDecimal nominalPayFee;

    //折扣减免费用
    private BigDecimal discountFee;

    //订单折扣
    private BigDecimal discount;

    //钉钉分销系统提单的代理商的企业corpId
    private String distributorCorpId;

    //钉钉分销系统提单的代理商的企业名称
    private String distributorCorpName;

    private Date createTime;

    private Date updateTime;

    //0过期 1正常 2重新购买废除
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSuiteKey() {
        return suiteKey;
    }

    public void setSuiteKey(String suiteKey) {
        this.suiteKey = suiteKey;
    }

    public String getBuyCorpId() {
        return buyCorpId;
    }

    public void setBuyCorpId(String buyCorpId) {
        this.buyCorpId = buyCorpId;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getSubQuantity() {
        return subQuantity;
    }

    public void setSubQuantity(Integer subQuantity) {
        this.subQuantity = subQuantity;
    }

    public Integer getMaxOfPeople() {
        return maxOfPeople;
    }

    public void setMaxOfPeople(Integer maxOfPeople) {
        this.maxOfPeople = maxOfPeople;
    }

    public Integer getMinOfPeople() {
        return minOfPeople;
    }

    public void setMinOfPeople(Integer minOfPeople) {
        this.minOfPeople = minOfPeople;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Date getPaidTime() {
        return paidTime;
    }

    public void setPaidTime(Date paidTime) {
        this.paidTime = paidTime;
    }

    public Date getServiceStopTime() {
        return serviceStopTime;
    }

    public void setServiceStopTime(Date serviceStopTime) {
        this.serviceStopTime = serviceStopTime;
    }

    public BigDecimal getPayFee() {
        return payFee;
    }

    public void setPayFee(BigDecimal payFee) {
        this.payFee = payFee;
    }

    public String getOrderCreateSource() {
        return orderCreateSource;
    }

    public void setOrderCreateSource(String orderCreateSource) {
        this.orderCreateSource = orderCreateSource;
    }

    public BigDecimal getNominalPayFee() {
        return nominalPayFee;
    }

    public void setNominalPayFee(BigDecimal nominalPayFee) {
        this.nominalPayFee = nominalPayFee;
    }

    public BigDecimal getDiscountFee() {
        return discountFee;
    }

    public void setDiscountFee(BigDecimal discountFee) {
        this.discountFee = discountFee;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public String getDistributorCorpId() {
        return distributorCorpId;
    }

    public void setDistributorCorpId(String distributorCorpId) {
        this.distributorCorpId = distributorCorpId;
    }

    public String getDistributorCorpName() {
        return distributorCorpName;
    }

    public void setDistributorCorpName(String distributorCorpName) {
        this.distributorCorpName = distributorCorpName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }


}
