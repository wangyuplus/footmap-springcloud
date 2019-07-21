package com.footmap.user.dao;

import com.footmap.user.model.Email;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/**
 * 2 * @Author: wangyu
 * 3 * @Date: 2019/7/16 20:15
 * 4
 */
@Mapper
@Repository(value = "emailDao")
public interface EmailDao {
    @Insert("insert into email(email,code) values (#{email},#{code})")
    void insertEmail(Email em);
    @Update("update  email set uid=#{id} where email=#{email}")
    void insertUidByemail(@Param("id") Integer id, @Param("email")String email);
    @Select("select code  from email where email=#{email}  order by eid desc limit 1")
    String selectCodeByEmail(String email);
    @Select("select * from email where  email=#{email}")
    Email selectByEmail(String email);
}
