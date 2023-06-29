package com.tr.chat.service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface ContactService{
    Object handle(Map<Object, String> map, HttpServletRequest request);
}
