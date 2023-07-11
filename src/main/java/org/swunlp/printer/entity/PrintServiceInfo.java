package org.swunlp.printer.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class PrintServiceInfo implements Serializable {

    private long serialVersionUID = 1L;

    private String id;

    /**
     * 设备名
     */
    private String name;

    /**
     * 是否支持彩色打印
     */
    private boolean isColor;

    /**
     * 是否支持双面打印
     */
    private boolean isBothSide;

    /**
     * 最大分辨率
     */
    private int dpi;

    /**
     * 分享人
     */
    private String sharePerson;

    /**
     * 支持的纸张打印类型
     */
    private String supportPaper;

    /**
     * 设备地址
     */
    private String ip;

    /**
     * 设备端口
     */
    private int port;
}
