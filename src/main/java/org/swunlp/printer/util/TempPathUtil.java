package org.swunlp.printer.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * 这个是初版中由于将文件直接放在主机中
 * 需要使用一些路径匹配
 * 新版本中直接使用对象存储来进行文件的存储
 * 故而弃用了，在后续版本中会直接删除这个文件
 */
@Deprecated
public class TempPathUtil {

    private static String storePath;

    private static void init(){
        //判断当前平台
        String osName = System.getProperties().get("os.name").toString();
        storePath = "/var/printerShare/files";
        if(osName!=null && osName.toLowerCase().contains("windows")){
            storePath = "\\\\TX1010\\shareFiles";
        }
    }

    public static String getStorePath() {
        if(storePath == null){
            init();
        }
        return storePath;
    }

    public static String generateTempPath(String fileName) {
        //空格代表不需要
        return generateTempPath(fileName,"");
    }

    /**
     * 需要单独文件夹
     * @param fileName 最终文件名
     * @param folderName 文件夹名字
     * @return
     */
    public static String generateTempPath(String fileName,String folderName) {
        //获取当日的格式化字符串
        String formattedDate = CommUtil.getNowDateLongStr();
        String tempDir = storePath;
        Path tempPath = Paths.get(tempDir, formattedDate,folderName, fileName);

        try {
            Files.createDirectories(tempPath.getParent());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tempPath.toString();
    }

}
