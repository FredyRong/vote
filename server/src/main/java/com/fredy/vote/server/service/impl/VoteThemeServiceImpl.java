package com.fredy.vote.server.service.impl;

import com.fredy.vote.api.Exception.CustomizeException;
import com.fredy.vote.api.enums.StatusCode;
import com.fredy.vote.model.dto.VoteThemeDto;
import com.fredy.vote.model.entity.VoteTheme;
import com.fredy.vote.model.mapper.VoteThemeMapper;
import com.fredy.vote.server.service.VoteThemeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Fredy
 * @date 2020-11-17 10:01
 */
@Service
public class VoteThemeServiceImpl implements VoteThemeService {

    @Resource
    private VoteThemeMapper voteThemeMapper;

    @Override
    public void addVoteTheme(VoteThemeDto voteThemeDto) {
//        int res = voteThemeMapper.insert(voteTheme);
//        if(res != 1) {
//            throw new CustomizeException(StatusCode.VOTE_THEME_ADD_FAILED);
//        }
    }
}
