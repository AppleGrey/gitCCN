package com.example.web_test.server;

import com.example.web_test.pojo.Notation;

import java.util.List;

public interface ApprovalServer {

    public List<Notation> getAppr(int uID);

    public boolean wareAppr(int waID, int grant);

    public boolean sysAppr(int saID, int grant);

    public List<Notation> getWareNotations(String wName);

    public boolean liveAppr(int laID, int grant);
}
