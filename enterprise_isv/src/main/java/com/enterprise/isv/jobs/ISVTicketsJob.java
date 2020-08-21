package com.enterprise.isv.jobs;

import com.enterprise.base.entity.IsvTicketsEntity;
import com.enterprise.service.isvTickets.IsvTicketsService;
import com.enterprise.service.suites.SuitesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * ISVTicketsJob
 *
 * @Author shisan
 * @Date 2017/11/30 下午5:24
 */
@Component
public class ISVTicketsJob {

    private static final Logger logger = LoggerFactory.getLogger(ISVTicketsJob.class);
    @Resource
    private IsvTicketsService isvTicketsService;

    @Resource
    private SuitesService suitesService;

    public void work() {
        logger.info("start isv tickets job...");
        List<IsvTicketsEntity> list = isvTicketsService.seachRefreshIsvTickets();
        if (CollectionUtils.isEmpty(list)) {
            logger.info("没`有要更新的内容...");
            return;
        }
        // todo 查出自己业务规定快过过期的ticket 列表     接入者自己查
        logger.info("开始刷新IsvTicket......,IsvTicket个数:" + list.size());
        if (!CollectionUtils.isEmpty(list)) {
            for (IsvTicketsEntity t : list) {
                try {
                    isvTicketsService.refreshIsvTicket(t);
                } catch (Exception e) {
                    logger.error("定时刷新IsvTicket 异常", e);
                }
            }
        }
        logger.info("刷新IsvTicket结束......");
    }

}
