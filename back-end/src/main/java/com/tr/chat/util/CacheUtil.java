package com.tr.chat.util;

import java.util.concurrent.TimeUnit;

public interface CacheUtil {

    //写入
    boolean set(String key, String value, Long expireTime, TimeUnit timeUnit);

    //读取
    String get(String key);

    //批量删除
    boolean remove(String... keys);
}
