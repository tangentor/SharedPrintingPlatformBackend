package org.swunlp.printer.controller;

import org.springframework.web.bind.annotation.*;
import org.swunlp.printer.entity.PrintInfo;
import org.swunlp.printer.result.ResponseResult;
import org.swunlp.printer.service.PDFParingService;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@ResponseResult
@RequestMapping("preview")
public class PreviewController {

    @Resource
    private PDFParingService pdfParingService;

    @GetMapping("/{md5}")
    public Map<String,Object> getPreViewImages(@PathVariable String md5){
        return pdfParingService.getPreviews(md5);
    }

    @PostMapping("/print")
    public Object print(@RequestBody PrintInfo printInfo){
        return pdfParingService.print(printInfo);
    }
}
