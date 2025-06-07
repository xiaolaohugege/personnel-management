package com.example.mapper;

import com.example.entity.Admin;
import com.example.entity.User;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    List<User> getListUser(User user);

    int addUser(User user);

    User selectUserById(Integer id);

    int update(User user);

    int deleteUserById(int id);

    User selectUserByName(String username);
}
