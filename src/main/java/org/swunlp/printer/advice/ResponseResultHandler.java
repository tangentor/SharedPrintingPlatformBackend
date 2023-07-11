package org.swunlp.printer.advice;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.swunlp.printer.result.ResponseResult;
import org.swunlp.printer.result.Result;
import org.swunlp.printer.util.JSONUtil;

@ControllerAdvice
public class ResponseResultHandler implements ResponseBodyAdvice<Object> {

	private static final String RESPONSE_RESULT_ANN = "RESPONSE_RESULT_ANN";

	/**
	 * 是否包含了预定义的注解，没有就直接返回
	 * @param returnType
	 * @param converterType
	 * @return
	 */
	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		ResponseResult attribute = (ResponseResult)requestAttributes.getRequest().getAttribute(RESPONSE_RESULT_ANN);
		return !(attribute == null);
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
		if(body instanceof String){
			return JSONUtil.toJSON(Result.adjust(body));
		}
		return Result.adjust(body);
	}
}
