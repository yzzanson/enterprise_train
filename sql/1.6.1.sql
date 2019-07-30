ALTER TABLE company_info ADD refresh_time  datetime COMMENT '更新时间' AFTER delete_time;

CREATE INDEX index_userid ON `user_x_questions`  (user_id);
CREATE INDEX index_company_userid ON `user_x_questions`  (company_id,user_id);
CREATE INDEX index_company_userid_questionid ON `user_x_questions`  (company_id,user_id,question_id);