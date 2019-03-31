package com.ealanta.productapp.label;

import com.ealanta.productapp.domain.LabelledProduct
import com.ealanta.productapp.domain.PriceInfo;
import com.ealanta.productapp.domain.ColorSwatch;
import com.ealanta.productapp.utils.TestUtils
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import mu.KotlinLogging
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StreamUtils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import kotlin.properties.Delegates

@RunWith(SpringRunner::class)
class LabelledProductSerializationtTest {

    private val logger = KotlinLogging.logger {}

    @Value("classpath:/output/labelledProduct1.json")
    lateinit var labelledProduct:Resource

    var labelledProductJson:String by Delegates.notNull()

    @Before
    fun setup() {
        this.labelledProductJson = StreamUtils.copyToString(labelledProduct.getInputStream(), StandardCharsets.UTF_8);
        logger.debug(this.labelledProductJson);

    }
    @Test
    fun testDeserialize() {
        val product = LabelledProduct()
        product.productId = "productID";
        product.title = "TITLE";
        product.nowPrice = "£12.34";
        val info = PriceInfo();
        info.nowPrice = TestUtils.getMoney("21.12");
        product.priceInfo = info;
        product.priceLabel = "THIS IS THE PRICE LABEL";

        val sw1 = ColorSwatch();
        sw1.rgbColor = "RGB1";
        sw1.color = "COLOR1";
        sw1.basicColor = "BASIC1";
        sw1.skuId = "SKU1";

        val sw2 = ColorSwatch()
        sw2.rgbColor = null;
        sw2.color = "COLOR2";
        sw2.basicColor = "BASIC2";
        sw2.skuId = "SKU2";

        product.colorSwatches.addAll(Arrays.asList(sw1,sw2));

        val mapper = jacksonObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)

        val productJson:String? = mapper.writeValueAsString(product);
        println(productJson)

        JSONAssert.assertEquals(labelledProductJson, productJson, JSONCompareMode.LENIENT);

    }
}
