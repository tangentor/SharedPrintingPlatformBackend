package org.swunlp.printer.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.swunlp.printer.cache.UploadFileCache;
import org.swunlp.printer.entity.UploadFile;
import org.swunlp.printer.service.UploadService;
import org.swunlp.printer.util.CommUtil;
import org.swunlp.printer.util.MinioUtil;
import org.swunlp.printer.util.UsernameUtil;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 用户文件上传后具体的处理
 */
@Service
public class UploadServiceImpl implements UploadService {

    private final MinioUtil minioUtil;


    private final UploadFileCache uploadFileCache;

    public UploadServiceImpl(MinioUtil minioUtil, UploadFileCache uploadFileCache) {
        this.minioUtil = minioUtil;
        this.uploadFileCache = uploadFileCache;
    }

    /**
     * 默认上传是在打印模块，故先保存为临时文件，会有七天的有效期，加上一个删除标注
     *
     * @param file 上传的文件
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UploadFile upload(MultipartFile file) {
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
        uploadFile.setUid(UsernameUtil.getLoginUser());
        //生成图片写入缓存
        uploadFileCache.set(md5,uploadFile);
        return uploadFile;
    }

    @Override
    public List<UploadFile> listUserRecord() {
        //redis中所有存在的上传记录
        List<UploadFile> list = uploadFileCache.list("*");
        //过滤出该用户的
        return list.stream()
                .filter(item -> Objects.equals(item.getUid(), UsernameUtil.getLoginUser()))
                .collect(Collectors.toList());
    }

}
