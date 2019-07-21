package com.footmap.zull.service;

import com.footmap.zull.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 2 * @Author: wangyu
 * 3 * @Date: 2019/7/19 23:00
 * 4
 */
@Service
public class UserService {

    String URL = "http://FOOTMAP-USER";

    @Autowired
    RestTemplate restTemplate;

    public User getUser(String token) {
        return restTemplate.getForObject(URL + "/getUserByToken?token=" + token, User.class);
    }
}
