package com.footmap.user.service;

import com.footmap.user.dao.TokenDao;
import com.footmap.user.model.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 2 * @Author: wangyu
 * 3 * @Date: 2019/7/19 22:49
 * 4
 */
@Service
public class TokenService {

    @Autowired
    TokenDao tokenDao;
    public Token getToken(String token) {
        return tokenDao.findToken(token);
    }
}
