package com.tr.chat.controller.method;

import com.tr.chat.entity.Resp;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


public interface GetController {
    Resp doGet(@RequestParam Map<Object,Object> map, HttpServletRequest request);
}
