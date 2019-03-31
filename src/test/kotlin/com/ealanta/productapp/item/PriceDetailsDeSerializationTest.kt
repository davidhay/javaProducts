package com.ealanta.productapp.item;

import com.ealanta.productapp.domain.PriceDetails
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import mu.KotlinLogging
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.util.StreamUtils
import java.nio.charset.StandardCharsets
import kotlin.properties.Delegates

@RunWith(SpringRunner::class)
class PriceDetailsDeSerializationTest {

    private val logger = KotlinLogging.logger {}

    @Value("classpath:input/price1.json")
    lateinit var price1: Resource

    @Value("classpath:input/price2.json")
    lateinit var price2: Resource

    var price1json: String by Delegates.notNull()
    var price2json: String by Delegates.notNull()

    @Before
    fun setup() {
        this.price1json = StreamUtils.copyToString(price1.getInputStream(), StandardCharsets.UTF_8);
        logger.debug(this.price1json);

        this.price2json = StreamUtils.copyToString(price2.getInputStream(), StandardCharsets.UTF_8);
        logger.debug(this.price2json);
    }

    @Test
    fun testPriceDetails1() {
        val mapper = jacksonObjectMapper()
        val details = mapper.readValue<PriceDetails>(this.price1json)
        Assert.assertEquals("99.00", details.was);
        Assert.assertEquals("75.00", details.then1);
        Assert.assertEquals("63.75", details.then2);
        Assert.assertEquals("59.00", details.now?.asText());
        Assert.assertEquals("GBP", details.currency);
        logger.debug(details.toString());
    }

    @Test
    fun testPriceDetails2() {
        val mapper = jacksonObjectMapper()
        val details = mapper.readValue<PriceDetails>(this.price2json)
        Assert.assertNotNull(details);
        Assert.assertEquals("85.00", details.was)
        Assert.assertEquals("68.00", details.then1)
        Assert.assertEquals("", details.then2)
        Assert.assertTrue(details.now!!.isObject)
        Assert.assertEquals("GBP", details.currency)
        logger.debug(details.toString())
    }

}
