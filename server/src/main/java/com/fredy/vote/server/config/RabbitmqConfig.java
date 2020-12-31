package com.fredy.vote.server.config;

import com.google.common.collect.Maps;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author Fredy
 * @date 2020-12-29 16:37
 */
@Configuration
@Log4j2
public class RabbitmqConfig {

    @Resource
    private Environment env;

    @Resource
    private CachingConnectionFactory connectionFactory;

    @Resource
    private SimpleRabbitListenerContainerFactoryConfigurer factoryConfigurer;

    /**
     * 单一消费者
     * @return
     */
    @Bean(name = "singleListenerContainer")
    public SimpleRabbitListenerContainerFactory listenerContainer(){
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        factory.setConcurrentConsumers(1);
        factory.setMaxConcurrentConsumers(1);
        factory.setPrefetchCount(1);
        return factory;
    }

    /**
     * 多个消费者
     * @return
     */
    @Bean(name = "multiListenerContainer")
    public SimpleRabbitListenerContainerFactory multiListenerContainer(){
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factoryConfigurer.configure(factory,connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        //确认消费模式-NONE
        factory.setAcknowledgeMode(AcknowledgeMode.NONE);
        factory.setConcurrentConsumers(env.getProperty("spring.rabbitmq.listener.simple.concurrency",int.class));
        factory.setMaxConcurrentConsumers(env.getProperty("spring.rabbitmq.listener.simple.max-concurrency",int.class));
        factory.setPrefetchCount(env.getProperty("spring.rabbitmq.listener.simple.prefetch",int.class));
        return factory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(){
        connectionFactory.setPublisherConfirms(true);
        connectionFactory.setPublisherReturns(true);
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                log.info("消息发送成功:correlationData({}),ack({}),cause({})",correlationData,ack,cause);
            }
        });
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                log.warn("消息丢失:exchange({}),route({}),replyCode({}),replyText({}),message:{}",exchange,routingKey,replyCode,replyText,message);
            }
        });
        return rabbitTemplate;
    }

    @Bean
    public MessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // 构建异步发送邮箱通知的消息模型
    @Bean
    public Queue successEmailQueue() {
        return new Queue(env.getProperty("mq.vote.success.email.queue"), true);
    }

    @Bean
    public TopicExchange successEmailExchange() {
        return new TopicExchange(env.getProperty("mq.vote.success.email.exchange"), true, false);
    }

    @Bean
    public Binding successEmailBinding() {
        return BindingBuilder.bind(successEmailQueue()).to(successEmailExchange()).with(env.getProperty("mq.vote.success.email.routing-key"));
    }

    // 构建死信队列
    @Bean
    public Queue successDeadQueue() {
        Map<String, Object> argsMap = Maps.newHashMap();
        argsMap.put("x-dead-letter-exchange", env.getProperty("mq.vote.dead.exchange"));
        argsMap.put("x-dead-letter-routing-key", env.getProperty("mq.vote.dead.routing-key"));
        return new Queue(env.getProperty("mq.vote.dead.queue"),true,false,false,argsMap);
    }

    // 基本交换机
    @Bean
    public TopicExchange successDeadProdExchange() {
        return new TopicExchange(env.getProperty("mq.vote.dead.prod.exchange"), true, false);
    }

    // 创建基本交换机+基本路由 -> 死信队列 的绑定
    @Bean
    public Binding successDeadProdBinding() {
        return BindingBuilder.bind(successDeadQueue()).to(successDeadProdExchange()).with(env.getProperty("mq.vote.dead.prod.routing-key"));
    }

    // 真正的队列
    @Bean
    public Queue successRealQueue() {
        return new Queue(env.getProperty("mq.vote.dead.real.queue"), true);
    }

    // 死信交换机
    @Bean
    public TopicExchange successDeadExchange() {
        return new TopicExchange(env.getProperty("mq.vote.dead.exchange"), true, false);
    }

    // 死信交换机 + 死信路由 -> 真正队列 的绑定
    @Bean
    public Binding successDeadBinding() {
        return BindingBuilder.bind(successRealQueue()).to(successDeadExchange()).with(env.getProperty("mq.vote.dead.routing-key"));
    }
}
