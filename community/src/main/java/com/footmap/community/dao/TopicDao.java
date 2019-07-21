package com.footmap.community.dao;

import com.footmap.community.model.Topic;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository("topicDao")
public interface TopicDao {
    String TABLE_NAME = "topic";
    String INSERT_FIELD = "uid,subject,sendTime,contents,likeCount,dislikeCount,commentCount,status";

    @Insert({"insert into",TABLE_NAME,"(",INSERT_FIELD,")values(#{uid},#{subject},#{sendTime},#{contents}," +
            "#{likeCount},#{dislikeCount},#{commentCount},#{status})"})
    void insertTopic(Topic topic);

    @Select({"select * from",TABLE_NAME,"where status = 0 order by sendTime desc limit #{start},#{size}"})
    List<Topic> getAllTopic(int start, int size);

    @Update({"update",TABLE_NAME,"set status=#{status} where tid=#{tid}"})
    void updateStatus(int tid, int status);

    @Update({"update",TABLE_NAME,"set commentCount=commentCount+1 where tid=#{tid}"})
    void addComment(int tid);

    @Update({"update",TABLE_NAME,"set likeCount=#{likeCount},dislikeCount=#{dislikeCount} where tid=#{tid}"})
    void updateLikeAndDisLikeCount(int tid, int likeCount, int dislikeCount);

    @Select({"select * from",TABLE_NAME,"where tid=#{tid} and status=0"})
    Topic getTopicByTid(int tid);

    @Select({"select tid from",TABLE_NAME,"where status=0"})
    List<Integer> getAllTid();
}
