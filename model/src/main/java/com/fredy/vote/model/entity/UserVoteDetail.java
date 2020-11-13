package com.fredy.vote.model.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class UserVoteDetail implements Serializable {
    // 用户id
    private Integer userId;

    // 投票主题id
    private Integer voteThemeId;

    // 投票选项id
    private Integer voteThemeOptionId;

    // 用户投票详情id
    private String id;

    // ip地址
    private String ip;

    // 投票结果
    private String choice;

    // 创建时间
    private LocalDateTime createTime;
}