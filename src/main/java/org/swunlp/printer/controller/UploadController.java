package org.swunlp.printer.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.swunlp.printer.annotation.ResponseResult;
import org.swunlp.printer.entity.UploadFile;
import org.swunlp.printer.entity.function.Result;
import org.swunlp.printer.other.BusinessException;
import org.swunlp.printer.service.UploadService;
import org.swunlp.printer.util.ConversionTypeUtil;

import java.util.List;

/**
 * 用户文件上传后，在这里进行接收以及后续处理
 */
@RestController
@ResponseResult
@RequestMapping("upload")
public class UploadController {

    private final UploadService uploadService;

    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }


    /**
     * 控制上传的文件大小小于20M
     * 上传的文件必需是支持的类型
     * @param file
     * @return
     */
    @PostMapping("")
    public Object upload(MultipartFile file) {
        // 判断是否为图片且大小小于20M
        if (file.getSize() > 20 * 1024 * 1024) {
            return Result.error("上传大小不能超过10M");
        }
        String type = file.getContentType();
        if (ConversionTypeUtil.isKey(type)) {
            throw new BusinessException("类型："+type+" 不受支持");
        }
        return uploadService.upload(file);
    }

    @GetMapping("support")
    public List<String> support() {
        return ConversionTypeUtil.allSupportTypes();
    }

    @GetMapping("/list/user")
    public List<UploadFile> listUserUploadRecord(){
        return uploadService.listUserRecord();
    }


}
