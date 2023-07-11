package org.swunlp.printer.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.swunlp.printer.result.Result;

@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

//	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//	@ExceptionHandler(RuntimeException.class)
//	public Result runtimeExceptionHandler(Exception e){
//		log.error(e.getMessage());
//		return Result.error(e.getMessage());
//	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(RuntimeException.class)
	public Result exceptionHandler(Exception e){
		log.error(e.getMessage());
		return Result.error(e.getMessage());
	}
}
