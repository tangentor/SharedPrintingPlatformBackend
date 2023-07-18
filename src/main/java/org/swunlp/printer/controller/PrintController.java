package org.swunlp.printer.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.swunlp.printer.entity.PrintInfo;
import org.swunlp.printer.annotation.ResponseResult;
import org.swunlp.printer.service.PrintService;

import javax.annotation.Resource;

@RestController
@ResponseResult
@RequestMapping("print")
public class PrintController {

    @Resource
    private PrintService printService;

    @PostMapping("")
    public Object print(@RequestBody PrintInfo printInfo){
        return printService.print(printInfo);
    }
}
