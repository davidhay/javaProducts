package com.ealanta.productapp

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties

@SpringBootApplication
@EnableConfigurationProperties
class ProductsApplication

fun main(args: Array<String>) {
    SpringApplication.run(ProductsApplication::class.java, *args)
}

