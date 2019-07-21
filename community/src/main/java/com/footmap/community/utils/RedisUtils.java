package com.footmap.community.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;


public class RedisUtils {
    private static final Logger logger = LoggerFactory.getLogger(RedisUtils.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 像set集合添加值
     * @param key
     * @param value
     * @return
     */
    public void sadd(String key, String value) {
        try {
            stringRedisTemplate.opsForSet().add(key,value);
        } catch (Exception e) {
            logger.error("redis sadd Exception:", e);
        }
    }

    /**
     * 删除集合中的值
     * @param key
     * @param value
     * @return
     */
    public void srem(String key, String value) {
        try {
            stringRedisTemplate.opsForSet().remove(key, value);
        } catch (Exception e) {
            logger.error("redis sadd Exception:", e);
        }
    }

    public long scard(String key) {
        try {
            return stringRedisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            logger.error("redis srem Exception", e);
            return 0;
        }
    }
}
