package com.ealanta.productapp.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RunWith(SpringRunner.class)
public class ProductItemDeSerializationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductItemDeSerializationTest.class);

    @Value("classpath:input/product1.json")
    private Resource product;

    private String product1Json;

    @Before
    public void setup() throws IOException {
        this.product1Json = StreamUtils.copyToString(product.getInputStream(), StandardCharsets.UTF_8);
        LOGGER.debug(product1Json);
    }

    @Test
    public void testProductJson() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ProductItem productItem = mapper.readValue(this.product1Json, ProductItem.class);
        Assert.assertNotNull(productItem);
        checkProductItem(productItem);
        LOGGER.debug(productItem.toString());
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

    public void checkProductItem(ProductItem item){
        Assert.assertEquals("3525085",item.getProductId());
        Assert.assertEquals("hush Tasha Vest Dress",item.getTitle());
        Assert.assertEquals("99.00",item.getPrice().getWas());
        Assert.assertEquals("75.00",item.getPrice().getThen1());
        Assert.assertEquals("63.75",item.getPrice().getThen2());
        Assert.assertEquals("59.00",item.getPrice().getNow().asText());
        Assert.assertEquals("GBP",item.getPrice().getCurrency());
        Assert.assertEquals(2,item.getColorSwatches().length);
        ColorSwatchItem swatch1 = item.getColorSwatches()[0];
        ColorSwatchItem swatch2 = item.getColorSwatches()[1];

        Assert.assertEquals("Wine",swatch1.getColor());
        Assert.assertEquals("Red",swatch1.getBasicColor());
        Assert.assertEquals("237494589",swatch1.getSkuId());

        Assert.assertEquals("Midnight",swatch2.getColor());
        Assert.assertEquals("Blue",swatch2.getBasicColor());
        Assert.assertEquals("237494562",swatch2.getSkuId());
    }

}
