package com.footmap.community.model;

/**
 * 响应类类
 * @param <T> 携带数据类型
 */
public class Response<T> {
    public int code;
    public String msg;
    public T data;

    public Response(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public Response(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

}
