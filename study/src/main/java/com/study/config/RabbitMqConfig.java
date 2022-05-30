package com.study.config;

import com.study.util.rabbitmq.CustomMessageConverter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionNameStrategy;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager;
import org.springframework.amqp.support.ConsumerTagStrategy;
import org.springframework.amqp.support.converter.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.ErrorHandler;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author : luolan
 * @Date: 2022-05-30 12:12
 * @Description :
 */
@Slf4j
@Configuration
public class RabbitMqConfig {

    /**
     * rabbitmq 参数
     */
    @Value("${spring.rabbitmq.addresses}")
    private String addresses;
    @Value("${spring.rabbitmq.username}")
    private String username;
    @Value("${spring.rabbitmq.password}")
    private String password;
    @Value("${orderinfo.rabbitmq.concurrentConsumer}")
    private String concurrentConsumer;
    @Value("${orderinfo.rabbitmq.maxConcurrentConsumer}")
    private String maxConcurrentConsumer;
    @Value("${orderinfo.rabbitmq.idleEventInterval}")
    private String idleEventInterval;
    @Value("${orderinfo.rabbitmq.backPolicyInitialInterval}")
    private String backPolicyInitialInterval;
    @Value("${orderinfo.rabbitmq.backPolicyMultiplier}")
    private String backPolicyMultiplier;
    @Value("${orderinfo.rabbitmq.backPolicyMaxInterval}")
    private String backPolicyMaxInterval;

    @Value("${orderinfo.rabbitmq.queue.name:}")
    private String  MY_QUEUE; //队列名
    public static String MY_EXCHANGE = "my_local_exchange"; //交换机
    public static String ROUTING_KEY = "my_local_routing_key";  //路由键


    /**
     * 声明队列
     * @return
     */
    @Bean
    public Queue generateQueue() {
        /*
          name：MY_QUEUE-队列名称
          durable：队列是否可持久化，默认为true。
                 true：持久化队列，队列的声明会存放到Erlang自带的Mnesia数据库中，所以该队列在服务器重新启动后继续存在。
                 false：非持久化队列，队列的声明会存放到内存中，当服务器关闭时内存会被清除，所以该队列在服务器重新启动后不存在。
          exclusive：队列是否具有排它性，默认为false
                 true：
                    a . 当连接关闭时connection.close()该队列会自动删除；
                    b . 会对该队列加锁，其他通道channel不能访问该队列。如果强制访问会报异常下述异常，即一个队列只能有一个消费者消费

          autoDelete：队列没有任何订阅的消费者时是否自动删除，默认为false
                false：可以通过RabbitMQ Management，查看某个队列的消费者数量。
                true：当consumers = 0时队列就会自动删除。
          arguments：一个Map集合，是AMQP协议留给AMQP实现做扩展使用的。
        */
        return new Queue(MY_QUEUE, true, false, false, null);
    }

    /**
     * 声明交换器--DirectExchange
     * @return
     */
    @Bean
    public DirectExchange generateDirectExchange() {
        return new DirectExchange(MY_EXCHANGE, true, false, null);
    }

    /*
     * 绑定--队列和交换机通过routingKey绑定
     * @return
     */
    public Binding generateBinding(){
        return BindingBuilder.bind(generateQueue()).to(generateDirectExchange()).with(ROUTING_KEY);
    }

    /**
     * 连接工厂命名策略
     *
     * @return
     */
    @Bean
    public ConnectionNameStrategy connectionNameStrategy() {
        return new ConnectionNameStrategy() {
            @Override
            public String obtainNewConnectionName(ConnectionFactory connectionFactory) {
                return "study-orderinfo 连接";
            }
        };
    }


    @Bean
    public ConnectionFactory rabbitConnectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setConnectionNameStrategy(connectionNameStrategy());
        connectionFactory.setChannelCacheSize(60);
        // 可以是设置当前实例的名称或者其他属性，在控制台的connenction中可以查看
        connectionFactory.getRabbitConnectionFactory().getClientProperties().put("study-orderinfo", ServerConfig.getInstanceId());
        // 设置发送消息异常，会添加默认的监听，输出错误日志
        connectionFactory.setPublisherReturns(true);
        connectionFactory.setAddresses(addresses);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        return connectionFactory;
    }

    /**
     * 普通发送消息rabbitTemplate类
     * 使用单独的发送链接
     * 重试和退避策略
     * 消息发送至交换器找不到队列，记录日志
     *
     * @return
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory rabbitConnectionFactory, MessageConverter customMessageConverter, RabbitTemplate.ReturnCallback returnCallback) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(rabbitConnectionFactory);
        /**
         * 一般情况下可以使用setUsePublisherConnection参数为生产者新建连接
         * 但是，如果使用独占队列的话，必须将setUsePublisherConnection参数值设为false
         * 因为RabbitAdmin为Listener容器声明独占队列，如果设置为true，则会导致容器和独占队列的连接使用的是不同的连接
         * 导致容器不能使用队列
         */
