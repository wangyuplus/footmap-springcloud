package com.footmap.community.utils;

/**
 * 生成key 喜欢/不喜欢
 */
public class RedisKeyUtils {
    private static String SPLIT = ":";
    private static String LIKE = "LIKE";
    private static String DISLIKE = "DISLIKE";

    /**
     * 产生key, 例如entityId=2, entityType=like, 则key是LIKE:ENTITY:2
     * @param entityId topic的id
     * @return
     */
    public static String getLikeKey(int entityId) {
        return LIKE + SPLIT + entityId;
    }

    /**
     * 不喜欢
     * @param entityId topic的id
     * @return
     */
    public static String getDisLikeKey(int entityId) {
        return DISLIKE + SPLIT + entityId;
    }
}
