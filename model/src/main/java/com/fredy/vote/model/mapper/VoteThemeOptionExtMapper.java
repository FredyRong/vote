package com.fredy.vote.model.mapper;

import com.fredy.vote.model.entity.UserVoteDetail;
import com.fredy.vote.model.entity.VoteThemeOption;

/**
 * @author Fredy
 * @date 2020-11-26 17:16
 */
public interface VoteThemeOptionExtMapper {

    int addVoteOptionTotal(VoteThemeOption record);
}
