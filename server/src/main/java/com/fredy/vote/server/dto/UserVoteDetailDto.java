package com.fredy.vote.server.dto;

import com.fredy.vote.model.entity.VoteThemeOption;
import lombok.Data;

import java.util.List;

/**
 * @author Fredy
 * @date 2020-12-05 17:10
 */
@Data
public class UserVoteDetailDto {

    private Integer userId;

    private VoteThemeDto voteTheme;

    private List<VoteThemeOption> voteThemeOptionList;

    private List<Integer> userVoteOptionList;
}
