ALTER TABLE bag_tool_people ADD INDEX company_user_tool(`company_id`,`user_id`,`tool_id`);

ALTER TABLE isv_tickets ADD is_buy INT(4) COMMENT '是否购买' Default 0 AFTER status;

drop table market_buy;
CREATE TABLE `market_buy` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `suite_key` varchar(64) NOT NULL COMMENT '应用suitekey',
  `buy_corp_id` varchar(64) NOT NULL COMMENT '公司corpid',
  `goods_code` varchar(64) NOT NULL COMMENT '商品码',
  `item_code` varchar(64) NOT NULL COMMENT '商品规格码',
  `item_name` varchar(32) NOT NULL COMMENT '商品规格名称',
  `sub_quantity` int(4) COMMENT '订购具体人数',
  `max_of_people` int(8) COMMENT '最多服务企业人数',
  `min_of_people` int(8) COMMENT '最少服务企业人数',
  `order_id` varchar(32) NOT NULL COMMENT '订单id',
  `paid_time` timestamp NOT NULL COMMENT '下单时间',
  `service_stop_time` timestamp NOT NULL COMMENT '服务到期时间',
  `pay_fee` decimal(6,2) NOT NULL COMMENT '订单支付费用，以分为单位',
  `order_create_source` varchar(32) COMMENT '订单创建来源',
  `nominal_pay_fee` decimal(6,2) COMMENT '钉钉分销系统提单价',
  `discountFee` decimal(6,2) COMMENT '折扣减免费用',
  `discount` decimal(6,2) COMMENT '订单折扣',
  `distributor_corp_id` varchar(64) COMMENT '分销系统提单的代理商的企业corpId',
  `distributor_corp_name` varchar(64) COMMENT '分销系统提单的代理商的企业名称',
  `status` int(4) COMMENT '状态(0:过期，1:正常,2:重新购买失效)',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `index_buy_corp_id` (`buy_corp_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='购买下单';

