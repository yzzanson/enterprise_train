package com.enterprise.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.SortedMap;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/12/4 下午5:49
 */
public class SignUtil {

    private static final Logger logger = LoggerFactory.getLogger(SignUtil.class);

    /**
     * @param params 所有的请求参数都会在这里进行排序加密
     * @return 得到签名
     */
    public static String getSign(SortedMap<String, String> params) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry entry : params.entrySet()) {
            if (!entry.getKey().equals("sign")) { //拼装参数,排除sign
                if (!StringUtils.isEmpty(entry.getKey()) && !StringUtils.isEmpty(entry.getValue()))
                    sb.append(entry.getKey()).append(entry.getValue());
            }
        }
        logger.info("Before Sign : {}", sb.toString());
        return DigestUtils.md5Hex(sb.toString()).toUpperCase();
    }

    /**
     * @param params 所有的请求参数都会在这里进行排序加密
     * @return 验证签名结果
     */
    public static boolean verifySign(SortedMap<String, String> params) {
        if (params == null || StringUtils.isEmpty(params.get("sign"))) return false;
        String sign = getSign(params);
        logger.info("verify Sign : {}", sign);
        return !StringUtils.isEmpty(sign) && params.get("sign").equals(sign);
    }

    public static void main(String[] args) {
//        SortedMap<String,String> resultMap = new TreeMap<>();
////        resultMap.put("sign", "sss");
//        resultMap.put("userid", "49");
//        resultMap.put("name", "anson");
//
//        String sign = getSign(resultMap);
////        Iterator itor = resultMap.keySet().iterator();
////        while(itor.hasNext()){
////            String key = (String) itor.next();
////            System.out.println(key+":"+ resultMap.get(key));
////        }
////       String sign =  getSign(resultMap);
//        System.out.println(sign);

    }


}
