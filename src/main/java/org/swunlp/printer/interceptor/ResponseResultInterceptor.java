package org.swunlp.printer.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.swunlp.printer.result.ResponseResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
public class ResponseResultInterceptor implements HandlerInterceptor {

	// 标记
	private static final String RESPONSE_RESULT_ANN = "RESPONSE_RESULT_ANN";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// 判断请求的方法
		if(handler instanceof HandlerMethod) {
			final HandlerMethod handlerMethod = (HandlerMethod) handler;
			final Class<?> beanType = handlerMethod.getBeanType();
			final Method method = handlerMethod.getMethod();
			// 判断是否加指定注解
			if(beanType.isAnnotationPresent(ResponseResult.class)) {
				request.setAttribute(RESPONSE_RESULT_ANN,beanType.getAnnotation(ResponseResult.class));
			} else if(method.isAnnotationPresent(ResponseResult.class)) {
				request.setAttribute(RESPONSE_RESULT_ANN,method.getAnnotation(ResponseResult.class));
			}
		}
		return true;
	}
}
