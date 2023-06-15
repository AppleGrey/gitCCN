package com.example.web_test.server.impl;


import com.example.web_test.mapper.*;
import com.example.web_test.pojo.*;
import com.example.web_test.server.WareServer;
import com.example.web_test.utils.JGitUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
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

    @Autowired
    private SysApprMapper sysApprMapper;

    @Autowired
    private NotationMapper notationMapper;

    @Override
    public void create(int saID) {
        SysApproval sysAppr = sysApprMapper.getSysAppr(saID);
        User user = userMapper.getUser(sysAppr.getSenderID());

        Warehouse ware = createWare(sysAppr.getWName(), sysAppr.getSenderID(), user.getName(), sysAppr.getContent());
        groupMapper.invite(sysAppr.getSenderID(), ware.getWID());
        String content = "您的仓库:" + sysAppr.getWName() + "创建请求已通过，现在您可以通过如下url克隆: git@123.57.181.79:" + ware.getWPath();
        notationMapper.createNote("系统", sysAppr.getWName(), content, sysAppr.getSenderID());
    }

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
        String rootPath = "D:/Warehouse/";
        rootPath += "user_" + adminID + "/";
        rootPath += wName + ".git";
        try {
            Git git = Git.init().setDirectory(new File(rootPath)).call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
        //将信息添加到数据库中
        Warehouse warehouse = new Warehouse();
        warehouse.setDescribe(describe);
        warehouse.setAdminID(adminID);
        warehouse.setWPath(rootPath);
        warehouse.setWName(wName);
        wareMapper.insertWare(warehouse);
        return warehouse;
    }

    @Override
    public Map<String, String> getFiles(int wID, String path, String branch) {
        List<Warehouse> wareList = wareMapper.list(wID);
        String warePath = wareList.get(0).getWPath();
        List<String> fileList = null;
        try {
            fileList = JGitUtils.getFiles("D:\\jgitTest\\test1", branch);
        } catch (IOException | GitAPIException e) {
            throw new RuntimeException(e);
        }

        Map<String, String> fileMaps = new HashMap<>();

        String[] pathDir = path.split("/");

        for(String l : fileList) {
//            if(l.contains("/")) {
//                fileMaps.put(l, "dir");
//            } else {
//                fileMaps.put(l, "file");
//            }
            String[] names = l.split("/");
            if(path.compareTo("") == 0) {
                if(names.length == 1) {
                    fileMaps.put(names[0], "file");
                } else {
                    fileMaps.put(names[0], "dir");
                }
                continue;
            }
            boolean isOK = true;
            for(int i = 0; i < pathDir.length; i++) {
                if(i < names.length && names[i].compareTo(pathDir[i]) != 0) {
                    isOK = false;
                    break;
                }
            }
            if(isOK) {
                if(names.length == pathDir.length + 1) {
                    fileMaps.put(names[pathDir.length], "file");
                } else {
                    fileMaps.put(names[pathDir.length], "dir");
                }
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
        int iID = users.get(0).getID();

        //判断邀请人是否为仓库管理员
        List<Warehouse> wares = wareMapper.isAdmin(wName, sID);
        List<Warehouse> warehouses = wareMapper.list_name(wName);
        if(wares.size() == 0) {
            //不是仓库管理员，发起审批
            if(!initiateApproval(sID, warehouses.get(0).getAdminID(), wName, 0, content, iID)) {
                return 1;//无法重复发送邀请
            }
            return 2;//成功发起审批
        }

        //是管理员，直接将被邀请人拉入
        inviteInto(sID, iID, wName);

        //通知被邀请人
        User admin = userMapper.getUser(sID);
        String noteContent = "你被" + admin.getName() + "拉入了仓库：" + wName;
        notationMapper.createNote(users.get(0).getName(), wName, noteContent, iID);
        return 0;
    }

    @Override
    public void approval(int waID) {
        WareApproval approval = wareApprMapper.getAppr_waID(waID);
        groupMapper.invite(approval.getInviterID(), approval.getWID());
    }

    //发起审批
    public boolean initiateApproval(int sID, int rID, String wName, int type, String content, int iID) {
//        List<Warehouse> wareID = wareMapper.getWareID(sID, wName);
//        int wID = wareID.get(0).getWID();
        int wID = wareMapper.getWareID(wName);
        List<WareApproval> wareApprovals = wareApprMapper.selects(sID, rID, wID);
        if(wareApprovals.size() != 0 && wareApprovals.get(0).getGrant() == 0) {
            return false;
        }
        wareApprMapper.initiate(sID, rID, wID, type, content, iID);
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
        sysApprMapper.sysAppr(uID, wName, content, 0);
        return 0;
    }

    public void createNote(String senderName, String scope, int receiverID, String content) {


    }
}
