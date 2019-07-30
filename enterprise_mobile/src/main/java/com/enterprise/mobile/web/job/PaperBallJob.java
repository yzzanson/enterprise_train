package com.enterprise.mobile.web.job;

import com.enterprise.base.common.RedisConstant;
import com.enterprise.base.entity.CompanyInfoEntity;
import com.enterprise.base.entity.DSwitchEntity;
import com.enterprise.base.entity.PaperBallEntity;
import com.enterprise.base.enums.StatusEnum;
import com.enterprise.base.enums.SwitchEnum;
import com.enterprise.mapper.companyInfo.CompanyInfoMapper;
import com.enterprise.service.paperBall.PaperBallService;
import com.enterprise.service.redis.RedisService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description 产生纸团
 * @Author zezhouyang
 * @Date 18/10/23 上午11:28
 */
@Component
public class PaperBallJob {

    @Resource
    private CompanyInfoMapper companyInfoMapper;

    @Resource
    private PaperBallService paperBallService;

    @Resource
    private RedisService redisService;

    public static String DSWITCH_TYPE_KEY = RedisConstant.DSWITCH_TYPE_KEY;

    /**
     * 10分钟产生一个
     * */
    public void generate(){
        String key = DSWITCH_TYPE_KEY + SwitchEnum.GEN_PAPER_BALL.getValue();
        DSwitchEntity objectFromRedis = (DSwitchEntity) redisService.getSerializeObj(key);
        if(objectFromRedis!=null && objectFromRedis.getStatus()!=null && objectFromRedis.getStatus().equals(0)){
            return;
        }
        List<CompanyInfoEntity> companyInfoList = companyInfoMapper.getAllCompanys();
        for (int i = 0; i < companyInfoList.size(); i++) {
            Integer companyId = companyInfoList.get(i).getId();
            paperBallService.genPaperBall(companyId);
        }
    }

    /**
     * 72小时到期消除
     * */
    public void vanish(){
        //获取产生超过72小时未消除的
        List<PaperBallEntity> updateList = new ArrayList<>();
        List<PaperBallEntity> expiredPaperBall = paperBallService.getExpiredPaper();
        if(CollectionUtils.isNotEmpty(expiredPaperBall)) {
            for (int i = 0; i < expiredPaperBall.size(); i++) {
                PaperBallEntity paperBallEntity = expiredPaperBall.get(i);
                paperBallEntity.setStatus(StatusEnum.DELETE.getValue());
                paperBallEntity.setElimateTime(new Date());
                updateList.add(paperBallEntity);
            }
            paperBallService.batchUpdate(updateList);
        }
    }

}
