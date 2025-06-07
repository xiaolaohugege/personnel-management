package com.example.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 管理员信息
 */
@Data
public class User extends Account {
    private Integer id;
    private String username;
    private String name;
    private String password;
    private String phone;
    private String email;
    private String role;
    private String avatar;

    private String ids;
    private String[] idsArr;
    private String token;
    private BigDecimal salary;
}

