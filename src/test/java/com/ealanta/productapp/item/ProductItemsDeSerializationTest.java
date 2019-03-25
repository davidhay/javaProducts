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
public class ProductItemsDeSerializationTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductItemsDeSerializationTest.class);

    @Value("classpath:input/products.json")
    private Resource products;

    private String productsJson;

    @Before
    public void setup() throws IOException {
        this.productsJson = StreamUtils.copyToString(products.getInputStream(), StandardCharsets.UTF_8);
        LOGGER.debug(this.productsJson);
    }

    @Test
    public void testJsonToProducts() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ProductItems products = mapper.readValue(this.productsJson, ProductItems.class);
        Assert.assertNotNull(products);
        Assert.assertEquals(50,products.getProducts().size());
    }
}
