package com.giftgracious.orderService.controller;

import com.giftgracious.orderService.dto.OrderRequestDTO;
import com.giftgracious.orderService.model.Order;
import com.giftgracious.orderService.service.OrderService;
import com.giftgracious.orderService.service.RabbitMQProducer;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    private final RabbitMQProducer mqProducer;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name="inventory", fallbackMethod = "fallbackMethod")
    @TimeLimiter(name="inventory")
    public CompletableFuture<String> placeOrder(@RequestBody OrderRequestDTO orderRequestDTO){
        log.info(orderRequestDTO.getItemDTOS().toString());
        Long id = orderService.placeOrder(orderRequestDTO);
        log.info("Order Id" + id);
        return CompletableFuture.supplyAsync(()->orderService.confirmationOrder(id));
    }

    public CompletableFuture<String> fallbackMethod(OrderRequestDTO orderRequestDTO, RuntimeException exception){
        return CompletableFuture.supplyAsync(()->"Unable to retrieve Inventory Information");
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OrderRequestDTO> getAllOrders (){
        return orderService.getallCurrentOrders();
    }

}
