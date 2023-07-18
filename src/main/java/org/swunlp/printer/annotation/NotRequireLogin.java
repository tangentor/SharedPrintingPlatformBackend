package org.swunlp.printer.annotation;

import java.lang.annotation.*;

/**
 * 标记方法或类不需要登录验证的注解。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface NotRequireLogin {
}

