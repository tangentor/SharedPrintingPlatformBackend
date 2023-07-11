package org.swunlp.printer.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {

    Object upload(MultipartFile file);
}
