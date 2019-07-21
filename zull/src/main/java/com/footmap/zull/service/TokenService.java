package com.footmap.zull.service;

import com.footmap.zull.model.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 2 * @Author: wangyu
 * 3 * @Date: 2019/7/19 22:39
 * 4
 */
@Service
public class TokenService {

    String URL = "http://FOOTMAP-USER";

    @Autowired
    RestTemplate restTemplate;

    public Token getToken(String token) {
        return restTemplate.getForObject(URL + "/getToken?token=" + token, Token.class);
    }
}
