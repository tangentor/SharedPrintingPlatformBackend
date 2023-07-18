package org.swunlp.printer.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.swunlp.printer.annotation.ResponseResult;
import org.swunlp.printer.entity.User;
import org.swunlp.printer.service.UserService;

@RestController
@RequestMapping("/user")
@ResponseResult
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/update")
    public boolean update(@RequestBody User user){
        //更新用户信息
        return userService.updateUser(user);
    }

    @PostMapping("/update/avatar")
    public boolean update(MultipartFile file){
        //更新用户信息
        return userService.updateAvatar(file);
    }

}
