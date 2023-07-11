package org.swunlp.printer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.swunlp.printer.cache.UploadFileCache;
import org.swunlp.printer.entity.UploadFile;
import org.swunlp.printer.service.PDFParingService;

import javax.annotation.Resource;

@SpringBootTest
class PrinterApplicationTests {

    @Resource
    private UploadFileCache uploadFileCache;

    @Resource
    private PDFParingService pdfParingService;

    @Test
    void contextLoads() {
//        UploadFile uploadFile = new UploadFile("a", "a", "a");
//        uploadFileCache.set(null,uploadFile);
//        System.out.println(uploadFileCache.get(null));
        UploadFile uploadFile = uploadFileCache.get("1e83c1a16647b011db8b9431794d26d6");
        String s = pdfParingService.transformToPDF(uploadFile);
        System.out.println(s);
    }

}
