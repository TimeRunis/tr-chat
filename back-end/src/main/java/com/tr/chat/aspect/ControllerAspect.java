package com.tr.chat.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tr.chat.util.LoggerUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class ControllerAspect {
    @Pointcut("execution(* com.tr.chat.controller..*.*(..))")
    public void one(){}

    @Around("one()")
    public Object handle(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        //获取请求参数
        Map<Object,Object> map;
        //controller参数均为JSONObject类型,所以进行强转
        try{
            map= (Map<Object,Object>) args[0];
        }catch (ClassCastException e){
            MultipartFile[] files = (MultipartFile[]) args[0];
            //强转错误为文件类型参数
            map=new HashMap<>();
            for(MultipartFile file:files){
                map.put(file.getOriginalFilename(), String.valueOf(file.getSize()));
            }
        }

        //获取请求头
        HttpServletRequest request= (HttpServletRequest) args[1];
        //上传文件的日志
        if(request.getRequestURI().contains("upload")) {
            LoggerUtil.info("\nIP:" + request.getRemoteAddr() + "\n调用接口:" + request.getRequestURI() + "\n方式:" + request.getMethod() + "\n上传文件:" + map);
        }
        //输出接口调用日志
        LoggerUtil.info("\nIP:"+request.getRemoteAddr()+"\n调用接口:"+request.getRequestURI()+"\n方式:"+request.getMethod()+"\n接口参数为:"+map);
        //执行
        Object proceed = pjp.proceed();
        //输出接口调用结果
        LoggerUtil.info("\n调用结果"+ JSON.toJSON(proceed));
        return proceed;
    }


}