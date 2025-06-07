package com.example.entity;

import lombok.Data;

@Data
public class Apply {

    private Integer id;
    private Integer userId;
    private String title;
    private String content;
    private String time;
    private Integer status;
    private String reason;

    //    非表中字段
    private String username;

}
