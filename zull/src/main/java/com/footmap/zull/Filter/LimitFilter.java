package com.footmap.zull.Filter;

import com.footmap.zull.utils.RedisUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 2 * @Author: wangyu
 * 3 * @Date: 2019/7/20 16:03
 * 4
 */
@Component
public class LimitFilter extends ZuulFilter {

    @Autowired
    RedisUtils redisUtils;

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String userIp;
        if (request.getHeader("x-forwarded-for") == null) {
            userIp = request.getRemoteAddr();
        } else {
            userIp = request.getHeader("x-forwarded-for");
        }
        long count = redisUtils.add(userIp);
        if (count > 100) {
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(403);
            ctx.getResponse().setContentType(MediaType.APPLICATION_JSON_UTF8.getType());
            String msg = "{'success':false, " + "'message':'Limit Your Request Count'}";
            ctx.setResponseBody(msg);
        }
        return null;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }
}
