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
    SUCCESS(200, "成功！"),
    FAIL(-1, "未知错误"),
    SYSTEM_ERROR(500, "系统错误，请联系管理员"),
    INVALID_PARAMS(501, "非法的参数！"),
    USER_NOT_LOGIN_IN(502, "用户未登录！"),
    VOTE_THEME_ADD_FAILED(503, "添加投票主题失败！"),
    ;

    @Getter
    private Integer code;
    @Getter
    private String msg;
}
