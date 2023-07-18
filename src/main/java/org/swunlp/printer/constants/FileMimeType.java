package org.swunlp.printer.constants;

public enum FileMimeType {
    PPT("ppt", "application/vnd.ms-powerpoint"),
    PDF("pdf", "application/pdf"),
    PPTX("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"),
    WORD("doc", "application/msword"),
    DOCX("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
    EXCEL("xls", "application/vnd.ms-excel"),
    XLSX("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),

    TXT("txt", "text/plain"),
    JPEG("jpeg", "image/jpeg"),
    JPG("jpg", "image/jpeg"),
    PNG("png", "image/png"),
    GIF("gif", "image/gif");

    private final String extension;
    private final String mimeType;

    FileMimeType(String extension, String mimeType) {
        this.extension = extension;
        this.mimeType = mimeType;
    }

    public String getExtension() {
        return extension;
    }

    public String getMimeType() {
        return mimeType;
    }
}

