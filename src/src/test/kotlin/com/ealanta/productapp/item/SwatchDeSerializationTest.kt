package com.ealanta.productapp.item;

import com.ealanta.productapp.domain.ColorSwatchItem
import com.ealanta.productapp.domain.PriceDetails
import com.fasterxml.jackson.databind.node.TextNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import mu.KotlinLogging
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.util.StreamUtils
import java.nio.charset.StandardCharsets
import kotlin.properties.Delegates
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@RunWith(SpringRunner::class)
class SwatchDeSerializationTest {

    private val logger = KotlinLogging.logger {}

    @Value("classpath:input/swatch.json")
    lateinit var swatch: Resource

    var swatchJson by Delegates.notNull<String>()

    @Before
    fun setup() {
        this.swatchJson = StreamUtils.copyToString(swatch.getInputStream(), StandardCharsets.UTF_8);
        logger.debug(this.swatchJson);
    }

    @Test
    fun testJsonToSwatch() {
        val mapper = jacksonObjectMapper()
        val swatch = mapper.readValue<ColorSwatchItem>(this.swatchJson)
        assertNotNull(swatch);
        assertEquals("237494589", swatch.skuId);
        assertEquals("Red", swatch.basicColor);
        assertEquals("Wine", swatch.color);
        logger.debug(swatch.toString());
    }

    @Test
    fun testSerialization() {
        val mapper = jacksonObjectMapper()
        val details = PriceDetails().apply{
            was="WAS"
            currency="GBP"
            then1="THEN1"
            then2="THEN2"
            now = TextNode("NOW")
        }
        print(details)
        val json = mapper.writeValueAsString(details);
        print(json)

        val pricedDetails2 = mapper.readValue<PriceDetails>(json)
        println(pricedDetails2)
    }
}
