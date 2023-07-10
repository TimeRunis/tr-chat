package com.tr.chat.strategy.impl.message;

import com.alibaba.fastjson.JSONObject;
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
    public Object handle(Map<Object,Object> map, HttpServletRequest request, BaseMapper<Message> mapper) {
        Page<Message> page = new Page<>(Long.parseLong((String) map.get("current")),Long.parseLong((String) map.get("size")));
        return ((MessageMapper)mapper).getPrivateMsg((String) map.get("toId"), (String) map.get("fromId"),page);
    }
}
