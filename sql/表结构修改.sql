alter table user drop column password;
ALTER TABLE department ADD parent_id INT(6)  COMMENT '父部门id';
alter table user_x_dept MODIFY corp_id VARCHAR(64);
alter table questions_library alter column operator int(8)  NOT NULL;
ALTER TABLE user_x_questions ADD INDEX userId_questionId(`user_id`,`question_id`);
ALTER TABLE user_x_questions ADD type INT(4) NOT NULL COMMENT '答题类型0/1 普通学习/打擂';
ALTER TABLE pet_experience ADD physical_value INT(4) NOT NULL COMMENT '获得体力值';
ALTER TABLE pet_experience ADD max_physical_value INT(4) NOT NULL COMMENT '体力值上限';

ALTER TABLE user_x_company ADD is_admin INT(2) COMMENT '是否管理员' DEFAULT 0;
ALTER TABLE user_x_company ADD is_leader INT(2) COMMENT '是否部门主管' DEFAULT 0;
ALTER TABLE user_x_company ADD is_boss INT(2) COMMENT '是否老板' DEFAULT 0;

ALTER TABLE user_x_company DROP COLUMN ding_id;
ALTER TABLE user_x_dept ADD ding_user_id VARCHAR(32) COMMENT '用户在企业内的id';

ALTER TABLE questions MODIFY description VARCHAR(200) NOT NULL COMMENT '题目标题';
ALTER TABLE questions MODIFY answer_desc VARCHAR(250) COMMENT '答案解析';
ALTER TABLE user_x_library MODIFY last_answer_time timestamp COMMENT '最近一次答题时间';

ALTER TABLE arena_result ADD expire_time timestamp COMMENT '擂台过期时间';


alter table arena_answer ADD COLUMN  arena_result_id int(6) comment '擂台结果id' AFTER arena_id;
alter table arena_answer ADD COLUMN  answer varchar(10) comment '回答选项' AFTER question_id;



CREATE TABLE `user_x_behavior` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `company_id` int(8)  COMMENT '公司id',
  `user_id` int(8) NOT NULL COMMENT '用户id',
  `type` int(11) NOT NULL COMMENT '行为类型',
  `arena_result_id` int(11) NOT NULL COMMENT '擂台pk结果id',
  `status` int(4) NOT NULL DEFAULT '1' COMMENT '状态(0:删除，1:正常)',
  `happen_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发生时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户行为表';

ALTER TABLE arena_result ADD COLUMN  pet_status int(4) DEFAULT 0 comment '宠物提醒状态' AFTER status;

ALTER TABLE questions_library ADD COLUMN  default_flag int(4) DEFAULT 0 comment '默认题库标志' AFTER parent_id;
ALTER TABLE question ADD COLUMN  restudy int(2) DEFAULT 0 comment '是否重新学习' AFTER answer_desc;


ALTER TABLE arena_result modify ADD COLUMN user_score INT(11)  COMMENT '摆擂台者总得分';

ALTER TABLE oa_msg modify content  VARCHAR(512) COMMENT '内容';

ALTER TABLE questions_library  DROP COLUMN sort_type;
ALTER TABLE questions_library  ADD COLUMN sort_type INT(4) COMMENT '摆擂台者总得分' DEFAULT 1 AFTER default_flag;

ALTER TABLE questions  ADD COLUMN parent_id INT(8) COMMENT '对应原始题库id' DEFAULT null AFTER restudy;


#test