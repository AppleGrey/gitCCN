package com.example.web_test;

import com.example.web_test.mapper.UserMapper;
import com.example.web_test.pojo.User;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
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

    @Test
    void gitTest() throws GitAPIException {
        String gitPath = "D:\\jgitTest\\test1";
        Git git = Git.init().setDirectory(new File(gitPath)).call();
        System.out.println(git);
    }

}
