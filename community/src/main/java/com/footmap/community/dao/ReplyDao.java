package com.footmap.community.dao;

import com.footmap.community.model.Reply;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository("replyDao")
public interface ReplyDao {
    String TABLE_NAME = "reply";
    String INSERT_FIELD = "tid,uid,replyTime,ownerReply,reply,status";

    @Insert({"insert into", TABLE_NAME,"(",INSERT_FIELD,")values(#{tid},#{uid},#{replyTime},#{ownerReply}," +
            "#{reply},#{status})"})
    void insertReply(Reply reply);

    @Select({"select * from",TABLE_NAME,"where tid=#{tid} and status=0 order by replyTime asc"})
    List<Reply> getReplyByTid(int tid);

    @Select({"select * from",TABLE_NAME,"where rid=#{rid}"})
    Reply getReplyByRid(int rid);

    @Update({"update",TABLE_NAME,"set ownerReply=#{ownerReply} where rid=#{rid}"})
    void updateOwnerReply(int rid, String ownerReply);

    @Update({"update",TABLE_NAME,"set status=#{status} where rid=#{rid}"})
    void updateStatus(int rid, int status);

    @Update({"update",TABLE_NAME,"set ownerReply='' where rid=#{rid}"})
    void deleOwnerReply(int rid);
}
