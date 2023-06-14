package com.example.web_test.server.impl;

import com.example.web_test.mapper.UserMapper;
import com.example.web_test.mapper.WareApprMapper;
import com.example.web_test.mapper.WareMapper;
import com.example.web_test.pojo.Notation;
import com.example.web_test.pojo.User;
import com.example.web_test.pojo.WareApproval;
import com.example.web_test.pojo.Warehouse;
import com.example.web_test.server.ApprovalServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ApprovalServerA implements ApprovalServer {

    @Autowired
    private WareApprMapper wareApprMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WareMapper wareMapper;

    @Override
    public List<Notation> getAppr(int uID) {
        List<WareApproval> wareApprovals = wareApprMapper.getAppr_rID(uID);
        List<Notation> notations = new ArrayList<>();
        for (WareApproval wareApproval : wareApprovals) {
            User user = userMapper.getUser(wareApproval.getSenderID());
            List<Warehouse> warehouses = wareMapper.list(wareApproval.getWID());
            String type;
            if(wareApproval.getGrant() == 1) {
                type = "WA-YES";
            } else if (wareApproval.getGrant() == -1) {
                type = "WA_NO";
            } else {
                type = "WA";
            }
            Notation notation = new Notation(wareApproval.getWaID(), type,
                    wareApproval.isReadStat(), user.getName(), warehouses.get(0).getWName(),
                    wareApproval.getContent(), wareApproval.getCreateTime());
            notations.add(notation);
        }
        return notations;
    }

    @Override
    public boolean wareAppr(int waID, int grant) {
        wareApprMapper.appr(waID, grant);
        return true;
    }
}
