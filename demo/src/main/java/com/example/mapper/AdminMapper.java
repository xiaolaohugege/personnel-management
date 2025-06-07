package com.example.mapper;

import com.example.entity.Admin;
import com.example.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminMapper{

    List<Admin> getListAdmin(Admin admin);

    List<Admin> getListAdmin();


    Admin selectAdminById(Integer id);

    int addAdmin(Admin admin);

    List<Admin> selectAdmin(Admin admin);

    int deleteAdminById(Integer id);

    Admin selectAdminByName(String username);

    void update(Admin admin);


//    void deleteBatch(Admin admin);
}

