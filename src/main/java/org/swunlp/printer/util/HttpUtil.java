package org.swunlp.printer.util;

import lombok.SneakyThrows;

import javax.servlet.http.HttpServletResponse;

public class HttpUtil {

	@SneakyThrows
	public static void writeUtf8Message(HttpServletResponse response, String message) {
		response.setContentType("text/plain; charset=utf-8");
		response.setStatus(401);
		response.getWriter().write(message);
	}
}
