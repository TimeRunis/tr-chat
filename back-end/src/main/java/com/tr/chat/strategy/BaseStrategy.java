package com.tr.chat.strategy;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Map;

public interface BaseStrategy<T>{
    Object handle(Map<Object,Object> map, BaseMapper<T> mapper,Object ...objects);
}
