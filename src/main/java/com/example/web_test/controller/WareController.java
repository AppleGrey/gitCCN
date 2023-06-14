package com.example.web_test.controller;

import com.example.web_test.pojo.Result;
import com.example.web_test.pojo.Warehouse;
import com.example.web_test.server.WareServer;
import com.example.web_test.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class WareController {

    @Autowired
    private WareServer wareServer;

    @RequestMapping("/Wares")
    @CrossOrigin
    public Result listWares(HttpServletRequest request) {
        int uID;
        String jwt = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(jwt);
        uID = (int) claims.get("ID");
        List<Warehouse> warehouses = wareServer.getWarehouses(uID);
        log.info("访问仓库列表，ID为" + uID);
        return Result.success(warehouses);
    }

    @PostMapping("/create")
    public Result createWare(int userID, String wName, String description) {
        return null;
    }

    @GetMapping("/getFiles")
    public Result getWareFiles(HttpServletRequest request) {
        int wID = Integer.parseInt(request.getParameter("wID"));
//        int uID;
//        String jwt = request.getHeader("token");
//        Claims claims = JwtUtils.parseJWT(jwt);
//        uID = (int) claims.get("ID");
        Map<String, String> fileMaps = wareServer.getFiles(wID, "D:\\jgitTest\\test1", "master");
        return Result.success(fileMaps);
    }

    //申请创建仓库
    @PostMapping("/applyWare")
    public Result applyWare(HttpServletRequest request) {

        String jwt = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(jwt);
        int uID = (int) claims.get("ID");
        String wName = request.getParameter("wName");
        String description = request.getParameter("description");


        return null;
    }

    @PostMapping("/invite")
//    public Result invite(int sID, String mail, String wName, String content) {
    public Result invite(HttpServletRequest request) {
        String mail = request.getParameter("mail");
        String wName = request.getParameter("wName");
        String content = request.getParameter("content");
        String jwt = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(jwt);
        int sID = (int) claims.get("ID");
        log.info("id为" + sID + "的用户请求将邮箱为" + mail + "的用户拉入仓库:" + wName);
        int feedback = wareServer.invite(sID, mail, wName, content);
        if(feedback == -1) {
            log.info("拉入请求失败，被邀请人未找到");
            return Result.error("Receiver not found");
        } else if (feedback == 0) {
            log.info("邀请人为管理员，拉入成功");
            return Result.success("Invite success!");
        } else if (feedback == 2){
            log.info("邀请人请求审批");
            return Result.success("Request sent");
        } else if (feedback == 1) {
            return Result.error("Already invited!");
        }
        return Result.error("");
    }
}
