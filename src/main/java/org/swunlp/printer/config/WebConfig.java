package org.swunlp.printer.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.swunlp.printer.interceptor.ResponseResultInterceptor;
import org.swunlp.printer.interceptor.UserLoginInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	private final ResponseResultInterceptor responseResultInterceptor;

	private final UserLoginInterceptor userLoginInterceptor;

	public WebConfig(ResponseResultInterceptor responseResultInterceptor, UserLoginInterceptor userLoginInterceptor) {
		this.responseResultInterceptor = responseResultInterceptor;
		this.userLoginInterceptor = userLoginInterceptor;
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		// 设置允许跨域的路径
		registry.addMapping("/**")
				// 设置允许跨域请求的域名
				.allowedOrigins("*")
				// 是否允许cookie
//				.allowCredentials(true)
				// 设置允许的请求方式
				.allowedMethods("*")
				// 设置允许的header属性
				.allowedHeaders("*")
				// 跨域允许时间
				.maxAge(3600);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		WebMvcConfigurer.super.addInterceptors(registry);
		// 统一处理响应结果
		registry.addInterceptor(responseResultInterceptor);

		registry.addInterceptor(userLoginInterceptor)
				.excludePathPatterns("/device/register");
	}

}
