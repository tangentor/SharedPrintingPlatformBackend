package org.swunlp.printer.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.swunlp.printer.result.ResponseResult;

import java.util.Random;

@RestController
@ResponseResult
public class TestController {

    @RequestMapping("test")
    public String test(){
        int i = new Random().nextInt(10);
        if(i>5){
            return "生成的是"+i;
        } else {
            throw new RuntimeException("生成的值不对hhh");
        }
    }
}
