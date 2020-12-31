package com.fredy.vote.server.service;

import com.fredy.vote.model.entity.VoteTheme;
import com.fredy.vote.model.mapper.VoteThemeMapper;
import com.fredy.vote.server.enums.SysConstant;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @author Fredy
 * @date 2020-12-30 17:20
 */
@Service
@Log4j2
public class RabbitReceiverService {

    @Resource
    private RabbitSenderService rabbitSenderService;

    @Resource
    private VoteThemeMapper voteThemeMapper;

    /**
     * @Description: 投票主题添加或更新后，过期-监听者
     * @Author: Fredy
     * @Date: 2020-12-30
     */
    @RabbitListener(queues = "${mq.vote.dead.real.queue}", containerFactory = "singleListenerContainer")
    public void consumeVoteThemeExpire(byte[] message) {
        VoteTheme voteTheme = (VoteTheme) SerializationUtils.deserialize(message);
        log.info("dead message  10s 后 消费消息 {}", voteTheme);

        VoteTheme now = voteThemeMapper.selectByPrimaryKey(voteTheme.getId());

        if (now.getEndTime().isAfter(voteTheme.getEndTime())) {
            rabbitSenderService.sendVoteThemeExpire(voteTheme.getId());
        } else if (now.getStatus().equals(SysConstant.VoteThemeStatus.SUCCESS.getCode()) && LocalDateTime.now().isAfter(now.getEndTime())) {
            now.setStatus(SysConstant.VoteThemeStatus.EXPIRED.getCode());
            now.setUpdateTime(null);

            voteThemeMapper.updateByPrimaryKeySelective(now);
        }
    }
}
