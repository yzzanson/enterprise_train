package com.enterprise.sign;

import java.lang.annotation.*;

/**
 * 自定义注解,对于需要验签的加入该注解
 * @Description
 * @Author zezhouyang
 * @Date 18/11/13 上午11:43
 */
@Target(value = {
        ElementType.TYPE,
        ElementType.METHOD
})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Sign {
}