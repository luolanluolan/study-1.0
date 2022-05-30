package com.study.order.service;

import com.alibaba.fastjson.JSON;
import com.study.config.RabbitMqConfig;
import com.study.order.api.OrderService;
import com.study.order.entity.OrderInfoDto;
import com.study.order.repository.OrderInfoRepository;
import com.study.util.DateUtils;
import com.study.util.rabbitmq.ISendMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author : luolan
 * @Date: 2022-05-27 17:19
 * @Description :
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderInfoRepository orderInfoRepository;

    @Autowired
    private ISendMessage sendMessage;

    @Override
    public OrderInfoDto insertOrderInfo(OrderInfoDto orderInfoDto) throws RuntimeException{
        orderInfoDto.setOrderNo(DateUtils.getNowToDf2Str());
        orderInfoDto = this.orderInfoRepository.save(orderInfoDto);
        //发送至消息队列
        sendEpayVoucherToMQ(orderInfoDto.getId());
        return orderInfoDto;
    }

    @Override
    public Page<OrderInfoDto> findAll(Pageable pageable) {
        return this.orderInfoRepository.findAll(pageable);
    }

    @Override
    public void delOrderInfoById(Integer id){
        try {
            this.orderInfoRepository.deleteById(id);
        }catch (Exception e){
            throw new RuntimeException("根据ID删除失败："+e.getMessage());
        }
    }

    @Override
    public OrderInfoDto findOrderInfoById(Integer id){
        return this.orderInfoRepository.findById(id).get();
    }

    /**
     * 发送消息到MQ队列
     */
    public void sendEpayVoucherToMQ(Integer id) {
        try {
            Assert.notNull(id, "发送的订单Id为空!");
            sendMessage.sendMessage(RabbitMqConfig.MY_EXCHANGE, RabbitMqConfig.ROUTING_KEY, id);
        }catch (RuntimeException e){
            log.error("发送订单消息至MQ失败：{}", e);
            throw new RuntimeException("发送订单消息至MQ失败："+e);
        }
    }
}
