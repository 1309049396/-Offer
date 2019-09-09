package com.xiehang.client.param;

public class Response {
    private Long id;
    private Object result;
    private String code = "000000";// 00000 表示成功，其他表示失败
    private String msg;//失败原因

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Response(Long id, Object result) {
        this.id = id;
        this.result = result;
    }

    public Response() {
    }
}
