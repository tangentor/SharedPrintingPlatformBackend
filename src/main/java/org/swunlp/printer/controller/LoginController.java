package org.swunlp.printer.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.swunlp.printer.entity.User;
import org.swunlp.printer.result.ResponseResult;
import org.swunlp.printer.service.UserService;

import javax.annotation.Resource;

@RestController
@ResponseResult
public class LoginController {

	@Resource
	private UserService userService;

	@PostMapping("/login")
	public Object login(@RequestBody User user){
		return userService.login(user);
	}

}
