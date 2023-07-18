package org.swunlp.printer.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.swunlp.printer.annotation.ResponseResult;
import org.swunlp.printer.service.PreviewService;

import javax.annotation.Resource;
import java.util.List;

@RestController
@ResponseResult
@RequestMapping("preview")
public class PreviewController {

    @Resource
    private PreviewService previewService;

    @GetMapping("/{md5}")
    public List<String> getPreViewImages(@PathVariable String md5){
        return previewService.getPreviews(md5);
    }
}
