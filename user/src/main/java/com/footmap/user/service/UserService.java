package com.footmap.user.service;

import com.footmap.user.dao.EmailDao;
import com.footmap.user.dao.TokenDao;
import com.footmap.user.dao.UserDao;
import com.footmap.user.model.Email;
import com.footmap.user.model.Token;
import com.footmap.user.model.User;
import com.footmap.user.utils.footMapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import com.alibaba.fastjson.JSON;

import javax.servlet.http.Cookie;
import javax.xml.ws.Response;

/**
 * 2 * @Author: wangyu
 * 3 * @Date: 2019/7/16 18:52
 * 4
 */
@Service
public class UserService {
    @Autowired
    UserDao userDao;
    @Autowired
    EmailDao emailDao;
    @Autowired
    TokenDao tokenDao;
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String username;
    /**
     * 注册
     */
    public Map<String,String> userRegister(String jsonStr){
        Map<String, String> map1 = (Map<String, String>) JSON.parse(jsonStr);
        String username=map1.get("username");
        String password=map1.get("password");
        String code=map1.get("code");
        String email=map1.get("email");
        System.out.println(map1);

        Map<String,String> map=new HashMap<>();
        if (username==null||"".equals(username)){
            map.put("msg","账号不能为空");
            return map;
        }
        if (StringUtils.isEmpty(password)){
            map.put("msg","密码不能为空");
        }
        User user = userDao.findUserByUsername(username);
        if (user != null) {
            map.put("msg", "用户名已经存在");
            return map;
        }
        user=new User();
        user.setUsername(username);
        user.setSalt(UUID.randomUUID().toString().substring(0,3));
        user.setPassword(footMapUtils.MD5(password+user.getSalt()));
        user.setStatus(0);//默认0正常
        String code1=emailDao.selectCodeByEmail(email);

        if (code.equals(code1)) {
            userDao.insertUser(user);
            User user1 = userDao.findUserByUsername(username);
            emailDao.insertUidByemail(user1.getUid(), email);

            map.put("msg", "注册成功");
            return map;
        }
        map.put("msg","验证码错误");
        return map;
    }
    /**
     * 登录
     */
    public Map<String,String> userLogin(String username, String password) {
        Map<String,String>  map = new HashMap<String, String>();
        if (StringUtils.isEmpty(username)) {
            map.put("msg", "用户名不能为空");
            return map;
        }
        if (StringUtils.isEmpty(password)) {
            map.put("msg", "密码不能为空");
            return map;
        }
        //检查身份合法性
        User user = userDao.findUserByUsername(username);
        if (user == null) {
            map.put("msg", "用户不存在");
            return map;
        }
        if (!footMapUtils.MD5(password + user.getSalt()).equals(user.getPassword())) {
            map.put("msg", "密码不正确");
            return map;
        }
        if (user.getStatus() == 1) {
            map.put("msg", "您的账号已经被禁用");
            return map;
        }
        String tokens = addLoginToken(user.getUid());//生成token

        //登陆成功，下发tokens
        map.put("tokens", tokens);
        return map;
    }
    /**
     * 根据uid下发token
     */
    public String addLoginToken(int uid) {
        Token token = new Token();
        token.setUid(uid);
        token.setStatus(0);
        token.setTokens(UUID.randomUUID().toString().replaceAll("-", "") + "user");
        //设置有效期
        Date date = new Date();
        date.setTime(date.getTime() + 1000 * 3600 * 24);
        token.setExpired(date);
        tokenDao.addToken(token);//添加token到数据库
        return token.getTokens();
    }
    /**
     * 根据token获取user
     */
    public User findUserByTokens(String token) {
        if (token == null) {
            return null;
        }
        Integer uid= tokenDao.findUserBytoken(token);
        User user1=userDao.findUserById(uid);
        if (user1 == null) {
            return null;
        }
        return user1;
    }
    /**
    * 获取验证码
    */
    public void getCode(String email) {
        Email em=new Email();
        em.setEmail(email);
        String code=UUID.randomUUID().toString().substring(0,4);
        em.setCode(code);
        emailDao.insertEmail(em);
        SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
        simpleMailMessage.setFrom(username);
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject("注册footmap账户");
        simpleMailMessage.setText("您的验证码是:" + code);
        javaMailSender.send(simpleMailMessage);


    }

    /**
     * 退出
     * @param token
     */
    public void logout(String token) {
        //修改token为禁用
        tokenDao.updateTokenStatus(token, 1);//默认是0,1禁用
    }

    public User findUserByUid(Integer uid) {
        return  userDao.findUserById(uid);

    }
}
