package com.ealanta.productapp.web

import com.ealanta.productapp.misc.UrlValidator
import org.junit.Test
import kotlin.test.assertTrue

class UrlValidationTest {

    private fun check(url: String): Boolean {
        return UrlValidator.isValid(url)
    }

    @Test
    fun testUrlValidation() {
        assertTrue(check("http://localhost:8080/products"))
        assertTrue(check("http://localhost:8080/products/"))
        assertTrue(check("https://localhost:8080/products"))
        assertTrue(check("https://jl-nonprod-syst.apigee.net/v1/categories/600001506/products?key=2ALHCAAs6ikGRBoy6eTHA58RaG097Fma"))
    }
}
