package com.fredy.vote.api.enums;

import lombok.AllArgsConstructor;
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
    VOTE_THEME_OPTION_ADD_FAILED(504, "添加投票主题选项失败！"),
    VOTE_THEME_NOT_EXIST(505, "不存在该投票主题！"),
    IP_FAILED(506, "获取ip地址失败！"),
    VOTE_DUPLICATE(507, "不能重复投票！"),
    VOTE_THEME_OPTION_NOT_EXIST(508, "不存在该投票选项！"),
    USER_VOTE_DETAIL_ADD_FAILED(509,"添加用户投票信息失败！"),
    VOTE_FAILED(510,"投票失败！"),
    USER_VOTE_DETAIL_NOT_EXIST(511, "不存在该用户投票信息！"),
    VOTE_THEME_DUPLICATE(512, "重复的投票主题！"),
    VOTE_THEME_UPDATE_FAILED(513, "更新投票主题失败！"),
    VOTE_THEME_OPTION_UPDATE_FAILED(514, "更新投票主题选项失败！"),
    VOTE_INVALID(515, "当前投票主题未启用！")
    ;

    @Getter
    private Integer code;
    @Getter
    private String msg;
}
