ALTER TABLE my_pet ADD weight INT(8) COMMENT '宠物体重' AFTER physical_value;
ALTER TABLE my_pet drop column weight;


CREATE TABLE `pet_food` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `company_id` int(8) NOT NULL COMMENT '公司id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `food_count` int(10) NOT NULL COMMENT '饲料值',
  `create_time` timestamp NOT NULL  COMMENT '创建时间',
  `update_time` timestamp NOT NULL  COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `index_company_user` (`company_id`,`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='宠物饲料';

CREATE TABLE `pet_food_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `company_id` int(8) NOT NULL COMMENT '公司id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `type` int(4) NOT NULL COMMENT '类型',
  `food_count` int(8) NOT NULL COMMENT '食物量',
  `create_time` timestamp NOT NULL  COMMENT '创建时间',
  `update_time` timestamp NOT NULL  COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `index_company_user` (`company_id`,`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='宠物饲料详情';

ALTER TABLE pet_food_plate_detail ADD status INT(4) COMMENT '状态0/1 消耗完/未消耗完' AFTER type ;
ALTER TABLE pet_food_plate_detail ADD consume_plate_id INT(8) COMMENT '消耗的喂养记录id' AFTER type ;


CREATE TABLE `pet_food_plate` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `company_id` int(8) NOT NULL COMMENT '公司id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `food_count` int(8) NOT NULL COMMENT '食物量',
  `create_time` timestamp NOT NULL  COMMENT '创建时间',
  `update_time` timestamp NOT NULL  COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `index_company_user` (`company_id`,`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='宠物饲料盘';


drop table if exists  pet_food_plate_detail;
CREATE TABLE `pet_food_plate_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `company_id` int(8) NOT NULL COMMENT '公司id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `feed_user_id` int(11) COMMENT '被包养用户id',
  `food_count` int(8) NOT NULL COMMENT '食物量',
  `type` int(4) NOT NULL COMMENT '类型',
  `create_time` timestamp NOT NULL  COMMENT '创建时间',
  `update_time` timestamp NOT NULL  COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `index_company_user` (`company_id`,`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='宠物饲料盘详情';

alter table pet_food_plate_detail add next_plan_consume_time timestamp comment '下次更新时间';


drop table if exists  paper_ball;
CREATE TABLE `paper_ball` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `company_id` int(8) NOT NULL COMMENT '公司id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `clean_user_id` int(11) COMMENT '清理用户id',
  `create_time` timestamp NOT NULL  COMMENT '创建时间',
  `clean_time` datetime  COMMENT '清理时间',
  `elimate_time` datetime COMMENT '消失时间',
  `status` int(4) NOT NULL  COMMENT '当前状态',
  PRIMARY KEY (`id`),
  KEY `index_company_user` (`company_id`,`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='纸团信息';




drop table if exists  pet_weight;
CREATE TABLE `pet_weight` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `company_id` int(8) NOT NULL COMMENT '公司id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `weight` int(8) COMMENT '宠物体重',
  `create_time` timestamp NOT NULL  COMMENT '创建时间',
  `update_time` timestamp NOT NULL  COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `index_company_user` (`company_id`,`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='宠物体重';


#new
ALTER TABLE bag_tool MODIFY relieve VARCHAR(128) COMMENT '解除方法';
ALTER TABLE bag_tool ADD COLUMN status INT(4) AFTER tip;
UPDATE bag_tool SET status = 1 WHERE id IN (1,2,3,4,7);
UPDATE bag_tool SET status = 0 WHERE id IN (5,6);
UPDATE bag_tool SET descript = '可封印一位同事的宠物',relieve = '答对10题后自动解除或使用回魂丹' WHERE id = 1;
UPDATE bag_tool SET descript = '使一名同事答题后无法获得猫粮',relieve = '答对5题后自动解除或者使用回魂丹' WHERE id = 2;
UPDATE bag_tool SET descript = '狂扁一名同事的宠物',relieve = '使用回魂丹' WHERE id = 3;
UPDATE bag_tool SET descript = '绑定一名同事，一方答题另一方获得相同猫粮',relieve = '与其他同事绑定或使用回魂丹' WHERE id = 4;
UPDATE bag_tool SET descript = '消除自身一个道具效果',relieve = null WHERE id = 6;
INSERT INTO bag_tool(tool_name, descript, effect, relieve, oa_model, explain_show, tip,status) VALUES
('生长液','使宠物体重增长50g','','','%s使用了生长液，你的小猫体重增加了50g','%s的小猫长胖了50g哦！','',1);
update bag_tool_rate set tool_id = 8 where id = 5;

update bag_tool_rate SET rate = 20 where id = 50;
update bag_tool_rate SET rate = 5 where id = 51;
update bag_tool_rate SET rate = 30 where id = 52;
update bag_tool_rate SET rate = 10 where id = 53;
delete from  bag_tool_rate  where id = 54;
update bag_tool_rate SET rate = 5 ,tool_id = 8 where id = 55;
update bag_tool_rate SET rate = 30 where id = 56;

update bag_tool_rate SET rate = 20 where id = 57;
update bag_tool_rate SET rate = 20 where id = 58;
update bag_tool_rate SET rate = 10 where id = 59;
update bag_tool_rate SET rate = 20 where id = 60;
delete from  bag_tool_rate  where id = 61;
update bag_tool_rate SET rate = 10 ,tool_id = 8 where id = 62;
update bag_tool_rate SET rate = 20 where id = 63;

update bag_tool_rate SET rate = 20 where id = 64;
update bag_tool_rate SET rate = 20 where id = 65;
update bag_tool_rate SET rate = 10 where id = 66;
update bag_tool_rate SET rate = 20 where id = 67;
delete from  bag_tool_rate  where id = 68;
update bag_tool_rate SET rate = 20 ,tool_id = 8 where id = 69;
update bag_tool_rate SET rate = 10 where id = 70;

update bag_tool_rate SET rate = 10 where id = 71;
update bag_tool_rate SET rate = 20 where id = 72;
update bag_tool_rate SET rate = 10 where id = 73;
update bag_tool_rate SET rate = 30 where id = 74;
delete from  bag_tool_rate  where id = 75;
update bag_tool_rate SET rate = 20 ,tool_id = 8 where id = 76;
update bag_tool_rate SET rate = 10 where id = 77;

ALTER TABLE week_rank ADD weight INT(8) COMMENT '体重' AFTER rank;



drop table if exists  pet_dynamic;
CREATE TABLE `pet_dynamic` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `company_id` int(8) NOT NULL COMMENT '公司id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `active_id` int(6) COMMENT '事件对应id',
  `dynamic_id` int(8) COMMENT '事件对应表的id',
  `dynamic_content` varchar(64) COMMENT '时间对应id',
  `status` int(4) NOT NULL  COMMENT '当前状态',
  `create_time` timestamp NOT NULL  COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `index_company_user` (`company_id`,`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='动态信息';

ALTER TABLE pet_dynamic ADD is_read INT(4) COMMENT '是否阅读过' DEFAULT 0 AFTER dynamic_content;


ALTER TABLE user_x_intro ADD feed_pet INT(4) DEFAULT 0 COMMENT '宠物喂食' AFTER bag;
ALTER TABLE user_x_intro ADD other_feed_pet INT(4) DEFAULT 0 COMMENT COMMENT '宠物投食' AFTER feed_pet;
ALTER TABLE user_x_intro ADD clean_pet INT(4) DEFAULT 0 COMMENT '宠物清扫' AFTER other_feed_pet;
ALTER TABLE user_x_intro ADD answer INT(4) DEFAULT 0 COMMENT '新版本答题' AFTER clean_pet;


#埋点相关
drop table if exists app_invite_bury;
CREATE TABLE `app_invite_bury` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `company_id` int(8) NOT NULL COMMENT '公司id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `type` int(4) NOT NULL  COMMENT '类型',
  `create_time` timestamp NOT NULL  COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `index_company_user` (`company_id`,`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='app邀请';


#埋点相关
drop table if exists pet_raise_bury;
CREATE TABLE `pet_raise_bury` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `company_id` int(8) NOT NULL COMMENT '公司id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `type` int(4) NOT NULL  COMMENT '类型',
  `create_time` timestamp NOT NULL  COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `index_company_user` (`company_id`,`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='宠物饲养';


#埋点相关
drop table if exists oa_send_bury;
CREATE TABLE `oa_send_bury` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `company_id` int(8) NOT NULL COMMENT '公司id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `type` int(4) NOT NULL  COMMENT '类型',
  `create_time` timestamp NOT NULL  COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `index_company_user` (`company_id`,`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='OA发送';


#埋点相关
drop table if exists oa_open_bury;
CREATE TABLE `oa_open_bury` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `company_id` int(8) NOT NULL COMMENT '公司id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `type` int(4) NOT NULL  COMMENT '类型',
  `create_time` timestamp NOT NULL  COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `index_company_user` (`company_id`,`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='OA打开';

INSERT INTO enterprise.pet_random_word (id, word, status) VALUES (35, '哼，别人家的小猫猫都有很酷炫的道具！', 1);
INSERT INTO enterprise.pet_random_word (id, word, status) VALUES (36, '主人主人，连对越多，获得道具几率越大噢', 1);
INSERT INTO enterprise.pet_random_word (id, word, status) VALUES (37, '钉钉钉钉钉钉，是一个工作方式~是触手可及的新未来', 1);
INSERT INTO enterprise.pet_random_word (id, word, status) VALUES (38, '上下班记得打卡噢！', 1);
INSERT INTO enterprise.pet_random_word (id, word, status) VALUES (39, '酷时代，酷公司，酷员工，酷猫猫！', 1);
INSERT INTO enterprise.pet_random_word (id, word, status) VALUES (40, '职场独门绝技【DING】：消灭99%职场沟通难题', 1);
INSERT INTO enterprise.pet_random_word (id, word, status) VALUES (41, '进步青年焦虑综合征：工作的时候想偷懒，闲下来又隐隐不安', 1);
INSERT INTO enterprise.pet_random_word (id, word, status) VALUES (42, '希望我的存在，让你的工作不再孤单', 1);





