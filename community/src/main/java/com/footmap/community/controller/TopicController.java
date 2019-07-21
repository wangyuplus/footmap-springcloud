package com.footmap.community.controller;

import com.footmap.community.model.Detail;
import com.footmap.community.model.Response;
import com.footmap.community.model.Topic;
import com.footmap.community.service.TopicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/topic")
public class TopicController {

    private static final Logger logger = LoggerFactory.getLogger(TopicController.class);

    @Autowired
    TopicService topicService;

    @RequestMapping(path = "/add", method = {RequestMethod.POST})
    public Response addTopic(@RequestBody String jsonStr, @CookieValue(value = "token", required = false) String token) {
        try {
            String s = topicService.addTopic(jsonStr, token);
            if (!"添加成功".equals(s)) {
                return new Response(1, s);
            }
            return new Response(0, s);
        } catch (Exception e) {
            logger.error("addTopic Exception:", e);
            return new Response(1,"出现错误了!");
        }
    }

    @RequestMapping(path = "/getAll", method = {RequestMethod.GET})
    public Response getAllTopic(@RequestParam("start") int start, @RequestParam("size") int size) {
        try {
            List<Topic> allTopic = topicService.getAllTopic(start, size);
            return new Response(0, "查询成功", allTopic);
        } catch (Exception e) {
            logger.error("getAllTopic error:", e);
            return new Response(1, "出现错误了");
        }
    }

    @RequestMapping(path = "/del", method = {RequestMethod.GET})
    public Response delTopic(@RequestParam("tid") int tid, @CookieValue("token") String token) {
        try {
            String s = topicService.delTopic(tid, token);
            if (!"删除成功".equals(s)) {
                return new Response(1, s);
            }
            return new Response(0, s);
        } catch (Exception e) {
            logger.error("delTopic Exception:", e);
            return new Response(1, "出现错误了");
        }
    }

    @RequestMapping(path = "/detail", method = {RequestMethod.GET})
    public Response getDetail(@RequestParam("tid") int tid) {
        try {
            Detail detail = topicService.getDetail(tid);
            return new Response(0, "获取成功", detail);
        } catch (Exception e) {
            logger.error("getDetail Exception:", e);
            return new Response(1, "出现错误了");
        }
    }

    @RequestMapping(path = "/like",method = {RequestMethod.GET})
    public Response like(@RequestParam("tid") int tid, @CookieValue("token") String token) {
        try {
            Map<String, Long> map = topicService.like(tid, token);
            if (map.get("msg") != null) {
                return new Response(1, "用户为空");
            }
            int likeCount = map.get("like").intValue();
            int dislikeCount = map.get("dislike").intValue();
            if (likeCount < 0) {
                map.put("like", 0L);
            }
            if (dislikeCount < 0) {
                map.put("dislike", 0L);
            }
            return new Response(0, "赞成功",map);
        } catch (Exception e) {
            logger.error("like Exception:", e);
            return new Response(1, "出现错误了");
        }
    }

    @RequestMapping(path = "/dislike",method = {RequestMethod.GET})
    public Response dislike(@RequestParam("tid") int tid, @CookieValue("token") String token) {
        try {
            Map<String, Long> map = topicService.dislike(tid, token);
            if (map.get("msg") != null) {
                return new Response(1, "用户为空");
            }
            int likeCount = map.get("like").intValue();
            int dislikeCount = map.get("dislike").intValue();
            if (likeCount < 0) {
                map.put("like", 0L);
            }
            if (dislikeCount < 0) {
                map.put("dislike", 0L);
            }
            return new Response(0, "踩成功",map);
        } catch (Exception e) {
            logger.error("dislike Exception:", e);
            return new Response(1, "出现错误了");
        }
    }
}
