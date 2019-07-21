package com.footmap.zull.model;

import lombok.Data;

import java.util.Date;

/**
 * 2 * @Author: wangyu
 * 3 * @Date: 2019/7/19 22:41
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
