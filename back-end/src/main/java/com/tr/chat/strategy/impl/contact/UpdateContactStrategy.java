package com.tr.chat.strategy.impl.contact;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tr.chat.entity.Contact;
import com.tr.chat.mapper.ContactMapper;
import com.tr.chat.mapper.MessageMapper;
import com.tr.chat.strategy.ContactStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Component("GETOwnerStrategy")
public class GetOwnerStrategy implements ContactStrategy {
    @Autowired
    MessageMapper messageMapper;

    @Override
    public Object handle(Map<Object, String> map, HttpServletRequest request, BaseMapper<Contact> mapper) {
        Page<Contact> page = new Page<>(Long.parseLong(map.get("current")), Long.parseLong(map.get("size")));
        page=((ContactMapper)mapper).getPageByOwner(map.get("id"),page);
        for(Contact contact:page.getRecords()){
            if(contact.getGroup()!=null){
                contact.setLastMessage(messageMapper.getGroupLastMsg(String.valueOf(contact.getGroup().getId())));
            }else if(contact.getUser()!=null){
                contact.setLastMessage(messageMapper.getPrivateLastMsg(String.valueOf(contact.getUser().getId()),String.valueOf(contact.getOwner().getId())));
            }
        }
        return page;
    }
}
