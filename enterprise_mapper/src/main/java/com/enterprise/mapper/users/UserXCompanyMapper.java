package com.enterprise.mapper.users;

import com.enterprise.base.entity.UserXCompany;
import com.enterprise.base.vo.UserPetVO;
import com.enterprise.base.vo.UserVO;
import com.enterprise.base.vo.UserVO2;
import com.enterprise.base.vo.UserXCompanyVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by anson on 18/3/26.
 */
public interface UserXCompanyMapper {

    void createUserXCompany(UserXCompany userXCompany);

    void updateUserXCompany(UserXCompany userXCompany);

    List<UserXCompanyVO> findUserCompany(@Param("corpId") String corpId, @Param("agentId") String agentId, @Param("companyName") String companyName, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    UserXCompany getByUserId(@Param("userId") Integer userId);

    UserXCompany getByDingUserId(@Param("dingUserId") String dingUserId);

    UserXCompany getByCompanyIdANdDingUserId(@Param("corpId") String corpId,@Param("dingUserId") String dingUserId);

    /**
     * 根据条件查询用户和企业的关联关系
     *
     * @param userXCompany 查询条件
     * @author shisan
     * @date 2018/3/26 下午3:03
     */
    UserXCompany getUserXCompany(UserXCompany userXCompany);


    /**
     * 获取该企业某天新增的用户
     * */
    Integer getNewUserByCompanyIdAndDate(@Param("companyId") Integer companyId,@Param("date") String date);

    /**
     * 获取某企业下所有用户
     * */
    List<UserVO> getUserByCorpId(@Param("corpId") String corpId,@Param("status") Integer status);

    void updateUserPosition(@Param("corpId") String corpId,@Param("userId") Integer userId,@Param("isAdmin") Integer isAdmin,@Param("isBoss") Integer isBoss,@Param("isLeader") Integer isLeader);

    /**
     * 获取用户的dingid
     * */
    UserXCompany getDingIdByCorpIdAndUserId(@Param("corpId") String corpId,@Param("userId") Integer userId);

    /**
     * 根据用户昵称获取用户信息
     * */
    List<UserVO2> getUserByName(@Param("corpId") String corpId,@Param("search") String search);

    UserXCompany getAdmin(@Param("corpId") String corpId);

    UserXCompany getOne(@Param("corpId") String corpId);

    Integer getSuperManageUserId(@Param("corpId") String corpId);

    /**
     * 获取排除答题、领养宠物后的用户列表
     * */
    List<Integer> getRemainUserIdList(@Param("corpId") String corpId,@Param("weekUserAnswerList") List<Integer> weekUserAnswerList,@Param("petUserList") List<Integer> petUserList,@Param("openUserList") List<Integer> openUserList);

    /**
     * 获取排除答题、领养宠物后的用户列表
     * */
    List<Integer> getRemainUserIdList3(@Param("corpId") String corpId,@Param("weekUserAnswerList") List<Integer> weekUserAnswerList,@Param("petUserList") List<Integer> petUserList);


    /**
     * 获取排除宠物重量后的用户列表
     * */
    List<Integer> getRemainUserIdList2(@Param("corpId") String corpId,@Param("petWeightUserList") List<Integer> petWeightUserList);

    /**
     * 获取用户dingid等信息
     * */
    UserVO2 getUserByCorpIdAndUserId(@Param("corpId") String corpId,@Param("userId") Integer userId);

    /**
     * 获取企业总人数
     * */
    Integer getCompanyTotalUserCount(@Param("corpId") String corpId);

    /**
     * 获取有宠物的人员
     * */
    List<UserPetVO> getUserRaisePet(@Param("corpId") String corpId);

    List<UserXCompany> getUserCompanyList(@Param("userId") Integer userId);

    /**
     * 所有的用户数
     * */
    Integer getTotalUserCount();

    /**
     * 所有有效用户数
     * */
    Integer getTotalActiveUserCount(@Param("corpId") String corpId);

    /**
     * 获取新增有效用户数
     * */
    Integer getNewActiveUserByCompanyIdAndDate(@Param("corpId") String corpId,@Param("date") String date);

    /**
     * 更新用户公司
     * */
    Integer batchUpdateUserXCompany(@Param("corpId") String corpId,@Param("status") Integer status);

    /**
     * 批量更新
     * */
    Integer batchUpdateUserXCompany2(@Param("userXCompanyList") List<UserXCompany> userXCompanyList);

    Integer batchInsert(@Param("userXCompanyList") List<UserXCompany> userXCompanyList);
}
