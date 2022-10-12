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
}
