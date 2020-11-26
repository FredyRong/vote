package com.fredy.vote.server.service;

import com.fredy.vote.server.dto.VoteDto;
import com.fredy.vote.server.dto.VoteThemeDto;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author Fredy
 * @date 2020-11-17 09:57
 */
public interface VoteThemeService {

    void addVoteTheme(VoteThemeDto voteThemeDto);

    VoteThemeDto getVoteTheme(Integer id);

    PageInfo getVoteThemeList(Integer pageNo, Integer pageSize, String filed, String direction);

    void vote(VoteDto voteDto, String ip);
}
