package com.fredy.vote.server.service.impl;

import com.fredy.vote.api.Exception.CustomizeException;
import com.fredy.vote.api.enums.StatusCode;
import com.fredy.vote.model.entity.*;
import com.fredy.vote.model.mapper.UserVoteDetailMapper;
import com.fredy.vote.model.mapper.VoteThemeOptionExtMapper;
import com.fredy.vote.server.dto.VoteDto;
import com.fredy.vote.server.dto.VoteThemeDto;
import com.fredy.vote.model.mapper.VoteThemeMapper;
import com.fredy.vote.model.mapper.VoteThemeOptionMapper;
import com.fredy.vote.server.service.VoteThemeService;
import com.fredy.vote.server.utils.SnowFlake;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Fredy
 * @date 2020-11-17 10:01
 */
@Service
@Log4j2
public class VoteThemeServiceImpl implements VoteThemeService {

    private SnowFlake snowFlake = new SnowFlake(2, 3);

    @Resource
    private VoteThemeMapper voteThemeMapper;

    @Resource
    private VoteThemeOptionMapper voteThemeOptionMapper;

    @Resource
    private VoteThemeOptionExtMapper voteThemeOptionExtMapper;

    @Resource
    private UserVoteDetailMapper userVoteDetailMapper;



    /**
     * @Description: 添加投票主题(以及对应选项)
     * @Author: Fredy
     * @Date: 2020-11-23
     */
    @Override
    @Transactional
    public void addVoteTheme(VoteThemeDto voteThemeDto) {
        VoteTheme voteTheme = new VoteTheme();
        BeanUtils.copyProperties(voteThemeDto, voteTheme);

        // 新增投票主题
        int res1 = voteThemeMapper.insertSelective(voteTheme);
        if(res1 != 1) {
            log.error("添加投票主题(以及对应选项)，失败！");
            throw new CustomizeException(StatusCode.VOTE_THEME_ADD_FAILED);
        }
        log.info(voteTheme.getId());

        // 新增投票主题对应的选项
        voteThemeDto.getOptions().entrySet().stream()
            .forEach(e -> {
                VoteThemeOption voteThemeOption = new VoteThemeOption();
                voteThemeOption.setVoteThemeId(voteTheme.getId());
                try {
                    voteThemeOption.setValue(Integer.valueOf(e.getKey()));
                } catch(Exception ex) {
                    throw ex;
                }
                voteThemeOption.setLabel(e.getValue());

                int res2 = voteThemeOptionMapper.insertSelective(voteThemeOption);
                if(res2 != 1) {
                    throw new CustomizeException(StatusCode.VOTE_THEME_OPTION_ADD_FAILED);
                }
            });
    }


    /**
     * @Description: 获取单个投票主题
     * @Author: Fredy
     * @Date: 2020-11-23
     */
    @Override
    @Transactional(readOnly = true)
    public VoteThemeDto getVoteTheme(Integer id) {
        // 查询投票主题
        VoteThemeExample voteThemeExample = new VoteThemeExample();
        voteThemeExample.createCriteria().andIdEqualTo(id);
        List<VoteTheme> voteThemes = voteThemeMapper.selectByExample(voteThemeExample);

        if(CollectionUtils.isEmpty(voteThemes)) {
            throw new CustomizeException(StatusCode.VOTE_THEME_NOT_EXIST);
        }

        VoteThemeDto voteThemeDto = new VoteThemeDto();
        BeanUtils.copyProperties(voteThemes.get(0), voteThemeDto);

        // 查询投票主题对应的选项
        VoteThemeOptionExample voteThemeOptionExample = new VoteThemeOptionExample();
        voteThemeOptionExample.createCriteria().andVoteThemeIdEqualTo(voteThemes.get(0).getId());
        List<VoteThemeOption> voteThemeOptionList = voteThemeOptionMapper.selectByExample(voteThemeOptionExample);

        Map<String, String> options = new HashMap<>();
        Map<String, Long> voteNumbers = new HashMap<>();
        voteThemeOptionList.stream().forEach(e -> {
            options.put(String.valueOf(e.getValue()), e.getLabel());
            voteNumbers.put(String.valueOf(e.getValue()), e.getTotal());
        });
        voteThemeDto.setOptions(options);
        voteThemeDto.setVoteNumbers(voteNumbers);

        return voteThemeDto;
    }

