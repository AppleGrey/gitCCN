package com.example.web_test.controller;

import com.example.web_test.pojo.Result;
import com.example.web_test.pojo.Warehouse;
import com.example.web_test.server.WareServer;
import com.example.web_test.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class WareController {

    @Autowired
    private WareServer wareServer;

    @GetMapping("/Wares")
    public Result listWares(HttpServletRequest request) {
        int uID;
        String jwt = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(jwt);
        uID = (int) claims.get("ID");
        List<Warehouse> warehouses = wareServer.getWarehouses(uID);
        return Result.success(warehouses);
    }

    @PostMapping("/create")
    public Result createWare(HttpServletRequest request) {
        //Todo: 比对jwt令牌

        String user_id_str = request.getParameter("userID");
        return null;
    }

    @GetMapping("/getFiles")
    public Result getWareFiles(int wID, String path) {
        //Todo: 判断是否有读的权限
        return null;
    }
}
