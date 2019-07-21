package com.footmap.zull.Filter;

import com.footmap.zull.model.Token;
import com.footmap.zull.service.TokenService;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 2 * @Author: wangyu
 * 3 * @Date: 2019/7/20 15:23
 * 4
 */
@Component
public class LoginFilter extends ZuulFilter {
    @Autowired
    TokenService tokenService;

    String[] ALLOW_FUNC = {"/footmap-user/login","/footmap-user/reg","/footmap-user/getCode","/footmap-user/getUserByToken"};

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        for (String s : ALLOW_FUNC) {
            if (request.getRequestURI().equals(s)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String token = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        if (token == null) {
            ResponseFail(ctx);
        } else {
            Token t = tokenService.getToken(token);
            if (t == null || t.getExpired().before(new Date()) || t.getStatus() != 0) {
                ResponseFail(ctx);
            }
        }
        return null;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    public void ResponseFail(RequestContext ctx) {
        ctx.setSendZuulResponse(false);
        ctx.setResponseStatusCode(401);
        ctx.getResponse().setContentType(MediaType.APPLICATION_JSON_UTF8.getType());
        String msg = "{'success':false, " + "'message':'Verification Code Error'}";
        ctx.setResponseBody(msg);
    }

}
