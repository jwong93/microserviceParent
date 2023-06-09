package com.giftgracious.authserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@EnableDiscoveryClient
@SpringBootApplication
public class AuthenticationServerMain {

    public static void main(String[] args) {

        SpringApplication.run(AuthenticationServerMain.class,args);
    }
}