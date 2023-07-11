package org.swunlp.printer.pdfconversion;

import com.aspose.words.Document;
import com.aspose.words.SaveFormat;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * 将Word文档转为PDF
 * 使用第三方包进行实现
 */
public class WordToPDFConverter implements PDFConverter{

    /**
     * 将Word文档转换为PDF格式。
     *
     * @param originFile Word文档的输入流。
     * @param desFile    转换后的PDF文件的输出流。
     * @throws RuntimeException 如果转换过程中发生任何错误。
     */
    @Override
    public void convertToPdf(InputStream originFile, OutputStream desFile) throws RuntimeException {
        try {
            Document doc = new Document(originFile);
            doc.save(desFile, SaveFormat.PDF);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
