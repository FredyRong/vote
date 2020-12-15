package com.fredy.vote.server.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author Fredy
 * @date 2020-12-15 15:55
 */
@Data
public class LoginDto {

    @NotBlank(message = "用户名不能为空！")
    private String userName;

    @NotBlank(message = "密码不能为空！")
    private String password;
}
