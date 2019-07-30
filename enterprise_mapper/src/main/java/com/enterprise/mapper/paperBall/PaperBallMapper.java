package com.enterprise.mapper.paperBall;

import com.enterprise.base.entity.PaperBallEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/10/23 上午10:31
 */
public interface PaperBallMapper {

    Integer createPaperBall(PaperBallEntity paperBallEntity);


    Integer updatePaperBall(PaperBallEntity paperBallEntity);

    /**
     * 获取当前人未消除的纸团数量
     * */
    Integer getExistPaperBallCount(@Param("companyId") Integer companyId,@Param("userId") Integer userId);

    /**
     * 获取过期的纸团
     * */
    List<PaperBallEntity> getExpiredPaper();

    Integer batchUpdate(@Param("paperBallList") List<PaperBallEntity> paperBallList);

    Integer batchUpdateType(@Param("paperBallList") List<PaperBallEntity> paperBallList);

    /**
     * 获取当前的清扫次数
     * */
    Integer getCleanCount(@Param("companyId") Integer companyId,@Param("userId") Integer userId,@Param("cleanUserId") Integer cleanUserId,@Param("dateTime") String dateTime);

    PaperBallEntity getById(@Param("id") Integer id);

    /**
     * 获取当前作用的纸团
     * */
    List<PaperBallEntity> getActiveBallList(@Param("companyId") Integer companyId,@Param("userId") Integer userId);

    List<PaperBallEntity> getActiveBallListAll();


    List<PaperBallEntity> getExistPaperBallGroup();

    /**
     * 获取不存在的纸团类型
     * */
    List<Integer> getUserNotGainPaperBallList(@Param("companyId") Integer companyId,@Param("userId") Integer userId);
}
