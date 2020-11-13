package com.fredy.vote.model.mapper;

import com.fredy.vote.model.entity.UserVoteDetail;
import com.fredy.vote.model.entity.UserVoteDetailExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserVoteDetailMapper {
    long countByExample(UserVoteDetailExample example);

    int deleteByExample(UserVoteDetailExample example);

    int deleteByPrimaryKey(@Param("userId") Integer userId, @Param("voteThemeId") Integer voteThemeId, @Param("voteThemeOptionId") Integer voteThemeOptionId);

    int insert(UserVoteDetail record);

    int insertSelective(UserVoteDetail record);

    List<UserVoteDetail> selectByExample(UserVoteDetailExample example);

    UserVoteDetail selectByPrimaryKey(@Param("userId") Integer userId, @Param("voteThemeId") Integer voteThemeId, @Param("voteThemeOptionId") Integer voteThemeOptionId);

    int updateByExampleSelective(@Param("record") UserVoteDetail record, @Param("example") UserVoteDetailExample example);

    int updateByExample(@Param("record") UserVoteDetail record, @Param("example") UserVoteDetailExample example);

    int updateByPrimaryKeySelective(UserVoteDetail record);

    int updateByPrimaryKey(UserVoteDetail record);
}