package com.fredy.vote.model.mapper;

import com.fredy.vote.model.entity.VoteThemeOption;
import com.fredy.vote.model.entity.VoteThemeOptionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface VoteThemeOptionMapper {
    long countByExample(VoteThemeOptionExample example);

    int deleteByExample(VoteThemeOptionExample example);

    int deleteByPrimaryKey(@Param("id") Integer id, @Param("voteThemeId") Integer voteThemeId);

    int insert(VoteThemeOption record);

    int insertSelective(VoteThemeOption record);

    List<VoteThemeOption> selectByExample(VoteThemeOptionExample example);

    VoteThemeOption selectByPrimaryKey(@Param("id") Integer id, @Param("voteThemeId") Integer voteThemeId);

    int updateByExampleSelective(@Param("record") VoteThemeOption record, @Param("example") VoteThemeOptionExample example);

    int updateByExample(@Param("record") VoteThemeOption record, @Param("example") VoteThemeOptionExample example);

    int updateByPrimaryKeySelective(VoteThemeOption record);

    int updateByPrimaryKey(VoteThemeOption record);
}