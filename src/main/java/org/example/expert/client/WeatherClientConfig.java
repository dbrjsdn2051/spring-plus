package org.example.expert.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
public class WeatherClientConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(List.of(new MappingJackson2HttpMessageConverter()));
    }
}
