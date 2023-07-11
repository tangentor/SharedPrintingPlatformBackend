package org.swunlp.printer.pdfconversion;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * 将其他文件类型转为PDF的规范接口
 * 所有的实现类实现该接口并在
 * @link ConversionType 中进行定义
 * 最终通过PdfConversionFactory进行调用
 */
public interface PDFConverter {

    void convertToPdf(InputStream originFile, OutputStream desFile);
}
