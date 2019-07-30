package com.enterprise.mapper.pet;

import com.enterprise.base.entity.MyPetEntity;
import com.enterprise.base.vo.MyPetVO;
import com.enterprise.base.vo.MyPetVONew;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by null on 2018-03-23 10:27:03
 */
public interface MyPetMapper {

    Integer createOrUpdateMyPet(MyPetEntity myPetEntity);

    Integer updateMyPet(MyPetEntity myPetEntity);

    Integer updatePetExp(MyPetEntity myPetEntity);

    Integer updatePetPhysicalValue(@Param("id") Integer id ,@Param("physicalValue") Integer physicalValue,@Param("level") Integer level);

    Integer updatePetPhysicalValue2(@Param("id") Integer id ,@Param("physicalValue") Integer physicalValue);

    MyPetVO getMyPet(@Param("userId") Integer userId);

    MyPetVONew getMyPetNew(@Param("userId") Integer userId);

    List<Integer> getUsersByCompany(@Param("corpId") String corpId,@Param("notIncludeUserList")List<Integer> notIncludeUserList);

}