package com.enterprise.service.paperBall;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.entity.PaperBallEntity;

import java.util.List;

/**
 * @Description PetService
 * @Author anson
 * @Date 2018/4/2 上午9:37
 */
public interface PaperBallService {

    Integer genPaperBall(Integer companyId);

    List<PaperBallEntity> getExpiredPaper();

    Integer batchUpdate(List<PaperBallEntity> paperBallList);

    JSONObject cleanBall(Integer ballId);

    JSONObject cleanBallTest(Integer companyId,Integer userId,Integer ballId);

    JSONObject getActiveBallList(Integer userId);

    /**
     * 将当前未清除的纸团标记类型
     * */
    JSONObject setUncleanBallType();


    //测试用
    JSONObject getUserPaperBall();
    //测试用
    JSONObject getFeedUser();

}

