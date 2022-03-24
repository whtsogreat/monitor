package com.mt.common.annotation;

import java.lang.annotation.*;

/**
 * 系统日志注解-方式方法
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {

	String value() default "";
}