//        rabbitTemplate.setUsePublisherConnection(true);
        RetryTemplate retryTemplate = new RetryTemplate();
        // 重试策略，设置最大重试次数，和异常类型
        SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy(3, Collections.<Class<? extends Throwable>, Boolean>singletonMap(Exception.class, true));
        // 幂等恢复策略
        // BackOffPolicy的实现，可以使用指数函数增加给定集中每次重试尝试的退避时间。此实现是线程安全的，适用于并发访问。对配置的修改不会影响任何正在进行的重试集。
        // setInitialInterval（long）属性控制传递给Math.exp（double）的初始值，而setMultiplier（double）属性控制此值在每次后续尝试中增加多少
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        // 初始化时间间隔,不是上来就开始
        backOffPolicy.setInitialInterval(Long.parseLong(backPolicyInitialInterval));
        // 乘数
        backOffPolicy.setMultiplier(Double.parseDouble(backPolicyMultiplier));
        // 最大时间间隔
        backOffPolicy.setMaxInterval(Long.parseLong(backPolicyMaxInterval));
        retryTemplate.setBackOffPolicy(backOffPolicy);
        retryTemplate.setRetryPolicy(simpleRetryPolicy);
        rabbitTemplate.setRetryTemplate(retryTemplate);
        // 设置消息转换器
        rabbitTemplate.setMessageConverter(customMessageConverter);
        // 设置消息通过exchange找不到队列时返回消息的标识，true为返回，false为不返回
        rabbitTemplate.setMandatory(true);
        // 设置消息路由失败时的回调
        rabbitTemplate.setReturnCallback(returnCallback);
        return rabbitTemplate;
    }

    /**
     * 事务RabbitTemplate不需要重试，失败就直接回滚
     */
    @Bean
    public RabbitTemplate transactionalRabbitTemplate(ConnectionFactory rabbitConnectionFactory, MessageConverter customMessageConverter, RabbitTemplate.ReturnCallback returnCallback) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(rabbitConnectionFactory);
        rabbitTemplate.setChannelTransacted(true);
//        rabbitTemplate.setUsePublisherConnection(true);
        rabbitTemplate.setMessageConverter(customMessageConverter);
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnCallback(returnCallback);
        return rabbitTemplate;
    }

    /**
     * Json转换
     *
     * @param classMapper
     * @return
     */
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter(ClassMapper classMapper) {
        Jackson2JsonMessageConverter jsonConverter = new Jackson2JsonMessageConverter();
        jsonConverter.setClassMapper(classMapper);
        return jsonConverter;
    }

    @Bean
    public SimpleMessageConverter simpleMessageConverter() {
        return new SimpleMessageConverter();
    }

    /**
     * 消息转换器的类匹配规则，需要的在这里添加就可以
     *
     * @return
     */
    @Bean
    public DefaultClassMapper classMapper() {
        DefaultClassMapper classMapper = new DefaultClassMapper();
        Map<String, Class<?>> classMapping = new HashMap<>();
        classMapper.setIdClassMapping(classMapping);
        return classMapper;
    }

    /**
     * 消息发送失败的一个监听,用于监听消息发送后找不到队列
     *
     * @return
     */
    @Bean
    public RabbitTemplate.ReturnCallback returnCallback() {
        return new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int i, String s, String s1, String s2) {
                log.error("支付系统发送消息未找到交换器信息，报错信息如下：replyText-{}, exchange-{}, routingKey-{}, body-{}", s, s1, s2, new String(message.getBody()));
            }
        };
    }

    /**
     * 声明事务管理器，使得rabbitmq的事务能够接入业务代码的事务管理器
     *
     * @param rabbitConnectionFactory
     * @return
     */
    @Bean
    public RabbitTransactionManager transactionManager(ConnectionFactory rabbitConnectionFactory) {
        return new RabbitTransactionManager(rabbitConnectionFactory);
    }

    /**
     * RabbitListenerContainer工厂类的Bean，使得使用RabbitListener注解时，能够默认使用connectionFactory
     *
     * @param rabbitConnectionFactory
     * @param customMessageConverter
     * @return
     */
    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(ConnectionFactory rabbitConnectionFactory, CustomMessageConverter customMessageConverter, ConsumerTagStrategy consumerTagStrategy, ErrorHandler errorHandler) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(rabbitConnectionFactory);
        factory.setConsumerTagStrategy(consumerTagStrategy);
        // 设置消费者并发数
        factory.setConcurrentConsumers(Integer.parseInt(concurrentConsumer));
        // 设置消费者最大并发数
        factory.setMaxConcurrentConsumers(Integer.parseInt(maxConcurrentConsumer));
        // 设置容器空闲事件间隔时长
        factory.setIdleEventInterval(Long.parseLong(idleEventInterval));
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        factory.setMessageConverter(customMessageConverter);
        factory.setErrorHandler(errorHandler);
        return factory;
    }


    @Bean
    public ErrorHandler errorHandler() {
        return new ErrorHandler() {
            @Override
            @SneakyThrows
            public void handleError(Throwable throwable) {
                throw throwable;
            }
        };
    }

    @Bean
    public ConsumerTagStrategy consumerTagStrategy() {
        return new ConsumerTagStrategy() {
            @Override
            public String createConsumerTag(String s) {
                return new StringBuilder("fpay-pay:consumer-").append(ServerConfig.getInstanceId()).append(" queue-").append(s).toString();
            }
        };
    }

    /**
     * 自定义消息转化器
     * @param jsonMessageConverter
     * @param simpleMessageConverter
     * @return
     */
    @Bean
    public CustomMessageConverter customMessageConverter(Jackson2JsonMessageConverter jsonMessageConverter, SimpleMessageConverter simpleMessageConverter) {
        return new CustomMessageConverter(jsonMessageConverter, simpleMessageConverter);
    }


















}
