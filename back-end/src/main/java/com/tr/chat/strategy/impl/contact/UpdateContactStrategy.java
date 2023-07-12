package com.tr.chat.strategy.impl.contact;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tr.chat.entity.Contact;
import com.tr.chat.entity.Resp;
import com.tr.chat.mapper.ContactMapper;
import com.tr.chat.strategy.ContactStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Component("PUTContactStrategy")
public class UpdateContactStrategy implements ContactStrategy {

    @Override
    public Object handle(Map<Object,Object> map, BaseMapper<Contact> mapper, Object... objects) {
        try {
            List<Map<Object,String>> list = (List<Map<Object,String>>) map.get("list");
            for(Map<Object,String> jsonMap:list){
                ((ContactMapper)mapper).updateContact(String.valueOf(jsonMap.get("id")),String.valueOf(jsonMap.get("unread")));
            }
            return new Resp().success(list,"成功");
        }catch (Exception e){
            return new Resp().error(null,"参数错误");
        }
    }
}
