package com.footmap.community.model;

import lombok.Data;

import java.util.Date;
@Data
public class Topic {
    private Integer tid;
    private Integer uid;
    private String subject;
    private Date sendTime;
    private String contents;
    private Integer likeCount;
    private Integer dislikeCount;
    private Integer commentCount;
    private Integer status;
}
