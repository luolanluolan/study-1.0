package com.study.order.controller;

import com.study.order.api.OrderService;
import com.study.order.entity.OrderInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author : luolan
 * @Date: 2022-05-26 9:23
 * @Description :
 */
@Slf4j
@RestController
@RequestMapping(value="/api/orderinfo")
public class OrderController {

    @Resource
    private OrderService orderService;

    @PostMapping(value={"/save"})
    public OrderInfoDto insertOrderInfo(OrderInfoDto dto) {
        return this.orderService.insertOrderInfo(dto);
    }

    @GetMapping(value={"/all"})
    public Page<OrderInfoDto> findAllOrderInfo(Pageable pageable) {
        return this.orderService.findAll(pageable);
    }

    @GetMapping(value={"/list/{id}"})
    public OrderInfoDto findOrderInfoByOrderNo(@PathVariable("id") Integer id) {
        return this.orderService.findOrderInfoById(id);
    }

    @PostMapping(value={"/del/{id}"})
    public void insertOrderInfo(@PathVariable("id") Integer id) {
        this.orderService.delOrderInfoById(id);
    }
}

