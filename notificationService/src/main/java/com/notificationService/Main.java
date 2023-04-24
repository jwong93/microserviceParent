package com.notificationService;

import com.notificationService.eventHandler.OrderPlacedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@Slf4j
@EnableDiscoveryClient
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class,args);
    }

    @KafkaListener(topics = "notificationTopic")
    public void handleNotification (OrderPlacedEvent orderPlacedEvent){
        log.info("Order Received. Sending email for Order No "+orderPlacedEvent.getOrderNumber());
    }
}