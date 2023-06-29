package com.tr.chat.strategy.impl.contact;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tr.chat.entity.Contact;
import com.tr.chat.entity.Resp;
import com.tr.chat.mapper.ContactMapper;
import com.tr.chat.strategy.ContactStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Component("GETContactStrategy")
public class GetContactStrategy implements ContactStrategy {
    @Override
    public Object handle(Map<Object, String> map, HttpServletRequest request, BaseMapper<Contact> mapper) {
        Page<Contact> page= new Page<>(1, 10);
        return  ((ContactMapper) mapper).getPageById(map.get("id"),page);
    }
}
