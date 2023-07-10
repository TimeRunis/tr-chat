package com.tr.chat.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

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
