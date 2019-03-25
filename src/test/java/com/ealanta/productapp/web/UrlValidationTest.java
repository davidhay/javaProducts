package com.ealanta.productapp.web;

import org.junit.Assert;
import org.junit.Test;

public class UrlValidationTest {

    private boolean check(String url){
        return UrlValidator.isValid(url);
    }

    @Test
    public void testUrlValidation(){
        Assert.assertTrue(check("http://localhost:8080/products"));
        Assert.assertTrue(check("http://localhost:8080/products/"));
        Assert.assertTrue(check("https://localhost:8080/products"));
        Assert.assertTrue(check("https://jl-nonprod-syst.apigee.net/v1/categories/600001506/products?key=2ALHCAAs6ikGRBoy6eTHA58RaG097Fma"));
    }
}
