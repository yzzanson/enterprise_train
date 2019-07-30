package com.enterprise.service.user;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.PageEntity;
import com.enterprise.base.entity.UserXCompany;
import com.enterprise.base.vo.UserVO;

import java.util.Date;
import java.util.List;

/**
 * Created by anson on 18/3/26.
 */
public interface UserXCompanyService {

    /**
     * 创建用户和企业关系关系
     *
     * @param userXCompany 用户和企业关系关系
     * @author shisan
     * @date 2018/3/26 下午3:02
     */
    void createUserXCompany(UserXCompany userXCompany);

    /**
     * 根据条件查询列表
     *
     * @author lanbo
     * @date 2018/3/26 下午3:03
     */
    JSONObject findUserCompany(String corpId, String agentId,  String companyName, Date startTime, Date endTime, PageEntity pageEntity);

    /**
     * 根据条件查询用户和企业的关联关系
     *
     * @param userXCompany 查询条件
     * @author shisan
     * @date 2018/3/26 下午3:03
     */
    UserXCompany getUserXCompany(UserXCompany userXCompany);

    /**
     * 新增或更新用户企业关联关系
     *
     * @param userXCompany 用户和企业关系关系
     * @author shisan
     * @date 2018/3/26 下午3:17
     */
    UserXCompany createOrUpdateUserXCompany(UserXCompany userXCompany);

    /**
     * 获取企业下所有用户
     * */
    List<UserVO> getUserByCorpId(String corpId);

    /**
     * 获取企业下所有用户
     * */
    List<UserVO> getUserByCorpId2(String corpId,Integer status);

    /**
     * 获取企业下的管理员
     * */
    Integer getAdmin(String corpId);

    void updateUserXCompany(UserXCompany userXCompany);
}

