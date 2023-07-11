package org.swunlp.printer.service;

import org.swunlp.printer.entity.PrintInfo;
import org.swunlp.printer.entity.UploadFile;

import java.util.List;

public interface PDFParingService {


    List<String> getPreviews(UploadFile uploadFile);

    Object print(PrintInfo printInfo);

    String transformToPDF(UploadFile uploadFile);
}
