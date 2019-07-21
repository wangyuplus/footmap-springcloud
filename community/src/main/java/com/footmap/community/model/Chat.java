package com.footmap.community.model;

import lombok.Data;

import java.util.Date;

@Data
public class Chat {
    private Integer cid;
    private Integer uid;
    private Integer fromUid;
    private String message;
    private Date sendTime;
}
