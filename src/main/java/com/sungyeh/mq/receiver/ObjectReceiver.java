package com.sungyeh.mq.receiver;

import com.sungyeh.bean.DemoObject;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Receiver
 *
 * @author sungyeh
 */
@Component
@RabbitListener(queues = "demo2")
public class ObjectReceiver {

    @RabbitHandler
    public void process(DemoObject message) {
        System.out.println("Receiver : " + message);
    }

}
