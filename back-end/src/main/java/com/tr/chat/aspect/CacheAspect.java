package com.tr.chat.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tr.chat.cache.CacheConfig;
import com.tr.chat.entity.Resp;
import com.tr.chat.util.CacheUtil;
import com.tr.chat.util.LoggerUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class CacheAspect {

    private final Map<String,CacheUtil> map;
    private final CacheUtil cacheUtil;
    private Resp resp;
    private boolean isOpen;
    private String cacheName;

    @Autowired
    public CacheAspect(Map<String, CacheUtil> map, Resp resp,@Value("${cache.name}")String cacheName,@Value("${cache.open}")String isOpen) {
        this.map = map;
        this.resp = resp;
        this.cacheUtil=map.get(cacheName+"Util");
        this.cacheName=cacheName;
        this.isOpen=Boolean.getBoolean(isOpen);
        LoggerUtil.info("缓存状态:"+isOpen);
    }

    @Pointcut("@annotation(com.tr.chat.cache.CacheConfig)")
    private void cache() {

    }

    @Around("cache()")
    public Object doCache(ProceedingJoinPoint pjp) {
        try {
            if(!isOpen){
                return pjp.proceed();
            }

            //获取controller对应的方法
            Method method = ((MethodSignature)pjp.getSignature()).getMethod();

            //获取cache注解信息
            CacheConfig annotation = method.getAnnotation(CacheConfig.class);
            long expire = annotation.expire();
            String name = annotation.name();

            //设置存储格式
            Signature signature = pjp.getSignature();
            //类名
            String className = pjp.getTarget().getClass().getSimpleName();
            //方法名
            String methodName = signature.getName();

            //将传递过来的参数进行处理
            //获取数据
            Object[] args = pjp.getArgs();

            //组成缓存中的key
            String cacheKey = name + ":" + className + ":" + methodName + ":" + args[0];
            //获取key对应的值
            String cacheValue = cacheUtil.get(cacheKey);
            //读取缓存
            if (cacheValue!=null) {
                Resp resp = JSON.parseObject(cacheValue,Resp.class);
                LoggerUtil.info("\n键名:" + cacheKey + cacheName + "命中缓存");
                return resp;
            }
            //执行查询数据库操作
            Object proceed = pjp.proceed();
            JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(proceed));

            //数据结果查询成功后才会添加进缓存
            if (jsonObject.getString("code").equals("0") && jsonObject.getString("data") != null && !"".equals(jsonObject.getString("data"))) {
                if(cacheUtil.set(cacheKey, JSON.toJSONString(proceed), expire, TimeUnit.MILLISECONDS)){
                    LoggerUtil.info("\n键值:" + cacheKey + "存入缓存");
                }else {
                    LoggerUtil.warn("\n键值:" + cacheKey + "存入缓存失败");
                }

            }
            return proceed;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return resp.error(null, "系统错误");
    }
}
