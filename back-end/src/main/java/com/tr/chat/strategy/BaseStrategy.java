package com.tr.chat.strategy;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface BaseStrategy<T>{
    Object handle(Map<Object,String> map, HttpServletRequest request, BaseMapper<T> mapper);
}
