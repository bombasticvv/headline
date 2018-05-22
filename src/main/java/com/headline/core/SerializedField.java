package com.headline.core;

import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface SerializedField {
    /**
     * 是否加密
     * @return
     */
    boolean encode() default true;
    
    /**
     * 是否解密
     * @return
     */
    boolean decode() default true;
}
