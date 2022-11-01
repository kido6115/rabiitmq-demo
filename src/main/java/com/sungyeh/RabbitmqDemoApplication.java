package com.sungyeh;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerEndpoint;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
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

    @PostConstruct
    public void init() {
        Queue q = new Queue("dynamic", true, false, true);

        RabbitAdmin rabbitAdmin = new RabbitAdmin(rabbitTemplate.getConnectionFactory());
        rabbitAdmin.declareQueue(q);

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(rabbitTemplate.getConnectionFactory());
        //当前消费者数量
        factory.setConcurrentConsumers(1);
        //最大消费者数量
        factory.setMaxConcurrentConsumers(5);
        //是否使用重队列
        factory.setDefaultRequeueRejected(false);
        //自动签收
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        SimpleRabbitListenerEndpoint endpoin = new SimpleRabbitListenerEndpoint();
        endpoin.setId("test");
        endpoin.setQueues(q);
        endpoin.setMessageListener(message -> {
            String msg = new String(message.getBody());
            System.err.println("----------消费者: " + msg);
        });
        System.out.println(rabbitAdmin.getQueueInfo("dynamic"));
        rabbitListenerEndpointRegistry.registerListenerContainer(endpoin, factory, true);
        rabbitListenerEndpointRegistry.unregisterListenerContainer("test").stop();
        System.out.println(rabbitListenerEndpointRegistry.getListenerContainerIds());
        System.out.println(rabbitAdmin.getQueueInfo("dynamic"));
    }

    public static void main(String[] args) {
        SpringApplication.run(RabbitmqDemoApplication.class, args);
    }

}
