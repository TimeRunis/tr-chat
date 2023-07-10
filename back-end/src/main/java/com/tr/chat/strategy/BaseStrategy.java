package com.tr.chat.strategy;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface BaseStrategy<T>{
    Object handle(Map<Object,Object> map, HttpServletRequest request, BaseMapper<T> mapper);
}
