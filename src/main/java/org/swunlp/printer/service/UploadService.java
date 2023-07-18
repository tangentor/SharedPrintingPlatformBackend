package org.swunlp.printer.service;

import org.springframework.web.multipart.MultipartFile;
import org.swunlp.printer.entity.UploadFile;

import java.util.List;

public interface UploadService {

    UploadFile upload(MultipartFile file);

    List<UploadFile> listUserRecord();
}
