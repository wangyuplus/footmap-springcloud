package com.footmap.community.model;

import lombok.Data;

import java.util.Date;

@Data
public class Reply {
    private Integer rid;
    private Integer tid;
    private Integer uid;
    private Date replyTime;
    private String ownerReply;
    private String reply;
    private Integer status;
}
