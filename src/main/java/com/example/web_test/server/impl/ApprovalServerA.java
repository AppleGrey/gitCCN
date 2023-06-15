package com.example.web_test.server.impl;

import com.example.web_test.mapper.*;
import com.example.web_test.pojo.*;
import com.example.web_test.server.ApprovalServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class ApprovalServerA implements ApprovalServer {

    @Autowired
    private WareApprMapper wareApprMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WareMapper wareMapper;

    @Autowired
    private SysApprMapper sysApprMapper;

    @Autowired
    private NotationMapper notationMapper;


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
                    wareApproval.getContent(), wareApproval.getCreateTime(), uID);
            notations.add(notation);
        }
        //判断用户身份
        User u = userMapper.getUser(uID);
        if(u.getRole() == 1) {
            //是管理员
            List<SysApproval> sysApprovals = sysApprMapper.selects();
            for(SysApproval sysApproval : sysApprovals) {
                User user = userMapper.getUser(sysApproval.getSenderID());
                String type;
                if(sysApproval.getGrant() == 1) {
                    type = "SA-YES";
                } else if (sysApproval.getGrant() == -1) {
                    type = "SA_NO";
                } else {
                    type = "SA";
                }
                Notation notation = new Notation(sysApproval.getSaID(), type, sysApproval.isReadStat(),
                        user.getName(), sysApproval.getWName(), sysApproval.getContent(), sysApproval.getCTime(), uID);
                notations.add(notation);
            }
        }
        List<Notation> notationList = notationMapper.select();
        notations.addAll(notationList);
        //根据时间排序
        notations.sort((n1, n2) -> n2.getSendTime().compareTo(n1.getSendTime()));
        return notations;
    }

    @Override
    public boolean wareAppr(int waID, int grant) {
        wareApprMapper.appr(waID, grant);
        WareApproval wareApproval = wareApprMapper.getAppr_waID(waID);
        User inviter = userMapper.getUser(wareApproval.getInviterID());
        User receiver = userMapper.getUser(wareApproval.getReceiverID());
        User sender = userMapper.getUser(wareApproval.getSenderID());
        List<Warehouse> warehouses = wareMapper.list(wareApproval.getWID());

        String content;

        if(grant == 1) {
            content = "您对" + inviter.getName() + "的邀请已被同意";
        } else {
            content = "您对" + inviter.getName() + "的邀请已被拒绝";
        }

        //发送通知
        notationMapper.createNote(receiver.getName(), warehouses.get(0).getWName(), content, sender.getID());
        return true;
    }

    @Override
    public boolean sysAppr(int saID, int grant) {
        sysApprMapper.appr(saID, grant);
        return true;
    }
}
