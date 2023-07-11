package org.swunlp.printer.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.swunlp.printer.result.Result;
import org.swunlp.printer.service.UploadService;

import javax.annotation.Resource;

@RestController
@RequestMapping("upload")
public class UploadController {
    @Resource
    private UploadService uploadService;


    @PostMapping("/pdf")
    public Result upload(MultipartFile file) {
        // 判断是否为图片且大小小于4M
        if (!file.getContentType().endsWith("pdf")) {
            return Result.error("请上传PDF");
        }
        if (file.getSize() > 4 * 1024 * 1024) {
            return Result.error("图片大小不能超过4M");
        }
        return uploadService.parsingPDF(file);
    }

}
