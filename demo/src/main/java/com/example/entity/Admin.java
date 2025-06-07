package com.example.entity;

import lombok.Data;

/**
 * 管理员信息
 */
@Data
public class Admin extends Account {
    private Integer id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private String email;
    private String role;
    private String token;
    private String avatar;

    private String ids;
    private String[] idsArr;

}
