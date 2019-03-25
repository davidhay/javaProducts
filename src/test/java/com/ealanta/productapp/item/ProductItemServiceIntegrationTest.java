package com.ealanta.productapp.item;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@Ignore
public class ProductItemServiceIntegrationTest {


    @Autowired
    Environment env;

    private ProductItemsSourceImpl sut;

    @Before
    public void setup(){
        this.sut = new ProductItemsSourceImpl("http://localhost:8080/products");
        System.out.println(Arrays.toString(env.getActiveProfiles()));
    }

    @Test
    public void testExternalHttpRequest() {
        List<ProductItem> productItems = sut.getProductItems();
        Assert.assertEquals(50,productItems.size());
    }
}
