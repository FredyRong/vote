package com.fredy.vote.server.service;

import com.fredy.vote.model.dto.VoteThemeDto;
import com.fredy.vote.model.entity.VoteTheme;
import org.springframework.stereotype.Service;

/**
 * @author Fredy
 * @date 2020-11-17 09:57
 */
public interface VoteThemeService {

    void addVoteTheme(VoteThemeDto voteThemeDto);
}
