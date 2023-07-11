package org.swunlp.printer.entity;

import lombok.Data;

import java.util.List;

@Data
public class PrintInfo {

    /**
     * 打印设备ID
     */
    private String service;

    private List<Integer> pages;

    private Integer num;

    /**
     * 默认A4
     */
    private String paper;

    /**
     * 默认黑白
     */
    private String color;

    private PrintServiceInfo printServiceInfo;

    private String documentId;

    private String fileUrl;


}
