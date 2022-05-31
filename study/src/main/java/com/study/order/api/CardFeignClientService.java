package com.study.order.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author : luolan
 * @Date: 2022-05-31 15:38
 */
@FeignClient("card")
public interface CardFeignClientService {

    @GetMapping(value = "card/api/cardinfo/addcardinfo/{id}")
    void addCardInfoByOrderId(@PathVariable("id") Integer id);

}
