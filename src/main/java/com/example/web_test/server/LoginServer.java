package com.example.web_test.server;

import com.example.web_test.pojo.User;

import java.util.List;

public interface LoginServer {

    public List<User> login(String mail, String password);

    public int register(String mail, String password, String name);
}
