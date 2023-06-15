package com.example.web_test.server;

import com.example.web_test.pojo.Warehouse;

import java.util.List;
import java.util.Map;

public interface WareServer {

    public List<Warehouse> getWarehouses(int uID);

    public Warehouse createWare(String wName, int adminID, String adminName, String describe);

    public List<Map<String, String>> getFiles(String wName, String path, String branch);

    public int invite(int sID, String mail, String wName, String content);

    public void approval(int waID);

    public int applyWare(int uID, String wName, String content);

    public void create(int saID);

    public int applyLive(int uID, String wName, String content, String sTime);

    public String getReadme(String wName);
}
