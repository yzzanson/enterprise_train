CREATE TABLE `question_library_title` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `company_id` int(8) NOT NULL COMMENT '公司id',
  `library_id` int(8) NOT NULL COMMENT '题库id',
  `title` varchar(20) NOT NULL COMMENT '头衔',
  `status` int(4) NOT NULL DEFAULT '1' COMMENT '状态(0:删除，1:正常)',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  INDEX index_company_id(company_id),
  INDEX index_title(title),
  INDEX index_library_id(library_id),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='题库头衔';


CREATE TABLE `user_x_title` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `company_id` int(8) NOT NULL COMMENT '公司id',
  `library_id` int(8) NOT NULL COMMENT '题库id',
  `user_id` int(8) NOT NULL COMMENT '题库id',
  `title_id` int(8) NOT NULL COMMENT '头衔id',
  `choose_flag` int(2)  COMMENT '是否选中',
  `status` int(4) NOT NULL DEFAULT '1' COMMENT '状态(0:删除，1:正常)',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  INDEX index_company_id(company_id),
  INDEX index_userid(user_id),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户头衔';

ALTER TABLE user_x_library ADD is_update INT(4) DEFAULT 0 AFTER last_answer_time;



DROP TABLE IF EXISTS user_x_intro;
CREATE TABLE user_x_intro
(
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    growth_answer INT DEFAULT 0 NOT NULL,
    choose_lib INT DEFAULT 0,
    wrong_answer INT DEFAULT 0,
    arena INT DEFAULT 0,
    arena_init INT DEFAULT 0,
    arena_time INT DEFAULT 0,
    choose_team INT DEFAULT 0,
    challenge INT DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);
CREATE INDEX index_userid ON user_x_intro (user_id);


ALTER TABLE right_resource_url ADD is_operate INT(4) DEFAULT 0 AFTER orders;

ALTER TABLE right_group ADD company_id INT(8) DEFAULT 0 AFTER id;
ALTER TABLE right_group ADD is_default INT(4) DEFAULT 0 AFTER value;

ALTER TABLE right_resource_group ADD company_id INT(8) DEFAULT 0 AFTER id;

ALTER TABLE right_group ADD UNIQUE KEY company_groupname (company_id,name);


ALTER TABLE user_right_group ADD company_id INT(8) DEFAULT 0 AFTER id;

ALTER TABLE user_x_company ADD COLUMN is_super_manage INT(4) DEFAULT 0 AFTER is_boss;

ALTER TABLE user_right_group ADD UNIQUE KEY company_user_group (company_id,user_id,right_group_id);


ALTER TABLE question_library_title ADD type INT(4) DEFAULT 1 AFTER title;

ALTER TABLE questions MODIFY answer_desc TEXT COMMENT '答案描述';