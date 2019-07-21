package com.footmap.community.model;

import lombok.Data;

import java.util.List;

@Data
public class Detail {
    private Topic topic;
    private List<Reply> replys;
    private User user;
}
