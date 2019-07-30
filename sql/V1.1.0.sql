CREATE TABLE `arena_flaunt` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `arena_result_id` int(8) NOT NULL COMMENT 'pk结果id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `status` int(4) NOT NULL DEFAULT '1' COMMENT '状态(0:删除，1:正常)',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `index_user_arena_result_id` (`user_id`,`arena_result_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='炫耀表';


CREATE TABLE `question_label` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `company_id` int(6) NOT NULL COMMENT '公司id',
  `library_id` int(8) NOT NULL COMMENT '题库id',
  `user_id` int(6) NOT NULL COMMENT '用户id',
  `label_name` varchar(40) NOT NULL COMMENT '用户id',
  `status` int(4) NOT NULL DEFAULT '1' COMMENT '状态(0:删除，1:正常)',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `index_company_library_id` (`company_id`,`library_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='题目标签表';


alter table questions add column label_id int(8) after library_id;


insert into right_resource_url(url, parent_id, name, orders, type, status, create_time, update_time) VALUES
('/question_label',18,'公共题目标签',0,1,1,now(),now());
insert into right_resource_url(url, parent_id, name, orders, type, status, create_time, update_time) VALUES
('/question_label',28,'企业题目标签',0,1,1,now(),now());

insert into right_resource_url(url, parent_id, name, orders, type, status, create_time, update_time) VALUES
('/question_label/createOrUpdate.json',74,'新增修改公共标签',0,2,1,now(),now());
insert into right_resource_url(url, parent_id, name, orders, type, status, create_time, update_time) VALUES
('/question_label/createOrUpdate.json',75,'新增修改企业标签',0,2,1,now(),now());
insert into right_resource_url(url, parent_id, name, orders, type, status, create_time, update_time) VALUES
('/question_label/getQuestionLabels.json',74,'获取公共题库标签列表',0,2,1,now(),now());
insert into right_resource_url(url, parent_id, name, orders, type, status, create_time, update_time) VALUES
('/question_label/getQuestionLabels.json',75,'获取企业题库标签列表',0,2,1,now(),now());
insert into right_resource_url(url, parent_id, name, orders, type, status, create_time, update_time) VALUES
('/question_label/getQuestionLabelById.json',74,'获取公共题库题目标签',0,2,1,now(),now());
insert into right_resource_url(url, parent_id, name, orders, type, status, create_time, update_time) VALUES
('/question_label/getQuestionLabelById.json',75,'获取企业题库题目标签',0,2,1,now(),now());


insert into right_resource_group(right_group_id, right_resource_id, status, create_time, update_time) VALUES
(1,74,1,now(),now());
insert into right_resource_group(right_group_id, right_resource_id, status, create_time, update_time) VALUES
(1,76,1,now(),now());
insert into right_resource_group(right_group_id, right_resource_id, status, create_time, update_time) VALUES
(1,78,1,now(),now());
insert into right_resource_group(right_group_id, right_resource_id, status, create_time, update_time) VALUES
(1,80,1,now(),now());
insert into right_resource_group(right_group_id, right_resource_id, status, create_time, update_time) VALUES
(2,75,1,now(),now());
insert into right_resource_group(right_group_id, right_resource_id, status, create_time, update_time) VALUES
(2,77,1,now(),now());
insert into right_resource_group(right_group_id, right_resource_id, status, create_time, update_time) VALUES
(2,79,1,now(),now());
insert into right_resource_group(right_group_id, right_resource_id, status, create_time, update_time) VALUES
(2,81,1,now(),now());




insert into right_resource_url(url, parent_id, name, orders, type, status, create_time, update_time)
 values('/official_library',8,'官方题库',3,1,1,now(),now());

INSERT INTO enterprise.right_resource_url (url, parent_id, name, orders, type, status, create_time, update_time)
VALUES ('/questions_library/createOrUpdate.json', 84, '新增/修改题库', 1, 2, 1, '2018-03-29 14:28:40', '2018-03-29 14:28:40');
INSERT INTO enterprise.right_resource_url (url, parent_id, name, orders, type, status, create_time, update_time)
VALUES ('/questions_library/getLibraryList.json', 84, '搜索题库列表', 2, 2, 1, '2018-03-29 14:28:40', '2018-03-29 14:28:40');
INSERT INTO enterprise.right_resource_url (url, parent_id, name, orders, type, status, create_time, update_time)
VALUES ('/questions_library/batchDelete.json', 84, '批量删除题库', 3, 2, 1, '2018-03-29 14:28:40', '2018-03-29 14:28:40');
INSERT INTO enterprise.right_resource_url (url, parent_id, name, orders, type, status, create_time, update_time)
VALUES ('/questions_library/detail.json', 84, '查看题库', 4, 2, 1, '2018-03-29 14:28:40', '2018-03-29 14:28:40');
INSERT INTO enterprise.right_resource_url (url, parent_id, name, orders, type, status, create_time, update_time)
VALUES ('/questions_library/edit', 84, '编辑', 5, 1, 1, '2018-03-29 14:28:40', '2018-03-29 14:28:40');
INSERT INTO enterprise.right_resource_url (url, parent_id, name, orders, type, status, create_time, update_time)
VALUES ('/study_remind/getArrangerdCompany.json', 84, '获取安排企业情况', 0, 2, 1, '2018-04-24 10:09:58', '2018-04-24 10:10:02');
INSERT INTO enterprise.right_resource_url (url, parent_id, name, orders, type, status, create_time, update_time)
VALUES ('/questions_library/arrangeOfficialLibrary.json', 84, '企业分配', 0, 2, 1, '2018-05-18 11:14:30', '2018-05-18 11:14:30');
INSERT INTO enterprise.right_resource_url (url, parent_id, name, orders, type, status, create_time, update_time)
VALUES ('/questions_library/importQuestions.json', 84, '导入公司题库', 1, 2, 1, '2018-05-25 11:57:42', '2018-05-25 11:57:42');
INSERT INTO enterprise.right_resource_url (url, parent_id, name, orders, type, status, create_time, update_time)
VALUES ('/questions_library/exportQuestionLibrary.json', 84, '导出公司题目', 1, 2, 1, '2018-05-25 16:44:01', '2018-05-25 16:44:01');


insert into right_resource_group(right_group_id, right_resource_id, status, create_time, update_time) VALUES
(1,84,1,now(),now());
insert into right_resource_group(right_group_id, right_resource_id, status, create_time, update_time) VALUES
(1,85,1,now(),now());
insert into right_resource_group(right_group_id, right_resource_id, status, create_time, update_time) VALUES
(1,86,1,now(),now());
insert into right_resource_group(right_group_id, right_resource_id, status, create_time, update_time) VALUES
(1,87,1,now(),now());
insert into right_resource_group(right_group_id, right_resource_id, status, create_time, update_time) VALUES
(1,88,1,now(),now());
insert into right_resource_group(right_group_id, right_resource_id, status, create_time, update_time) VALUES
(1,89,1,now(),now());
insert into right_resource_group(right_group_id, right_resource_id, status, create_time, update_time) VALUES
(1,90,1,now(),now());
insert into right_resource_group(right_group_id, right_resource_id, status, create_time, update_time) VALUES
(1,91,1,now(),now());
insert into right_resource_group(right_group_id, right_resource_id, status, create_time, update_time) VALUES
(1,92,1,now(),now());
insert into right_resource_group(right_group_id, right_resource_id, status, create_time, update_time) VALUES
(1,93,1,now(),now());


insert into right_resource_group(right_group_id, right_resource_id, status, create_time, update_time) VALUES
(3,84,1,now(),now());
insert into right_resource_group(right_group_id, right_resource_id, status, create_time, update_time) VALUES
(3,85,1,now(),now());
insert into right_resource_group(right_group_id, right_resource_id, status, create_time, update_time) VALUES
(3,86,1,now(),now());
insert into right_resource_group(right_group_id, right_resource_id, status, create_time, update_time) VALUES
(3,87,1,now(),now());
insert into right_resource_group(right_group_id, right_resource_id, status, create_time, update_time) VALUES
(3,88,1,now(),now());
insert into right_resource_group(right_group_id, right_resource_id, status, create_time, update_time) VALUES
(3,89,1,now(),now());
insert into right_resource_group(right_group_id, right_resource_id, status, create_time, update_time) VALUES
(3,90,1,now(),now());
insert into right_resource_group(right_group_id, right_resource_id, status, create_time, update_time) VALUES
(3,91,1,now(),now());
insert into right_resource_group(right_group_id, right_resource_id, status, create_time, update_time) VALUES
(3,92,1,now(),now());
insert into right_resource_group(right_group_id, right_resource_id, status, create_time, update_time) VALUES
(3,93,1,now(),now());


ALTER TABLE user_x_library ADD company_id INT(8) COMMENT '公司id' AFTER id;
ALTER TABLE user_x_questions ADD company_id INT(8) COMMENT '公司id' AFTER id;
ALTER TABLE arena_answer ADD company_id INT(8) COMMENT '公司id' AFTER id;
ALTER TABLE arena ADD company_id INT(8) COMMENT '公司id' AFTER id;