package org.swunlp.printer.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.swunlp.printer.interceptor.ResponseResultInterceptor;
import org.swunlp.printer.interceptor.UserLoginInterceptor;
import org.swunlp.printer.util.TempPathUtil;

import javax.annotation.Resource;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Resource
	private ResponseResultInterceptor responseResultInterceptor;

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

		registry.addInterceptor(new UserLoginInterceptor())
                .excludePathPatterns("/files/**")
				.excludePathPatterns("/test")
				.excludePathPatterns("/device/register");
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String storePath = TempPathUtil.getStorePath();
		//将当前项目的files目录进行映射
		registry.addResourceHandler("/files/**").addResourceLocations("file:"+storePath+"/");
		WebMvcConfigurer.super.addResourceHandlers(registry);
	}
}
