package com.fredy.vote.server.service;

import com.fredy.vote.model.entity.VoteTheme;
import com.fredy.vote.model.mapper.VoteThemeMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.AbstractJavaTypeMapper;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * RabbitMQ发送消息服务
 * @author Fredy
 * @date 2020-12-30 16:48
 */
@Service
@Log4j2
public class RabbitSenderService {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private VoteThemeMapper voteThemeMapper;

    @Resource
    private Environment env;

    /**
     * @Description: 投票主题添加或更新后，加入死信队列，定时修改过期
     * @Author: Fredy
     * @Date: 2020-12-30
     */
    public void sendVoteThemeExpire(final Integer voteThemeId) {
        try{
            VoteTheme info = voteThemeMapper.selectByPrimaryKey(voteThemeId);
            if (info != null) {
                rabbitTemplate.setExchange(env.getProperty("mq.vote.dead.prod.exchange"));
                rabbitTemplate.setRoutingKey(env.getProperty("mq.vote.dead.prod.routing-key"));
                rabbitTemplate.convertAndSend(info, message -> {
                    MessageProperties mp = message.getMessageProperties();
                    mp.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    mp.setHeader(AbstractJavaTypeMapper.DEFAULT_CONTENT_CLASSID_FIELD_NAME, MessageProperties.CONTENT_TYPE_JSON);

                    mp.setExpiration(String.valueOf(Duration.between(LocalDateTime.now(), info.getEndTime()).toMillis()));
                    return message;
                });
            }
        } catch (Exception e) {
            log.error("投票主题添加或更新后，加入死信队列失败：消息为{}", voteThemeId, e.fillInStackTrace());
        }
    }
}
