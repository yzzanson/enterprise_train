ALTER TABLE user_x_intro ADD bag INT(4) COMMENT '背包' DEFAULT  0 AFTER challenge ;
ALTER TABLE user_x_intro ADD new_version INT(4) COMMENT '新版本' DEFAULT  0 AFTER bag ;


CREATE TABLE `week_rank` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `company_id` int(8) NOT NULL COMMENT '公司id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `rank` int(4) NOT NULL COMMENT '排行',
  `date_time` VARCHAR(10) NOT NULL  COMMENT '统计时间',
  PRIMARY KEY (`id`),
  KEY `index_company_user` (`company_id`,`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='周排行';

ALTER TABLE week_rank ADD title VARCHAR(20) COMMENT '头衔'  AFTER rank ;
ALTER TABLE week_rank ADD title_type INT(4) COMMENT '头衔类型'  AFTER title ;


CREATE TABLE `bag_tool` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `tool_name` VARCHAR(12) NOT NULL COMMENT '道具名',
  `descript` VARCHAR(128) NOT NULL COMMENT '用法描述',
  `effect` VARCHAR(128) NOT NULL COMMENT '实现效果',
  `relieve` VARCHAR(128) NOT NULL  COMMENT '解除方法',
  `oa_model` VARCHAR(128) NOT NULL  COMMENT 'OA消息模板',
  `explain_show` VARCHAR(128) NOT NULL  COMMENT '提示模板',
  `tip` VARCHAR(64)  COMMENT '提示'
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='背包道具';


CREATE TABLE `bag_tool_rate` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `event_type` int(8) NOT NULL COMMENT '事件',
  `tool_id` int(8) NOT NULL COMMENT '道具id',
  `rate` int(4) NOT NULL COMMENT '概率',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='道具概率';




insert into bag_tool(tool_name, descript, effect, relieve,oa_model,explain_show,tip) VALUES
('封印咒 S','可封印一位同事的宠物','对方的等级和经验值都不改变，只把对方的宠物形象封印成一个蛋，对方需要答对15道题，才能解封，宠物对话内容固定为：我到底怎么回事？？？','解除方法1：答对15题后解开封印 解除方法2：使用回魂丹','主人，我被%s封印了，快帮我解除封印！','%s把%s封印住啦!','解开封印还需答题%s道');
insert into bag_tool(tool_name, descript, effect, relieve,oa_model,explain_show,tip) VALUES
('小衰神 S','使一位同事答题增速减半','答对两题，经验+1，答对数+1，经验条上附上一只小衰神','解除方法：使用回魂丹','主人，我被%s的小衰神附体了，囧…','%s已被%s的小衰神附身,每答对2题仅获得1题增长','使用回魂丹即可解除');
insert into bag_tool(tool_name, descript, effect, relieve,oa_model,explain_show,tip) VALUES
('爆裂拳 S','狂扁一名同事的宠物','对方宠物形象被打得鼻青脸肿，趴在地上无精打采，一直在说：你有还魂丹吗？','解除方法：使用回魂丹','主人，我被%s狂扁了，快帮我报仇！！！','%s被%s狂扁一通，一整天都无精打采_(:з」∠)_，快回击对方替宠物复仇！；使用回魂丹即可解除','使用回魂丹即可解除');
insert into bag_tool(tool_name, descript, effect, relieve,oa_model,explain_show,tip) VALUES
('丘比特 S','绑定一名同事，一方答题另一方获得相同增长','绑定双方，任何一方答题，双方都增加经验和答对数','解除方法1：与其他用户绑定后解除 解除方法2：使用回魂丹','主人，%s给你发射了爱神之箭哟','已与%s喜结连理，一方答题另一方获得相同增长','%s为我增加了%s点经验');
insert into bag_tool(tool_name, descript, effect, relieve,oa_model,explain_show,tip) VALUES
('马赛克 S','将排行榜上一位同事信息打码','将本周排行榜上的某个人的头像和昵称打上马赛克，宠物的样子被涂上马赛克','解除方法1：本周结束自动失效 解除方法2：使用回魂丹','主人，有人在排行榜上将你的名字写写画画','被%s从排行榜上抹去了！','使用回魂丹即可解除');
insert into bag_tool(tool_name, descript, effect, relieve,oa_model,explain_show,tip) VALUES
('窜天炮 SSR','本周答对数+50道','在原来排行榜上答对数的基础上+50题，多个窜天猴可以重复叠加','解除方法1：本周结束自动失效 解除方法2：使用回魂丹','','','');
insert into bag_tool(tool_name, descript, effect, relieve,oa_model,explain_show,tip) VALUES
('回魂丹 S','消除自身一个道具效果','解除自身的一种道具效果','','','','');


#1登陆 2累计答对4题 3累计答对12 4累计答对24 5累计答对36 6累计答对48 7累计答对60 8combo3 9combo5 10combo7 11combo9
#登陆
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (1, 1, 1, 20);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (2, 1, 2, 10);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (3, 1, 3, 20);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (4, 1, 4, 10);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (5, 1, 5, 10);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (6, 1, 6, 10);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (7, 1, 7, 20);

#累计4题
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (8, 2, 1, 20);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (9, 2, 2, 30);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (10, 2, 3, 10);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (11, 2, 4, 0);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (12, 2, 5, 0);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (13, 2, 6, 0);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (14, 2, 7, 40);

#累计12题
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (15, 3, 1, 10);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (16, 3, 2, 20);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (17, 3, 3, 20);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (18, 3, 4, 10);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (19, 3, 5, 20);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (20, 3, 6, 10);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (21, 3, 7, 10);

#累计24题
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (22, 4, 1, 0);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (23, 4, 2, 0);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (24, 4, 3, 10);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (25, 4, 4, 10);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (26, 4, 5, 30);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (27, 4, 6, 20);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (28, 4, 7, 30);

#累计36题
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (29, 5, 1, 0);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (30, 5, 2, 0);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (31, 5, 3, 0);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (32, 5, 4, 20);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (33, 5, 5, 40);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (34, 5, 6, 30);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (35, 5, 7, 10);

#累计48题
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (36, 6, 1, 0);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (37, 6, 2, 0);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (38, 6, 3, 10);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (39, 6, 4, 30);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (40, 6, 5, 0);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (41, 6, 6, 40);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (42, 6, 7, 20);

#累计60题
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (43, 7, 1, 10);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (44, 7, 2, 0);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (45, 7, 3, 0);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (46, 7, 4, 40);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (47, 7, 5, 0);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (48, 7, 6, 50);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (49, 7, 7, 0);

#combo3
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (50, 8, 1, 10);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (51, 8, 2, 10);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (52, 8, 3, 20);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (53, 8, 4, 10);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (54, 8, 5, 10);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (55, 8, 6, 10);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (56, 8, 7, 30);


#combo5
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (57, 9, 1, 20);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (58, 9, 2, 20);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (59, 9, 3, 10);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (60, 9, 4, 10);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (61, 9, 5, 10);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (62, 9, 6, 10);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (63, 9, 7, 20);

#combo7
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (64, 10, 1, 30);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (65, 10, 2, 30);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (66, 10, 3, 0);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (67, 10, 4, 20);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (68, 10, 5, 10);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (69, 10, 6, 10);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (70, 10, 7, 0);

#como9
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (71, 11, 1, 0);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (72, 11, 2, 0);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (73, 11, 3, 10);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (74, 11, 4, 30);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (75, 11, 5, 30);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (76, 11, 6, 30);
INSERT INTO enterprise.bag_tool_rate (id, event_type, tool_id, rate) VALUES (77, 11, 7, 0);

CREATE TABLE `bag_tool_people` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `company_id` int(8) NOT NULL COMMENT '公司id',
  `user_id` int(8) NOT NULL COMMENT '用户id',
  `tool_id` int(8) NOT NULL COMMENT '道具id',
  `event_type` int(4) NOT NULL COMMENT '获取途径',
  `status` int(4) NOT NULL COMMENT '状态',
  `create_time` timestamp NOT NULL  COMMENT '创建时间',
  `update_time` timestamp NOT NULL  COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户道具';


CREATE TABLE `bag_tool_desc` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `play_desc` varchar(128) NOT NULL COMMENT '玩法介绍',
  `gain_tool` varchar(128) NOT NULL COMMENT '道具获取方式',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='玩法介绍';


insert into bag_tool_desc(play_desc,gain_tool) values('游戏中将获得道具，使用道具后可作用在自己或他人的宠物身上，使用后对方将收到OA消息提示；道具可叠加使用。','方法一：每天登陆获得宝箱，打开宝箱随机获得道具 方法二：根据答题情况，随机掉落道具');


CREATE TABLE `bag_tool_effect` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `company_id` int(8) NOT NULL COMMENT '公司id',
  `tool_id` int(8) NOT NULL COMMENT '道具id',
  `tool_people_id` int(8) NOT NULL COMMENT '用户拥有道具id',
  `user_id` int(8) NOT NULL COMMENT '用户id',
  `effect_user_id` int(8) NOT NULL COMMENT '被作用用户id',
  `status` int(4) NOT NULL COMMENT '状态',
  `create_time` timestamp NOT NULL  COMMENT '创建时间',
  `update_time` timestamp NOT NULL  COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `index_company_user_tool` (`company_id`,`user_id`,`tool_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='道具作用';

ALTER TABLE bag_tool_effect ADD elimate_id INT(8) COMMENT '消除的作用id' AFTER effect_user_id;
ALTER TABLE user_x_questions ADD tool_type INT(4) COMMENT '道具类型' AFTER answer_time;

CREATE TABLE `bag_tool_effect_question` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `company_id` int(8) NOT NULL COMMENT '公司id',
  `tool_id` int(8) NOT NULL COMMENT '道具id',
  `user_id` int(8) NOT NULL COMMENT '用户id',
  `user_x_question_id` int(8) NOT NULL COMMENT '用户答题id',
  `bag_tool_effect_id` int(8) NOT NULL COMMENT '道具影响id',
  `create_time` timestamp NOT NULL  COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `index_company_user_tool` (`company_id`,`user_id`,`tool_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='答题道具统计';

ALTER TABLE bag_tool_effect_question ADD add_exp INT(4) DEFAULT 0 COMMENT '是否增加经验' AFTER bag_tool_effect_id;


#20180914
CREATE TABLE `week_rank_open` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `company_id` int(8) NOT NULL COMMENT '公司id',
  `user_id` int(8) NOT NULL COMMENT '用户id',
  `week_time` timestamp NOT NULL  COMMENT '周时间',
  `create_time` timestamp NOT NULL  COMMENT '创建时间',
  `update_time` timestamp NOT NULL  COMMENT '创建时间'
  PRIMARY KEY (`id`),
  KEY `index_company_user` (`company_id`,`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='周排行访问统计';



CREATE TABLE `pet_random_word` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `word` VARCHAR(40) NOT NULL COMMENT '宠物的话',
  `status` int(4) NOT NULL  COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='宠物的话';


INSERT INTO enterprise.pet_random_word (id, word, status) VALUES (1, '无知的人不配跟我说话', 1);
INSERT INTO enterprise.pet_random_word (id, word, status) VALUES (2, '窥探我的过去是很危险的', 1);
INSERT INTO enterprise.pet_random_word (id, word, status) VALUES (3, '我讨厌所有打扰我思考的人', 1);
INSERT INTO enterprise.pet_random_word (id, word, status) VALUES (4, '……', 1);
INSERT INTO enterprise.pet_random_word (id, word, status) VALUES (5, '喵', 1);
INSERT INTO enterprise.pet_random_word (id, word, status) VALUES (6, '无知的人才会盲目自信', 1);
INSERT INTO enterprise.pet_random_word (id, word, status) VALUES (7, '芝士就是力量，哎饿了', 1);
INSERT INTO enterprise.pet_random_word (id, word, status) VALUES (8, '在你面前的，是世界上最聪明的猫', 1);
INSERT INTO enterprise.pet_random_word (id, word, status) VALUES (9, '面无表情', 1);
INSERT INTO enterprise.pet_random_word (id, word, status) VALUES (10, '时间更应该被用在有意义的事情上，比如答题', 1);
INSERT INTO enterprise.pet_random_word (id, word, status) VALUES (11, '抱怨是没有用的', 1);
INSERT INTO enterprise.pet_random_word (id, word, status) VALUES (12, '窥探我的过去是很危险的', 1);
INSERT INTO enterprise.pet_random_word (id, word, status) VALUES (13, '这世上从不缺有雄心的失败者，坚持下去的才是王者', 1);
INSERT INTO enterprise.pet_random_word (id, word, status) VALUES (14, '脾气这玩意我控制不好', 1);
INSERT INTO enterprise.pet_random_word (id, word, status) VALUES (15, '哼 愚蠢的人类', 1);
INSERT INTO enterprise.pet_random_word (id, word, status) VALUES (16, '你可以保持安静吗', 1);
INSERT INTO enterprise.pet_random_word (id, word, status) VALUES (17, '其实我是一个诗人', 1);
INSERT INTO enterprise.pet_random_word (id, word, status) VALUES (18, '城市不就是贩卖梦想的地方吗', 1);
INSERT INTO enterprise.pet_random_word (id, word, status) VALUES (19, '性别少女，智商高，爱好一本正经的卖萌', 1);
INSERT INTO enterprise.pet_random_word (id, word, status) VALUES (20, '哎呀 笨skr人了', 1);
INSERT INTO enterprise.pet_random_word (id, word, status) VALUES (21, '喂 你好像很喜欢戳我。。', 1);
INSERT INTO enterprise.pet_random_word (id, word, status) VALUES (22, '傲娇脸(`へ´*)ノ', 1);
INSERT INTO enterprise.pet_random_word (id, word, status) VALUES (23, '我也是有尊严的猫！', 1);
INSERT INTO enterprise.pet_random_word (id, word, status) VALUES (24, '不许调戏我！', 1);
INSERT INTO enterprise.pet_random_word (id, word, status) VALUES (25, '哼 随便你了', 1);
INSERT INTO enterprise.pet_random_word (id, word, status) VALUES (26, '不管黑猫白猫，会答题的就是好猫', 1);
INSERT INTO enterprise.pet_random_word (id, word, status) VALUES (27, '再说一次这是富态，不是胖！', 1);
INSERT INTO enterprise.pet_random_word (id, word, status) VALUES (28, '把葡萄干和大便混在一起，得到的还是大便', 1);
INSERT INTO enterprise.pet_random_word (id, word, status) VALUES (29, '猫每天要睡14~18个小时才能保持身体健康', 1);
INSERT INTO enterprise.pet_random_word (id, word, status) VALUES (30, '哼 我才不会告诉你我很想你呢(｡・`ω´･)', 1);
INSERT INTO enterprise.pet_random_word (id, word, status) VALUES (31, '听说天天来看我的人，会越来越好看', 1);
INSERT INTO enterprise.pet_random_word (id, word, status) VALUES (32, '近朱者赤，近我者甜⁄(⁄ ⁄•⁄ω⁄•⁄ ⁄)⁄', 1);
INSERT INTO enterprise.pet_random_word (id, word, status) VALUES (33, '哪儿题多，上哪儿待着去', 1);
INSERT INTO enterprise.pet_random_word (id, word, status) VALUES (34, '今天刷题了吗！', 1);


alter table company_info add stop_time TIMESTAMP COMMENT '停用时间';
alter table company_info add delete_time TIMESTAMP COMMENT '删除时间';