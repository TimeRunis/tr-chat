package com.tr.chat.entity;

import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class Resp implements Serializable {
    Integer code;
    Object data;
    String msg;

    public Resp success(Object data, String msg) {
        this.code=RespCode.SUCCESS.getCode();
        this.data=data;
        this.msg=msg;
        return this;
    }

    public Resp error(Object data, String msg) {
        this.code=RespCode.ERROR.getCode();
        this.data=data;
        this.msg=msg;
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "Resp{" +
                "code=" + code +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }
}
