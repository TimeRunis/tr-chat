package com.tr.chat.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {


    @Bean
    public RedisTemplate<String,String> redisTemplate(RedisConnectionFactory factory){

        RedisTemplate<String,String> redisTemplate = new RedisTemplate<>();
        // 设置连接工厂类
        redisTemplate.setConnectionFactory(factory);

        // 设置k-v的序列化方式
        // FastJsonRedisSerializer 实现了 RedisSerializer接口
        FastJsonRedisSerializer fastJsonRedisSerializer = new FastJsonRedisSerializer(Object.class);
        redisTemplate.setValueSerializer(fastJsonRedisSerializer);
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        return redisTemplate;
    }
}
