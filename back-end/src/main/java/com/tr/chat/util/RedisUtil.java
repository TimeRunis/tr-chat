package com.tr.chat.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Component("RedisUtil")
public class RedisUtil implements CacheUtil {

    private final RedisTemplate<String,String> redisTemplate;

    @Autowired
    public RedisUtil(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    //写入
    public boolean set(String key, String value, Long expireTime, TimeUnit timeUnit){
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set(key,value);
        redisTemplate.expire(key,expireTime,timeUnit);
        return true;
    }

    @Override
    //读取
    public String get(String key){
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        return operations.get(key);
    }

    //读取列表
    @Override
    public List<?> getList(String key, Long start, Long end) {
        ListOperations<String, String> operations = redisTemplate.opsForList();
        return operations.range(key, start, end);
    }

    @Override
    //分页读取列表
    public List<?> getListPage(String key, Long current, Long size){
        ListOperations<String, String> operations = redisTemplate.opsForList();
        return operations.range(key, (current-1) *size, current *size-1);
    }

    @Override
    //获取列表总大小
    public Long getListSize(String key){
        ListOperations<String, String> operations = redisTemplate.opsForList();
        return operations.size(key);
    }

    @Override
    //批量从后写入列表
    public void pushList(String key, List<?> list){
        ListOperations<String, String> operations = redisTemplate.opsForList();
        for (Object obj:list){
            operations.rightPush(key, JSONObject.toJSONString(obj));
        }
    }

    @Override
    //批量从前写入列表
    public void leftPushList(String key, List<?> list){
        ListOperations<String, String> operations = redisTemplate.opsForList();
        for (Object obj:list){
            operations.leftPush(key, JSONObject.toJSONString(obj));
        }
    }

    @Override
    //单个写入列表
    public void pushOne(String key, Object object){
        List<Object> list = new ArrayList<>();
        list.add(object);
        this.pushList(key,list);
    }

    //弹出缓存队列的最后一条消息
    @Override
    public String popListLastOne(String key){
        ListOperations<String, String> operations = redisTemplate.opsForList();
        return operations.rightPop(key);
    }

    @Override
    //单个从前写入列表
    public void leftPushOne(String key, Object object){
        List<Object> list = new ArrayList<>();
        list.add(object);
        this.leftPushList(key,list);
    }


    @Override
    //批量删除
    public boolean remove(String... keys){
        for(String key : keys){
            if(Boolean.TRUE.equals(redisTemplate.hasKey(key))){ //key存在就删除
                redisTemplate.delete(key);
            }
        }
        return true;
    }

}
