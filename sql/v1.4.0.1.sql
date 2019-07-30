CREATE TABLE `pet_visit` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `company_id` int(8) NOT NULL COMMENT '公司id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `visit_user_id` int(10) NOT NULL COMMENT '被访问打用户id',
  `create_time` timestamp NOT NULL  COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `index_company_user` (`company_id`,`visit_user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户串门';



ALTER TABLE paper_ball ADD ball_type INT(4)  COMMENT '纸团类型' DEFAULT  0 AFTER user_id ;