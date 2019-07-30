package com.enterprise.dswitch;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解,对于查询使用缓存的方法加入该注解
 * @Description
 * @Author zezhouyang
 * @Date 18/11/13 上午11:43
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface DSwitch {

    String type() default "";

}
