package com.sungyeh.mq.sender;

import com.sungyeh.bean.DemoObject;
import lombok.Setter;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Sender
 *
 * @author sungyeh
 */
@Component
public class Sender {

    @Setter(onMethod_ = @Autowired)
    RabbitTemplate rabbitTemplate;

    // 發送消息
    public void send(String msg) {
        System.out.println("Sender : " + msg);
        this.rabbitTemplate.convertAndSend("demo", msg);
    }

    public void send(DemoObject msg) {
        System.out.println("Sender : " + msg);
        this.rabbitTemplate.convertAndSend("demo2", msg);
    }

}
