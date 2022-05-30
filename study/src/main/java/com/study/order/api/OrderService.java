package com.study.order.api;

import com.study.order.entity.OrderInfoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @Author : luolan
 * @Date: 2022-05-27 17:19
 */
public interface OrderService {

    OrderInfoDto insertOrderInfo(OrderInfoDto orderInfoDto);

    Page<OrderInfoDto> findAll(Pageable pageable);

    void delOrderInfoById(Integer id);

    OrderInfoDto findOrderInfoById(Integer id);
}
