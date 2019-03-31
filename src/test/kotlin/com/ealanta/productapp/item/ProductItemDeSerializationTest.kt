package com.ealanta.productapp.item;

import com.ealanta.productapp.domain.ColorSwatchItem
import com.ealanta.productapp.domain.ProductItem
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
class ProductItemDeSerializationTest {

    private val logger = KotlinLogging.logger {}

    @Value("classpath:input/product1.json")
    lateinit var product: Resource

    var product1Json: String by Delegates.notNull();
    val mapper = jacksonObjectMapper();

    @Before
    fun setup() {
        this.product1Json = StreamUtils.copyToString(product.getInputStream(), StandardCharsets.UTF_8);
        logger.debug(product1Json)
    }

    @Test
    fun testProductJson() {
        val productItem = mapper.readValue<ProductItem>(this.product1Json)
        assertNotNull(productItem);
        checkProductItem(productItem);
        logger.debug(productItem.toString());
    }

    /*
    {
  "productId": "3525085",
  "title": "hush Tasha Vest Dress",
  "price": {
    "was": "99.00",
    "then1": "75.00",
    "then2": "63.75",
    "now": "59.00",
    "uom": "",
    "currency": "GBP"
  },
  "colorSwatches": [
    {
      "color": "Wine",
      "basicColor": "Red",
      "skuId": "237494589"
    },
    {
      "color": "Midnight",
      "basicColor": "Blue",
      "skuId": "237494562"
    }
  ]
}*/

    fun checkProductItem(item: ProductItem) {
        assertEquals("3525085", item.productId);
        assertEquals("hush Tasha Vest Dress", item.title);

        item.price?.let { pd ->
            assertEquals("99.00", pd.was)
            assertEquals("75.00", pd.then1)
            assertEquals("63.75", pd.then2)
            assertEquals("59.00", pd.now?.asText())
            assertEquals("GBP", pd.currency)

        }
        item.colorSwatches?.let { sws ->
            assertEquals(2, sws.size)
            val swatch1: ColorSwatchItem = sws[0]
            val swatch2: ColorSwatchItem = sws[1]
            assertEquals("Wine", swatch1.color)
            assertEquals("Red", swatch1.basicColor)
            assertEquals("237494589", swatch1.skuId)

            assertEquals("Midnight", swatch2.color)
            assertEquals("Blue", swatch2.basicColor)
            assertEquals("237494562", swatch2.skuId)
        }
    }
}
