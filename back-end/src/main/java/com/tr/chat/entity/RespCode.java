package com.tr.chat.entity;

public enum RespCode {
    // 系统模块
    SUCCESS(0, "操作成功"),
    ERROR(-1, "操作失败"),

    //web
    WEB_400(400,"错误请求"),
    WEB_401(401,"访问未得到授权"),
    WEB_404(404,"资源未找到"),
    WEB_500(500,"服务器内部错误"),
    WEB_UNKNOWN(999,"未知错误");


    private String msg;
    private Integer code;

    RespCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }
    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String toString(){
        return "code:"+code+";msg:"+msg;
    }
}
