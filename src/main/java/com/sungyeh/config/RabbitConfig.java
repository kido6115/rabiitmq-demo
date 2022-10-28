package com.sungyeh.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * RabbitConfig
 *
 * @author sungyeh
 */
@Component
public class RabbitConfig {

    @Bean
    public Queue demoChannel() {
        return new Queue("demo");
    }

    @Bean
    public Queue demo2Channel() {
        return new Queue("demo2");
    }

    @Bean
    public Queue authDelete() {
        Queue queue = new Queue("auto", true, false, true);
        return queue;
    }
}
