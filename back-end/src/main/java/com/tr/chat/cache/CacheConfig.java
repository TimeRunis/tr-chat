package com.tr.chat.cache;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheConfig {

    long expire() default 60 * 1000;

    String name() default "";
}
