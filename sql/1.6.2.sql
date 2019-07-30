CREATE INDEX index_corp_userid_status ON `user_x_company`  (corp_id,user_id,status);

ALTER TABLE questions_library ADD option_sort_type INT COMMENT '选项规则' DEFAULT 1 AFTER sort_type;

ALTER TABLE questions_library MODIFY option_sort_type INT COMMENT '选项规则' DEFAULT 1 AFTER sort_type;


ALTER TABLE my_pet CONVERT TO charset utf8mb4;
ALTER TABLE user CONVERT TO charset utf8mb4;
ALTER TABLE user_x_dept CONVERT TO charset utf8mb4;
