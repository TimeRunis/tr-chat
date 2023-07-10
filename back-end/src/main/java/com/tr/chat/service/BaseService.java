package com.tr.chat.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tr.chat.strategy.BaseStrategy;
import com.tr.chat.util.LoggerUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class BaseService<T> extends ServiceImpl<BaseMapper<T>,T> implements IService<T> {
    //策略集
    protected Map<String, BaseStrategy<T>> strategyMap;

    public BaseService(Map<String, BaseStrategy<T>> strategyMap) {
        this.strategyMap = strategyMap;
    }

    protected BaseStrategy<T> key2StrategyName(String name){
        //只需参数包含部分策略名即可,不需要在参数中暴露策略的完整名
        for (String key : strategyMap.keySet()) {
            if(key.contains(name)){
                return strategyMap.get(key);
            }
        }
        return strategyMap.get(strategyMap.keySet().iterator().next());
    }

    public Object handle(Map<Object,Object> map, HttpServletRequest request) {
        // 根据strategyName获取对应的策略
        BaseStrategy<T> strategy = key2StrategyName(request.getMethod()+map.get("type"));
        LoggerUtil.info("\n获取到字段:"+request.getMethod()+map.get("type")+"\n使用策略:"+strategy.toString());
        return strategy.handle(map, request ,getBaseMapper());
    }
}
