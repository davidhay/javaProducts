package com.ealanta.productapp.label;

import com.ealanta.productapp.label.LabelledProduct;
import com.ealanta.productapp.misc.TestUtils;
import com.ealanta.productapp.price.PriceInfo;
import com.ealanta.productapp.product.ColorSwatch;
import com.ealanta.productapp.product.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@RunWith(SpringRunner.class)
public class LabelledProductSerializationtTest {

    private Logger LOGGER = LoggerFactory.getLogger(LabelledProductSerializationtTest.class);

    @Value("classpath:/output/labelledProduct1.json")
    private Resource labelledProduct;

    private String labelledProductJson;

    @Before
    public void setup() throws IOException, JSONException {
        this.labelledProductJson = StreamUtils.copyToString(labelledProduct.getInputStream(), StandardCharsets.UTF_8);
        LOGGER.debug(this.labelledProductJson);

    }
    @Test
    public void testDeserialize() throws IOException,JSONException {
        LabelledProduct product = new LabelledProduct();
        product.setProductId("productID");
        product.setTitle("TITLE");
        product.setNowPrice("£12.34");
        PriceInfo info = new PriceInfo();
        info.setNowPrice(TestUtils.getMoney("21.12"));
        product.setPriceInfo(info);
        product.setPriceLabel("THIS IS THE PRICE LABEL");

        ColorSwatch sw1 = new ColorSwatch();
        sw1.setRgbColor("RGB1");
        sw1.setColor("COLOR1");
        sw1.setBasicColor("BASIC1");
        sw1.setSkuId("SKU1");

        ColorSwatch sw2 = new ColorSwatch();
        sw2.setRgbColor(null);
        sw2.setColor("COLOR2");
        sw2.setBasicColor("BASIC2");
        sw2.setSkuId("SKU2");

        product.getColorSwatches().addAll(Arrays.asList(sw1,sw2));

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        String productJson = mapper.writeValueAsString(product);

        JSONAssert.assertEquals(labelledProductJson, productJson, JSONCompareMode.LENIENT);

    }
}
