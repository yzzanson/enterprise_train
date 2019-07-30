package com.enterprise.mobile.web.filter;

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

    public final static String a = "a.taofairy.com";
    public final static String b = "b.taofairy.com";
    public final static String c = "s.taofairy.com";
    public final static String d = "yfl.taofairy.com";
    public final static String e = "yfl2.taofairy.com";
    public final static String f = "neixun.forwe.store";
    public final static String g = "neixun.forwe.store";
    public final static String h = "localhost";

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request =(HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;


        String curOrigin = request.getHeader("Origin");
        //String curOrigin = ((HttpServletRequest) req).getRequestURL().toString();
        if(curOrigin!=null){
            String head="https://";
            String headHttp="http://";
            if(curOrigin.contains(a)){
                response.setHeader("Access-Control-Allow-Origin", head+a +"," + headHttp+a);
            }else if (curOrigin.contains(b)){
                response.setHeader("Access-Control-Allow-Origin", head+b +"," + headHttp+b);
            }else if (curOrigin.contains(c)){
                response.setHeader("Access-Control-Allow-Origin", head+c +"," + headHttp+c);
            }else if (curOrigin.contains(d)){
                response.setHeader("Access-Control-Allow-Origin", head+d +"," + headHttp+d);
            }else if (curOrigin.contains(e)){
                response.setHeader("Access-Control-Allow-Origin", head+e +"," + headHttp+e);
            }else if (curOrigin.contains(f)){
                response.setHeader("Access-Control-Allow-Origin", head+f +"," + headHttp+f);
            }else if (curOrigin.contains(g)){
                response.setHeader("Access-Control-Allow-Origin", head+g +"," + headHttp+g);
            }
        }
//        else{
//            response.setHeader("Access-Control-Allow-Origin", "http://localhost:8080");
//        }
        if(org.apache.commons.lang3.StringUtils.isBlank(request.getHeader("sslheader"))){
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
