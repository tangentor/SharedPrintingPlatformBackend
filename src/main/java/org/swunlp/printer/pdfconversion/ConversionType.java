package org.swunlp.printer.pdfconversion;

import org.swunlp.printer.constants.FileMimeType;

/**
 * ConversionType 是一个枚举类型，表示不同的文件转换为 PDF 的类型。
 * 每个枚举常量与特定的 FileMimeType 和相应的 PDFConverter 实现关联。
 * 该枚举提供了一个方法来获取与特定转换类型相关的 PDFConverter。
 */
public enum ConversionType {
    /**
     * 将 Word 文档转换为 PDF。
     */
    DOC(FileMimeType.WORD, new WordToPDFConverter()),

    /**
     * 将 Word 文档转换为 PDF。
     */
    DOCX(FileMimeType.DOCX, new WordToPDFConverter()),

    /**
     * 将 Excel 表格转换为 PDF。
     */
    XLSX(FileMimeType.XLSX, new ExcelToPDFConverter()),

    /**
     * 将 Excel 表格转换为 PDF。
     */
    XLS(FileMimeType.EXCEL, new ExcelToPDFConverter()),

    /**
     * 将 PowerPoint 演示文稿转换为 PDF（PPT 格式）。
     */
    PPT(FileMimeType.PPT, new SlideToPDFConverter()),

    /**
     * 将 PowerPoint 演示文稿转换为 PDF（PPTX 格式）。
     */
    PPTX(FileMimeType.PPTX, new SlideToPDFConverter());


    private final FileMimeType fileType;
    private final PDFConverter converter;

    /**
     * 构造一个具有指定 FileMimeType 和 PDFConverter 的 ConversionType 枚举常量。
     *
     * @param fileType   转换类型关联的 FileMimeType
     * @param converter  转换类型的 PDFConverter 实现
     */
    ConversionType(FileMimeType fileType, PDFConverter converter) {
        this.fileType = fileType;
        this.converter = converter;
    }

    /**
     * 返回与该转换类型关联的 PDFConverter。
     *
     * @return 转换类型的 PDFConverter 实现
     */
    public PDFConverter getConverter() {
        return converter;
    }

    public FileMimeType getFileMimeType() {
        return fileType;
    }
}

