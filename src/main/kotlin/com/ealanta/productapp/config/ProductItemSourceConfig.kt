package com.ealanta.productapp.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class ProductItemSourceConfig {

    @Bean
    fun restTemplate():RestTemplate {
        return RestTemplate();
    }
}