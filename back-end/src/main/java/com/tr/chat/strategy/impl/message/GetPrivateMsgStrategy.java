package com.tr.chat.strategy.impl.message;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tr.chat.cache.CacheConfig;
import com.tr.chat.entity.Message;
import com.tr.chat.mapper.MessageMapper;
import com.tr.chat.strategy.MessageStrategy;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("GETPrivateMsgStrategy")
public class GetPrivateMsgStrategy implements MessageStrategy {
    @CacheConfig(expire = 60*60*1000,name = "getMessage")
    @Override
    public Object handle(Map<Object,Object> map, BaseMapper<Message> mapper, Object... objects) {
        Page<Message> page = new Page<>(Long.parseLong((String) map.get("current")),Long.parseLong((String) map.get("size")));
        return ((MessageMapper)mapper).getPrivateMsg((String) map.get("toId"), (String) map.get("fromId"),page);
    }
}
