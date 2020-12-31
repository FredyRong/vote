package com.fredy.vote.server.service.impl;

import com.fredy.vote.api.Exception.CustomizeException;
import com.fredy.vote.api.enums.StatusCode;
import com.fredy.vote.model.entity.*;
import com.fredy.vote.model.mapper.*;
import com.fredy.vote.server.dto.UserVoteDetailDto;
import com.fredy.vote.server.dto.VoteDto;
import com.fredy.vote.server.dto.VoteThemeDto;
import com.fredy.vote.server.enums.SysConstant;
import com.fredy.vote.server.service.RabbitSenderService;
import com.fredy.vote.server.service.VoteThemeService;
import com.fredy.vote.server.utils.SnowFlake;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.log4j.Log4j2;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Fredy
 * @date 2020-11-17 10:01
 */
@Service
@Log4j2
public class VoteThemeServiceImpl implements VoteThemeService {

    private final SnowFlake snowFlake = new SnowFlake(2, 3);

    @Resource
    private VoteThemeMapper voteThemeMapper;

    @Resource
    private VoteThemeExtMapper voteThemeExtMapper;

    @Resource
    private VoteThemeOptionMapper voteThemeOptionMapper;

    @Resource
    private VoteThemeOptionExtMapper voteThemeOptionExtMapper;

    @Resource
    private UserVoteDetailMapper userVoteDetailMapper;

    @Resource
    private RabbitSenderService rabbitSenderService;

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

        // 判重
        VoteThemeExample voteThemeExample = new VoteThemeExample();
        voteThemeExample.createCriteria()
                .andTitleEqualTo(voteThemeDto.getTitle());
        List<VoteTheme> voteThemeList = voteThemeMapper.selectByExample(voteThemeExample);
        if(voteThemeList.size() > 0) {
            throw new CustomizeException(StatusCode.VOTE_THEME_DUPLICATE);
        }

        // 新增投票主题
        int res1 = voteThemeMapper.insertSelective(voteTheme);
        if(res1 != 1) {
            throw new CustomizeException(StatusCode.VOTE_THEME_ADD_FAILED);
        }

        // 新增投票主题对应的选项
        voteThemeDto.getOptions().forEach((key, value) -> {
            VoteThemeOption voteThemeOption = new VoteThemeOption();
            voteThemeOption.setVoteThemeId(voteTheme.getId());
            voteThemeOption.setValue(Integer.valueOf(key));
            voteThemeOption.setLabel(value);

            int res2 = voteThemeOptionMapper.insertSelective(voteThemeOption);
            if (res2 != 1) {
                throw new CustomizeException(StatusCode.VOTE_THEME_OPTION_ADD_FAILED);
            }
        });

