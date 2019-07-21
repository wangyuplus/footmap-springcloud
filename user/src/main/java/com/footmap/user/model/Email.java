package com.footmap.user.model;

import lombok.Data;

/**
 * 2 * @Author: wangyu
 * 3 * @Date: 2019/7/16 19:33
 * 4
 */
@Data
public class Email {
    private Integer eid;
    private String email;
    private String code;
    private Integer uid;
}
