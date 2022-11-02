package com.sungyeh.socket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sungyeh.bean.OutputMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerEndpoint;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import javax.annotation.Resource;

/**
 * STOMPConnectEventListener
 *
 * @author sungyeh
 */
@Component
@Slf4j
public class STOMPConnectEventListener implements ApplicationListener<SessionConnectEvent> {


    @Autowired
    private WebSocketSessions sessions;

    @Resource
    RabbitTemplate rabbitTemplate;


    @Resource
    private MsgTemplate msgTemplate;


    @Resource
    RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;

    @Override
    public void onApplicationEvent(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String user = accessor.getNativeHeader("user").get(0);
        String sessionId = accessor.getSessionId();
        sessions.registerSessionId(user, sessionId);
        log.info("user login, user:{}, sessionId:{}", user, sessionId);
        log.info(sessions.toString());


        Queue q = new Queue(user, true, false, true);
        Queue q2 = new Queue(user + "-ccs", true, false, true);

        RabbitAdmin rabbitAdmin = new RabbitAdmin(rabbitTemplate.getConnectionFactory());
        rabbitAdmin.declareQueue(q);
        rabbitAdmin.declareQueue(q2);

        this.rabbitTemplate.convertAndSend(user + "-ccs", "安安");
        this.rabbitTemplate.convertAndSend(user + "-ccs", "出事了");

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
            ObjectMapper mapper = new ObjectMapper();
            OutputMessage outputMessage = null;
            try {
                outputMessage = mapper.readValue(new String(message.getBody()), OutputMessage.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            msgTemplate.sendMsgToUser(user, outputMessage);
        });
        rabbitListenerEndpointRegistry.registerListenerContainer(endpoin, factory, true);
    }
}
