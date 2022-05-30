package com.study.util.rabbitmq;

import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author : luolan
 * @Date: 2022-05-30 12:12
 * @Description :
 */
@Service
@Slf4j
public class SendMessageImpl implements ISendMessage {


    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private RabbitTemplate transactionalRabbitTemplate;

    @Override
    public void sendMessage(String exchangeName, String routingKey, Object message) {
        rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
    }

    @Override
    public void sendMessageWithinTransactional(String exchangeName, String routingKey, Object message) {
        transactionalRabbitTemplate.convertAndSend(exchangeName, routingKey, message);
    }

    @SneakyThrows
    @Override
    public long getMessageCount(String queueName) {
        ConnectionFactory connectionFactory = null;
        Connection connection = null;
        Channel channel = null;
        long count = 0;
        try {
            connectionFactory = rabbitTemplate.getConnectionFactory();
            connection = connectionFactory.createConnection();
            channel = connection.createChannel(false);
            count = channel.messageCount(queueName);
            log.info("消息数量：{}", count);
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (channel != null) {
                channel.close();
            }
        }
        return count;
    }

    @SneakyThrows
    @Override
    public void ackMessageWithTransactional(String deliveryTag) {
        ConnectionFactory connectionFactory = null;
        Connection connection = null;
        Channel channel = null;
        try {
            connectionFactory = rabbitTemplate.getConnectionFactory();
            connection = connectionFactory.createConnection();
            channel = connection.createChannel(true);
            channel.basicAck(Long.parseLong(deliveryTag), true);
            log.info("确认消息deliveryTag：{}", deliveryTag);
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (channel != null) {
                channel.close();
            }
        }
    }

}
