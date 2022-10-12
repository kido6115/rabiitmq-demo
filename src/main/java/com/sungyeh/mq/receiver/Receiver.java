package com.sungyeh.mq.receiver;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Receiver
 *
 * @author sungyeh
 */
@Component
@RabbitListener(queues = "demo")
public class Receiver {
    
    @RabbitHandler
    public void process(String message) {
        System.out.println("Receiver : " + message);
    }

}
