package org.swunlp.printer.util;

import org.swunlp.printer.pdfconversion.ConversionType;

import java.util.ArrayList;
import java.util.List;


public class ConversionTypeUtil {

    private static List<String> keys = new ArrayList<>(10);

    /**
     * 初始化所有的类型
     */
    static {
        ConversionType[] types = ConversionType.values();
        for (ConversionType type : types) {
            keys.add(type.getFileMimeType().getExtension());
        }
    }

    public static boolean isKey(String type){
        return keys.contains(type);
    }

    public static List<String> allSupportTypes(){
        return keys;
    }

}
