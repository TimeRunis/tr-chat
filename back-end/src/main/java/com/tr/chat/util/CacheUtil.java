package com.tr.chat.util;

import java.util.List;
import java.util.concurrent.TimeUnit;

public interface CacheUtil {

    //写入
    boolean set(String key, String value, Long expireTime, TimeUnit timeUnit);

    //读取
    String get(String key);

    //读取列表
    List<?> getList(String key, Long start, Long end);

    //分页读取列表
    List<?> getListPage(String key, Long current, Long size);

    //获取列表总大小
    Long getListSize(String key);

    //批量写入列表
    void pushList(String key, List<?> list);

    //批量从前写入列表
    void leftPushList(String key, List<?> list);

    //单个写入列表
    void pushOne(String key, Object object);

    //弹出缓存队列的最后一条消息
    String popListLastOne(String key);

    //单个从前写入列表
    void leftPushOne(String key, Object object);

    //批量删除
    boolean remove(String... keys);
}
