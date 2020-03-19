package com.enterprise.mobile.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.*;
import com.enterprise.base.entity.CompanyInfoEntity;
import com.enterprise.base.entity.MarketBuyEntity;
import com.enterprise.base.entity.UserEntity;
import com.enterprise.base.entity.WeekRankEntity;
import com.enterprise.base.vo.UserAnswerRankSimpleNewVO;
import com.enterprise.isv.thread.SynchronizeLibrary;
import com.enterprise.lock.RedisLockUtil;
import com.enterprise.oa.OAMessageUtil;
import com.enterprise.service.company.CompanyInfoService;
import com.enterprise.service.marketBuy.MarketBuyService;
import com.enterprise.service.paperBall.PaperBallService;
import com.enterprise.service.petFood.PetFoodService;
import com.enterprise.service.question.UserXQuestionsService;
import com.enterprise.service.question.WeekRankService;
import com.enterprise.service.redis.RedisService;
import com.enterprise.service.sysorg.SysOrgService;
import com.enterprise.service.user.UserService;
import com.enterprise.service.user.UserXTitleService;
import com.enterprise.thread.ModifyUserLibraryThread;
import com.enterprise.util.DateUtil;
import com.enterprise.util.oa.message.OAMessage;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;

/**
 * TestController
 *
 * @author anson
 * @create 2018-03-22 上午11:53
 **/
