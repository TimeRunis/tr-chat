package com.tr.chat.service;


import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface ContactService{
    Object handle(Map<Object,Object> map,Object ...objects);
}
