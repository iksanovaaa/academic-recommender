package com.shitajimado.academicwritingrecommender.config;

import com.mongodb.MongoClientOptions;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpTraceActuatorConfiguration {
    @Bean
    public HttpTraceRepository httpTraceRepository() {
        return new InMemoryHttpTraceRepository();
    }

    @Bean
    public MongoClientOptions mongoClientOptions() {
        return MongoClientOptions.builder().socketTimeout(20000).build();
    }
}
