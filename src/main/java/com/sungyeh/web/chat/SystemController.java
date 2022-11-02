package com.sungyeh.web.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sungyeh.bean.Message;
import com.sungyeh.bean.OutputMessage;
import com.sungyeh.socket.MsgTemplate;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerEndpoint;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * SystemController
 *
 * @author sungyeh
 */
@RestController
@RequestMapping(value = "/system", method = RequestMethod.POST)
public class SystemController {

    @Autowired
    private MsgTemplate template;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;

    @RequestMapping("/broadcast")
    public OutputMessage broadcast(@RequestBody Message message) {
        OutputMessage outputMessage = new OutputMessage(new Date().toString(), message);
        template.broadcast(outputMessage);
        return outputMessage;
    }

    @RequestMapping("/send/{user}")
    public OutputMessage broadcast(@PathVariable("user") String user, @RequestBody Message message) throws JsonProcessingException {
        OutputMessage outputMessage = new OutputMessage(new Date().toString(), message);
        ObjectMapper objectMapper = new ObjectMapper();
        this.rabbitTemplate.convertAndSend(user, objectMapper.writeValueAsString(outputMessage));
        return outputMessage;
    }

    @RequestMapping("/stop/{user}")
    public void stop(@PathVariable("user") String user) throws JsonProcessingException {
        Queue q = new Queue(user + "-ccs", true, false, true);

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
        endpoin.setId("test-2");
        endpoin.setQueues(q);

        endpoin.setMessageListener(message -> {
            System.out.println(new String(message.getBody()));
        });
        rabbitListenerEndpointRegistry.registerListenerContainer(endpoin, factory, true);
        rabbitListenerEndpointRegistry.unregisterListenerContainer("test").stop();
    }
}
