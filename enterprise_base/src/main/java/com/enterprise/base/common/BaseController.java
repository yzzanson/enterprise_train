package com.enterprise.base.common;

import com.enterprise.base.exceptions.BusinessException;
import com.enterprise.base.exceptions.LoginException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * BaseController 异常捕获,需要时 在controller层继承该类即可
 *
 * @author shisan
 * @create 2017-08-30 上午11:18
 **/
public class BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(value = Exception.class)
    public Object defaultErrorHandler(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        try {
            // 非业务异常 往日志中记录
            if (exception instanceof LoginException) {
                logger.info("未登录");
                out(response, ResultJson.hasLogin(exception.getMessage()).toString());
            } else if (exception instanceof BusinessException) {
                logger.error("全局业务异常捕获:" + exception.getMessage());
                out(response, ResultJson.errorResultJson(exception.getMessage()).toString());
            } else {
                logger.error("全局系统异常捕获 Exception:", exception);
                out(response, ResultJson.errorResultJson("系统异常!," + exception.getMessage()).toString());
            }
        } catch (Exception e) {
            logger.error("Exception", e);
        }
        return null;
    }

    /**
     * response 输出JSON
     *
     * @param response
     * @param resultMap
     */
    private void out(ServletResponse response, Object resultMap) {
        //PrintWriter out = null;
        OutputStream out = null;
        try {
            response.setContentType("text/html;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            //out = response.getWriter();
            //out.println(resultMap);
            out = response.getOutputStream();
            out.write(resultMap.toString().getBytes("UTF-8"));
            out.flush();
            return;
        } catch (Exception e) {
            logger.error("输出JSON报错。", e);
        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    logger.error("OutputStream 关流异常:", e);
                }
            }
        }
    }

}
