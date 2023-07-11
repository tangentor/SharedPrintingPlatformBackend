package org.swunlp.printer.pdfconversion;

import com.aspose.cells.SaveFormat;
import com.aspose.cells.Workbook;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * 将Excel表格转为PDF
 * 使用第三方包进行实现
 */
public class ExcelToPDFConverter implements PDFConverter{

    /**
     * 将Excel文档转换为PDF格式。
     *
     * @param originFile Excel文档的输入流。
     * @param desFile    转换后的PDF文件的输出流。
     * @throws RuntimeException 如果转换过程中发生任何错误。
     */
    @Override
    public void convertToPdf(InputStream originFile, OutputStream desFile) throws RuntimeException {
        try {
            Workbook doc = new Workbook(originFile);
            doc.save(desFile, SaveFormat.PDF);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
