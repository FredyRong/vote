package com.fredy.vote.model.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class VoteThemeOption implements Serializable {
    // 投票选项自增id
    private Integer id;

    // 投票主题id
    private Integer voteThemeId;

    // 选项值
    private Integer value;

    // 选项描述
    private String label;

    // 选项投票总数
    private Long total;

    // 创建时间
    private LocalDateTime createTime;

    // 更新时间
    private LocalDateTime updateTime;
}