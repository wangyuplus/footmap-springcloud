package com.footmap.user.service;

import com.footmap.user.dao.EmailDao;
import com.footmap.user.model.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 2 * @Author: wangyu
 * 3 * @Date: 2019/7/19 21:14
 * 4
 */
@Service
public class EmailService {
    @Autowired
    EmailDao emailDao;
    public Email findByEmail(String email) {
        return  emailDao.selectByEmail(email);

    }
}
