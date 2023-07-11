package org.swunlp.printer.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.swunlp.printer.cache.UploadFileCache;
import org.swunlp.printer.entity.UploadFile;
import org.swunlp.printer.other.BusinessException;
import org.swunlp.printer.service.PDFParingService;
import org.swunlp.printer.service.PreviewService;
import org.swunlp.printer.util.CommUtil;

import java.util.List;

@Service
public class PreviewServiceImpl implements PreviewService {
    private final UploadFileCache uploadFileCache;

    private final PDFParingService pdfParingService;

    public PreviewServiceImpl(UploadFileCache uploadFileCache, PDFParingService pdfParingService) {
        this.uploadFileCache = uploadFileCache;
        this.pdfParingService = pdfParingService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> getPreviews(String md5) {
        //找找缓存
        UploadFile uploadFile = uploadFileCache.get(md5);
        if(uploadFile == null){
            throw new BusinessException("无已上传文件匹配,无法解析");
        }
        //判断是否已经处理过
        if(uploadFile.getPreviews() != null && uploadFile.getPreviews().size() > 0){
            return uploadFile.getPreviews();
        }
        //具体处理，首先先根据源文件类型是是否需要生成PDF版本
        String[] args = CommUtil.splitFilename(uploadFile.getFileName());
        if("pdf".equalsIgnoreCase(args[1])){
            //本就是PDF版本
            uploadFile.setType("pdf");
            uploadFile.setPdfUrl(uploadFile.getOriginalFileUrl());
        } else {
            //将其它类型进行转化PDF处理
            String pdfUrl = pdfParingService.transformToPDF(uploadFile);
            uploadFile.setPdfUrl(pdfUrl);
        }
        //根据PDF去生成相应的图片
//        System.out.println(uploadFile);
        List<String> previews = pdfParingService.getPreviews(uploadFile);
        //缓存更新
        uploadFile.setPreviews(previews);
        uploadFileCache.set(md5,uploadFile);
        return previews;
    }
}
