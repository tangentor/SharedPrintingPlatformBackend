package org.swunlp.printer.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.swunlp.printer.result.Result;
import org.swunlp.printer.service.PDFParingService;
import org.swunlp.printer.service.UploadService;

import javax.annotation.Resource;

@Service
public class UploadServiceImpl implements UploadService {

    @Resource
    private PDFParingService pdfParingService;

    @Override
    public Result saveFile(MultipartFile file) {
        return null;
    }

    @Override
    public Result parsingPDF(MultipartFile file) {
        return Result.adjust(pdfParingService.parse(file));
    }
}
