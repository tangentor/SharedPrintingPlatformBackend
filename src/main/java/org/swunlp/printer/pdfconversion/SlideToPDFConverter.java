package org.swunlp.printer.pdfconversion;

import com.aspose.slides.Presentation;
import com.aspose.slides.SaveFormat;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * 将PowerPoint演示转为PDF
 * 使用第三方包进行实现
 */
public class SlideToPDFConverter implements PDFConverter{

    /**
     * 将PowerPoint转换为PDF格式。
     *
     * @param originFile PowerPoint文档的输入流。
     * @param desFile    转换后的PDF文件的输出流。
     * @throws RuntimeException 如果转换过程中发生任何错误。
     */
    @Override
    public void convertToPdf(InputStream originFile, OutputStream desFile) throws RuntimeException {
        try {
            Presentation doc = new Presentation(originFile);
            doc.save(desFile, SaveFormat.Pdf);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
