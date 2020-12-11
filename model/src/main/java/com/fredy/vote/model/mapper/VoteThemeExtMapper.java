package com.fredy.vote.model.mapper;

import com.fredy.vote.model.entity.VoteTheme;
import com.fredy.vote.model.entity.VoteThemeExample;

import java.util.List;

/**
 * @author Fredy
 * @date 2020-12-08 23:55
 */
public interface VoteThemeExtMapper {

    List<VoteTheme> selectAll(VoteThemeExample example);
}
