package com.example.web_test.server;

import com.example.web_test.pojo.Warehouse;

import java.util.List;

public interface WareServer {

    public List<Warehouse> getWarehouses(int uID);

    public Warehouse createWare(String wName, int adminID, String adminName, String describe);
}
