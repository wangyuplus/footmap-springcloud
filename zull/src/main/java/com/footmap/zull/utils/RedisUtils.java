package com.footmap.zull.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 2 * @Author: wangyu
 * 3 * @Date: 2019/7/20 16:05
 * 4
 */
@Component
public class RedisUtils {
    private static final Logger logger = LoggerFactory.getLogger(RedisUtils.class);

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    public long add(String ip) {
        try {
            stringRedisTemplate.expire(ip, 60, TimeUnit.SECONDS);
            Long count = stringRedisTemplate.opsForValue().increment(ip);
            return count;
        } catch (Exception e) {
            logger.error("RedisUtils add Exception:", e);
            return 0;
        }
    }
}
