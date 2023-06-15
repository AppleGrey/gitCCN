package com.example.web_test.controller;

import com.example.web_test.pojo.Notation;
import com.example.web_test.pojo.Result;
import com.example.web_test.server.ApprovalServer;
import com.example.web_test.server.WareServer;
import com.example.web_test.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
public class ApprovalController {

    @Autowired
    private ApprovalServer approvalServer;

    @Autowired
    private WareServer wareServer;

    @PostMapping("/WareAppr")
    public Result wareAppr(int waID, int grant) {
        if(approvalServer.wareAppr(waID, grant)) {
            if(grant == 1) {
                wareServer.approval(waID);
            }
            return Result.success();
        } else {
            return Result.error("");
        }
    }

    @PostMapping("/SysAppr")
    public Result sysAppr(int saID, int grant) {
        log.info("saID:" + saID + "审批结果为" + grant);
        if(approvalServer.sysAppr(saID, grant)) {
            if(grant == 1) {
                //创建仓库
                wareServer.create(saID);
            }
            return Result.success();
        } else {
            return Result.error("");
        }
    }

    @GetMapping("/Notations")
    public Result getNotations(HttpServletRequest request) {
        String jwt = request.getHeader("token");
//        System.out.println(jwt);
        Claims claims = JwtUtils.parseJWT(jwt);
        int uID = (int) claims.get("ID");
        List<Notation> notations = approvalServer.getAppr(uID);
        log.info("ID为" + uID + "的用户访问公告");
        return Result.success(notations);
    }

}
