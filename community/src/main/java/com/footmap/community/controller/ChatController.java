package com.footmap.community.controller;

import com.footmap.community.model.Chat;
import com.footmap.community.model.Response;
import com.footmap.community.service.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/chat")
public class ChatController {
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    ChatService chatService;

    /**
     * 插入一条聊天信息
     * @param token 当前用户token
     * @param jsonStr 对方uid和message
     * @return
     */
    @RequestMapping(path = "/add", method = {RequestMethod.POST})
    @ResponseBody
    public Response addChat(@CookieValue("token") String token, @RequestBody String jsonStr) {
        try {
            String s = chatService.addChat(jsonStr, token);
            if (!"添加成功".equals(s)) {
                return new Response(1, s);
            }
            return new Response(0, s);
        } catch (Exception e) {
            logger.error("addChat Exception:",e);
            return new Response(1, "出现错误了");
        }
    }

    /**
     * 得到聊天信息
     * @param token 当前用户token
     * @param fromUid 对方uid
     * @return
     */
    @RequestMapping(path = "/get", method = {RequestMethod.GET})
    public Response getChat(@CookieValue("token") String token, @RequestParam("fromUid") Integer fromUid) {
        try {
            List<List<Chat>> chats = chatService.getChat(fromUid, token);
            return new Response(0, "获取成功", chats);
        } catch (Exception e) {
            logger.error("getChat Exception:",e);
            return new Response(1, "出现错误了");
        }
    }



}
