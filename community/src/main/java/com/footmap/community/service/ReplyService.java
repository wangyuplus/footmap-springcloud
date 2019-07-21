package com.footmap.community.service;

import com.footmap.community.dao.ReplyDao;
import com.footmap.community.dao.TopicDao;
import com.footmap.community.model.Reply;
import com.footmap.community.model.Topic;
import com.footmap.community.model.User;
import com.footmap.community.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ReplyService {

    @Autowired
    UserService userService;
    @Autowired
    ReplyDao replyDao;
    @Autowired
    TopicDao topicDao;

    /**
     * 如果添加reply/comment失败 则回滚
     * @param jsonStr
     * @param token
     * @return
     */
    @Transactional
    public String addReply(String jsonStr, String token) {
        User user = userService.findUserByToken(token);
        if (user == null) {
            return "请登录";
        }
        Map<String, String> map = JsonUtils.strToMap(jsonStr);
        if (map.get("tid") == null || map.get("reply") == null) {
            return "相关参数不能为空";
        }
        Integer tid = Integer.valueOf(map.get("tid"));
        String reply = map.get("reply");
        Reply replyEntity = new Reply();
        replyEntity.setTid(tid);
        replyEntity.setUid(user.getUid());
        replyEntity.setReply(reply);
        replyEntity.setReplyTime(new Date());
        replyEntity.setStatus(0);
        replyEntity.setOwnerReply("");
        replyDao.insertReply(replyEntity);
        //topic commentcount++
        topicDao.addComment(tid);
        return "添加成功";
    }

    public List<Reply> getReplyByTid(int tid) {
        return replyDao.getReplyByTid(tid);
    }

    public String addOwnerReply(String jsonStr, String token) {
        User user = userService.findUserByToken(token);
        if (user == null) {
            return "请登录";
        }
        Map<String, String> map = JsonUtils.strToMap(jsonStr);
        Integer rid = Integer.valueOf(map.get("rid"));
        String ownerReply = map.get("ownerReply");
        boolean managerForTopic = isManagerForTopic(rid, user.getUid());
        if (!managerForTopic) {
            return "您没有权限回复";
        }
        replyDao.updateOwnerReply(rid, ownerReply);
        return "添加成功";
    }


    public String deleReply(int rid, String token) {
        User user = userService.findUserByToken(token);
        boolean managerForReply = isManagerForReply(rid, user.getUid());
        if (!managerForReply) {
            return "您没有权限删除";
        }
        replyDao.updateStatus(rid, 1);
        return "删除成功";
    }

    public String deleOwnerReply(int rid, String token) {
        User user = userService.findUserByToken(token);
        boolean managerForTopic = isManagerForTopic(rid, user.getUid());
        if (!managerForTopic) {
            return "您没有权限删除";
        }
        replyDao.deleOwnerReply(rid);
        return "删除成功";
    }


    /**
     * 判断用户是否是帖子的发布者(楼主)
     * @param rid
     * @param uid
     * @return
     */
    public boolean isManagerForTopic(int rid, int uid) {
        Reply reply = replyDao.getReplyByRid(rid);
        Topic topic = topicDao.getTopicByTid(reply.getTid());
        if (topic.getUid() != uid) {
            return false;
        }
        return true;
    }

    /**
     * 判断是否是回复的发布者(回复者)
     * @param rid
     * @param uid
     * @return
     */
    public boolean isManagerForReply(int rid, int uid) {
        Reply reply = replyDao.getReplyByRid(rid);
        if (reply.getUid() != uid) {
            return false;
        }
        return true;
    }
}
