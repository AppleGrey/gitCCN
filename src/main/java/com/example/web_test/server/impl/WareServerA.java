package com.example.web_test.server.impl;


import com.example.web_test.mapper.GroupMapper;
import com.example.web_test.mapper.UserMapper;
import com.example.web_test.mapper.WareApprMapper;
import com.example.web_test.mapper.WareMapper;
import com.example.web_test.pojo.Group;
import com.example.web_test.pojo.User;
import com.example.web_test.pojo.WareApproval;
import com.example.web_test.pojo.Warehouse;
import com.example.web_test.server.WareServer;
import com.example.web_test.utils.JGitUtils;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class WareServerA implements WareServer {

    @Autowired
    private WareMapper wareMapper;

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WareApprMapper wareApprMapper;

    @Override
    public List<Warehouse> getWarehouses(int uID) {
        List<Warehouse> wareList = new ArrayList<>();
        List<Group> groups = groupMapper.getWares(uID);
        for(Group group : groups) {
            List<Warehouse> warehouse = wareMapper.list(group.getWID());
            wareList.add(warehouse.get(0));
        }
        return wareList;
    }

    @Override
    public Warehouse createWare(String wName, int adminID, String adminName, String describe) {
        //分配仓库
        return null;
    }

    @Override
    public Map<String, String> getFiles(int wID, String path, String branch) {
        List<Warehouse> wareList = wareMapper.list(wID);
        String warePath = wareList.get(0).getWPath();
        List<String> fileList = null;
        try {
            fileList = JGitUtils.getFiles(path, branch);
        } catch (IOException | GitAPIException e) {
            throw new RuntimeException(e);
        }

        Map<String, String> fileMaps = new HashMap<>();

        for(String l : fileList) {
            if(l.contains("/")) {
                fileMaps.put(l, "dir");
            } else {
                fileMaps.put(l, "file");
            }
        }

        return fileMaps;
    }

    @Override
    public int invite(int sID, String mail, String wName, String content) {
        //判断被邀请人是否存在
        List<User> users = userMapper.getUsers(mail);
        if(users.size() == 0) {
            return -1;//不存在
        }
        int receiverID = users.get(0).getID();

        //判断邀请人是否为仓库管理员
        List<Warehouse> wares = wareMapper.isAdmin(wName, sID);
        if(wares.size() == 0) {
            //不是仓库管理员，发起审批
            if(!initiateApproval(sID, receiverID, wName, 0, content)) {
                return 1;//无法重复发送邀请
            }
            return 2;//成功发起审批
        }

        //是管理员，直接将被邀请人拉入
        inviteInto(sID, receiverID, wName);

        //todo: 通知被邀请人
        return 0;
    }

    @Override
    public void approval(int waID) {
        WareApproval approval = wareApprMapper.getAppr_waID(waID);
        groupMapper.invite(approval.getReceiverID(), approval.getWID());
    }

    //发起审批
    public boolean initiateApproval(int sID, int rID, String wName, int type, String content) {
//        List<Warehouse> wareID = wareMapper.getWareID(sID, wName);
//        int wID = wareID.get(0).getWID();
        int wID = wareMapper.getWareID(wName);
        List<WareApproval> wareApprovals = wareApprMapper.selects(sID, rID, wID);
        if(wareApprovals.size() != 0 && wareApprovals.get(0).getGrant() == 0) {
            return false;
        }
        wareApprMapper.initiate(sID, rID, wID, type, content);
        return true;
    }

    public void inviteInto(int sID, int rID, String wName) {
//        List<Warehouse> wareID = wareMapper.getWareID(sID, wName);
//        int wID = wareID.get(0).getWID();
        int wID = wareMapper.getWareID(wName);
        groupMapper.invite(rID, wID);
    }

    @Override
    public int applyWare(int uID, String wName, String content) {
        //判断仓库是否重名
        Integer wareID = wareMapper.getWareID(wName);
        if(wareID != null) {
            //重名
            return -1;
        }

        return 0;
    }
}
