import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.entity.MarketBuyEntity;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/12/28 下午7:01
 */
public class JSONObjectTest {

    @Test
    public void parseMarketBuy(){
        String jsonMarketBuyJSON = "{\n" +
                "    \"EventType\": \"market_buy\",\n" +
                "    \"SuiteKey\": \"suited6db0pze8yao1b1y\",\n" +
                "    \"buyCorpId\": \"dingxxxxxxxx\",\n" +
                "    \"goodsCode\": \"FW_GOODS-xxxxxxxx\",\n" +
                "    \"itemCode\": \"1c5f70cf04c437fb9aa1b20xxxxxxxx\",\n" +
                "    \"itemName\": \"按照范围收费规格0-300\",\n" +
                "    \"subQuantity\": 1（订购的具体人数）,           \n" +
                "    \"maxOfPeople\": 300,\n" +
                "    \"minOfPeople\": 0,\n" +
                "    \"orderId\": 308356401xxxxxxxx,\n" +
                "    \"paidtime\": 1474535702000,\n" +
                "    \"serviceStopTime\": 1477065600000,\n" +
                "    \"payFee\":147600,\n" +
                "    \"orderCreateSource\":\"DRP\",\n" +
                "    \"nominalPayFee\":147600,\n" +
                "    \"discountFee\":600,\n" +
                "    \"discount\":0.06,\n" +
                "    \"distributorCorpId\":\"ding9f50b15bccd16741\",\n" +
                "   \t\"distributorCorpName\":\"测试企业\"\n" +
                "}";

        System.out.println(jsonMarketBuyJSON);

        MarketBuyEntity markeetBuy = JSONObject.parseObject(jsonMarketBuyJSON, MarketBuyEntity.class);
        MarketBuyEntity marketBuyEntityForSave = new MarketBuyEntity();
        try {
            BeanUtils.copyProperties(marketBuyEntityForSave, markeetBuy);
            System.out.println(marketBuyEntityForSave.toString());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void parseIsvAuthCorp(){
        String authCorpInfo = "{\n" +
                "   \"auth_corp_info\":{\n" +
                "\t  \"corp_logo_url\":\"http://xxxx.png\",\n" +
                "\t  \"corp_name\":\"corpid\",\n" +
                "\t  \"corpid\":\"auth_corpid_value\",\n" +
                "\t  \"industry\":\"互联网\",\n" +
                "\t  \"invite_code\" : \"1001\",\n" +
                "\t  \"license_code\": \"xxxxx\",\n" +
                "\t  \"auth_channel\": \"xxxxx\",\n" +
                "\t  \"auth_channel_type\": \"xxxxx\",\n" +
                "\t  \"is_authenticated\":false,\n" +
                "\t  \"auth_level\":0,\n" +
                "\t  \"invite_url\":\"https://yfm.dingtalk.com/invite/index?code=xxxx\"\n" +
                "\t},\n" +
                "\t\"auth_user_info\":\n" +
                "    {\n" +
                "    \t\"userId\":\"sxsxsxsx\"\n" +
                "\t},\n" +
                "    \"auth_info\":{\n" +
                "\t\"agent\":[{\n" +
                "\t\t\t\"agent_name\":\"aaaa\",\n" +
                "\t\t\t\"agentid\":1,\n" +
                "\t\t\t\"appid\":-3,\n" +
                "\t\t\t\"logo_url\":\"http://aaaaaa.com\",\n" +
                "\t\t\t\"admin_list\":[\"zhangsan\",\"lisi\"]\n" +
                "\t}\n" +
                "\t,{\n" +
                "\t\t\t\"agent_name\":\"bbbb\",\n" +
                "\t\t\t\"agentid\":4,\n" +
                "\t\t\t\"appid\":-2,\n" +
                "\t\t\t\"logo_url\":\"http://vvvvvv.com\",\n" +
                "\t\t\t\"admin_list\":[]\n" +
                "\t}]\n" +
                "\t},\n" +
                "        \"channel_auth_info\": {\n" +
                "\t\t\"channelAgent\": [\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"agent_name\": \"应用1\",\n" +
                "\t\t\t\t\t\"agentid\": 36,\n" +
                "\t\t\t\t\t\"appid\": 6,\n" +
                "\t\t\t\t\t\"logo_url\": \"http://i01.lw.test.aliimg.com/media/lALOAFWTc8zIzMg_200_200.png\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"agent_name\": \"应用2\",\n" +
                "\t\t\t\t\t\"agentid\": 35,\n" +
                "\t\t\t\t\t\"appid\": 7,\n" +
                "\t\t\t\t\t\"logo_url\": \"http://i01.lw.test.aliimg.com/media/lALOAFWTc8zIzMg_200_200.png\"\n" +
                "\t\t\t\t}]\n" +
                "\t\t},\n" +
                "\t \"errcode\":0,\n" +
                "\t\"errmsg\":\"ok\"\n" +
                "}";

        System.out.println(authCorpInfo);
        JSONObject jsonObject = JSONObject.parseObject(authCorpInfo);
        Object userDingId = JSONObject.parseObject(jsonObject.get("auth_user_info").toString()).get("userId");
        System.out.println(userDingId);
    }
}
