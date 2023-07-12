package com.tr.chat.strategy.impl.message;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tr.chat.cache.CacheConfig;
import com.tr.chat.entity.Message;
import com.tr.chat.strategy.MessageStrategy;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("InsertMsgStrategy")
public class InsertMsgStrategy implements MessageStrategy {
    @CacheConfig(name = "insertMessage")
    @Override
    public Object handle(Map<Object, Object> map, BaseMapper<Message> mapper, Object... objects) {
        Message message= (Message) map.get("msg");
        return mapper.insert(message);
    }
}
