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
public class PriceDetailsDeSerializationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PriceDetailsDeSerializationTest.class);

    @Value("classpath:input/price1.json")
    private Resource price1;

    @Value("classpath:input/price2.json")
    private Resource price2;

    private String price1json;
    private String price2json;

    @Before
    public void setup() throws IOException {
        this.price1json = StreamUtils.copyToString(price1.getInputStream(), StandardCharsets.UTF_8);
        LOGGER.debug(this.price1json);

        this.price2json = StreamUtils.copyToString(price2.getInputStream(), StandardCharsets.UTF_8);
        LOGGER.debug(this.price2json);
    }

    @Test
    public void testPriceDetails1() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        PriceDetails details = mapper.readValue(this.price1json, PriceDetails.class);
        Assert.assertNotNull(details);
        Assert.assertEquals("99.00",details.getWas());
        Assert.assertEquals("75.00",details.getThen1());
        Assert.assertEquals("63.75",details.getThen2());
        Assert.assertEquals("59.00",details.getNow().asText());
        Assert.assertEquals("GBP",details.getCurrency());
        LOGGER.debug(details.toString());
    }

    @Test
    public void testPriceDetails2() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        PriceDetails details = mapper.readValue(this.price2json, PriceDetails.class);
        Assert.assertNotNull(details);
        Assert.assertEquals("85.00",details.getWas());
        Assert.assertEquals("68.00",details.getThen1());
        Assert.assertEquals("",details.getThen2());
        Assert.assertTrue(details.getNow().isObject());
        Assert.assertEquals("GBP",details.getCurrency());
        LOGGER.debug(details.toString());
    }

}
