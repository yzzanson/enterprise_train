package com.enterprise.web.job;

import com.enterprise.oa.OAMessageUtil;
import com.enterprise.service.rank.RankPraiseService;
import com.enterprise.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 每日0点执行
 * 更新企业昨日相比前日
 * @Description
 * @Author zezhouyang
 * @Date 18/4/13 上午11:01
 */
@Deprecated
public class RankPraiseWeekJob {

    private static final Logger logger = LoggerFactory.getLogger(RankPraiseWeekJob.class);

    @Resource
    private RankPraiseService rankPraiseService;

    /**
     * 获取昨天的数据
     * */
    public void work() {
        Date weekBegin = DateUtil.getDateStartDateTime(DateUtil.getFirstDayOfWeek(new Date()));
        rankPraiseService.deleteExpireData(weekBegin);
        StringBuffer sb = new StringBuffer();
        sb.append("清空点赞记录").append("\n");
        sb.append("更新时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "\n");
        OAMessageUtil.sendTextMsgToDept(sb.toString());

    }

    public static void main(String[] args) {
        Date weekBegin = DateUtil.getDateStartDateTime(DateUtil.getFirstDayOfWeek(new Date()));
        System.out.println(weekBegin);
    }

}
