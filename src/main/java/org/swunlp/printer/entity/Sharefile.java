package org.swunlp.printer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName t_sharefile
 */
@TableName(value ="t_sharefile")
@Data
public class Sharefile implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 文件名字
     */
    private String name;

    /**
     * 类型
     */
    private String type;

    /**
     * 文件的MD5码，会用来检索
     */
    private String md5;

    /**
     * 链接
     */
    private String url;

    /**
     * 描述
     */
    @TableField("`desc`")
    private String desc;

    /**
     * 上传者ID
     */
    private String uid;

    @TableField(exist = false)
    private String username;

    /**
     * 下载次数
     */
    private Integer downloadTimes;

    /**
     * 打印次数
     */
    private Integer printingTimes;

    /**
     * 上传时间
     */
    private Date uploadTime;

    /**
     * 缩略图
     */
    private String thumbnail;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Sharefile other = (Sharefile) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getMd5() == null ? other.getMd5() == null : this.getMd5().equals(other.getMd5()))
            && (this.getUrl() == null ? other.getUrl() == null : this.getUrl().equals(other.getUrl()))
            && (this.getDesc() == null ? other.getDesc() == null : this.getDesc().equals(other.getDesc()))
            && (this.getUid() == null ? other.getUid() == null : this.getUid().equals(other.getUid()))
            && (this.getDownloadTimes() == null ? other.getDownloadTimes() == null : this.getDownloadTimes().equals(other.getDownloadTimes()))
            && (this.getPrintingTimes() == null ? other.getPrintingTimes() == null : this.getPrintingTimes().equals(other.getPrintingTimes()))
            && (this.getUploadTime() == null ? other.getUploadTime() == null : this.getUploadTime().equals(other.getUploadTime()))
            && (this.getThumbnail() == null ? other.getThumbnail() == null : this.getThumbnail().equals(other.getThumbnail()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getMd5() == null) ? 0 : getMd5().hashCode());
        result = prime * result + ((getUrl() == null) ? 0 : getUrl().hashCode());
        result = prime * result + ((getDesc() == null) ? 0 : getDesc().hashCode());
        result = prime * result + ((getUid() == null) ? 0 : getUid().hashCode());
        result = prime * result + ((getDownloadTimes() == null) ? 0 : getDownloadTimes().hashCode());
        result = prime * result + ((getPrintingTimes() == null) ? 0 : getPrintingTimes().hashCode());
        result = prime * result + ((getUploadTime() == null) ? 0 : getUploadTime().hashCode());
        result = prime * result + ((getThumbnail() == null) ? 0 : getThumbnail().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", type=").append(type);
        sb.append(", md5=").append(md5);
        sb.append(", url=").append(url);
        sb.append(", desc=").append(desc);
        sb.append(", uid=").append(uid);
        sb.append(", downloadTimes=").append(downloadTimes);
        sb.append(", printingTimes=").append(printingTimes);
        sb.append(", uploadTime=").append(uploadTime);
        sb.append(", thumbnail=").append(thumbnail);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}