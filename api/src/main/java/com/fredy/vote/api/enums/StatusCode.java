package com.fredy.vote.api.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * 通用状态码
 * @author Fredy
 * @date 2020-11-16 15:42
 */
@AllArgsConstructor
public enum StatusCode {
    Success(200, "成功！"),
    Fail(-1, "未知错误"),
    InvalidParams(201, "非法的参数！"),
    UserNotLogin(202, "用户未登录！"),
    ;

    @Getter
    private Integer code;
    @Getter
    private String msg;
}
