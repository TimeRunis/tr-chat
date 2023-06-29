package com.tr.chat.service.impl;


import com.tr.chat.entity.Contact;
import com.tr.chat.service.BaseService;
import com.tr.chat.service.ContactService;
import com.tr.chat.strategy.BaseStrategy;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ContactServiceImpl extends BaseService<Contact> implements ContactService {
    public ContactServiceImpl(Map<String, BaseStrategy<Contact>> stringBaseStrategyMap) {
        super(stringBaseStrategyMap);
    }
}
