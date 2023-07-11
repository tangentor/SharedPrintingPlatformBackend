package org.swunlp.printer.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class UploadFile implements Serializable {

    /**
     * id 用于更新信息
     */
    private int id;
    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件保存在OSS的链接
     */
    private String originalFileUrl;

    /**
     * 文件的MD5码
     */
    private String md5;

    /**
     * 是否是临时文件，临时文件会定期清理
     */
    private boolean delete;

    /**
     * 上传的时间
     */
    private Date uploadTime;

    /**
     * 文件的封面
     */
    private String cover;

    /**
     * 文件的类型
     */
    private String type;

    /**
     * 该文件对应的PDF版本的URL
     * 这个存在是因为在发送到打印机时只支持PDF形式的
     * 故将其他类型的转为PDF版本的
     */
    private String pdfUrl;

    /**
     * 该文件的所有预览图片，这里是懒加载的
     */
    private List<String> previews;

    public UploadFile(String filename, String originalFileUrl, String md5) {
        this.fileName = filename;
        this.originalFileUrl = originalFileUrl;
        this.md5 = md5;
    }
}
