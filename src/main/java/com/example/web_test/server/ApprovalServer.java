package com.example.web_test.server;

import com.example.web_test.pojo.Notation;

import java.util.List;

public interface ApprovalServer {

    public List<Notation> getAppr(int uID);

    public boolean wareAppr(int waID, int grant);

}