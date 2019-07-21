package com.footmap.user.utils;

import com.footmap.user.model.User;
import lombok.Data;

/**
 * 2 * @Author: wangyu
 * 3 * @Date: 2019/7/16 19:16
 * 4
 */
@Data
public class Response<T> {
    private Integer code;
    private String message;
    private T data;

    public Response (Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Response (Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }


}
