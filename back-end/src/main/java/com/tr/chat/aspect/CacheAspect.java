package com.tr.chat.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.SerializationUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tr.chat.cache.CacheConfig;
import com.tr.chat.entity.Message;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        this.isOpen= Boolean.parseBoolean(isOpen);
        LoggerUtil.info("缓存状态:"+this.isOpen);
    }

    @Pointcut("@annotation(com.tr.chat.cache.CacheConfig)")
    private void cache() {

    }

    @Around("cache()")
    public Object doCache(ProceedingJoinPoint pjp) {
        try {
            if(!isOpen){
                LoggerUtil.info("未启用缓存");
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
            String cacheKey = name + ":" + className + ":" + methodName + ":";

            Object cacheValue;


            switch (name){
                case "getMessage":
                    //转换参数
                    HashMap<Object, Object> map=  SerializationUtils.clone((HashMap<Object, Object>) args[0]);
                    Long current=Long.parseLong((String) map.get("current"));
                    Long size=Long.parseLong((String) map.get("size"));
                    //去除多余参数
                    map.remove("current");
                    map.remove("size");
                    //查询是否存在消息缓存
                    cacheValue=cacheUtil.getListPage(cacheKey+map,current,size);
                    List<String> list= (List<String>) cacheValue;
                    List<Message> msgList=new ArrayList<>();
                    for (String s:list){
                        msgList.add(JSON.parseObject(s,Message.class));
                    }
                    if(msgList.size()!=0){
                        //存在缓存就包装成page类型返回
                        Page<Message> page= new Page<>(current,size);
                        page.setRecords(msgList);
                        page.setPages(10);
                        LoggerUtil.info("\n键值:" + cacheKey + "命中");
                        return page;
                    }else {
                        //不存在缓存就执行查询数据库操作
                        Object proceed = pjp.proceed();
                        //转换参数
                        Page<Message> page = (Page<Message>)proceed;
                        if(page.getRecords()!=null){
                            //存入缓存
                            map.remove("current");
                            map.remove("size");
                            cacheUtil.pushList(cacheKey+map,page.getRecords());
                        }
                        return proceed;
                    }
                case "insertMessage":
                    //转换参数
                    map = SerializationUtils.clone((HashMap<Object, Object>) args[0]);
                    Message message = (Message) map.get("msg");
                    String key;
                    if(message.getToUserId()!=null){
                        key = "getMessage:GetPrivateMsgStrategy:handle:{toId=" + message.getToUserId() + ", fromId=" + message.getFromUser().getId() + ", type=Private}";
                        //添加到私聊消息队列
                        cacheUtil.pushOne("PriMsgQueue",message);
                    }else {
                        key = "getMessage:GetGroupMsgStrategy:handle:{toId=" + message.getToGroupId() + ", fromId=" + message.getFromUser().getId() + ", type=Group}";
                        //添加到群聊消息队列
                        cacheUtil.pushOne("GroupMsgQueue",message);
                    }
                    cacheUtil.leftPushOne(key,message);
                    if(cacheUtil.getListSize(key)>=100){
                        cacheUtil.popListLastOne(key);
                    }
                    LoggerUtil.info("\n缓存更新");
                    return true;
                default:
                    cacheValue=cacheUtil.get(cacheKey);
                    if(cacheValue!=null){
                        return cacheValue;
                    }
            }
            return pjp.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return resp.error(null, "系统错误");
    }
}
