package com.footmap.community.service;

import com.footmap.community.dao.TopicDao;
import com.footmap.community.model.Detail;
import com.footmap.community.model.Reply;
import com.footmap.community.model.Topic;
import com.footmap.community.model.User;
import com.footmap.community.utils.JsonUtils;
import com.footmap.community.utils.RedisKeyUtils;
import com.footmap.community.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class TopicService {
    @Autowired
    UserService userService;
    @Autowired
    ReplyService replyService;
    @Autowired
    TopicDao topicDao;
    @Autowired
    RedisUtils redisUtils;
    public String addTopic(String jsonStr, String token) {
        User user = userService.findUserByToken(token);
        if (user == null) {
            return "请登录";
        }
        Map<String, String> map = JsonUtils.strToMap(jsonStr);
        String subject = map.get("subject");
        String contents = map.get("contents");
        if (subject == null || contents == null) {
            return "相关参数不能为空";
        }
        Topic topic = new Topic();
        topic.setUid(user.getUid());
        topic.setSubject(subject);
        topic.setContents(contents);
        topic.setSendTime(new Date());
        topic.setLikeCount(0);
        topic.setDislikeCount(0);
        topic.setCommentCount(0);
        topic.setStatus(0);
        topicDao.insertTopic(topic);
        return "添加成功";
    }


    public List<Topic> getAllTopic(int start, int size) {
        start = (start - 1) * size;
        List<Topic> allTopic = topicDao.getAllTopic(start, size);
        return allTopic;
    }

    public String delTopic(int tid, String token) {
        boolean flag = delVerify(tid, token);
        if (!flag) {
            return "您没有权限操作";
        }
        topicDao.updateStatus(tid, 1);
        return "删除成功";
    }

    public Detail getDetail(int tid) {
        Topic topic = topicDao.getTopicByTid(tid);
        //根据uid获取到用户信息
        User user = userService.findUserByUid(topic.getUid());
        List<Reply> replys = replyService.getReplyByTid(tid);
        Detail detail = new Detail();
        detail.setTopic(topic);
        detail.setReplys(replys);
        detail.setUser(user);
        return detail;
    }


    public Map<String, Long> like(int tid, String token) {
        User user = userService.findUserByToken(token);
        Map<String, Long> map = new HashMap<>();
        if (user == null) {
            map.put("msg",1L);
            return map;
        }
        String likeKey = RedisKeyUtils.getLikeKey(tid);
        redisUtils.sadd(likeKey, String.valueOf(user.getUid()));
        String dislikeKey = RedisKeyUtils.getDisLikeKey(tid);
        redisUtils.srem(dislikeKey, String.valueOf(user.getUid()));
        //返回点赞数量
        map.put("like", redisUtils.scard(likeKey));
        map.put("dislike", redisUtils.scard(dislikeKey));
        return map;
    }

    public Map<String, Long> dislike(int tid, String token) {
        User user = userService.findUserByToken(token);
        Map<String, Long> map = new HashMap<>();
        if (user == null) {
            map.put("msg",1L);
            return map;
        }
        //喜欢集合 删除
        String likeKey = RedisKeyUtils.getLikeKey(tid);
        redisUtils.srem(likeKey, String.valueOf(user.getUid()));
        //反对集合新增
        String dislikeKey = RedisKeyUtils.getDisLikeKey(tid);
        redisUtils.sadd(dislikeKey, String.valueOf(user.getUid()));
        //返回点赞数量
        map.put("like", redisUtils.scard(likeKey));
        map.put("dislike", redisUtils.scard(dislikeKey));
        return map;
    }

    /**
     * 每小时执行一次
     */
    @Scheduled(cron = "0 0 0/1 * * ? ")
    public void updateLikeAndDisLikeCount() {
        List<Integer> allTids = topicDao.getAllTid();
        //System.out.println(Arrays.toString(allTids.toArray()));
        for (Integer tid : allTids) {
            String likeKey = RedisKeyUtils.getLikeKey(tid);
            String disLikeKey = RedisKeyUtils.getDisLikeKey(tid);
            //System.out.println(likeKey + ":" + disLikeKey);
            int likeCount = (int)redisUtils.scard(likeKey);
            int dislikeCount = (int)redisUtils.scard(disLikeKey);
            //System.out.println(likeCount + ":" + dislikeCount);
            topicDao.updateLikeAndDisLikeCount(tid, likeCount, dislikeCount);
        }
    }


    public boolean delVerify(int tid, String token) {
        //todo tid做验证必须存在
        int readUid = topicDao.getTopicByTid(tid).getUid();
        int verifyUid = userService.findUserByToken(token).getUid();
        return verifyUid == readUid;
    }
}
