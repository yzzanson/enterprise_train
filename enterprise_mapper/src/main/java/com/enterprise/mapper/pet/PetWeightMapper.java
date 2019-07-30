package com.enterprise.mapper.pet;

import com.enterprise.base.entity.PetWeightEntity;
import com.enterprise.base.vo.rank.UserPetWeightRankVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by null on 2018-03-23 10:27:03
 */
public interface PetWeightMapper {

    Integer createPetWeight(PetWeightEntity petWeightEntity);

    Integer batchInsert(@Param("petWeightList") List<PetWeightEntity> petWeightList);

    Integer updatePetWeight(PetWeightEntity petWeightEntity);

    PetWeightEntity getPetWeight(@Param("companyId") Integer companyId,@Param("userId") Integer userId);

    List<Integer> getPetWeightUserList(@Param("corpId") String corpId);

    List<UserPetWeightRankVO> getRankWeightList(@Param("companyId") Integer companyId,@Param("petWeightUserList") List<Integer> petWeightUserList,@Param("petRemainUserList") List<Integer> petRemainUserList);

    /**
     * 前五
     * */
    List<UserPetWeightRankVO> getRankWeightListFive(@Param("companyId") Integer companyId,@Param("petWeightUserList") List<Integer> petWeightUserList,@Param("petRemainUserList") List<Integer> petRemainUserList);


    List<UserPetWeightRankVO> getRankOtherList(@Param("companyId") Integer companyId,@Param("petRemainUserList") List<Integer> petRemainUserList);

    /**
     * 前五
     * */
    List<UserPetWeightRankVO> getRankOtherListFive(@Param("companyId") Integer companyId,@Param("petRemainUserList") List<Integer> petRemainUserList);
}