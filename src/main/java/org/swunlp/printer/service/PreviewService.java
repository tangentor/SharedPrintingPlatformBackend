package org.swunlp.printer.service;

import java.util.List;

/**
 * 预览相关的功能
 */
public interface PreviewService {
    List<String> getPreviews(String md5);
}
