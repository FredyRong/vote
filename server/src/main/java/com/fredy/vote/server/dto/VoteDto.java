package com.fredy.vote.server.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Fredy
 * @date 2020-11-26 11:53
 */
@Data
public class VoteDto {

    @NotNull(message = "用户ID不能为空！")
    private Integer userId;

    @NotNull(message = "投票主题ID不能为空！")
    private Integer voteThemeId;

    @NotNull(message = "投票选项值不能为空！")
    private List<Integer> optionValue;

    private String ip;
}
