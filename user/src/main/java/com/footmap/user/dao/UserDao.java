package com.footmap.user.dao;

import com.footmap.user.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * 2 * @Author: wangyu
 * 3 * @Date: 2019/7/16 18:51
 * 4
 */
@Mapper
@Repository(value = "userDao")
public interface UserDao {
    String TABLE_NAME = "user";
    String INSERT_FIELD = "username, password, status, salt ";

    @Insert("insert into user (username,password,salt,status) values (#{username},#{password},#{salt},#{status})")
    void insertUser(User user);

    @Select("select * from user where username =#{username} and status=0")
    User findUserByUsername(String username);


    @Select("select * from user where uid =#{uid} and status=0")
    User findUserById(@Param("uid") Integer uid);
}
