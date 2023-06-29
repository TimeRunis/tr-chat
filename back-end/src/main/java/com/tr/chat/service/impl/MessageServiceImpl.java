package com.tr.chat.service.impl;

import com.tr.chat.entity.Message;
import com.tr.chat.service.BaseService;
import com.tr.chat.service.MessageService;
import com.tr.chat.strategy.BaseStrategy;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class MessageServiceImpl extends BaseService<Message> implements MessageService {
    public MessageServiceImpl(Map<String, BaseStrategy<Message>> stringBaseStrategyMap) {
        super(stringBaseStrategyMap);
    }
}
