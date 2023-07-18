package org.swunlp.printer.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.swunlp.printer.annotation.ResponseResult;
import org.swunlp.printer.util.CommUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class ResponseResultInterceptor implements HandlerInterceptor {

	// 标记
	private static final String RESPONSE_RESULT_ANN = "RESPONSE_RESULT_ANN";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// 判断请求的方法
		if(CommUtil.isAnnotated(handler, ResponseResult.class)){
			//设置的值可以进行约定，这是满足不为null即可
			request.setAttribute(RESPONSE_RESULT_ANN,"YES!");
		}
		return true;
	}
}
