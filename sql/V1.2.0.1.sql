CREATE TABLE `rank_praise` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `company_id` int(8) NOT NULL COMMENT '公司id',
  `user_id` int(8) NOT NULL COMMENT '用户id',
  `praise_user_id` int(8) NOT NULL COMMENT '被点赞用户id',
  `type` int(4) NOT NULL DEFAULT '1' COMMENT '类型',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  INDEX companyid_userid(company_id,user_id),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='排行榜点赞';
