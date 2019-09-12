package com.enterprise.isv.servlets;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.dingtalk.open.client.api.model.corp.CorpUserDetail;
import com.enterprise.base.common.GlobalConstant;
import com.enterprise.base.common.SpringContextHolder;
import com.enterprise.base.entity.CompanyInfoEntity;
import com.enterprise.base.entity.IsvTicketsEntity;
import com.enterprise.base.entity.SuitesEntity;
import com.enterprise.base.enums.AgentStatusEnum;
import com.enterprise.base.enums.SendOAEnum;
import com.enterprise.base.enums.StatusEnum;
import com.enterprise.base.exceptions.BusinessException;
import com.enterprise.isv.thread.SynchronizeOrganization;
import com.enterprise.isv.thread.SynchronizePublicSingle;
import com.enterprise.isv.thread.SynchronizeRight;
import com.enterprise.isv.thread.WelcomeThread;
import com.enterprise.isv.utils.DingTalkEncryptException;
import com.enterprise.isv.utils.DingTalkEncryptor;
import com.enterprise.oa.OAMessageUtil;
import com.enterprise.service.company.CompanyInfoService;
import com.enterprise.service.isvTickets.IsvTicketsService;
import com.enterprise.service.marketBuy.MarketBuyService;
import com.enterprise.service.suites.SuitesService;
import com.enterprise.service.user.UserService;
import com.enterprise.service.user.UserXCompanyService;
import com.enterprise.util.AssertUtil;
import com.enterprise.util.DateUtil;
import com.enterprise.util.HttpUtil;
import com.enterprise.util.JSONUtil;
import com.enterprise.util.dingtalk.AuthHelper;
import com.enterprise.util.dingtalk.DDConfig;
import com.enterprise.util.dingtalk.DingHelper;
import com.taobao.api.ApiException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * IsvReceiveServlet 这个servlet用来接收钉钉服务器回调接口的推送
 *
 * @Author shisan
 * @Date 2018/3/26 上午9:43
 */
