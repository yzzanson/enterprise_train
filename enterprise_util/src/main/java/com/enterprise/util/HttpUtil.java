package com.enterprise.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

/**
 * Http请求工具类
 */
public class HttpUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class.getCanonicalName());

    /**
     * 发送get请求
     *
     * @param url url
     * @author shisan
     * @date 2017/11/16 下午4:47
     */
    public static JSONObject get(String url) {
        return get(url, null);
    }

    /**
     * 发送get请求
     *
     * @param url url
     * @author shisan
     * @date 2017/11/16 下午4:47
     */
    public static JSONObject getNew(String url) {
        return get2(url, null);
    }

    /**
     * 发送get请求
     *
     * @param url   url
     * @param param 请求参数
     * @author shisan
     * @date 2017/11/16 下午4:47
     */
    public static JSONObject get(String url, Map<String, Object> param) {
        logger.info("发送get请求 url = " + url);
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(3000).build();
        JSONObject result = null;
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (String key : param.keySet()) {
                    builder.addParameter(key, param.get(key).toString());
                }
            }
            URI uri = builder.build();
            HttpGet httpGet = new HttpGet(uri);
            httpGet.setConfig(requestConfig);

            //执行请求
            response = httpClient.execute(httpGet, new BasicHttpContext());
            //处理响应码
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    result = JSON.parseObject(EntityUtils.toString(entity, "utf-8"));
                    logger.info("响应结果 value=" + result.toString());
                }
            }
        } catch (Exception e) {
            logger.error("http 请求发送失败, url=" + url, e);
        } finally {
            try {
                httpClient.close();
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                logger.error("关流失败!", e);
            }
        }
        Assert.isTrue(result != null && result.getInteger("errcode") == 0, result.toString());
        return result;

    }


    /**
     * 发送get请求
     *
     * @param url   url
     * @param param 请求参数
     * @author shisan
     * @date 2017/11/16 下午4:47
     */
    public static JSONObject get2(String url, Map<String, Object> param) {
        logger.info("发送get请求 url = " + url);
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(3000).setConnectTimeout(3000).build();
        JSONObject result = null;
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (String key : param.keySet()) {
                    builder.addParameter(key, param.get(key).toString());
                }
            }
            URI uri = builder.build();
            HttpGet httpGet = new HttpGet(uri);
            httpGet.setConfig(requestConfig);

            //执行请求
            response = httpClient.execute(httpGet, new BasicHttpContext());
            //处理响应码
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    result = JSON.parseObject(EntityUtils.toString(entity, "utf-8"));
                    logger.info("响应结果 value=" + result.toString());
                }
            }
        } catch (Exception e) {
            logger.error("http 请求发送失败, url=" + url, e);
        } finally {
            try {
                httpClient.close();
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                logger.error("关流失败!", e);
            }
        }
        return result;

    }

    /**
     * 发送post 请求
     *
     * @param url   url
     * @param param 参数
     * @author shisan
     * @date 2017/11/16 下午5:25
     */
    public static JSONObject doPost(String url, Map<String, Object> param) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        JSONObject resultString = null;
        // 创建Http Post请求
        HttpPost httpPost = new HttpPost(url);
        if (param != null) {
            StringEntity entity = new StringEntity(JSON.toJSONString(param), "utf-8");
            httpPost.setEntity(entity);
        }
        // 执行http请求
        try {
            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                resultString = JSONObject.parseObject(EntityUtils.toString(response.getEntity(), "utf-8"));
                logger.info("url = " + url + ",请求结果:" + resultString);
            }
        } catch (IOException e) {
            logger.error("发送Post 请求失败! url = " + url + ", result=" + resultString, e);
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                logger.error("关流失败! ", e);
            }
        }
        Assert.isTrue(resultString != null && resultString.getString("errcode").equals("0"), "请求失败! 返回结果:" + resultString);
        return resultString;
    }

    /**
     * 发送post 请求
     *
     * @param url url
     * @author shisan
     * @date 2017/11/16 下午5:25
     */
    public static JSONObject doPost(String url) {
        return doPost(url, null);
    }

    public static JSONObject httpPost(String url, Object data) {
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom().
                setSocketTimeout(2000).setConnectTimeout(2000).build();
        httpPost.setConfig(requestConfig);
        httpPost.addHeader("Content-Type", "application/json");

        try {
            StringEntity requestEntity = new StringEntity(JSON.toJSONString(data), "utf-8");
            httpPost.setEntity(requestEntity);

            response = httpClient.execute(httpPost, new BasicHttpContext());

            if (response.getStatusLine().getStatusCode() != 200) {
                logger.error("request url failed, http code=" + response.getStatusLine().getStatusCode()
                        + ", url=" + url);
                return null;
            }
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String resultStr = EntityUtils.toString(entity, "utf-8");

                JSONObject result = JSON.parseObject(resultStr);
                logger.info("执行结果：" + result.toJSONString());
                //errcode = 0 表示成功
                if (result.getInteger("errcode") == 0) {
                    return result;
                } else {
                    logger.error("request url=" + url + ",resultStr = " + resultStr);
                    return result;
                }
            }
        } catch (IOException e) {
            logger.error("request url=" + url + ", exception:", e);
        } finally {
            if (response != null) try {
                response.close();
            } catch (IOException e) {
                logger.error("关流失败, IOException:", e);
            }
        }
        return null;
    }


    public static void main(String[] args) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("a", "a");
        map.put("b", "b");

        JSONObject json = doPost("http://api.weixin.qq.com/cgi-bin/media/get", map);
        System.out.println(json);
    }

}
