package org.swunlp.printer.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.swunlp.printer.other.BusinessException;
import org.swunlp.printer.result.Result;

@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(RuntimeException.class)
	public Result<?> handleRuntimeException(Exception e){
		log.warn(e.getMessage());
		e.printStackTrace();
		return Result.error(e.getMessage());
	}

	@ExceptionHandler(BusinessException.class)
	public Result<?> handleBusinessException(BusinessException e) {
		log.warn(e.getMessage());
		// 创建一个标准的 Result 对象，并设置异常消息作为错误提示
		return Result.error(e.getMessage());
	}

}
