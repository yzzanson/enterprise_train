import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.CorpMessageCorpconversationAsyncsendRequest;
import com.dingtalk.api.response.CorpMessageCorpconversationAsyncsendResponse;
import com.taobao.api.ApiException;
import org.junit.Test;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/5/21 上午11:39
 */
public class DingTest {

    String access_token = "6b98fac5f89e3f94a66b61213642c025";

    @Test
    public void testSendOA(){
        DingTalkClient client = new DefaultDingTalkClient("https://eco.taobao.com/router/rest");
        CorpMessageCorpconversationAsyncsendRequest req = new CorpMessageCorpconversationAsyncsendRequest();
        req.setMsgtype("oa");
        req.setAgentId(176042971L);
//        req.setUseridList("yangzezhou");
//        req.setDeptIdList("123,456");
        req.setToAllUser(true);
        req.setMsgcontentString("{\"message_url\": \"http://dingtalk.com\",\"head\": {\"bgcolor\": \"FFBBBBBB\",\"text\": \"头部标题\"},\"body\": {\"title\": \"正文标题\",\"form\": [{\"key\": \"姓名:\",\"value\": \"张三\"},{\"key\": \"爱好:\",\"value\": \"打球、听音乐\"}],\"rich\": {\"num\": \"15.6\",\"unit\": \"元\"},\"content\": \"大段文本大段文本大段文本大段文本大段文本大段文本大段文本大段文本大段文本大段文本大段文本大段文本\",\"image\": \"@lADOADmaWMzazQKA\",\"file_count\": \"3\",\"author\": \"李四 \"}}");
        try {
            CorpMessageCorpconversationAsyncsendResponse rsp = client.execute(req, access_token);
            System.out.println(rsp.getResult());
            System.out.println(rsp.getBody());
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void sendAsyncMessage(){
        DingTalkClient client = new DefaultDingTalkClient("https://eco.taobao.com/router/rest");
        CorpMessageCorpconversationAsyncsendRequest req = new CorpMessageCorpconversationAsyncsendRequest();
        req.setMsgtype("oa");
        req.setAgentId(179372602L);
//        req.setUseridList("zhangsan,lisi");
//        req.setDeptIdList("123,456");
//        req.setToAllUser(false);
        req.setToAllUser(true);
        req.setMsgcontentString("{\"message_url\": \"http://dingtalk.com\",\"head\": {\"bgcolor\": \"FFBBBBBB\",\"text\": \"头部标题\"},\"body\": {\"title\": \"正文标题\",\"form\": [{\"key\": \"姓名:\",\"value\": \"张三\"},{\"key\": \"爱好:\",\"value\": \"打球、听音乐\"}],\"rich\": {\"num\": \"15.6\",\"unit\": \"元\"},\"content\": \"大段文本大段文本大段文本大段文本大段文本大段文本大段文本大段文本大段文本大段文本大段文本大段文本\",\"image\": \"@lADOADmaWMzazQKA\",\"file_count\": \"3\",\"author\": \"李四 \"}}");
        CorpMessageCorpconversationAsyncsendResponse rsp = null;
        try {
            rsp = client.execute(req, access_token);
            System.out.println(rsp.getBody());
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        new Thread(new Thread1()).run();
       // new Thread(new Thread2()).start();

    }



}
