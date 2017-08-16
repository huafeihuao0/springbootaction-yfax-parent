package com.yfax.webapi;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.yfax.webapi.utils.JsonResult;
import com.yfax.webapi.utils.ResultCode;
import com.yfax.webapi.utils.StrUtil;

/**
 * 拦截处理类
 * 
 * @author Minbo.He
 */
@Component
public class HttpInterceptor extends HandlerInterceptorAdapter {
	
	protected static Logger logger = LoggerFactory.getLogger(HttpInterceptor.class);

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String url = request.getRequestURL().toString();
		String method = request.getMethod();
		String uri = request.getRequestURI();
		String queryString = request.getQueryString();
		logger.info(String.format("请求参数, url: %s, method: %s, uri: %s, params: %s", url, method, uri, queryString));
		
        //常见问题faq页不做拦截
        if(uri.equals(GlobalUtils.URL + GlobalUtils.PROJECT_QMTT + "/faq")){
            return true;
        }
		
        //请求拦截
        if(uri.startsWith(GlobalUtils.URL + GlobalUtils.PROJECT_QMTT)) {
	        	//拦截请求
	    		String phoneNum = request.getParameter("phoneNum");
	    		if (!StrUtil.null2Str(phoneNum).equals("")) {
	    			//注册和登录不做拦截
	    	        if(uri.equals(GlobalUtils.URL + GlobalUtils.PROJECT_QMTT + "/doRegister") 
	    	        		|| uri.equals(GlobalUtils.URL + GlobalUtils.PROJECT_QMTT + "/doLogin")){
	    	            return true;
	    	        }
	    			return true;
	    		} else {
	    			String result = new JsonResult(ResultCode.PARAMS_ERROR).toJsonString();
	    			this.output(response, result);
	    			return false;
	    		}
        }else {
        		return true;
        }
	}
	
	/**
	 * 输出结果
	 */
	private void output(HttpServletResponse response, String result) throws Exception{
		response.setHeader("content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.println(result);
	}
	
}