        if(voteThemeDto.getStatus() == SysConstant.VoteThemeStatus.SUCCESS.getCode()) {
            rabbitSenderService.sendVoteThemeExpire(voteThemeDto.getId());
        }

    }


    /**
     * @Description: 更新投票主题
     * @Author: Fredy
     * @Date: 2020-12-13
     */
    @Override
    @Transactional
    public void updateVoteTheme(VoteThemeDto voteThemeDto) {
        VoteTheme voteTheme = new VoteTheme();
        BeanUtils.copyProperties(voteThemeDto, voteTheme);

        // 更新投票主题
        int res = voteThemeMapper.updateByPrimaryKeySelective(voteTheme);
        if(res != 1) {
            throw new CustomizeException(StatusCode.VOTE_THEME_UPDATE_FAILED);
        }

        // 更新投票主题的选项
        // 统计有多少个选项
        VoteThemeOptionExample voteThemeOptionExample = new VoteThemeOptionExample();
        voteThemeOptionExample.createCriteria()
                .andVoteThemeIdEqualTo(voteThemeDto.getId());
        long count = voteThemeOptionMapper.countByExample(voteThemeOptionExample);

        int size = voteThemeDto.getOptions().size();
        if(count < size) {
            // 更新旧的
            voteThemeDto.getOptions().forEach((key, value) -> {
                VoteThemeOption voteThemeOption = new VoteThemeOption();
                voteThemeOption.setLabel(value);
                voteThemeOption.setUpdateTime(LocalDateTime.now());

                VoteThemeOptionExample voteThemeOptionExample1 = new VoteThemeOptionExample();
                voteThemeOptionExample1.createCriteria()
                        .andVoteThemeIdEqualTo(voteTheme.getId())
                        .andValueEqualTo(Integer.valueOf(key));

                int res2 = voteThemeOptionMapper.updateByExampleSelective(voteThemeOption, voteThemeOptionExample1);
                if (res2 != 1) {
                    throw new CustomizeException(StatusCode.VOTE_THEME_UPDATE_FAILED);
                }
            });

            // 删除多余的
            for(int i = size+1; size <= count; i++) {
                VoteThemeOptionExample voteThemeOptionExample2 = new VoteThemeOptionExample();
                voteThemeOptionExample2.createCriteria()
                        .andVoteThemeIdEqualTo(voteTheme.getId())
                        .andValueEqualTo(i);
                int res3 = voteThemeOptionMapper.deleteByExample(voteThemeOptionExample2);
                if(res3 != 1) {
                    throw new CustomizeException(StatusCode.VOTE_THEME_UPDATE_FAILED);
                }
            }
        } else {
            voteThemeDto.getOptions().forEach((key, value1) -> {
                VoteThemeOption voteThemeOption = new VoteThemeOption();
                voteThemeOption.setLabel(value1);

                int value;
                value = Integer.parseInt(key);

                int res4;
                if (value > count) {
                    // 新的增加
                    voteThemeOption.setValue(value);
                    res4 = voteThemeOptionMapper.insertSelective(voteThemeOption);
                    if (res4 != 1) {
                        throw new CustomizeException(StatusCode.VOTE_THEME_OPTION_ADD_FAILED);
                    }
                } else {
                    // 旧的更新
                    VoteThemeOptionExample voteThemeOptionExample1 = new VoteThemeOptionExample();
                    voteThemeOptionExample1.createCriteria()
                            .andVoteThemeIdEqualTo(voteTheme.getId())
                            .andValueEqualTo(value);

                    res4 = voteThemeOptionMapper.updateByExampleSelective(voteThemeOption, voteThemeOptionExample1);
                    if (res4 != 1) {
                        throw new CustomizeException(StatusCode.VOTE_THEME_UPDATE_FAILED);
                    }
                }
            });
        }

        if(voteThemeDto.getStatus() == SysConstant.VoteThemeStatus.SUCCESS.getCode()) {
            rabbitSenderService.sendVoteThemeExpire(voteThemeDto.getId());
        }
    }


    /**
     * @Description: 删除投票主题
     * @Author: Fredy
     * @Date: 2020-12-13
     */
    @Override
    public void deleteVoteTheme(Integer id) {
        VoteTheme voteTheme = new VoteTheme();
        voteTheme.setId(id);
        voteTheme.setStatus(SysConstant.VoteThemeStatus.DELETED.getCode());

        int res = voteThemeMapper.updateByPrimaryKeySelective(voteTheme);
        if(res != 1) {
            throw new CustomizeException(StatusCode.VOTE_THEME_UPDATE_FAILED);
        }
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
        List<VoteTheme> voteThemes = voteThemeExtMapper.selectAll(voteThemeExample);

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
        voteThemeOptionList.forEach(e -> {
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
    public PageInfo getVoteThemeList(Integer pageNo, Integer pageSize, String filed, String direction, boolean allStatus) {

        VoteThemeExample voteThemeExample = new VoteThemeExample();
        voteThemeExample.setOrderByClause(filed + " " + direction);
        if(!allStatus) {
            voteThemeExample.createCriteria().andStatusEqualTo(SysConstant.VoteThemeStatus.SUCCESS.getCode());
        }

        PageHelper.startPage(pageNo, pageSize);
        List<VoteTheme> voteThemeList = voteThemeExtMapper.selectAll(voteThemeExample);
        PageInfo pageInfo = new PageInfo(voteThemeList);

        return pageInfo;

    }

    @Resource
    private RedissonClient redissonClient;

    /**
     * @Description: 投票
     * @Author: Fredy
     * @Date: 2020-11-26
     */
    @Override
    @Transactional
    public void vote(VoteDto voteDto, String ip) throws Exception {
        VoteTheme voteTheme = voteThemeMapper.selectByPrimaryKey(voteDto.getVoteThemeId());

        // 判断所投票选项是否符合单选
        if(voteTheme.getSelectType().equals(SysConstant.VoteThemeSelectType.SELECT.getCode()) && voteDto.getOptionValue().size() != 1) {
            throw new CustomizeException(StatusCode.INVALID_PARAMS);
        }

        // 判断是否能投票
        if(!voteTheme.getStatus().equals(SysConstant.VoteThemeStatus.SUCCESS.getCode())) {
            throw new CustomizeException(StatusCode.VOTE_INVALID);
        }

        // 判断选项是否存在
        VoteThemeOptionExample voteThemeOptionExample = new VoteThemeOptionExample();
        voteThemeOptionExample.createCriteria()
                .andVoteThemeIdEqualTo(voteDto.getVoteThemeId())
                .andValueIn(voteDto.getOptionValue());
        List<VoteThemeOption> voteThemeOptionList = voteThemeOptionMapper.selectByExample(voteThemeOptionExample);
        if(voteThemeOptionList.size() != voteDto.getOptionValue().size()) {
            throw new CustomizeException(StatusCode.VOTE_THEME_OPTION_NOT_EXIST);
        }

        // 判断是否重复投票
        if(!validateVote(voteDto, ip)) {
            throw new CustomizeException(StatusCode.VOTE_DUPLICATE);
        }

        final String lockKey = String.valueOf(voteDto.getVoteThemeId()) + voteDto.getUserId() + "-RedissonLock";
        RLock lock = redissonClient.getLock(lockKey);

        try {
            boolean cacheRes = lock.tryLock(30, 10, TimeUnit.SECONDS);
            if(cacheRes) {
                // 再次判断是否重复投票
                if(!validateVote(voteDto, ip)) {
                    throw new CustomizeException(StatusCode.VOTE_DUPLICATE);
                }

                // 投票详情入库
                voteThemeOptionList.forEach(e -> {
                    // 插入详情
                    UserVoteDetail userVoteDetail = new UserVoteDetail();
                    userVoteDetail.setUserId(voteDto.getUserId());
                    userVoteDetail.setVoteThemeId(voteDto.getVoteThemeId());
                    userVoteDetail.setId(String.valueOf(snowFlake.nextId()));
                    userVoteDetail.setIp(ip);
                    userVoteDetail.setVoteThemeOptionId(e.getId());
                    int res = userVoteDetailMapper.insertSelective(userVoteDetail);
                    if(res != 1) {
                        throw new CustomizeException(StatusCode.USER_VOTE_DETAIL_ADD_FAILED);
                    }

                    // 选项投票数+1
                    res = voteThemeOptionExtMapper.addVoteOptionTotal(e);
                    if(res != 1) {
                        throw new CustomizeException(StatusCode.VOTE_FAILED);
                    }
                });
            }
        } finally {
            lock.unlock();
        }
    }

    private boolean validateVote(VoteDto voteDto, String ip) {
        List<UserVoteDetail> userVoteDetailList = null;

        // 判断用户是否重复投票
        UserVoteDetailExample userVoteDetailExample = new UserVoteDetailExample();
        userVoteDetailExample.createCriteria()
                .andUserIdEqualTo(voteDto.getUserId())
                .andVoteThemeIdEqualTo(voteDto.getVoteThemeId());
        userVoteDetailList = userVoteDetailMapper.selectByExample(userVoteDetailExample);

        if(userVoteDetailList.size() > 0) {
            return false;
        }

        // 判断ip是否重复投票
        userVoteDetailExample.clear();
        userVoteDetailExample.createCriteria()
                .andIpEqualTo(ip)
                .andVoteThemeIdEqualTo(voteDto.getVoteThemeId());
        userVoteDetailList = userVoteDetailMapper.selectByExample(userVoteDetailExample);

        if(userVoteDetailList.size() > 0) {
            return false;
        }

        return true;
    }


    /**
     * @Description: 获取特定的用户投票详情
     * @Author: Fredy
     * @Date: 2020-11-30
     */
    @Override
    public UserVoteDetailDto getSpecificUserVoteDetail(Integer userId, Integer voteThemeId) {
        UserVoteDetailExample userVoteDetailExample = new UserVoteDetailExample();
        userVoteDetailExample.createCriteria()
                .andUserIdEqualTo(userId)
                .andVoteThemeIdEqualTo(voteThemeId);
        List<UserVoteDetail> voteDetailList = userVoteDetailMapper.selectByExample(userVoteDetailExample);

        if(voteDetailList.size() == 0) {
            throw new CustomizeException(StatusCode.USER_VOTE_DETAIL_NOT_EXIST);
        }

        UserVoteDetailDto userVoteDetailDto = new UserVoteDetailDto();
        userVoteDetailDto.setUserId(userId);

        // 查询投票主题
        VoteTheme voteTheme = voteThemeMapper.selectByPrimaryKey(voteDetailList.get(0).getVoteThemeId());
        VoteThemeDto voteThemeDto = new VoteThemeDto();
        BeanUtils.copyProperties(voteTheme, voteThemeDto);
        userVoteDetailDto.setVoteTheme(voteThemeDto);

        // 查询投票主题对应所有选项的结果
        VoteThemeOptionExample voteThemeOptionExample = new VoteThemeOptionExample();
        voteThemeOptionExample.createCriteria()
                .andVoteThemeIdEqualTo(voteThemeId);
        List<VoteThemeOption> voteThemeOptionList = voteThemeOptionMapper.selectByExample(voteThemeOptionExample);
        userVoteDetailDto.setVoteThemeOptionList(voteThemeOptionList);

        // 查询所投票的选项
        List<Integer> userVoteOptionList = new ArrayList<>();
        voteDetailList.forEach(e -> userVoteOptionList.add(e.getVoteThemeOptionId()));
        userVoteDetailDto.setUserVoteOptionList(userVoteOptionList);

        return userVoteDetailDto;
    }
}
