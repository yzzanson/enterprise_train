
drop table if exists community_invite;
CREATE TABLE `community_invite` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `company_id` int(8) NOT NULL COMMENT '企业id',
  `user_id` int(8) NOT NULL COMMENT '被邀请用户id',
  `invite_user` int(8) NOT NULL COMMENT '邀请用户id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `index_company_user` (`company_id`,`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='邀请开通云社区';

drop table if exists community_invite_detail;
CREATE TABLE `community_invite_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `company_id` int(8) NOT NULL COMMENT '企业id',
  `user_id` int(8) NOT NULL COMMENT '被邀请用户id',
  `user_name` varchar(32) NOT NULL COMMENT '管理员姓名',
  `phone_num` varchar(16) NOT NULL COMMENT '管理员手机号',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `index_company_user` (`company_id`,`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='报名开通云社区';