    /**
     * @Description: 按排序获取投票主题list
     * @Author: Fredy
     * @Date: 2020-11-24
     */
    public PageInfo getVoteThemeList(Integer pageNo, Integer pageSize, String filed, String direction) {

        VoteThemeExample voteThemeExample = new VoteThemeExample();
        voteThemeExample.setOrderByClause(filed + " " + direction);

        PageHelper.startPage(pageNo, pageSize);
        List<VoteTheme> voteThemeList = voteThemeMapper.selectByExample(voteThemeExample);
        PageInfo pageInfo = new PageInfo(voteThemeList);

        return pageInfo;

    }


    /**
     * @Description: 投票
     * @Author: Fredy
     * @Date: 2020-11-26
     */
    @Override
    @Transactional
    public void vote(VoteDto voteDto, String ip) {
        List<UserVoteDetail> userVoteDetailList = null;

        // 判断用户是否重复投票
        UserVoteDetailExample userVoteDetailExample = new UserVoteDetailExample();
        userVoteDetailExample.createCriteria()
                .andUserIdEqualTo(voteDto.getUserId())
                .andVoteThemeIdEqualTo(voteDto.getVoteThemeId());
        userVoteDetailList = userVoteDetailMapper.selectByExample(userVoteDetailExample);

        if(userVoteDetailList.size() > 0) {
            throw new CustomizeException(StatusCode.VOTE_DUPLICATE);
        }

        // 判断ip是否重复投票
        userVoteDetailExample.clear();
        userVoteDetailExample.createCriteria()
                .andIpEqualTo(ip)
                .andVoteThemeIdEqualTo(voteDto.getVoteThemeId());
        userVoteDetailList = userVoteDetailMapper.selectByExample(userVoteDetailExample);

        if(userVoteDetailList.size() > 0) {
            throw new CustomizeException(StatusCode.VOTE_DUPLICATE);
        }

        // 投票详情入库
        voteDto.getOptionValue().stream().forEach(e -> {
            VoteThemeOptionExample voteThemeOptionExample = new VoteThemeOptionExample();
            voteThemeOptionExample.createCriteria()
                    .andVoteThemeIdEqualTo(voteDto.getVoteThemeId())
                    .andValueEqualTo(e);
            List<VoteThemeOption> voteThemeOptionList = voteThemeOptionMapper.selectByExample(voteThemeOptionExample);

            if(voteThemeOptionList.size() <= 0) {
                throw new CustomizeException(StatusCode.VOTE_THEME_OPTION_NOT_EXIST);
            }

            VoteThemeOption voteThemeOption = voteThemeOptionList.get(0);
            // 插入详情
            UserVoteDetail userVoteDetail = new UserVoteDetail();
            userVoteDetail.setUserId(voteDto.getUserId());
            userVoteDetail.setVoteThemeId(voteDto.getVoteThemeId());
            userVoteDetail.setId(String.valueOf(snowFlake.nextId()));
            userVoteDetail.setIp(ip);
            userVoteDetail.setVoteThemeOptionId(voteThemeOption.getId());
            int res = userVoteDetailMapper.insertSelective(userVoteDetail);
            if(res != 1) {
                throw new CustomizeException(StatusCode.USER_VOTE_DETAIL_ADD_FAILED);
            }

            // 选项投票数+1
            res = voteThemeOptionExtMapper.addVoteOptionTotal(voteThemeOption);
            if(res != 1) {
                throw new CustomizeException(StatusCode.VOTE_FAILED);
            }
        });

    }
}
