package com.giftgracious.orderService.controller;

import com.giftgracious.orderService.dto.OrderRequestDTO;
import com.giftgracious.orderService.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody OrderRequestDTO orderRequestDTO){
        log.info(orderRequestDTO.getItemDTOS().toString());
        orderService.placeOrder(orderRequestDTO);
        return "Order Placed Successfully" + orderRequestDTO.getItemDTOS();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OrderRequestDTO> getAllOrders (){
        return orderService.getallCurrentOrders();
    }
}
