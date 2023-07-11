package org.swunlp.printer.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.swunlp.printer.cache.UploadFileCache;
import org.swunlp.printer.entity.UploadFile;
import org.swunlp.printer.service.PDFParingService;
import org.swunlp.printer.service.UploadService;
import org.swunlp.printer.util.CommUtil;
import org.swunlp.printer.util.MinioUtil;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

/**
 * 用户文件上传后具体的处理
 */
@Service
public class UploadServiceImpl implements UploadService {

    @Resource
    private PDFParingService pdfParingService;

    @Resource
    private MinioUtil minioUtil;


    @Resource
    private UploadFileCache uploadFileCache;

    /**
     * 默认上传是在打印模块，故先保存为临时文件，会有七天的有效期，加上一个删除标注
     * @param file 上传的文件
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object upload(MultipartFile file) {
        //将文件上传与具体的文件解析分成两个步骤
        String md5;
        try {
            md5 = DigestUtils.md5DigestAsHex(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("解析文件时出错");
        }
        //检查是否缓存
        if(uploadFileCache.exist(md5)){
            return uploadFileCache.get(md5);
        }
        //源文件文件保存一份
        String filename = CommUtil.filterFilename(Objects.requireNonNull(file.getOriginalFilename()));
        String fileUrl = minioUtil.upload(file, filename);
        //记录一下文件信息
        UploadFile uploadFile = new UploadFile(filename, fileUrl, md5);
        uploadFile.setUploadTime(new Date());
        //默认标记为临时文件
        uploadFile.setDelete(true);
        //生成图片写入缓存
        uploadFileCache.set(md5,uploadFile);
        return uploadFile;
    }

}
