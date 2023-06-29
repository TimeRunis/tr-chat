package com.tr.chat.strategy.impl.message;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tr.chat.entity.Message;
import com.tr.chat.mapper.MessageMapper;
import com.tr.chat.strategy.MessageStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Component("GETPrivateMsgStrategy")
public class GetPrivateMsgStrategy implements MessageStrategy {
    @Override
    public Object handle(Map<Object, String> map, HttpServletRequest request, BaseMapper<Message> mapper) {
        Page<Message> page = new Page<>(Long.parseLong(map.get("current")), Long.parseLong(map.get("size")));
        return ((MessageMapper)mapper).getPrivateMsg(map.get("toId"),map.get("fromId"),page);
    }
}
