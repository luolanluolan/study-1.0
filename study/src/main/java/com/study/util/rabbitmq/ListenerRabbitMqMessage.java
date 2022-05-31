package com.study.util.rabbitmq;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * @Author : luolan
 * @Date: 2022-05-30 20:39
 * @Description :
 */
@Component
@Slf4j
public class ListenerRabbitMqMessage {

    @RabbitListener(queues = "${orderinfo.rabbitmq.queue.name:}")
    public void receiveOrderInfo(@Payload String msg, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, @Header(AmqpHeaders.CHANNEL) Channel channel) {
        try {
//            epayVoucherSign.doBusiness(msg, channel, deliveryTag);
            log.info("实时消息消费完成--{}", msg);
            channel.basicAck(deliveryTag, true);
            channel.close();
        }catch (Exception e) {
            log.error("消费消息失败：{}",e);
            try {

//                deliveryTag:该消息的index multiple：是否批量.true:将一次性拒绝所有小于deliveryTag的消息 requeue：被拒绝的是否重新入队列
//                channel.basicNack(deliveryTag, false, true);
//                deliveryTag:该消息的index multiple：是否批量.true:将一次性ack所有小于deliveryTag的消息。
                channel.basicAck(deliveryTag, true);
                channel.close();
            } catch (Exception ex) {
                log.error("MQ ack and close 失败：{}",ex);
            }

        }
    }
}
