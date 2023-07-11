package org.swunlp.printer.util;

import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class CommUtil {

    public static String getNowDateLongStr(String format){
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(format);
        return currentDate.format(dateFormatter);
    }

    /**
     * 获取当日的格式化时间
     */
    public static String getNowDateLongStr(){
        return getNowDateLongStr("yyMMdd");
    }

    /**
     * 获取UUID
     * @param withLine 是否带横线
     */
    public static String getUUID(boolean withLine){
        String uuid = UUID.nameUUIDFromBytes(getNowDateLongStr().getBytes()).toString();
        return withLine ? uuid : uuid.replaceAll("-","");
    }

    /**
     *
     * @param filename 待处理的文件名
     * @return 返回原始文件名与文件后缀，如 111.pdf => 111 与 pdf
     */
    public static String[] splitFilename(String filename) {
        filename = filterFilename(filename);
        int dotIndex = filename.lastIndexOf(".");
        if (dotIndex != -1 && dotIndex < filename.length() - 1) {
            String name = filename.substring(0, dotIndex);
            String extension = filename.substring(dotIndex + 1);
            return new String[]{name, extension};
        } else {
            return new String[]{filename, ""};
        }
    }

    /**
     * 对文件名进行非中文的特俗字符进行过滤
     * 过滤特殊字符和空格，只保留允许的字符，并限制长度小于100
     * @param filename 待处理的文件名
     * @return 处理之后的文件名
     */
    public static String filterFilename(String filename) {
        String filteredFilename = filename.replaceAll("[^\\u4E00-\\u9FA5a-zA-Z0-9.-]", "");
        if (filteredFilename.length() > 100) {
            filteredFilename = filteredFilename.substring(0, 100);
        }
        return filteredFilename;
    }



}
