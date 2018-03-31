package com.yfax.webapi;

import com.yfax.utils.JsonResult;
import com.yfax.utils.NetworkUtil;
import com.yfax.utils.ResultCode;
import com.yfax.utils.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


/***
 *  【处理器拦截器】
 * */
@Component
public class HttpInterceptor
        extends HandlerInterceptorAdapter   //继承处理器拦截器适配器
{

    protected static Logger logger = LoggerFactory.getLogger(HttpInterceptor.class);

    /***
     *  预处理
     * */
    public boolean preHandle(HttpServletRequest requ,
                             HttpServletResponse resp,
                             Object wrappedHandler) throws Exception
    {
        //记录访问日志
        logAccess(requ);

        String uri = requ.getRequestURI();
        //广告平台回调不做拦截
        if (uri.startsWith(GlobalUtils.URL + GlobalUtils.PROJECT_CFDB + "/sendAdvInfo"))
        {
            return true;
        }

        //常见问题faq页不做拦截
        if (uri.equals(GlobalUtils.URL + GlobalUtils.PROJECT_CFDB + "/faq"))
        {
            return true;
        }

        //请求拦截
        if (uri.startsWith(GlobalUtils.URL + GlobalUtils.PROJECT_CFDB))
        {
            //拦截请求
            String phoneId = requ.getParameter("phoneId");
            if (!StrUtil.null2Str(phoneId)
                        .equals(""))
            {
                //登录不做拦截
                if (uri.equals(GlobalUtils.URL + GlobalUtils.PROJECT_CFDB + "/doLogin"))
                {
                    return true;
                }
                return true;
            } else
            {
                String result = new JsonResult(ResultCode.PARAMS_ERROR).toJsonString();
                this.outputResp(resp, result);
                return false;
            }
        } else
        {
            return true;
        }
    }

    /***
     *  记录访问日志
     * */
    private void logAccess(HttpServletRequest requ)
    {
        String url = requ.getRequestURL()
                         .toString();
        String method = requ.getMethod();
        String uri = requ.getRequestURI();
        String queryString = requ.getQueryString();
        String ip = NetworkUtil.getIpAddress(requ);
        String pattern="请求参数, url: %s, method: %s, uri: %s, params: %s, ip: %s";
        logger.info(String.format(pattern, url, method, uri, queryString, ip));
    }

    /**
     * 输出结果
     */
    private void outputResp(HttpServletResponse response, String result) throws Exception
    {
        response.setHeader("content-type", "text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.println(result);
    }

}