package com.fredy.vote.model.mapper;

import com.fredy.vote.model.entity.VoteTheme;
import com.fredy.vote.model.entity.VoteThemeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface VoteThemeMapper {
    long countByExample(VoteThemeExample example);

    int deleteByExample(VoteThemeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(VoteTheme record);

    int insertSelective(VoteTheme record);

    List<VoteTheme> selectByExample(VoteThemeExample example);

    VoteTheme selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") VoteTheme record, @Param("example") VoteThemeExample example);

    int updateByExample(@Param("record") VoteTheme record, @Param("example") VoteThemeExample example);

    int updateByPrimaryKeySelective(VoteTheme record);

    int updateByPrimaryKey(VoteTheme record);
}