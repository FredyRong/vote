package com.fredy.vote.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 系统级别的常量
 * @author Fredy
 * @date 2020-11-17 10:09
 */
public class SysConstant {

    @AllArgsConstructor
    public enum UserType {
        ADMIN(1, "管理员"),
        NORMAL(2, "普通用户")
        ;

        @Getter
        private Integer code;
        @Getter
        private String msg;

        @Override
        public String toString() {
            return String.valueOf(getCode());
        }
    }

    @AllArgsConstructor
    public enum VoteThemeStatus {
        DELETED(0, "该投票主题已删除"),
        EXPIRED(1, "该投票主题已过期"),
        PAUSED(2, "该投票主题已被管理员停止投票"),
        SUCCESS(3, "该投票主题已被启用")
        ;

        @Getter
        private Integer code;
        @Getter
        private String msg;


        @Override
        public String toString() {
            return String.valueOf(getCode());
        }
    }
}