public class IsvReceiveServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(IsvReceiveServlet.class);

    private SuitesService suitesService;

    private IsvTicketsService ticketsService;

    private CompanyInfoService companyInfoService;

    private MarketBuyService marketBuyService;

    private UserXCompanyService userXCompanyService;

    private UserService userService;

    public IsvReceiveServlet() {
        suitesService = SpringContextHolder.getBean(SuitesService.class);
        ticketsService = SpringContextHolder.getBean(IsvTicketsService.class);
        companyInfoService = SpringContextHolder.getBean(CompanyInfoService.class);
        marketBuyService = SpringContextHolder.getBean(MarketBuyService.class);
        logger.info("IsvReceiveServlet初始化....");
    }

    /**
     * 接收钉钉服务器的回调数据
     *
     * @author shisan
     * @date 2018/3/26 上午9:45
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            //url中的签名
            String msgSignature = request.getParameter("signature");
            //url中的时间戳
            String timeStamp = request.getParameter("timestamp");
            //url中的随机字符串
            String nonce = request.getParameter("nonce");
            //套件Id
            String id = request.getParameter("suiteId");

            logger.info("套件ID : " + id + ", signature=" + msgSignature + ", timestamp = " + timeStamp + ", nonce = " + nonce + ", id = " + id);

            //todo 如果多个套件钉钉并没有传相应的标示。这边自己定义一个
            Integer suiteId = null;
            if (StringUtils.isNotEmpty(id)) {
                suiteId = Integer.valueOf(id);
            } else {
                throw new RuntimeException("套件ID 不能为空");
            }

            //post数据包数据中的加密数据
            StringBuilder sb = new StringBuilder();
            try {
                ServletInputStream sis = request.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(sis));
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
            } catch (Exception e) {
                logger.error("post数据包数据中的加密数据失败!", e);
            }

            //post数据包数据中的加密数据转换成JSON对象，JSON对象的格式如下:{"encrypt":"1ojQf0NSvw2WPvW7LijxS8UvISr8pdDP+rXpPbcLGOmIBNbWetRg7IP0vdhVgkVwSoZBJeQwY2zhROsJq/HJ+q6tp1qhl9L1+ccC9ZjKs1wV5bmA9NoAWQiZ+7MpzQVq+j74rJQljdVyBdI/dGOvsnBSCxCVW0ISWX0vn9lYTuuHSoaxwCGylH9xRhYHL9bRDskBc7bO0FseHQQasdfghjkl}
            JSONObject jsonEncrypt = JSONObject.parseObject(sb.toString());

            //取得JSON对象中的encrypt字段
            String encrypt = jsonEncrypt.getString("encrypt");

            //对encrypt进行解密
            DingTalkEncryptor dingTalkEncryptor = null;
            String plainText = null;
            DingTalkEncryptException dingTalkEncryptException = null;

            SuitesEntity suitEntiy = suitesService.getSuitesentity(new SuitesEntity(StatusEnum.OK.getValue(), suiteId));
            AssertUtil.notNull(suitEntiy, "无效的套件ID : " + suiteId);
            try {
                /**
                 *创建加解密类
                 * 第一个参数为注册套件的之时填写的token
                 * 第二个参数为注册套件的之时生成的数据加密密钥（ENCODING_AES_KEY）
                 * 第三个参数，ISV开发传入套件的suiteKey，普通企业开发传Corpid
                 * 具体参数值请查看开发者后台(http://console.d.aliyun.com)
                 *
                 * 注：其中，对于第三个参数，在第一次接受『验证回调URL有效性事件的时候』
                 * 传入Env.CREATE_SUITE_KEY，对于这种情况，已在异常中catch做了处理。
                 * 具体区别请查看文档『验证回调URL有效性事件』
                 */
                dingTalkEncryptor = new DingTalkEncryptor(suitEntiy.getToken(), suitEntiy.getEncodingAesKey(), suitEntiy.getSuiteKey());

                //获取从encrypt解密出来的明文
                plainText = dingTalkEncryptor.getDecryptMsg(msgSignature, timeStamp, nonce, encrypt);
            } catch (DingTalkEncryptException e) {
                dingTalkEncryptException = e;
                logger.error("创建加解密类失败,", e);
            } finally {
                if (dingTalkEncryptException != null) {
                    if (dingTalkEncryptException.code == DingTalkEncryptException.COMPUTE_DECRYPT_TEXT_CORPID_ERROR) {
                        try {
                            //第一次创建套件生成加解密类需要传入Env.CREATE_SUITE_KEY
                            dingTalkEncryptor = new DingTalkEncryptor(suitEntiy.getToken(), suitEntiy.getEncodingAesKey(), DDConfig.CREATE_SUITE_KEY);
                            plainText = dingTalkEncryptor.getDecryptMsg(msgSignature, timeStamp, nonce, encrypt);
                        } catch (DingTalkEncryptException e) {
                            logger.error("DingTalkEncryptException,", e);
                        }
                    } else {
                        logger.error("IsvReceiveServlet.doGet() Exception:", dingTalkEncryptException);
                    }
                }
            }

            //对从encrypt解密出来的明文进行处理,不同的eventType的明文数据格式不同
            JSONObject plainTextJson = JSONObject.parseObject(plainText);
            String suiteKey = plainTextJson.getString("SuiteKey");
            logger.info("服务窗请求参数 > " + plainTextJson.toJSONString() + "----------");
            String eventType = plainTextJson.getString("EventType");

            // res是需要返回给钉钉服务器的字符串，一般为success "check_create_suite_url"和"check_update_suite_url"事件为random字段,具体请查看文档或者对应eventType的处理步骤
            String res = "success";
            String url;
            JSONObject object;
            JSONObject p = new JSONObject();

            switch (eventType) {
                case "suite_ticket":
                    //"suite_ticket"事件每二十分钟推送一次,数据格式如下: { "SuiteKey": "suitexxxxxx", "EventType": "suite_ticket ", "TimeStamp": 1234456, "SuiteTicket": "adsadsad" }
                    try {
                        logger.info("【消息推送管理 suite_ticket】进入事件每5个小时推送一次");

                        String suiteTicket = plainTextJson.getString("SuiteTicket");
                        String suiteSecret = suitEntiy.getSuiteSecret();

                        //获取到suiteTicket之后需要换取suiteToken，
                        logger.info("suiteTicket > " + suiteTicket + " suite_key>" + suitEntiy.getSuiteKey() + " SUITE_SECRET> " + suitEntiy.getSuiteSecret());

                        //应用套件access_token
                        JSONObject resultJson = AuthHelper.getSuiteAccessToken(suiteKey, suiteSecret, suiteTicket);
                        String suiteToken = resultJson.getString("suite_access_token");
                        int suiteTokenExpireTime = Integer.valueOf(resultJson.getString("expires_in"));
                        /*
                         * ISV应当把最新推送的suiteTicket做持久化存储，
                         * 以防重启服务器之后丢失了当前的suiteTicket
                         */
                        suitEntiy.setSuiteTicket(suiteTicket);
                        suitEntiy.setSuiteAccessToken(suiteToken);
                        suitEntiy.setSuiteAccessTokenExpireTime(DateUtil.getSecondLaterDate(new Date(), suiteTokenExpireTime));
                        suitEntiy.setUpdateTime(new Date());
                        suitesService.modifySuites(suitEntiy);
                        logger.info("每5个小时推送一次  成功suiteToken 获取成功 >" + suiteToken);
                    } catch (Exception e) {
                        res = "fail";
                        logger.error("每5个小时推送一次 suiteToken 获取失败:", e);
                    }

                    break;
                case "tmp_auth_code":
                    //"tmp_auth_code"事件将企业对套件发起授权的时候推送,数据格式如下{"SuiteKey": "suitexxxxxx","EventType": " tmp_auth_code","TimeStamp": 1234456,"AuthCode": "adads"}
                    String authCode = plainTextJson.getString("AuthCode");
                    //value.toString();
                    String suiteTokenPerm = suitEntiy.getSuiteAccessToken();

                    // 拿到tmp_auth_code（临时授权码）后，需要向钉钉服务器获取企业的corpId（企业id）和permanent_code（永久授权码）
                    p.clear();
                    p.put("suite_access_token", suiteTokenPerm);
                    url = JSONUtil.appendJsonParamsToUrl(DDConfig.GET_PERMANENT_CODE, p);
                    p.clear();
                    p.put("tmp_auth_code", authCode);
                    object = HttpUtil.doPost(url, p);
                    logger.info(url);
                    // 将corpId（企业id）和permanent_code（永久授权码）做持久化存储 之后在获取企业的access_token时需要使用
                    //todo 保存将corpId（企业id）和permanent_code（永久授权码）做持久化存储
                    //{"auth_corp_info":{"corp_name":" 实名认证2","corpid":"dingdad68ec54087869f35c2f4657eb6378f"},"ch_permanent_code":"M_13SWAuUPT9MjgNwlH4NWdB4YZPipy8k1QvbHxZGuL7OBUst2NWmbPIBYRTFmR6"}
                    String corpId = object.getJSONObject("auth_corp_info").getString("corpid");
                    //String channelPermanentCode = object.getString("ch_permanent_code");
                    String corpPermanentCode = object.getString("permanent_code");
                    String corpName = object.getJSONObject("auth_corp_info").getString("corp_name");

                    AuthHelper.getActivateSuite(suiteTokenPerm, suitEntiy.getSuiteKey(), corpId);
                    Boolean isNewCompany = false;
                    IsvTicketsEntity isvTicketsEntity = ticketsService.getIsvTicketByCorpId(corpId);
                    //获取对应企业的access_token，每一个企业都会有一个对应的access_token，访问对应企业的数据都将需要带上这个access_token access_token的过期时间为两个小时
                    String corpAccessToken = "";
                    String corpTikect = "";
                    if (StringUtils.isNotBlank(corpPermanentCode)) {
                        try {
                            corpAccessToken = AuthHelper.getCorpAccessToken(corpId, suiteTokenPerm, corpPermanentCode);
                            corpTikect = AuthHelper.getCorpTicket(corpAccessToken);
                        } catch (Exception e) {
                            logger.error("获取 CorpAccessToken 或 CorpTicket 失败!", e);
                        }

                    }

                    String authUserId = AuthHelper.getAuthInfo(suitEntiy.getSuiteTicket(), suitEntiy.getSuiteKey(), suitEntiy.getSuiteSecret(), corpId);

                    // todo 获取授权后企业中的agentid  为之后免登准备
                    p.clear();
                    p.put("suite_access_token", suiteTokenPerm);
                    url = JSONUtil.appendJsonParamsToUrl(DDConfig.GET_AUTH_INFO, p);

                    p.put("auth_corpid", corpId);
                    p.put("suite_key", suitEntiy.getSuiteKey());
                    object = HttpUtil.doPost(url, p);

                    logger.info("企业详细信息 > " + object.toJSONString());

                    // todo 暂时不需要
                    JSONObject auth_info = object.getJSONObject("auth_info");
                    JSONArray agents = auth_info.getJSONArray("agent");
                    String corpAppId = suitEntiy.getCorpAppid();
                    String corpAgentId = "";
                    if (agents != null && agents.size() > 0)
                        for (int i = 0; i < agents.size(); i++) {
                            JSONObject obj = agents.getJSONObject(i);
                            logger.info("corpAppId:" + corpAppId + " appid:" + obj.getString("appid"));
                            if (corpAppId.equals(obj.getString("appid"))) {
                                corpAgentId = obj.getString("agentid");
                                logger.info("agentid:" + corpAgentId);
                                break;
                            }
                        }

                    String authedUserId = (String) JSONObject.parseObject(object.get("auth_user_info").toString()).get("userId");

                    if (isvTicketsEntity == null) {
                        isNewCompany = true;
                        //新增companyInfo信息
                        CompanyInfoEntity company = new CompanyInfoEntity();
                        company.setName(corpName);
                        company.setSendOa(SendOAEnum.SEND.getValue());
                        company.setAgentStatus(AgentStatusEnum.USEING.getValue());
                        company.setStatus(StatusEnum.OK.getValue());

                        companyInfoService.createCompanyInfo(company);
                        //新增企业的isvTickets 信息
                        isvTicketsEntity = new IsvTicketsEntity();
                        isvTicketsEntity.setCorpId(corpId);
                        isvTicketsEntity.setCompanyId(company.getId());
                        isvTicketsEntity.setSuiteId(suiteId);
                        isvTicketsEntity.setCorpAgentId(corpAgentId);
                        isvTicketsEntity.setCorpPermanentCode(corpPermanentCode);
                        isvTicketsEntity.setCorpAccessToken(corpAccessToken);
                        isvTicketsEntity.setCorpTicket(corpTikect);
                        isvTicketsEntity.setStatus(StatusEnum.OK.getValue());
                        if (StringUtils.isNotEmpty(authUserId)) {
                            isvTicketsEntity.setAuthUserId(authUserId);
                        }
                        ticketsService.createIsvTickets(isvTicketsEntity);
                    } else {
                        isNewCompany = false;
                        logger.info("更新企业信息corpAgentId:" + corpAgentId);
                        //更新企业名称
                        CompanyInfoEntity company = new CompanyInfoEntity();
                        company.setId(isvTicketsEntity.getCompanyId());
                        company.setName(corpName);
                        company.setAgentStatus(AgentStatusEnum.USEING.getValue());
                        companyInfoService.modifyCompanyInfo(company);
                        //更新企业的isvTickets 信息
                        isvTicketsEntity.setCorpAgentId(corpAgentId);
                        isvTicketsEntity.setCorpPermanentCode(corpPermanentCode);
                        isvTicketsEntity.setCorpAccessToken(corpAccessToken);
                        isvTicketsEntity.setCorpTicket(corpTikect);
                        isvTicketsEntity.setSuiteId(suiteId);
                        isvTicketsEntity.setStatus(StatusEnum.OK.getValue());
                        if (StringUtils.isNotEmpty(authUserId)) {
                            isvTicketsEntity.setAuthUserId(authUserId);
                        }
                        ticketsService.modifyIsvTickets(isvTicketsEntity);


                        try {
                            boolean isBuy = marketBuyService.isBuy(corpId);
                            if (isBuy) {
                                isvTicketsEntity.setIsBuy(StatusEnum.OK.getValue());
                                ticketsService.modifyIsvTickets(isvTicketsEntity);
                            }
                        } catch (Exception e) {
                            logger.error(e.getMessage());
                            OAMessageUtil.sendTextMsgToDept(corpId + "查询是否购买过时异常" + e.getMessage());
                        }

                    }
                    //人员同步要堵塞,不然后面获取员工信息会出事
                    new SynchronizeOrganization(corpId, isvTicketsEntity).run();
                    //将默认的题库导入给用户
                    //new Thread(new SynchronizeLibrary(corpId)).start();
                    //同步公共题库!!
                    if (isNewCompany) {
                        Thread.sleep(10000);
                        //公司管理员新增管理员权限
                        new Thread(new SynchronizeRight(corpId)).start();
                        //暂时关闭全员OA消息通知
                        new Thread(new SynchronizePublicSingle(corpId)).start();
                        new Thread(new WelcomeThread(corpId)).start();
                    }

                    String registerUrl = GlobalConstant.getGateWay() + request.getContextPath() + "/suite/regist_callback?suiteId=" + suiteId;
                    AuthHelper.registerCallBack(corpAccessToken, suitEntiy, registerUrl);
                    logger.info(corpName + "设置回调地址:" + registerUrl + " 成功!");
                    break;
                case "change_auth":
                    // "change_auth"事件将在企业授权变更消息发生时推送 ,数据格式如下 {"SuiteKey": "suitexxxxxx", "EventType": " change_auth", "TimeStamp": 1234456, "AuthCorpId": "xxxxx" }
                    logger.info("change_auth > ==== 事件将在企业授权变更消息发生时推送");
                    String corpid = plainTextJson.getString("AuthCorpID");
                    corpId = plainTextJson.getString("AuthCorpId");
                    isvTicketsEntity = ticketsService.getIsvTicketByCorpId(corpId);
                    new SynchronizeOrganization(corpId, isvTicketsEntity).run();

                    Integer result = AuthHelper.checkCall(isvTicketsEntity.getCorpAccessToken());
                    isvTicketsEntity.setIsCall(result);
                    ticketsService.modifyIsvTickets(isvTicketsEntity);
                    //将默认的题库导入给用户
                    //new SynchronizeLibrary(corpId).run();


                    /**
                     * 由于以下操作需要从持久化存储中获得数据，而本demo并没有做持久化存储（因为部署环境千差万别），所以没有具体代码，只有操作指导。
                     * 1.根据corpid查询对应的permanent_code(永久授权码)
                     * 2.调用『企业授权的授权数据』接口（ServiceHelper.getAuthInfo方法），此接口返回数据具体详情请查看文档。
                     * 3.遍历从『企业授权的授权数据』接口中获取所有的微应用信息
                     * 4.对每一个微应用都调用『获取企业的应用信息接口』（ServiceHelper.getAgent方法）
                     * 5.获取『获取企业的应用信息接口』接口返回值其中的"close"参数，才能得知微应用在企业用户做了授权变更之后的状态，有三种状态码
                     * 	分别为0，1，2.含义如下：
                     *  0:禁用（例如企业用户在OA后台禁用了微应用）
                     *  1:正常 (例如企业用户在禁用之后又启用了微应用)
                     *  2:待激活 (企业已经进行了授权，但是ISV还未为企业激活应用)
                     *  再根据具体状态做具体操作。
                     */

                    break;
                case "check_create_suite_url":
                    // "check_create_suite_url"事件将在创建套件的时候推送, { "EventType":"check_create_suite_url", "Random":"brdkKLMW", "TestSuiteKey":"suite4xxxxxxxxxxxxxxx" }
                    //此事件需要返回的"Random"字段，
                    res = plainTextJson.getString("Random");
                    plainTextJson.getString("TestSuiteKey");
                    logger.info("事件将在创建套件的时候推送");
                    break;
                case "check_update_suite_url":
                    // "check_update_suite_url"事件将在更新套件的时候推送,{ "EventType":"check_update_suite_url", "Random":"Aedr5LMW", "TestSuiteKey":"suited6db0pze8yao1b1y" }
                    res = plainTextJson.getString("Random");
                    logger.info("[修改套件]------将在更新套件的时候推送");
                    break;
                case "suite_relieve":
                    // 解除套件 { "EventType":"suite_relieve",  "SuiteKey":"suited6db0pze8yao1b1y", "TimeStamp":"12351458245", "AuthCorpId":"ding4583267d28sd61" }
                    String AuthCorpId = plainTextJson.getString("AuthCorpId");
                    IsvTicketsEntity isvTicket = ticketsService.getIsvTicketByCorpId(AuthCorpId);
                    if (isvTicket != null) {
                        isvTicket.setStatus(StatusEnum.DELETE.getValue());
                        ticketsService.modifyIsvTickets(isvTicket);

                        CompanyInfoEntity companyInfoEntity = new CompanyInfoEntity();
                        companyInfoEntity.setId(isvTicket.getCompanyId());
                        companyInfoEntity.setAgentStatus(AgentStatusEnum.UNINSTALL.getValue());
                        companyInfoEntity.setDeleteTime(new Date());
                        companyInfoService.modifyCompanyInfo(companyInfoEntity);
                    }
                    logger.info(AuthCorpId + "企业停用套件并删除了授权信息.....");
                    break;
                case "org_micro_app_stop":
                    // 停用应用 {"AgentId":169293023,"AppId":4407,"AuthCorpId":"dinge1253172014accdd35c2f4657eb6378f","EventType":"org_micro_app_stop","SuiteKey":"suitez7pi8x9ud00vgzvj","TimeStamp":"1522379578749"}
                    IsvTicketsEntity isvTicketStop = ticketsService.getIsvTicketByCorpId(plainTextJson.getString("AuthCorpId"));
                    CompanyInfoEntity companyInfoEntity = new CompanyInfoEntity();
                    companyInfoEntity.setId(isvTicketStop.getCompanyId());
                    companyInfoEntity.setAgentStatus(AgentStatusEnum.STOP_USE.getValue());
                    companyInfoEntity.setStopTime(new Date());
                    companyInfoService.modifyCompanyInfo(companyInfoEntity);
                    logger.info(plainTextJson.getString("AuthCorpId") + "企业停用了套件.....");
                    break;
                case "org_micro_app_restore":
                    // 启用应用 {"AgentId":169287253,"AppId":4407,"AuthCorpId":"dinge1253172014accdd35c2f4657eb6378f","EventType":"org_micro_app_restore","SuiteKey":"suitez7pi8x9ud00vgzvj","TimeStamp":"1522381482726"}
                    IsvTicketsEntity isvTicketRestore = ticketsService.getIsvTicketByCorpId(plainTextJson.getString("AuthCorpId"));
                    CompanyInfoEntity companyInfoRestore = new CompanyInfoEntity();
                    companyInfoRestore.setId(isvTicketRestore.getCompanyId());
                    companyInfoRestore.setAgentStatus(AgentStatusEnum.USEING.getValue());
                    companyInfoService.modifyCompanyInfo(companyInfoRestore);
                    logger.info(plainTextJson.getString("AuthCorpId") + "企业启用了套件.....");
                    break;
                case "market_buy":
                    //isv企业购买
                    try {
                        synchronized (this) {
                            logger.info("企业购买Info" + plainTextJson.toString());
                            String buyCorpId = plainTextJson.getString("buyCorpId");
                            String payFee = plainTextJson.getString("payFee");
                            String template = plainTextJson.getString("itemName");
                            marketBuyService.add(plainTextJson);
                            boolean isBuy = marketBuyService.isBuy(buyCorpId);
                            if (isBuy) {
                                IsvTicketsEntity isvTicketBuy = ticketsService.getIsvTicketByCorpId(buyCorpId);
                                isvTicketBuy.setIsBuy(StatusEnum.OK.getValue());
                                ticketsService.modifyIsvTickets(isvTicketBuy);
                                String companyName = companyInfoService.getCompanyNameById(isvTicketBuy.getCompanyId());
                                String authedId = AuthHelper.getAuthInfo(suitEntiy.getSuiteTicket(), suitEntiy.getSuiteKey(), suitEntiy.getSuiteSecret(), buyCorpId);
                                String dateTime = DateUtil.getDisplayYMDHMS(new Date());
                                if (!StringUtils.isEmpty(authedId)) {
                                    CorpUserDetail corpUserDetail = DingHelper.getMemberDetail(buyCorpId, authedId);
                                    if (corpUserDetail != null) {
                                        sendRobortMessage(companyName, corpUserDetail.getName(), dateTime, template, changeF2Y(new BigDecimal(payFee)));
                                    } else {
                                        sendRobortMessage(companyName, authedId, dateTime, template, changeF2Y(new BigDecimal(payFee)));
                                    }
                                }
                            }
                        }
                    } catch (BusinessException e) {
                        logger.error("购买失败!");
                    }
                    break;
                default:
                    logger.info("------这一步不知道什么操作.....");
                    break;
            }
            //对返回信息进行加密
            long timeStampLong = System.currentTimeMillis();
            Map<String, String> jsonMap = null;
            try {
                //jsonMap是需要返回给钉钉服务器的加密数据包
                jsonMap = dingTalkEncryptor.getEncryptedMap(res, timeStampLong, nonce);
            } catch (DingTalkEncryptException e) {
                logger.error("jsonMap是需要返回给钉钉服务器的加密数据包失败:", e);
            }
            JSONObject json = new JSONObject();
            json.putAll(jsonMap);
            logger.info("最终返回 > " + json.toString());
            try {
                response.getWriter().append(json.toString());
            } catch (IOException e) {
                logger.error("response.getWriter().append(json.toString()) 失败!", e);
            }
        } catch (Exception e) {
            logger.error("doGet Exception!", e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

    private String robortOATemplate = "%s的 %s 于 %s 在购买了 %s 套餐,共 %s 元。";

    private void sendRobortMessage(String companyName, String userName, String createTime, String template, BigDecimal price) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/robot/send?access_token=de43e03cf0837f265357f163f21943766ce001c28029a500af6708b489fda4db");
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype("text");
        OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
        String robortText = String.format(robortOATemplate, companyName, userName, createTime, template, price);
        logger.info("企业购买Infodetail:" + robortText);
        text.setContent(robortText);
        request.setText(text);
        logger.info(request.toString());
        OapiRobotSendResponse response = client.execute(request);
    }

    public static BigDecimal changeF2Y(BigDecimal price) {
        return price.divide(new BigDecimal(100), 2, BigDecimal.ROUND_DOWN);
    }

}
