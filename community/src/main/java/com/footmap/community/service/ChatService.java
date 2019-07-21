package com.footmap.community.service;

import com.footmap.community.dao.ChatDao;
import com.footmap.community.model.Chat;
import com.footmap.community.model.User;
import com.footmap.community.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ChatService {
    @Autowired
    UserService userService;
    @Autowired
    ChatDao chatDao;

    public String addChat(String jsonStr,String token) {
        User user = userService.findUserByToken(token);
        if (user == null) {
            return "请登录";
        }
        Map<String, String> map = JsonUtils.strToMap(jsonStr);
        if (map.get("fromUid") == null || map.get("message") == null) {
            return "相关参数不能为空";
        }
        Integer fromUid = Integer.valueOf(map.get("fromUid"));
        String message = map.get("message");
        Chat chat = new Chat();
        chat.setUid(user.getUid());
        chat.setFromUid(fromUid);
        chat.setMessage(message);
        chat.setSendTime(new Date());
        chatDao.insertChat(chat);
        return "添加成功";
    }

    public List<List<Chat>> getChat(Integer fromUid, String token) {
        User user = userService.findUserByToken(token);
        if (user == null) {
            return null;
        }
        //这个人发给我的消息
        List<Chat> list1 = chatDao.getChat(user.getUid(), fromUid);
        //我发给这个人的消息
        List<Chat> list2 = chatDao.getChat(fromUid, user.getUid());
        List<List<Chat>> lists = new ArrayList<>();
        lists.add(list1);
        lists.add(list2);
        return lists;
    }

    /**
     * 定时任务 每隔1天删除30天以上的信息
     */
    @Scheduled(cron = "0 0 0/1 1/1 1/1 ? ")
    public void deleChat() {
        chatDao.deleChat();
    }
}