@Controller
@RequestMapping("/test")
public class TestController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Long expireTime = RedisConstant.EXPIRE_TIME_COMMON;

    @Resource
    private UserService userService;

    @Resource
    private CompanyInfoService companyInfoService;
    @Resource
    private WeekRankService weekRankService;
    @Resource
    private UserXQuestionsService userXQuestionsService;

    @Resource
    private PaperBallService paperBallService;

    @Resource
    private UserXTitleService userXTitleService;

    @Resource
    private RedisService redisService;

    @Resource
    private PetFoodService petFoodService;

    @Resource
    private MarketBuyService marketBuyService;

    @Resource
    private SysOrgService sysOrgService;

    @RequestMapping("/getJson")
    @ResponseBody
    public JSONObject getJson(Integer id) {
        MobileLoginUser m = MobileLoginUser.getUser();
        MobileLoginUser mobileLoginUser2 = ThreadLocalManager.getMobileLoginUser();
        logger.info(mobileLoginUser2.toString());
        return ResultJson.succResultJson(id);
    }

    @RequestMapping("/getUserById")
    @ResponseBody
    public JSONObject getUserById(Integer id) {
        logger.info("id" + id);
        String key = "user_" + id;
        UserEntity userEntity = userService.getUserById(id);
        redisService.setSerialize(key, expireTime, userEntity);
//        redisTemplate.opsForValue().set(key, userEntity.toString());
        UserEntity result = (UserEntity) redisService.getSerializeObj(key);
        return ResultJson.succResultJson(result);
    }


    @RequestMapping("/synchronizeLib")
    @ResponseBody
    public JSONObject synchronizeLib(String corpId) {
        new Thread(new SynchronizeLibrary(corpId)).start();
        return ResultJson.succResultJson(corpId);
    }


    @RequestMapping("/lastWeekRank")
    @ResponseBody
    public void lastWeekRank() {
        //获取本周一的日期
        String weekStartDay = DateUtil.getDateYMD(DateUtil.getFirstDayOfWeek(new Date()));
        String LastweekStartDay = DateUtil.getDateYMD(DateUtil.getFirstDayOfWeek(DateUtil.getSeveralDaysLaterDate(new Date(), -7)));
        List<CompanyInfoEntity> companyList = companyInfoService.getAllCompanys();
        for (int i = 0; i < companyList.size(); i++) {
            CompanyInfoEntity companyInfo = companyList.get(i);
            Integer companyId = companyInfo.getId();
            //删除上上周的,保留上周的
            weekRankService.singleDelete(companyId, LastweekStartDay);
            List<UserAnswerRankSimpleNewVO> userRankList = userXQuestionsService.getTop5Rank(companyInfo.getId());
            List<WeekRankEntity> weeRankList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(userRankList)) {
                for (UserAnswerRankSimpleNewVO userAnswerRankSimpleVO : userRankList) {
                    //public WeekRankEntity(Integer companyId, Integer userId, Integer rank, String dateTime) {
                    WeekRankEntity weekRankEntity = new WeekRankEntity(companyId, userAnswerRankSimpleVO.getUserId(), userAnswerRankSimpleVO.getUserId(), weekStartDay);
                    weeRankList.add(weekRankEntity);
                }
                weekRankService.batchInsert(weeRankList);
            }
        }
    }


    @RequestMapping("/genPaperBall")
    @ResponseBody
    public JSONObject genPaperBall(Integer companyId) {
        //获取本周一的日期
        Integer result = paperBallService.genPaperBall(companyId);
        return ResultJson.succResultJson(result);
    }


    /**
     * 用户头衔修复
     */
    @RequestMapping("/updateUserTitle")
    @ResponseBody
    public JSONObject updateUserTitle(Integer libraryId) {
        //获取本周一的日期
        return userXTitleService.updateUserTitle(libraryId);
    }


    /**
     * 用户头衔修复
     */
    @RequestMapping("/clearTitle")
    @ResponseBody
    public JSONObject clearTitle() {
        //获取本周一的日期
        return userXTitleService.clearTitle();
    }


    /**
     * 用户头衔修复
     */
    @RequestMapping("/testCreateCompany")
    @ResponseBody
    public JSONObject testCreateCompany(CompanyInfoEntity companyInfoEntity) {
        //获取本周一的日期
        companyInfoService.createCompanyInfo(companyInfoEntity);
        return ResultJson.succResultJson(companyInfoEntity);
    }

    /**
     * 用户头衔修复
     */
    @RequestMapping("/testGetCompany")
    @ResponseBody
    public JSONObject testGetCompany(Integer id) {
        //获取本周一的日期
        return ResultJson.succResultJson(companyInfoService.getCompanyById(id));
    }


    /**
     * 获取数据
     * companyId,userId,ballId
     */
    @RequestMapping("/getUserPaperBall")
    @ResponseBody
    public JSONObject getUserPaperBall() {
        return paperBallService.getUserPaperBall();
    }


    /**
     * 喂养
     * companyId,userId,otheruserid
     */
    @RequestMapping("/getFeedUser")
    @ResponseBody
    public JSONObject getFeedUser() {
        return paperBallService.getFeedUser();
    }

    /**
     * 喂养
     * companyId,userId,otheruserid
     */
    @RequestMapping("/getPetFood")
    @ResponseBody
    public JSONObject getPetFood() {
        return paperBallService.getFeedUser();
    }


    /**
     *
     * */
    @RequestMapping("/addMaket")
    @ResponseBody
    public JSONObject addMaket(MarketBuyEntity marketBuyEntity) {
        String str = "{\"EventType\": \"market_buy\", \"SuiteKey\": \"suited6db0pze8yao1b1y\", \"buyCorpId\": \"dingxxxxxxxx\", \"goodsCode\": \"FW_GOODS-xxxxxxxx\", \"itemCode\": \"1c5f70cf04c437fb9aa1b20xxxxxxxx\", \"itemName\": \"按照范围收费规格0-300\", \"subQuantity\": 1, \"maxOfPeople\": 300, \"minOfPeople\": 0,\"orderId\": \"308356401xxxxxxxx\", \"paidtime\": 1474535702000, \"serviceStopTime\": 1477065600000, \"payFee\":147600, \"orderCreateSource\":\"DRP\", \"nominalPayFee\":147600, \"discountFee\":600, \"discount\":0.06, \"distributorCorpId\":\"ding9f50b15bccd16741\", \"distributorCorpName\":\"测试企业\"}";
        JSONObject s = JSONObject.parseObject(str);
        s.put("SuiteKey", marketBuyEntity.getSuiteKey());
        s.put("buyCorpId", marketBuyEntity.getBuyCorpId());
        s.put("goodsCode", marketBuyEntity.getGoodsCode());
        s.put("itemCode", marketBuyEntity.getItemCode());
        s.put("itemName", marketBuyEntity.getItemName());
        s.put("subQuantity", marketBuyEntity.getSubQuantity());
        s.put("maxOfPeople", marketBuyEntity.getMaxOfPeople());
        s.put("minOfPeople", marketBuyEntity.getMinOfPeople());
        s.put("orderId", marketBuyEntity.getOrderId());
        s.put("paidtime", new Date());
        s.put("serviceStopTime", new Date());
        s.put("payFee", marketBuyEntity.getPayFee());

        s.put("orderCreateSource", null);
        s.put("nominalPayFee", null);
        s.put("discountFee", null);
        s.put("discount", null);
        s.put("distributorCorpId", null);
        s.put("distributorCorpName", null);
        marketBuyService.add(s);
        return ResultJson.succResultJson(1);
    }


    @RequestMapping("/isBuy")
    @ResponseBody
    public JSONObject isBuy(String corpId) {
        boolean isBuy = marketBuyService.isBuy(corpId);
        return ResultJson.succResultJson(isBuy);
    }


    @RequestMapping("/modifyUserStudyProgress")
    @ResponseBody
    public JSONObject modifyUserStudyProgress() {
        Integer companyId = MobileLoginUser.getUser().getCompanyID();
        Integer userId = MobileLoginUser.getUser().getUserID();
        new Thread(new ModifyUserLibraryThread(companyId, userId)).run();
        return ResultJson.succResultJson("");
    }

    static class Outputer {

        RedisLockUtil redisLockUtil = new RedisLockUtil(10000L);

        public void output(String name) {
            //传统java加锁
            //synchronized (Outputer.class){
            redisLockUtil.lock("testone", String.valueOf(name), null);
            //lock.lock();
            try {
                for (int i = 0; i < name.length(); i++) {
                    System.out.print(name.charAt(i));
                }
                System.out.println();
            } finally {
                //任何情况下都有释放锁
                //lock.unlock();
                redisLockUtil.unlock("testone");
            }
            //}
        }
    }

    @RequestMapping("/modifyUser")
    @ResponseBody
    public JSONObject modifyUser(String name) {
        MobileLoginUser mobileLoginUser = MobileLoginUser.getUser();
        userService.updateUser(new UserEntity(mobileLoginUser.getUserID(),name,null,new Date()));
        return ResultJson.succResultJson("success");
    }


//    @RequestMapping("/getAllSysOrg")
//    @ResponseBody
//    public JSONObject getAllSysOrg() {
//        sysOrgService.getAllSysOrg();
//        return ResultJson.succResultJson("success");
//    }

    @RequestMapping("/getDepartmentList")
    @ResponseBody
    public JSONObject getDepartmentList() {
        sysOrgService.getDepartmentList();
        return ResultJson.succResultJson("success");
    }

}
