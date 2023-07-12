package com.tr.chat.timer;

import com.alibaba.fastjson.JSON;
import com.tr.chat.entity.Message;
import com.tr.chat.mapper.MessageMapper;
import com.tr.chat.util.CacheUtil;
import com.tr.chat.util.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CacheUpdateTimer implements Condition, SchedulingConfigurer {
    private MessageMapper messageMapper;
    private Map<String, CacheUtil> map;
    private CacheUtil cacheUtil;
    private String priClock;
    private String groupClock;

    public CacheUpdateTimer() {
    }

    @Autowired
    public CacheUpdateTimer(MessageMapper messageMapper, @Value("${cache.name}")String cacheName, Map<String, CacheUtil> map,@Value("${cache.clock.private}") String priClock,@Value("${cache.clock.group}") String groupClock) {
        this.messageMapper = messageMapper;
        this.map = map;
        this.priClock=priClock;
        this.groupClock=groupClock;
        this.cacheUtil = map.get(cacheName+"Util");;
    }

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return Boolean.parseBoolean(context.getEnvironment().getProperty("cache.open"));
    }

    public void priMsgCacheUpdate(){
        String queue="PriMsgQueue";
        if(cacheUtil.getListSize(queue)>0){
            int num=0;
            for(Object obj:cacheUtil.getList(queue,-1L,100L)){
                Message message= JSON.parseObject(obj.toString(),Message.class);
                messageMapper.insert(message);
                //弹出队列最后一个
                cacheUtil.popListLastOne(queue);
                num++;
            }
            LoggerUtil.info("私聊队列中"+num+"个消息已经存入数据库");
        }else {
            LoggerUtil.info("私聊队列为空,无需更新");
        }
    }

    public void groupMsgCacheUpdate(){
        String queue="GroupMsgQueue";
        if(cacheUtil.getListSize(queue)>0){
            int num=0;
            for(Object obj:cacheUtil.getList(queue,-1L,100L)){
                Message message= JSON.parseObject(obj.toString(),Message.class);
                messageMapper.insert(message);
                //弹出队列最后一个
                cacheUtil.popListLastOne(queue);
                num++;
            }
            LoggerUtil.info("群聊队列中"+num+"个消息已经存入数据库");
        }else {
            LoggerUtil.info("群聊队列为空,无需更新");
        }
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.addTriggerTask(this::priMsgCacheUpdate,
                triggerContext -> {
                    String cron = priClock;
                    if (cron.isEmpty()) {
                        System.out.println("cron is null");
                    }
                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
                });
        scheduledTaskRegistrar.addTriggerTask(this::groupMsgCacheUpdate,
                triggerContext -> {
                    String cron = groupClock;
                    if (cron.isEmpty()) {
                        System.out.println("cron is null");
                    }
                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
                });
    }
}