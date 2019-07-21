package com.footmap.community.dao;

import com.footmap.community.model.Chat;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository("chatDao")
public interface ChatDao {
    String TABLE_NAME = "chat";
    String INSERT_FIELD = "uid,fromUid,message,sendTime";

    @Insert({"insert into",TABLE_NAME,"(",INSERT_FIELD,")values(#{uid},#{fromUid},#{message},#{sendTime})"})
    void insertChat(Chat chat);

    //(匹配,最近30天,倒序)
    @Select({"select * from",TABLE_NAME,"where uid=#{uid} and fromUid=#{fromUid} " +
            "and sendTime >= curdate()-interval 30 day order by sendTime asc"})
    List<Chat> getChat(Integer uid, Integer fromUid);

    @Delete({"delete from",TABLE_NAME,"where sendTime < curdate()-interval 30 day"})
    void deleChat();
}
