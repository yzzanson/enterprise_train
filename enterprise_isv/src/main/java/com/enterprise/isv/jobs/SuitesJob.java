package com.enterprise.isv.jobs;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.entity.SuitesEntity;
import com.enterprise.service.suites.SuitesService;
import com.enterprise.util.DateUtil;
import com.enterprise.util.dingtalk.AuthHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 套件更新定时任务
 *
 * @Author shisan
 * @Date 2017/11/30 下午5:24
 */
@Component
public class SuitesJob {

    private static final Logger logger = LoggerFactory.getLogger(SuitesJob.class);

    @Resource
    private SuitesService suitesService;

    public void work() {


        // todo 查出自己业务规定快过过期的Suites 列表
        List<SuitesEntity> list = suitesService.seachRefeshSuites();
        if (CollectionUtils.isEmpty(list)) {
            logger.info("没有要更新的内容...");
            return;
        }
        logger.info("开始刷新Suites......,套件个数 = " + list.size());
        for (SuitesEntity suitesEntity : list) {
            try {
                JSONObject resultJson = AuthHelper.getSuiteAccessToken(suitesEntity.getSuiteKey(), suitesEntity.getSuiteSecret(), suitesEntity.getSuiteTicket());
                String suiteToken = resultJson.getString("suite_access_token");
                int suiteTokenExpireTime = Integer.valueOf(resultJson.getString("expires_in"));

                suitesEntity.setSuiteAccessToken(suiteToken);
                suitesEntity.setSuiteAccessTokenExpireTime(DateUtil.getSecondLaterDate(new Date(), suiteTokenExpireTime));
                suitesService.modifySuites(suitesEntity);
            } catch (Exception e) {
                logger.error("套件" + suitesEntity.toString() + " 定时刷新Suites 异常", e);
            }
        }

        logger.info("刷新Suites结束......");

    }
}
