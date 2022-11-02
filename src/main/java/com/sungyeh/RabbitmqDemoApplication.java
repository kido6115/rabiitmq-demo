package com.sungyeh;

import com.sungyeh.socket.MsgTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@SpringBootApplication
public class RabbitmqDemoApplication {

    @Resource
    RabbitTemplate rabbitTemplate;


    @Resource
    RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;

    @Resource
    private MsgTemplate msgTemplate;

    @PostConstruct
    public void init() {
    }

    public static void main(String[] args) {
        SpringApplication.run(RabbitmqDemoApplication.class, args);
    }

}
