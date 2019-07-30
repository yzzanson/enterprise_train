package com.enterprise.web.job;

import com.enterprise.base.entity.IsvTicketsEntity;
import com.enterprise.mapper.isvTickets.IsvTicketsMapper;
import com.enterprise.service.dingCall.DingCallService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 每日0点执行
 * 更新企业昨日相比前日
 * @Description
 * @Author zezhouyang
 * @Date 18/4/13 上午11:01
 */
@Component
public class AuthUserJob {

    @Resource
    private IsvTicketsMapper isvTicketsMapper;

    @Resource
    private DingCallService dingCallService;

    /**
     * 获取昨天的数据
     * */
    public void work() {
        List<IsvTicketsEntity> isvTicketList = isvTicketsMapper.getNoAuthList();
        for (int i = 0; i < isvTicketList.size(); i++) {
            IsvTicketsEntity isvTicketsInDB = isvTicketList.get(i);
            Integer companyId = isvTicketsInDB.getCompanyId();
            if(StringUtils.isEmpty(isvTicketsInDB.getAuthUserId())){
                dingCallService.setAuthUserSingle(companyId);
            }
            if(isvTicketsInDB.getIsCall()==null){
                dingCallService.setIsCallSingle(companyId);
            }
        }
    }


}
