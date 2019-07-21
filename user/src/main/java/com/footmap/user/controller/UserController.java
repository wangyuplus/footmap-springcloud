package com.footmap.user.controller;

import com.footmap.user.model.Email;
import com.footmap.user.model.ResponseUser;
import com.footmap.user.model.User;
import com.footmap.user.service.EmailService;
import com.footmap.user.service.UserService;
import com.footmap.user.utils.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 2 * @Author: wangyu
 * 3 * @Date: 2019/7/16 18:35
 * 4
 */
@RestController
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    UserService userService;
    @Autowired
    EmailService emailService;

    /**
     * 根据邮箱获取验证码
     */
    @RequestMapping(path = {"/getCode"},method = {RequestMethod.GET})
    public Response getCode(String email) {
        Email email1=emailService.findByEmail(email);
        if (email1!=null){
            return  new Response(1, "此邮箱已注册");
        }
        try {
            userService.getCode(email);
        } catch (Exception e) {
            logger.error("获取验证码失败", e);
            return new Response(1, "获取验证码失败");
        }
        return  new Response(0, "已发送验证码到邮箱");
    }
    /**
     * 注册
     **/
    @RequestMapping(path = {"/reg"},method = {RequestMethod.POST})
    public Response userRegister(@RequestBody String jsonStr){
        try {
            Map<String,String> map=userService.userRegister(jsonStr);
            if (map.get("msg").equals("注册成功")) {
                // msg为空代表 注册成功
                return new Response(0, map.get("msg"));
            } else {
                return new Response(1, map.get("msg"));
            }
        }catch (Exception e){
            logger.error("userRegister",e);
            return new Response(1,"注册失败");
        }
    }
    /**
     * 登录
     */
    @RequestMapping(path = {"/login"},method = {RequestMethod.GET})
    public Response userLogin(@RequestParam("username") String username, @RequestParam("password") String password, HttpServletResponse response,HttpServletRequest request){
        try {
            Map<String, String> map = userService.userLogin(username, password);
            String tokens = map.get("tokens");
            if (tokens == null) {
                return new Response(1, map.get("msg"));
            }
            Cookie cookie = new Cookie("token", tokens);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
            logger.info("登录成功");
            return new Response(0, "登录成功", username);
        }catch (Exception e){
            logger.error("userLogin:" , e);
            return new Response(1, "登陆失败");
        }

    }
    /**
     * 根据token查找用户
     **/
    @RequestMapping(path = {"/getUserByToken"},method = {RequestMethod.GET})
    public ResponseUser getUserByTokens( @RequestParam("token") String token){
        try {
            User user= userService.findUserByTokens(token);
            if(user==null){
                return  null;
            }
            ResponseUser responseUser=new ResponseUser();
            responseUser.setUid(user.getUid());
            responseUser.setUsername(user.getUsername());
            return responseUser;
        }catch (Exception e){
            logger.error("getUserByToken:" , e);
            return null;
        }

    }
    /**
     * 根据uid查找用户
     **/
    @RequestMapping(path = {"/getUserById"},method = {RequestMethod.GET})
    public ResponseUser getUserByUId(@RequestParam("uid") Integer uid){
        try {
            User user=userService.findUserByUid(uid);
            if(user==null){
                return  null;
            }
            ResponseUser responseUser=new ResponseUser();
            responseUser.setUid(uid);
            responseUser.setUsername(user.getUsername());
            return responseUser;
        }catch (Exception e){
            logger.error("getUserById:" , e);
            return null;
        }

    }
    /**
     * 用户退出
     * @param token
     * @return
     */
    @RequestMapping(path = "/logout", method = {RequestMethod.GET})
    @ResponseBody
    public Response userLogout(@CookieValue("token") String token) {
        try {
            userService.logout(token);
            return new Response(0, "退出成功");
        } catch (Exception e) {
            logger.error("userLogout:" , e);
            return new Response(1, "退出失败");
        }
    }

}