package com.giftgracious.orderService.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitMQProducer {

    @Value("${rabbitmq.queue.exchange_name}")
    private String exchange;

    @Value("${rabbitmq.queue.routing_key}")
    private String routingKey;

    private final RabbitTemplate rabbitTemplate;

    public void sendMessage (String message){
        log.info("Message preparing to be send");
        rabbitTemplate.convertAndSend(exchange,routingKey,message);    }

}
