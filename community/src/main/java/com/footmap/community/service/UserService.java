package com.footmap.community.service;

import com.footmap.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 调用用户服务 获取数据
 */
@Service
public class UserService {

    private static final String REST_URL_PREFIX = "http://FOOTMAP-USER";
    //entity(response实体), object(response.body)
    @Autowired
    RestTemplate restTemplate;

    public User findUserByToken(String token) {
        return restTemplate.getForObject(REST_URL_PREFIX + "/getUserByToken?token=" + token, User.class);
    }

    public User findUserByUid(int uid) {
        return restTemplate.getForObject(REST_URL_PREFIX + "/getUserById?uid=" + uid, User.class);
    }
}
