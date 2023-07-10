package com.tr.chat.controller;

import com.alibaba.fastjson.JSONObject;
import com.tr.chat.controller.method.GetController;
import com.tr.chat.controller.method.PutController;
import com.tr.chat.entity.Resp;
import com.tr.chat.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class ContactController implements GetController,PutController {
    private final String path="/contact";
    private Resp resp;
    private final ContactService contactService;

    @Autowired
    public ContactController(Resp resp, ContactService contactService) {
        this.resp = resp;
        this.contactService = contactService;
    }

    @GetMapping(path)
    @Override
    public Resp doGet(Map<Object,Object> map, HttpServletRequest request) {
        resp.success(contactService.handle(map,request),"成功");
        return resp;
    }

    @PutMapping(path)
    @Override
    public Resp doPut(Map<Object,Object> map, HttpServletRequest request) {
        return (Resp) contactService.handle(map,request);
    }
}
