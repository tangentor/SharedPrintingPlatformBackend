package org.swunlp.printer.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

/**
 * 功能描述:
 *	JSON操作
 * @author TangXi
 * @date 2021/10/29 下午10:38
 */
public class JSONUtil {

	private static final ObjectMapper op = new ObjectMapper();

	private JSONUtil(){}

	@SneakyThrows
	public static String toJSON(Object object){
		return op.writeValueAsString(object);
	}

	@SneakyThrows
	public static<T> T toObject(String json, Class<T> valueType){
		return op.readValue(json, valueType);
	}
	@SneakyThrows
	public static<T> T toObject(Object obj, Class<T> valueType){
		// 此时Object为LinkedHashMap
		if(obj instanceof String){
			return toObject(obj.toString(), valueType);
		} else {
			return toObject(toJSON(obj),valueType);
		}
	}
}

