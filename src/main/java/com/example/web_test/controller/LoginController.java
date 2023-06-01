package com.example.web_test.controller;

import com.example.web_test.pojo.Result;
import com.example.web_test.pojo.User;
import com.example.web_test.server.LoginServer;
import com.example.web_test.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class LoginController {

    @Autowired
    private LoginServer loginServer;

    @PostMapping("/login")
    public Result login(String mail, String password) {
        System.out.println(mail + "," + password);
        List<User> users = loginServer.login(mail, password);
        if(users.size() != 0) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("mail", mail);
            claims.put("ID", users.get(0).getID());
            String jwt = JwtUtils.generateJwt(claims);
            log.info("用户:" + users.get(0).getMail() + "，ID:" + users.get(0).getID() + "登录成功");
            return Result.success(jwt);
        }
        log.info("用户尝试登录失败，邮箱为:" + mail);
        return Result.error("login fail");
    }

    @PostMapping("/register")
    public Result register(String mail, String password, String name) {
        int res = loginServer.register(mail, password, name);
        if(res == -1) {
            return Result.error("邮箱已被注册");
        }
        return Result.success();
    }
}
