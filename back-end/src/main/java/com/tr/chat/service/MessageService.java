package com.tr.chat.service;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface MessageService {
    Object handle(Map<Object,Object> map, HttpServletRequest request);
}
