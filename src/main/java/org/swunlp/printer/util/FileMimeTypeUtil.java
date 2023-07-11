package org.swunlp.printer.util;

import org.swunlp.printer.constants.FileMimeType;

import java.util.ArrayList;
import java.util.List;


public class FileMimeTypeUtil {

    private static List<String> keys = new ArrayList<>(10);

    /**
     * 初始化所有的类型
     */
    static {
        FileMimeType[] types = FileMimeType.values();
        for (FileMimeType type : types) {
            keys.add(type.getExtension());
        }
    }

    public static boolean isKey(String type){
        return keys.contains(type);
    }

    public static List<String> allSupportTypes(){
        return keys;
    }

}
