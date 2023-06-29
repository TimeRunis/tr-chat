package com.tr.chat.aspect;

import com.tr.chat.entity.Resp;
import com.tr.chat.util.LoggerUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Aspect
@Component
public class ControllerAspect {
    private Resp resp;

    @Autowired
    public ControllerAspect(Resp resp) {
        this.resp = resp;
    }

    @Pointcut("execution(* com.tr.chat.controller..*.*(..))")
    public void one(){}

    @Around("one()")
    public Object handle(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        //获取请求参数
        //controller参数均为Map<Object,String>类型,所以进行强转
        Map<Object,String> map=(Map<Object, String>) args[0];
        //获取请求头
        HttpServletRequest request= (HttpServletRequest) args[1];

        //空参数检查
        if(map.isEmpty()){
            //日志输出
            LoggerUtil.info("\nIP:"+request.getRemoteAddr()+"\n调用接口:"+request.getRequestURI()+"\n方式:"+request.getMethod()+"\n接口参数为:"+map);
            resp.error(null,"空参数!");
            return resp;
        }else if(!map.containsKey("type")||!map.containsKey("current")||!map.containsKey("size")){
            //日志输出
            LoggerUtil.info("\nIP:"+request.getRemoteAddr()+"\n调用接口:"+request.getRequestURI()+"\n方式:"+request.getMethod()+"\n接口参数为:"+map);
            resp.error(null,"缺少必要参数!");
            return resp;
        }
        //日志输出
        LoggerUtil.info("\nIP:"+request.getRemoteAddr()+"\n调用接口:"+request.getRequestURI()+"\n方式:"+request.getMethod()+"\n接口参数为:"+map);

        return pjp.proceed();
    }

    @AfterReturning(value = "one()",returning = "resp")
    public void afterReturning(Object resp){
        //结果输出
        LoggerUtil.info("\n调用结果"+resp);
    }

}