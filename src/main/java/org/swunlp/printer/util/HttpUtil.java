package org.swunlp.printer.util;

import lombok.SneakyThrows;
import org.springframework.web.util.UriUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {

	@SneakyThrows
	public static void writeUtf8Message(HttpServletResponse response, String message) {
		response.setContentType("text/plain; charset=utf-8");
		response.setStatus(401);
		response.getWriter().write(message);
	}

	public static InputStream getInputStreamFromURL(String urlString) throws IOException {
		String encodedUrl = UriUtils.encodePath(urlString, "UTF-8");
		URL url = new URL(encodedUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		return connection.getInputStream();
	}
}
