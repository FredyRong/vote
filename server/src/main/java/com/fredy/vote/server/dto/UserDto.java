package com.fredy.vote.server.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * @author Fredy
 * @date 2020-12-15 17:43
 */
@Data
public class UserDto {

    private Integer id;

    private Integer type;

    @NotBlank(message = "用户名不能为空！")
    private String userName;

    @NotBlank(message = "密码不能为空！")
    private String password;

    @NotBlank(message = "邮箱不能为空！")
    @Email(message = "邮箱格式错误！")
    private String email;
}
