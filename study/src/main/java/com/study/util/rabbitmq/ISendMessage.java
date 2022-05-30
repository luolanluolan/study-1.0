package com.study.util.rabbitmq;

/**
 * @Author : luolan
 * @Date: 2022-05-30 12:12
 * @Description :
 */
public interface ISendMessage {

    /**
     * 普通发送消息方法，不保证消息发送至队列
     *
     * @param exchangeName
     * @param routingKey
     * @param message
     */
    void sendMessage(String exchangeName, String routingKey, Object message);

    /**
     * 保证消息发送至队列，和业务代码事务绑定
     *
     * @param exchangeName
     * @param routingKey
     * @param message
     */
    void sendMessageWithinTransactional(String exchangeName, String routingKey, Object message);

    /**
     * 获取消息队列消息数量
     *
     * @param queueName
     * @return
     */
    long getMessageCount(String queueName);


    /**
     * 确认消息
     *
     * @param deliveryTag
     */
    void ackMessageWithTransactional(String deliveryTag);
}
