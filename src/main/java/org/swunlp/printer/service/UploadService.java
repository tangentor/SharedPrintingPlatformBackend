package org.swunlp.printer.service;

import org.springframework.web.multipart.MultipartFile;
import org.swunlp.printer.result.Result;

public interface UploadService {
    Result saveFile(MultipartFile file);

    Result parsingPDF(MultipartFile file);
}
