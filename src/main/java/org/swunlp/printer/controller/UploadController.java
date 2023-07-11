package org.swunlp.printer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.swunlp.printer.other.BusinessException;
import org.swunlp.printer.result.ResponseResult;
import org.swunlp.printer.result.Result;
import org.swunlp.printer.service.UploadService;
import org.swunlp.printer.util.FileMimeTypeUtil;
import org.swunlp.printer.util.MinioUtil;

import javax.annotation.Resource;

/**
 * 用户文件上传后，在这里进行接收以及后续处理
 */
@RestController
@RequestMapping("upload")
public class UploadController {
    @Autowired
    private UploadService uploadService;

    @Resource
    private MinioUtil minioUtil;


    /**
     * 控制上传的文件大小小于20M
     * 上传的文件必需是支持的类型
     * @param file
     * @return
     */
    @PostMapping("")
    @ResponseResult
    public Object upload(MultipartFile file) {
        // 判断是否为图片且大小小于20M
        if (file.getSize() > 20 * 1024 * 1024) {
            return Result.error("上传大小不能超过10M");
        }
        String type = file.getContentType();
        if (FileMimeTypeUtil.isKey(type)) {
            throw new BusinessException("类型："+type+" 不受支持");
        }
        return uploadService.upload(file);
    }


}
