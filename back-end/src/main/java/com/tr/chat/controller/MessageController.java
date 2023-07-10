package com.tr.chat.controller;


import com.alibaba.fastjson.JSONObject;
import com.tr.chat.controller.method.GetController;
import com.tr.chat.entity.Resp;
import com.tr.chat.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class MessageController implements GetController {
    private final String path="/message";
    private Resp resp;
    private final MessageService messageService;

    @Autowired
    public MessageController(Resp resp, MessageService messageService) {
        this.resp = resp;
        this.messageService = messageService;
    }

    @GetMapping(path)
    @Override
    public Resp doGet(Map<Object,Object> map, HttpServletRequest request) {
        resp.success(messageService.handle(map,request),"成功");
        return resp;
    }
}
