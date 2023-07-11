package org.swunlp.printer.controller;

import org.springframework.web.bind.annotation.*;
import org.swunlp.printer.entity.PrintServiceInfo;
import org.swunlp.printer.result.ResponseResult;
import org.swunlp.printer.service.PrintService;
import org.swunlp.printer.service.PrintServiceService;

import javax.annotation.Resource;
import java.util.List;

@RestController
@ResponseResult
@RequestMapping("device")
public class PrinterServiceController {

    @Resource
    private PrintServiceService printServiceService;

    @PostMapping("register")
    public String register(@RequestBody PrintServiceInfo printServiceInfo){
        return printServiceService.register(printServiceInfo);
    }

    @GetMapping("list")
    public List<PrintServiceInfo> list(){
        return printServiceService.listAll();
    }

    @RequestMapping("hello")
    public String hello(){
        return "hello";
    }
}
