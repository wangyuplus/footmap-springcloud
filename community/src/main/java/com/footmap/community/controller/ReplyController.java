package com.footmap.community.controller;

import com.footmap.community.model.Response;
import com.footmap.community.service.ReplyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reply")
public class ReplyController {

    private static final Logger logger = LoggerFactory.getLogger(ReplyController.class);

    @Autowired
    ReplyService replyService;


    @RequestMapping(path = "/add", method = {RequestMethod.POST})
    public Response addReply(@RequestBody String jsonStr, @CookieValue("token") String token) {
        try {
            String s = replyService.addReply(jsonStr, token);
            if ("添加成功".equals(s)) {
                return new Response(0, s);
            }
            return new Response(1, s);
        } catch (Exception e) {
            logger.error("addReply Exception:", e);
            return new Response(1, "出现错误了!");
        }
    }

    @RequestMapping(path = "/addOwnerReply", method = {RequestMethod.POST})
    public Response addOwnerReply(@RequestBody String jsonStr, @CookieValue("token") String token) {
        try {
            String s = replyService.addOwnerReply(jsonStr, token);
            if ("添加成功".equals(s)) {
                return new Response(0, s);
            }
            return new Response(1, s);
        } catch (Exception e) {
            logger.error("addOwnReply Exception:", e);
            return new Response(1, "出现错误了!");
        }
    }

    @RequestMapping(path = "/del", method = {RequestMethod.GET})
    public Response delReply(@CookieValue("token") String token, @RequestParam("rid") int rid) {
        try {
            String s = replyService.deleReply(rid, token);
            if ("删除成功".equals(s)) {
                return new Response(0, s);
            }
            return new Response(1, s);
        } catch (Exception e) {
            logger.error("delReply Exception:", e);
            return new Response(1, "出现错误了");
        }
    }

    @RequestMapping(path = "/delOwnerReply", method = {RequestMethod.GET})
    public Response delOwnerReply(@CookieValue("token") String token, @RequestParam("rid") int rid) {
        try {
            String s = replyService.deleOwnerReply(rid, token);
            if ("删除成功".equals(s)) {
                return new Response(0, s);
            }
            return new Response(1, s);
        } catch (Exception e) {
            logger.error("delOwnerReply Exception:", e);
            return new Response(1, "出现错误了");
        }
    }
}
