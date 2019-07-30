#内训系统表结构：
#宠物表
CREATE TABLE `pet` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `type` int(11) NOT NULL COMMENT '宠物类型',
  `init_pic` varchar(128) COLLATE utf8mb4_bin NOT NULL COMMENT '宠物初始图片',
  `final_pic` varchar(128) COLLATE utf8mb4_bin NOT NULL COMMENT '宠物孵化出来后的图片',
  `status` int(4) NOT NULL DEFAULT '1' COMMENT '状态(0:删除，1:正常)',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='宠物表';

#我的宠物表
CREATE TABLE `my_pet` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_id` int(11) NOT NULL COMMENT '用户Id',
  `pet_id` int(11) NOT NULL COMMENT '宠物Id',
  `pet_name` varchar(128) COLLATE utf8mb4_bin NOT NULL COMMENT '宠物名称',
  `level` int(11) NOT NULL DEFAULT 0 COMMENT '宠物等级',
  `experience_value` int(11) NOT NULL DEFAULT 0 COMMENT '宠物经验值',
  `physical_value` int(11) NOT NULL DEFAULT 0 COMMENT '宠物体力值',
  `status` int(4) NOT NULL DEFAULT '1' COMMENT '状态(0:删除，1:正常)',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_userId_petId` (`user_id`,`pet_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='我的宠物表';

#擂台表
CREATE TABLE `arena` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_id` int(11) NOT NULL COMMENT '摆擂台者Id',
  `library_id` int(11) NOT NULL COMMENT '题库Id',
  'dept_id' int(8) COMMENT '部门id',
  'question_ids' VARCHAR(64) NOT NULL COMMENT '题目id',
  `expiry_time` timestamp NOT NULL COMMENT '过期时间',
  `status` int(4) NOT NULL DEFAULT '1' COMMENT '状态(0:删除，1:正常)',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_userId_libraryId` (`user_id`,`library_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='擂台表';
#alter table arena add column dept_id int(8) after library_id;

#擂台挑战表
CREATE TABLE `arena_answer` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `arena_id` int(11) NOT NULL COMMENT '擂台Id',
  `user_id` int(11) NOT NULL COMMENT '答题者Id',
  `question_id` int(11) NOT NULL COMMENT '题目Id',
  `answer_status` int(4) NOT NULL COMMENT '答题的状态(1:答题正确 2:答题错误)',
  `answer_time` int(4) NOT NULL COMMENT '答题用时(单位：秒)',
  `score` int(4) NOT NULL DEFAULT '0' COMMENT '得分',
  `time_limit` int(4) NOT NULL DEFAULT '10' COMMENT '限时(单位：秒，默认为10秒)',
  `status` int(4) NOT NULL DEFAULT '1' COMMENT '状态(0:删除，1:正常)',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `index_arena_id` (`arena_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='擂台挑战表';

#擂台挑战结果表
CREATE TABLE `arena_result` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `arena_id` int(11) NOT NULL COMMENT '擂台Id',
  `user_id` int(11) NOT NULL COMMENT '摆擂台者Id',
  `user_dept_id` int(6) COMMENT '用户部门id',
  `user_score` int(11) NOT NULL COMMENT '摆擂台者总得分',
  `pk_user_id` int(11) NOT NULL COMMENT '挑战者Id',
  `pk_user_dept_id` int(6) COMMENT '挑战者部门id',
  `pk_user_score` int(11) COMMENT '挑战者总得分',
  `is_same_dept` int(11) COMMENT '是否同部门',
  `winner_id` int(11) COMMENT '获胜者Id',
  `status` int(4) NOT NULL DEFAULT '1' COMMENT '状态(0:删除，1:正常 ,2:结束)',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `index_arena_id` (`arena_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='擂台挑战结果表';

#######################################  pc端  #####################################################################
############################################################################################################
#员工表
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ding_id` varchar(4) NOT NULL COMMENT '钉钉中(userid)员工唯一标识ID',
  `name` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '名称',
  `avatar` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '头像',
  `source` int(4) NOT NULL DEFAULT '1' COMMENT '来源(0:内训，1:后台)',
  `status` int(4) NOT NULL DEFAULT '1' COMMENT '状态(0:删除，1:正常)',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_dingId` (`ding_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='员工表';

alter table user drop column password;
#alter table user add column source int(4)  NOT NULL COMMENT '源(0:内训，1:后台)';

#员工和企业关联表表
CREATE TABLE `user_x_company` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `corp_id` varchar(121) COLLATE utf8mb4_bin NOT NULL COMMENT '企业corpId',
  `ding_id` varchar(4) NOT NULL COMMENT '钉钉中(ding_id)员工唯一标识ID',
  `ding_user_id` varchar(4) NOT NULL COMMENT '员工在企业内的UserID',
  `user_id` int(4) NOT NULL COMMENT 'user表中的ID',
  `status` int(4) NOT NULL DEFAULT '1' COMMENT '状态(0:删除，1:正常)',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_corpId_dingId` (`corp_id`,`ding_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='员工和企业关联表表';

#套件表
CREATE TABLE `suites` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `suite_key` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT 'suite4xxxxxxxxxxxxxxx',
  `suite_secret` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  `token` varchar(100) COLLATE utf8mb4_bin NOT NULL COMMENT '用于生成签名,校验回调请求的合法性。本套件下相关应用产生的回调消息都使用该值来解密',
  `encoding_aes_key` varchar(100) COLLATE utf8mb4_bin NOT NULL COMMENT '回调消息加解密参数，是AES密钥的Base64编码，用于解密回调消息内容对应的密文。本套件下相关应用产生的回调消息都使用该值来解密。',
  `suite_access_token` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '会失效,定时刷新',
  `suite_access_token_expire_time` timestamp NOT NULL DEFAULT '1970-01-01 08:00:01' COMMENT 'suite_access_token 过期时间',
  `description` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '套件描述',
  `suite_ticket` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '官方定时推送刷新',
  `corp_appid` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '微应用ID,企业授权成功通过此id找到isv应用在企业中对应的agentid',
  `status` int(4) NOT NULL DEFAULT '1' COMMENT '状态(0:删除，1:正常)',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='套件表';

#isv_tickets表
CREATE TABLE `isv_tickets` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `corp_id` varchar(121) COLLATE utf8mb4_bin DEFAULT NULL,
  `company_id` int(4) DEFAULT NULL COMMENT '公司Id',
  `suite_id` int(11) DEFAULT NULL COMMENT '套件Id',
  `corp_agent_id` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '微应用实例化id,由钉钉分配',
  `corp_access_token` varchar(121) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '企业access_token',
  `corp_ticket` varchar(121) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '企业ticket',
  `corp_permanent_code` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '企业永久授权码',
  `status` int(4) NOT NULL DEFAULT '1' COMMENT '状态(0:删除，1:正常)',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_corpId` (`corp_id`	) USING BTREE
  UNIQUE KEY `unique_corpId_companyId_suiteId` (`corp_id`,`company_id`,`suite_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=121 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin

#企业信息表
CREATE TABLE `company_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `name` varchar(128) COLLATE utf8mb4_bin NOT NULL COMMENT '企业名称(即在钉钉创建的组织架构的名称)',
  `send_oa` int(4) NOT NULL COMMENT '是否给学习题库的员工发送消息通知(1:发送 0:不发送)',
  `agent_status` int(4) NOT NULL DEFAULT '1' COMMENT '微应用的状态(0:卸载，1:正常使用 2:停用)',
  `status` int(4) NOT NULL DEFAULT '1' COMMENT '状态(0:删除，1:正常)',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='企业信息表';

#部门表
CREATE TABLE `department` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `company_id` int(4) NOT NULL COMMENT '部门所在公司的Id',
  `ding_dept_id` int(4) NOT NULL COMMENT '部门在钉钉里的Id',
  `name` varchar(128) COLLATE utf8mb4_bin NOT NULL COMMENT '部门名称',
  `status` int(4) NOT NULL DEFAULT '1' COMMENT '状态(0:删除，1:正常)',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `index_companyId` (`company_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='部门表';

ALTER TABLE department ADD parent_id INT(6)  COMMENT '父部门id';

#员工和部门关联表
CREATE TABLE `user_x_dept` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `corp_id` VARCHAR(20) COMMENT 'corpid',
  `dept_id` int(4) NOT NULL COMMENT '部门Id(department表中的Id)',
  `dept_name` VARCHAR (32) COMMENT '部门名',
  `user_id` int(4) NOT NULL COMMENT '员工Id',
  `status` int(4) NOT NULL DEFAULT '1' COMMENT '状态(0:删除，1:正常)',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_corpId_agentId` (`dept_id`,`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='员工和部门关联表';


alter table user_x_dept MODIFY corp_id VARCHAR(64);

#题库表
CREATE TABLE `questions_library` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `company_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '公司id',
  `name` varchar(128) COLLATE utf8mb4_bin NOT NULL COMMENT '题库名称',
  `use_count` int(4) NOT NULL DEFAULT '0' COMMENT '使用次数',
  `subject` int(4) NOT NULL DEFAULT '0' COMMENT '分类(0:公共题库(对应官方题库), 1:私有题库(对应自定义题库)',
  `operator` INT(8) NOT NULL DEFAULT '0' COMMENT '题库创建人',
  `label` varchar(64) NOT NULL COMMENT '标签',
  `parent_id` int(6) NOT NULL COMMENT '父id',
  `status` int(4) NOT NULL DEFAULT '1' COMMENT '状态(0:删除，1:正常)',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_corpId_agentId` (`name`,`subject`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='题库表';
CREATE INDEX name_subject ON questions_library (name,subject);


#题目表
CREATE TABLE `questions` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `library_id` int(4) NOT NULL COMMENT '所属题库Id',
  `operator` int(6) NOT NULL COMMENT '创建人Id',
  `type` int(4) NOT NULL COMMENT '题目类型',
  `description` varchar(500) NOT NULL COMMENT '题目描述',
  `options` varchar(500) NOT NULL COMMENT '题目选项({"A":"","B":"",...})',
  `answer` varchar(20) NOT NULL COMMENT '题目正确选项(多个用,隔开)',
  `answer_desc` varchar(20) NOT NULL COMMENT '题目正确选项(多个用,隔开)',
  `status` int(4) NOT NULL DEFAULT '1' COMMENT '状态(0:删除，1:正常)',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='题目表';
alter table questions add index library_type (library_id,type) ;

#题目反馈记录表
DROP TABLE IF EXISTS questions_feedback ;
CREATE TABLE `questions_feedback` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `question_id` int(6) NOT NULL COMMENT '题目Id',
  `user_id` int(6) COMMENT '反馈人Id',
  `handle_user_id` int(4)  COMMENT '反馈处理人Id',
  `content` varchar(500) NOT NULL COMMENT '反馈的内容',
  `status` int(4) NOT NULL DEFAULT '1' COMMENT '状态(0:删除，1:正常)',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='题目反馈记录表';

#题库和公司关联表
CREATE TABLE `company_x_library` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `company_id` int(4) NOT NULL COMMENT '公司Id',
  `library_id` int(4) NOT NULL COMMENT '题库Id',
  `user_number` int(4) NOT NULL COMMENT '学习人数',
  `status` int(4) NOT NULL DEFAULT '1' COMMENT '状态(0:删除，1:正常)',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_companyId_libraryId` (`company_id`,`library_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='题库和公司关联表';

#题库和员工关联表
CREATE TABLE `user_x_library` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_id` int(4) NOT NULL COMMENT '员工Id',
  `library_id` int(4) COMMENT '题库Id',
  `schedule` int(11) NOT NULL COMMENT '学习进度(乘以了100)',
  `last_answer_time` timestamp COMMENT '最近一次答题时间',
  `status` int(4) NOT NULL DEFAULT '1' COMMENT '状态(0:删除，1:正常，2:已完成(完成题库全部题目)',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_userId_libraryId` (`user_id`,`library_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='题库和员工关联表';



#题目和员工关联表
CREATE TABLE `user_x_questions` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_id` int(4) NOT NULL COMMENT '员工Id',
  `question_id` int(4) NOT NULL COMMENT '题目Id',
  `answer_status` int(4) NOT NULL COMMENT '答题的状态(0:未答题 1:答题正确 2:答题错误)',
  `answer_time` int(4) COMMENT '答题使用的时间(单位：秒)',
  `status` int(4) NOT NULL DEFAULT '1' COMMENT '状态(0:删除，1:正常)',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  INDEX `userId_questionId` (`user_id`,`question_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='题目和员工关联表';

ALTER TABLE user_x_questions ADD INDEX userId_questionId(`user_id`,`question_id`);
ALTER TABLE user_x_questions ADD type INT(4) NOT NULL COMMENT '答题类型0/1 普通学习/打擂';

#应用打开记录表
CREATE TABLE `user_x_open` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_id` int(4) NOT NULL COMMENT '员工Id',
  `company_id` int(4) NOT NULL COMMENT '公司Id',
  `status` int(4) NOT NULL DEFAULT '1' COMMENT '状态(0:删除，1:正常)',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='员工打开应用记录表';



#权限组表
CREATE TABLE `right_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `name` varchar(10) NOT NULL COMMENT '权限组名',
  `value` int(4) NOT NULL COMMENT '权限组值',
  `status` int(4) NOT NULL DEFAULT '1' COMMENT '状态(0:删除，1:正常)',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='权限组表';



#权限资源表
CREATE TABLE `right_resource_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `right_group_id` int(8) NOT NULL COMMENT '权限组id',
  `right_resource_id` int(8) NOT NULL COMMENT '权限资源id',
  `status` int(4) NOT NULL DEFAULT '1' COMMENT '状态(0:删除，1:正常)',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='权限资源表';



#权限资源表
CREATE TABLE `right_resource_url` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `url` varchar(1024) NOT NULL COMMENT '权限url',
  `parent_id` int(8) COMMENT '父节点id',
  `name` varchar(64) NOT NULL COMMENT '资源名',
  `orders` int(3) NOT NULL COMMENT '资源顺序',
  `project` varchar(20) NOT NULL COMMENT '所属项目',
  `status` int(4) NOT NULL DEFAULT '1' COMMENT '状态(0:删除，1:正常)',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='资源表';
ALTER TABLE `right_resource_url` ADD UNIQUE ( 'project','url' );


#权限资源表
CREATE TABLE `user_right_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_id` int(8) NOT NULL COMMENT '用户id',
  `right_group_id` int(8) NOT NULL COMMENT '权限组id',
  `status` int(4) NOT NULL DEFAULT '1' COMMENT '状态(0:删除，1:正常)',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='权限资源表';



#宠物经验值表
CREATE TABLE `pet_experience` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `level` int(4) NOT NULL COMMENT '等级',
  `experience_low` int(4) NOT NULL DEFAULT '1' COMMENT '等级最低要求经验',
  `experience_high` int(4) NOT NULL DEFAULT '1' COMMENT '等级最高要求经验',
  `status` int(4) NOT NULL DEFAULT '1' COMMENT '状态(0:删除，1:正常)',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='宠物经验值';

ALTER TABLE pet_experience ADD physical_value INT(4) NOT NULL COMMENT '获得体力值';
ALTER TABLE pet_experience ADD max_physical_value INT(4) NOT NULL COMMENT '体力值上限';



#企业每天数据汇总
CREATE TABLE `company_day_summary` (
  `id` int(8) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `company_id` int(6) NOT NULL COMMENT '公司id',
  `enter_count` int(6) NOT NULL  COMMENT '每日进入应用人数',
  `study_count` int(6) NOT NULL  COMMENT '每日答题数',
  `new_user_count` int(6) NOT NULL  COMMENT '每日新增用户数',
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '日期',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `index_date` (`date`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='企业每日数据汇总';


CREATE TABLE `study_remind_conf` (
 `id` int(8) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `company_id` int(6) NOT NULL COMMENT '公司id',
  `content` varchar(60) NOT NULL COMMENT '提醒内容',
  `is_open` int(2) COMMENT '是否开启' DEFAULT 0,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='企业学习提醒';



CREATE TABLE `study_remind` (
 `id` int(8) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `company_id` int(6) NOT NULL COMMENT '公司id',
  `library_id` int(6) NOT NULL COMMENT '题库id',
  `department_id` int(6)  COMMENT '部门id' DEFAULT 0,
  `user_id` int(6) COMMENT '员工id' DEFAULT 0,
  `status` int(2) COMMENT '状态' DEFAULT 0,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `index_companyid_libraryid_department` (`company_id`,`library_id`,`department_id`) USING BTREE,
  KEY `index_companyid_libraryid_user` (`company_id`,`library_id`,`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='企业学习提醒';






CREATE TABLE `oa_msg` (
 `id` int(8) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `err_code` int(6) NOT NULL COMMENT '错误码',
  `err_msg` VARCHAR(64) NOT NULL COMMENT '错误信息',
  `invalid_user` VARCHAR(12)  COMMENT '错误的用户',
  `invalid_party` VARCHAR(12) COMMENT '错误的部门',
  `message_id` VARCHAR(32) COMMENT '信息id',
  `company_id` int(6) COMMENT '公司id',
  `arena_id` int(6) COMMENT '竞技场id',
  `library_id` int(6) COMMENT '题库id',
  `user_id` int(6) COMMENT '用户id',
  `code_flag` VARCHAR(12) COMMENT '结果',
  `content`  VARCHAR(256) COMMENT '内容',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='OA消息提醒';









