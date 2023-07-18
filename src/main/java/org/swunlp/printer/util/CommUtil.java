package org.swunlp.printer.util;

import org.springframework.web.method.HandlerMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
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


    /**
     * 判断处理器对象是否被指定注解所标注。
     *
     * @param handler           处理器对象
     * @param annotationClass   指定的注解类型
     * @return 如果处理器对象被指定注解所标注则返回true，否则返回false
     */
    public static boolean isAnnotated(Object handler, Class<? extends Annotation> annotationClass) {
        if (handler instanceof HandlerMethod) {
            final HandlerMethod handlerMethod = (HandlerMethod) handler;
            final Class<?> beanType = handlerMethod.getBeanType();
            final Method method = handlerMethod.getMethod();

            // 判断处理器类是否标注了指定注解
            if (beanType.isAnnotationPresent(annotationClass)) {
                return true;
            } else if (method.isAnnotationPresent(annotationClass)) {
                return true;
            }
        }
        return false;
    }


}
