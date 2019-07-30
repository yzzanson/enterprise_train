package com.enterprise.mapper.bury;

import com.enterprise.base.entity.bury.OASendBuryEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/9/5 下午2:50
 */
public interface OASendBuryMapper {

   Integer createOASendBury(OASendBuryEntity oaSendBuryEntity);

   Integer batchInsert(@Param("oaSendBuryList") List<OASendBuryEntity> oaSendBuryList);

}
