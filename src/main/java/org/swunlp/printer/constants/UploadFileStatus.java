package org.swunlp.printer.constants;

public enum UploadFileStatus {
    UPLOADING(1, "Uploading"),
    PARSING(2, "Parsing"),
    UPLOADED(3, "Uploaded"),
    DELETED(4, "Deleted");

    private final int statusCode;
    private final String status;

    UploadFileStatus(int statusCode, String status) {
        this.statusCode = statusCode;
        this.status = status;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatus() {
        return status;
    }
}
