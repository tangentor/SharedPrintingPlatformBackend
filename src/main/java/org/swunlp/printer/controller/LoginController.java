package org.swunlp.printer.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.swunlp.printer.annotation.NotRequireLogin;
import org.swunlp.printer.entity.User;
import org.swunlp.printer.annotation.ResponseResult;
import org.swunlp.printer.service.UserService;

@RestController
@ResponseResult
@NotRequireLogin
public class LoginController {

	private final UserService userService;

	public LoginController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/login")
	public Object login(@RequestBody User user){
		return userService.login(user);
	}


	@PostMapping("/register")
	public Object register(@RequestBody User user){
		return userService.register(user);
	}
}
