package com.enterprise.mobile.web.job;

import com.enterprise.base.enums.BagToolTypeEnum;
import com.enterprise.base.enums.StatusEnum;
import com.enterprise.mapper.bagTool.BagToolEffectMapper;
import com.enterprise.oa.OAMessageUtil;
import com.enterprise.util.DateUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/9/4 下午4:31
 */
@Component
public class BagToolElimateJob {

    @Resource
    private BagToolEffectMapper bagToolEffectMapper;

    //消除上周的窜天猴效果
    public void work() {
        //获取上周一的时间
        Date LastweekStartDay = DateUtil.getDateStartDateTime(DateUtil.getFirstDayOfWeek(DateUtil.getSeveralDaysLaterDate(new Date(), -7)));
        Date LastweekEndDay = DateUtil.getDateStartDateTime(DateUtil.getLastDayOfWeek(DateUtil.getSeveralDaysLaterDate(new Date(), -7)));
        //消除窜天猴效果
        Integer resusCount = bagToolEffectMapper.getResusCount(LastweekStartDay,LastweekEndDay, BagToolTypeEnum.RHESUS.getValue());
        Integer updateCount = bagToolEffectMapper.batchElimateResus(LastweekStartDay,LastweekEndDay, BagToolTypeEnum.RHESUS.getValue(), StatusEnum.DELETE.getValue());
        String updateResusOA = getMessageContent(resusCount,updateCount);
        OAMessageUtil.sendTextMsgToDept(updateResusOA);
    }

    private String getMessageContent(Integer resusCount,Integer updateCount){
        StringBuffer sb = new StringBuffer();
        sb.append("更新了"+updateCount+"条窜天猴记录");
        sb.append("存在"+resusCount+"条窜天猴记录");
        sb.append("更新时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "\n");
        return sb.toString();
    }


    public static void main(String[] args) {
        Date LastweekStartDay = DateUtil.getDateStartDateTime(DateUtil.getFirstDayOfWeek(DateUtil.getSeveralDaysLaterDate(new Date(), -7)));
        Date LastweekEndDay = DateUtil.getDateStartDateTime(DateUtil.getLastDayOfWeek(DateUtil.getSeveralDaysLaterDate(new Date(), -7)));
        System.out.println(LastweekStartDay);
        System.out.println(LastweekEndDay);

    }

}
