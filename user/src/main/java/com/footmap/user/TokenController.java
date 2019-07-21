package com.footmap.user;

import com.footmap.user.controller.UserController;
import com.footmap.user.model.Token;
import com.footmap.user.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 2 * @Author: wangyu
 * 3 * @Date: 2019/7/19 22:46
 * 4
 */
@RestController
public class TokenController {
    private static final Logger logger = LoggerFactory.getLogger(TokenController.class);

    @Autowired
    TokenService tokenService;
    @RequestMapping(path = "/getToken", method = {RequestMethod.GET})
    public Token getToken(@RequestParam("token") String token) {
        try {
            return tokenService.getToken(token);
        } catch (Exception e) {
            logger.error("getToken Exception:", e);
            return null;
        }
    }
}
