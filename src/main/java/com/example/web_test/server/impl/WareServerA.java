package com.example.web_test.server.impl;


import com.example.web_test.mapper.GroupMapper;
import com.example.web_test.mapper.WareMapper;
import com.example.web_test.pojo.Group;
import com.example.web_test.pojo.Warehouse;
import com.example.web_test.server.WareServer;
import com.example.web_test.utils.JGitUtils;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class WareServerA implements WareServer {

    @Autowired
    private WareMapper wareMapper;

    @Autowired
    private GroupMapper groupMapper;

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
            fileList = JGitUtils.getFiles(warePath, branch);
        } catch (IOException | GitAPIException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
