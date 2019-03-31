package com.ealanta.productapp.item;

import com.ealanta.productapp.domain.ProductItems
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import mu.KotlinLogging
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StreamUtils;

import java.nio.charset.StandardCharsets;
import kotlin.properties.Delegates
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@RunWith(SpringRunner::class)
class ProductItemsDeSerializationTest {
    private val logger = KotlinLogging.logger {}

    @Value("classpath:input/products.json")
    lateinit var products:Resource;

    var productsJson:String by Delegates.notNull()
    val mapper = jacksonObjectMapper();

    @Before
    fun setup() {
        this.productsJson = StreamUtils.copyToString(products.getInputStream(), StandardCharsets.UTF_8)
        logger.debug(this.productsJson)
    }

    @Test
    fun testJsonToProducts() {
        val products = mapper.readValue<ProductItems>(this.productsJson)
        assertNotNull(products)
        assertEquals(50,products.products.size)
    }
}
