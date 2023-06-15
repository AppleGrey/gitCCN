package com.example.web_test.mapper;

import com.example.web_test.pojo.Notation;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NotationMapper {

    @Select("select * from Notation")
    public List<Notation> select();

    @Insert("insert into Notation(senderName, scope, content, receiverID) VALUES (#{senderName}, #{scope}, #{content}, #{receiverID})")
    public void createNote(String senderName, String scope, String content, int receiverID);

    @Select("select * from Notation where scope=#{wName}")
    public List<Notation> getWareNote(String wName);
}
