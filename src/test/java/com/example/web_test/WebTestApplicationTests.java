package com.example.web_test;

import com.example.web_test.mapper.UserMapper;
import com.example.web_test.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class WebTestApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    void contextLoads() {
        List<User> users = userMapper.login("apple", "123456");
        for (User user : users) {
            System.out.println(user);
        }

    }

}
