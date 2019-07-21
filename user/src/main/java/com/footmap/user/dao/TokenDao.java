package com.footmap.user.dao;

import com.footmap.user.model.Token;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

/**
 * 2 * @Author: wangyu
 * 3 * @Date: 2019/7/16 21:34
 * 4
 */
@Mapper
@Repository(value = "tokenDao")
public interface TokenDao {




    @Insert("insert into token (tokens,uid,expired,status) values (#{tokens},#{uid},#{expired},#{status})")
    void addToken(Token token);
    @Select("select tokens from token where uid=#{uid}")
    String getTokensByUid(@Param("uid") Integer uid);
    @Update("update token set tokens=#{tokens} where uid=#{uid}")
    void updateToken(@Param("uid") int uid, @Param("tokens") String tokens);
    @Select("select uid from token where tokens =#{token}")
    Integer findUserBytoken(@Param("token") String token);
    @Select({"select * from token where tokens=#{tokens}"})
    Token findToken(String tokens);

    @Update("update token set status=#{i} where tokens=#{token}")
    void updateTokenStatus(@Param("token") String token, @Param("i") int i);
}
