package org.swunlp.printer.service;

import org.springframework.web.multipart.MultipartFile;
import org.swunlp.printer.entity.PrintInfo;

import java.util.Map;

public interface PDFParingService {


    Object parse(MultipartFile file);

    Map<String,Object> getPreviews(String md5);


    Object print(PrintInfo printInfo);
}
