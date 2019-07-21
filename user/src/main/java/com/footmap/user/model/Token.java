package com.footmap.user.model;

import lombok.Data;

import java.util.Date;

/**
 * 2 * @Author: wangyu
 * 3 * @Date: 2019/7/16 21:31
 * 4
 */
@Data
public class Token {
    private Integer tid;
    private String tokens;
    private Integer uid;
    private Date expired;
    private Integer status;
}
