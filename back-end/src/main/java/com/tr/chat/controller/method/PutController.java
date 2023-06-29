package com.tr.chat.controller.method;

import com.tr.chat.entity.Resp;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface PutController {
    Resp doPut(@RequestBody Map<Object,String> map, HttpServletRequest request);
}
