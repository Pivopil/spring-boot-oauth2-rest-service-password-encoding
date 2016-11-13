package io.github.pivopil.rest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;

/**
 * Created on 11.11.16.
 */
@Configuration
public class HttpSessionConfig {

    @Bean
    public JedisConnectionFactory connectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    public HttpSessionStrategy httpSessionStrategy() {
        HeaderHttpSessionStrategy headerHttpSessionStrategy = new HeaderHttpSessionStrategy();
        headerHttpSessionStrategy.setHeaderName("Authorization");
        return headerHttpSessionStrategy;
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

}
