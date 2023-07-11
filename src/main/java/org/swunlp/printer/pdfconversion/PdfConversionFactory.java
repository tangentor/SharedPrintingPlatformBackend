package org.swunlp.printer.pdfconversion;

/**
 * 通过工厂方法根据参数调用不同的策略
 */
public class PdfConversionFactory {
    public static PDFConverter createConverter(ConversionType conversionType) {
        return conversionType.getConverter();
    }
}

