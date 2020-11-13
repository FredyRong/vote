package com.fredy.vote.model.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {
    // 用户自增id
    private Integer id;

    // 用户名
    private String username;

    // 用户类型 1：管理员 2：普通用户
    private Integer type;

    // 密码
    private String password;

    // 邮箱
    private String email;
}