package org.swunlp.printer.interceptor;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.swunlp.printer.annotation.NotRequireLogin;
import org.swunlp.printer.util.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class UserLoginInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler){
		//判断是否使用注解不需要登录
		if(CommUtil.isAnnotated(handler, NotRequireLogin.class)){
			return true;
		}
		//拦截器取到请求先进行判断，如果是OPTIONS请求，则放行
		if("OPTIONS".equalsIgnoreCase(request.getMethod())) {
			return true;
		}
		//检查token
		String token = request.getHeader("token");
		if(token == null || token.equals("")){
			//尝试cookie中获取
			Cookie[] cookies = request.getCookies();
			if(cookies!=null){
				for(Cookie cookie:cookies){
					if("token".equals(cookie.getName())){
						token = cookie.getValue();
					}
				}
			}
			if(token == null || token.equals("")){
				HttpUtil.writeUtf8Message(response,"未登录");
				return false;
			}
		}
		try {
			String subject = JwtUtil.parseJWT(token).getSubject();
			if(subject==null){
				HttpUtil.writeUtf8Message(response,"登陆信息无效");
			}
			//保存到ThreadLocal
			UsernameUtil.setLoginUser(subject,false);
			TokenUtil.setToken(token);
		} catch (Exception e) {
			HttpUtil.writeUtf8Message(response,"登陆信息无效");
		}
		return true;
	}
}
