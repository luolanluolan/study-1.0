package com.study.order.repository;

import com.study.order.entity.OrderInfoDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface OrderInfoRepository extends JpaRepository<OrderInfoDto, Integer> {

    List<OrderInfoDto> findByOrderNo(String orderNo);

    List<OrderInfoDto> findByOrderStatus(String orderStatus);

}