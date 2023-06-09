package com.example.web_test.controller;

import com.example.web_test.pojo.Result;
import com.example.web_test.pojo.User;
import com.example.web_test.server.LoginServer;
import com.example.web_test.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class LoginController {

    @Autowired
    private LoginServer loginServer;

    @PostMapping("/login")
    @CrossOrigin
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

//    @PostMapping("/login")
//    @CrossOrigin
//    public Result login(HttpServletRequest request) {
////        System.out.println(mail + "," + password);
////        List<User> users = loginServer.login(mail, password);
////        if(users.size() != 0) {
////            Map<String, Object> claims = new HashMap<>();
////            claims.put("mail", mail);
////            claims.put("ID", users.get(0).getID());
////            String jwt = JwtUtils.generateJwt(claims);
////            log.info("用户:" + users.get(0).getMail() + "，ID:" + users.get(0).getID() + "登录成功");
////            return Result.success(jwt);
////        }
////        log.info("用户尝试登录失败，邮箱为:" + mail);
////        return Result.error("login fail");
//        Enumeration enu=request.getParameterNames();
//        while(enu.hasMoreElements()){
//            String paraName=(String)enu.nextElement();
//            System.out.println(paraName+": "+request.getParameter(paraName));
//        }
//        System.out.println(request.getRemoteHost());
//        System.out.println(request.getRequestURI());
//        System.out.println(request.getQueryString());
//        return Result.success();
//    }

    @PostMapping("/register")
    @CrossOrigin
    public Result register(String mail, String password, String name) {
        int res = loginServer.register(mail, password, name);
        if(res == -1) {
            return Result.error("邮箱已被注册");
        }
        return Result.success();
    }
}
