package com.enterprise.filter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * CORS过滤器
 * Created by Saber on 16/4/25.
 */
@Component
public class SimpleCORSFilter implements Filter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public final static String a = "a.taofairy.com";
    public final static String b = "b.taofairy.com";
    public final static String c = "s.taofairy.com";
    public final static String d = "yfl.taofairy.com";
    public final static String e = "yfl2.taofairy.com";
    public final static String f = "neixuntest.forwe.store";
    public final static String g = "neixun.forwe.store";

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request =(HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

//        Enumeration ee =request.getHeaderNames();                //通过枚举类型获取请求文件的头部信息集
//        //遍历头部信息集
//        while(ee.hasMoreElements()){
//        //取出信息名
//            String name=(String)ee.nextElement();
//        //取出信息值
//            String value=request.getHeader(name);
//            System.out.println(name + "=" + value);
//        }

        String curOrigin = request.getHeader("Origin");
        logger.info("curOrigin:"+curOrigin);
        //String curOrigin = ((HttpServletRequest) req).getRequestURL().toString();
        if(curOrigin!=null && curOrigin.contains("https")){
            String head="https://";
            if(curOrigin.contains(a)){
                response.setHeader("Access-Control-Allow-Origin", head+a);
            }else if (curOrigin.contains(b)){
                response.setHeader("Access-Control-Allow-Origin", head+b);
            }else if (curOrigin.contains(c)){
                response.setHeader("Access-Control-Allow-Origin", head+c);
            }else if (curOrigin.contains(d)){
                response.setHeader("Access-Control-Allow-Origin", head+d);
            }else if (curOrigin.contains(e)){
                response.setHeader("Access-Control-Allow-Origin", head+e);
            }else if (curOrigin.contains(f)){
                response.setHeader("Access-Control-Allow-Origin", head+f);
            }else if (curOrigin.contains(g)){
                response.setHeader("Access-Control-Allow-Origin", head+g);
            }
        }else{
            String head="http://";
            if(StringUtils.isNotEmpty(curOrigin)) {
                if (curOrigin.contains(a)) {
                    response.setHeader("Access-Control-Allow-Origin", head + a);
                } else if (curOrigin.contains(b)) {
                    response.setHeader("Access-Control-Allow-Origin", head + b);
                } else if (curOrigin.contains(c)) {
                    response.setHeader("Access-Control-Allow-Origin", head + c);
                } else if (curOrigin.contains(d)) {
                    response.setHeader("Access-Control-Allow-Origin", head + d);
                } else if (curOrigin.contains(e)) {
                    response.setHeader("Access-Control-Allow-Origin", head + e);
                }else if (curOrigin.contains(f)){
                    response.setHeader("Access-Control-Allow-Origin", head + f);
                }else if (curOrigin.contains(g)){
                    response.setHeader("Access-Control-Allow-Origin", head + g);
                }
            }
        }
        if(StringUtils.isBlank(request.getHeader("sslheader"))){
            response.setHeader("sslheader","99");
        }else {
            response.setHeader("sslheader",request.getHeader("sslheader"));
        }
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
        chain.doFilter(req, res);
    }

    public void init(FilterConfig filterConfig) {}

    public void destroy() {}

    public static void main(String[] args) {
        String a = "http://b.taofairy.com";
        String b = "http://b.taofairy.com:9988/wangbaDictionaryapi/getNameForEdit.json";
        System.out.println(a.matches(b));
        System.out.println(b.contains(a));
    }
}
