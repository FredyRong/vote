package com.fredy.vote.model.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class VoteTheme implements Serializable {
    // 投票主题自增id
    private Integer id;

    // 标题
    private String title;

    // 描述
    private String description;

    // 选项类型，1：单选 2：多选
    private Integer selectType;

    // 状态，0：已删除 1：已过期 2：暂停 3：启用
    private Integer status;

    // 投票开始时间
    private LocalDateTime startTime;

    // 投票结束时间
    private LocalDateTime endTime;

    // 创建时间
    private LocalDateTime createTime;

    // 更新时间
    private LocalDateTime updateTime;

    // 自定义属性
    private Boolean canVote;
}