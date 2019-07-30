ALTER TABLE isv_tickets ADD auth_user_id VARCHAR(64) COMMENT '开通用户dingid' AFTER is_buy;
ALTER TABLE isv_tickets ADD is_call INT(4) COMMENT '是否可' AFTER is_buy;

ALTER TABLE questions ADD blank_index INT(4) COMMENT '是否按顺序' AFTER parent_id;

ALTER TABLE questions MODIFY options VARCHAR(500) NULL;

ALTER TABLE questions MODIFY answer VARCHAR(80);


ALTER TABLE user_x_library ADD answer_count INT(4) COMMENT '回答题数' AFTER schedule;
ALTER TABLE questions_library ADD total_count INT(4) COMMENT '题目总数' AFTER use_count;
ALTER TABLE questions_library ADD is_oa INT(4) COMMENT '题目总数' AFTER sort_type;

ALTER TABLE user_x_library MODIFY schedule DECIMAL(8,2) COMMENT '进度';


ALTER TABLE user_x_library ADD finish_time datetime NULL COMMENT '完成时间' AFTER last_answer_time;



drop table if exists user_config;
CREATE TABLE `user_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_id` int(8) NOT NULL COMMENT '用户id',
  `is_voice` int(8) DEFAULT 0 COMMENT '是否开启游戏音效',
  `is_oa` int(4) DEFAULT 0 COMMENT '是否开启OA消息',
  `is_only_wrong` int(4) DEFAULT 0 COMMENT '是否只显示错题',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户设置';




desc user_config;

