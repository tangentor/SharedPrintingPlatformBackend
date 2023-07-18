package org.swunlp.printer.service.impl;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;
import org.swunlp.printer.cache.UploadFileCache;
import org.swunlp.printer.constants.FileMimeType;
import org.swunlp.printer.constants.RecordState;
import org.swunlp.printer.entity.PrintInfo;
import org.swunlp.printer.entity.PrintServiceInfo;
import org.swunlp.printer.entity.UploadFile;
import org.swunlp.printer.pdfconversion.ConversionType;
import org.swunlp.printer.pdfconversion.PDFConverter;
import org.swunlp.printer.pdfconversion.PdfConversionFactory;
import org.swunlp.printer.service.PDFParingService;
import org.swunlp.printer.service.PrintServiceService;
import org.swunlp.printer.service.RecordService;
import org.swunlp.printer.util.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class PDFParingServiceImpl implements PDFParingService {

    /**
     * 经过测试,dpi为96,100,105,120,150,200中,105显示效果较为清晰,体积稳定,dpi越高图片体积越大,一般电脑显示分辨率为96
     */
    public static final float DEFAULT_DPI = 150;

    /**
     * 默认转换的图片格式为jpg
     */
    public static final String DEFAULT_FORMAT = "jpg";


    private final UploadFileCache uploadFileCache;

    private final MinioUtil minioUtil;

    private final PrintServiceService printServiceService;

    private final RecordService recordService;

    public PDFParingServiceImpl(UploadFileCache uploadFileCache, MinioUtil minioUtil, PrintServiceService printServiceService, RecordService recordService) {
        this.uploadFileCache = uploadFileCache;
        this.minioUtil = minioUtil;
        this.printServiceService = printServiceService;
        this.recordService = recordService;
    }

    @Override
    public List<String> getPreviews(UploadFile uploadFile) {
        try (InputStream inputStream = HttpUtil.getInputStreamFromURL(uploadFile.getPdfUrl());
             PDDocument doc = Loader.loadPDF(inputStream)) {
            PDFRenderer renderer = new PDFRenderer(doc);
            int pageCount = doc.getNumberOfPages();
            List<String> stringList = new ArrayList<>(pageCount);
            BufferedImage image;
            for (int i = 0; i < pageCount; i++) {
                // Windows native DPI
                image = renderer.renderImageWithDPI(i, DEFAULT_DPI);
                // BufferedImage srcImage = resize(image, 240, 240);//产生缩略图
                // 产生的文件目录 文件名_preview_index
                String uid = CommUtil.splitFilename(uploadFile.getFileName())[0] + "_preview_" + i + "." + DEFAULT_FORMAT;
                //使用文件流中转一下
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ImageIO.write(image, DEFAULT_FORMAT, byteArrayOutputStream);
                // 使用ByteArrayMultipartFileEditor将普通的输出流转换为MultipartFile
                // 不使用临时文件的方式是因为避免在服务器端产生不必要的垃圾
                // 这里使用第二种方式
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
                //保存处理之后的图片
                String path = minioUtil.upload(
                        byteArrayInputStream,byteArrayOutputStream.size(),
                        -1, FileMimeType.PNG.getMimeType(),
                        uid);
                stringList.add(path);
                byteArrayOutputStream.close();
            }
            return stringList;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    @Override
    public String transformToPDF(UploadFile uploadFile) {
        try {
            //获取到原文件输入流
            InputStream originIS = HttpUtil.getInputStreamFromURL(uploadFile.getOriginalFileUrl());
            //转换为PDF文件
            String[] args = CommUtil.splitFilename(uploadFile.getFileName());
            //根据文件的类型获取对应的转换器
            PDFConverter converter = PdfConversionFactory.createConverter(
                    ConversionType.valueOf(args[1].toUpperCase()));
            //新建一个输出流来接收
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            converter.convertToPdf(originIS,os);
            ByteArrayInputStream distOS = new ByteArrayInputStream(os.toByteArray());
            //将转换后的PDF文件上传,保持原来的名字
            String path = minioUtil.upload(
                    distOS,os.size(),
                    -1, FileMimeType.PDF.getMimeType(),
                    args[0]+".pdf");
            return path;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("解析文件时异常："+e.getMessage());
        }
    }

}
