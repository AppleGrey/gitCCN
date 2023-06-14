package com.example.web_test.mapper;

import com.example.web_test.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("select * from User where mail = #{mail} and password = #{password}")
    public List<User> login(String mail, String password);

    @Select("select * from User where mail = #{mail}")
    public List<User> getUsers(String mail);

    @Options(keyProperty = "ID", useGeneratedKeys = true)
    @Insert("insert into User(Name, mail, password) values (#{name}, #{mail}, #{password})")
    public void register(User user);

    @Select("select * from User where ID=#{uID}")
    public User getUser(int uID);
}
