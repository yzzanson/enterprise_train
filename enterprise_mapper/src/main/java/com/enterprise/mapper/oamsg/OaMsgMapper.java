package com.enterprise.mapper.oamsg;

import com.enterprise.base.entity.OaMsgEntity;

/**
 * Created by anson on 2018-04-23 18:00:20
 */
public interface OaMsgMapper {
    /**
     * 保存oa消息
     *
     * @param oaMsg 新增内容
     * @author anson
     * @date 2018-04-23 18:00:30
     */
    void createOaMsg(OaMsgEntity oaMsg);
}