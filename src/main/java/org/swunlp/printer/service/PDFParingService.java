package org.swunlp.printer.service;

import org.swunlp.printer.entity.UploadFile;

import java.util.List;

public interface PDFParingService {


    List<String> getPreviews(UploadFile uploadFile);

    String transformToPDF(UploadFile uploadFile);
}
