package com.example.web_test.controller;

import com.example.web_test.pojo.Notation;
import com.example.web_test.pojo.Result;
import com.example.web_test.server.ApprovalServer;
import com.example.web_test.server.WareServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class ApprovalController {

    @Autowired
    private ApprovalServer approvalServer;

    @Autowired
    private WareServer wareServer;

    @PostMapping("/WareAppr")
    public Result WareAppr(int waID, int grant) {
        if(approvalServer.wareAppr(waID, grant)) {
            if(grant == 1) {
                wareServer.approval(waID);
            }
            return Result.success();
        } else {
            return Result.error("");
        }
    }

    @GetMapping("/Notations")
    public Result getNotations(int uID) {
        List<Notation> notations = approvalServer.getAppr(uID);
        return Result.success(notations);
    }

}
