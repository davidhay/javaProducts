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
public class SwatchDeSerializationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SwatchDeSerializationTest.class);

    @Value("classpath:input/swatch.json")
    private Resource swatch;

    private String swatchJson;

    @Before
    public void setup() throws IOException {
        this.swatchJson = StreamUtils.copyToString(swatch.getInputStream(), StandardCharsets.UTF_8);
        LOGGER.debug(this.swatchJson);
    }

    @Test
    public void testJsonToSwatch() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ColorSwatchItem swatch = mapper.readValue(this.swatchJson, ColorSwatchItem.class);
        Assert.assertNotNull(swatch);
        Assert.assertEquals("237494589", swatch.getSkuId());
        Assert.assertEquals("Red", swatch.getBasicColor());
        Assert.assertEquals("Wine", swatch.getColor());
        LOGGER.debug(swatch.toString());
    }

}
