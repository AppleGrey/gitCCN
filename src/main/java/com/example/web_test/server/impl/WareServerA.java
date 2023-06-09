package com.example.web_test.server.impl;


import com.example.web_test.mapper.GroupMapper;
import com.example.web_test.mapper.WareMapper;
import com.example.web_test.pojo.Group;
import com.example.web_test.pojo.Warehouse;
import com.example.web_test.server.WareServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
}
