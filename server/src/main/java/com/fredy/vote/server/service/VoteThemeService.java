package com.fredy.vote.server.service;

import com.fredy.vote.server.dto.UserVoteDetailDto;
import com.fredy.vote.server.dto.VoteDto;
import com.fredy.vote.server.dto.VoteThemeDto;
import com.github.pagehelper.PageInfo;


/**
 * @author Fredy
 * @date 2020-11-17 09:57
 */
public interface VoteThemeService {

    void addVoteTheme(VoteThemeDto voteThemeDto);

    void updateVoteTheme(VoteThemeDto voteThemeDto);

    void deleteVoteTheme(Integer id);

    VoteThemeDto getVoteTheme(Integer id);

    PageInfo getVoteThemeList(Integer pageNo, Integer pageSize, String filed, String direction, boolean allStatus);

    void vote(VoteDto voteDto, String ip) throws Exception;

    UserVoteDetailDto getSpecificUserVoteDetail(Integer userId, Integer voteThemeId);
}
