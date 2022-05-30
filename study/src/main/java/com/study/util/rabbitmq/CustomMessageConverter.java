package com.study.util.rabbitmq;


import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;

/**
 * 自定义的消息类型转换器
 * Jackson2JsonMessageConverter只支持Java对象转为json，不支持字符串的转换
 * SimpleMessageConverter只支持字符串的转换和简单的Java序列化对象，所以为兼容两者特性创建此自定义转换器
 * 该转换器后期可以根据业务需要自由修改，以确保支持业务复杂多样性
 * @Author : luolan
 * @Date: 2022-05-30 12:12
 * @Description :
 */
public class CustomMessageConverter implements MessageConverter {

    private Jackson2JsonMessageConverter jackson2JsonMessageConverter;

    private SimpleMessageConverter simpleMessageConverter;

    public CustomMessageConverter(Jackson2JsonMessageConverter jackson2JsonMessageConverter
            , SimpleMessageConverter simpleMessageConverter) {
        this.jackson2JsonMessageConverter = jackson2JsonMessageConverter;
        this.simpleMessageConverter = simpleMessageConverter;
    }

    @Override

    public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {

        Message message = null;
        if (messageProperties != null) {
            String contentType = messageProperties.getContentType();
            if (contentType != null && contentType.contains(MessageProperties.CONTENT_TYPE_JSON)) {
                message = jackson2JsonMessageConverter.toMessage(object, messageProperties);
            } else if (contentType != null && contentType.contains(MessageProperties.CONTENT_TYPE_BYTES)
                    || contentType.contains(MessageProperties.CONTENT_TYPE_TEXT_PLAIN)) {
                message = simpleMessageConverter.toMessage(object, messageProperties);
            } else {
                throw new UnsupportedOperationException("不支持的数据类型：" + contentType);
            }
        }
        return message;
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {

        Object content = null;
        MessageProperties properties = message.getMessageProperties();

        if (properties != null) {
            String contentType = properties.getContentType();
            if (contentType != null && contentType.contains(MessageProperties.CONTENT_TYPE_JSON)) {
                content = jackson2JsonMessageConverter.fromMessage(message);
            } else if (contentType != null &&
                    (contentType.contains(MessageProperties.CONTENT_TYPE_BYTES) ||
                            contentType.contains(MessageProperties.CONTENT_TYPE_TEXT_PLAIN) ||
                            contentType.contains(MessageProperties.CONTENT_TYPE_SERIALIZED_OBJECT))) {
                content = simpleMessageConverter.fromMessage(message);
            } else {
                throw new UnsupportedOperationException("不支持的数据类型：" + contentType);
            }

        }
        if (content == null) {
            content = message.getBody();
        }
        return content;
    }
}
