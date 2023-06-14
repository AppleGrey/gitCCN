package com.example.web_test.server.impl;

import com.example.web_test.mapper.UserMapper;
import com.example.web_test.pojo.User;
import com.example.web_test.server.LoginServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LoginServerA implements LoginServer {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> login(String mail, String password) {
        return userMapper.login(mail, password);
    }

    @Override
    public int register(String mail, String password, String name) {
        //判断邮箱是否被注册
        List<User> users = userMapper.getUsers(mail);
        if(users.size() != 0) {
            return -1;
        }

        User user = new User();
        user.setMail(mail);
        user.setPassword(password);
        user.setName(name);

        userMapper.register(user);

        return user.getID();
    }
}
