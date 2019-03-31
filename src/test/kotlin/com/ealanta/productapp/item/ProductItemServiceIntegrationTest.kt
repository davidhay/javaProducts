package com.ealanta.productapp.item;

import mu.KotlinLogging
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.test.context.junit4.SpringRunner
import java.util.*
import kotlin.properties.Delegates
import kotlin.test.assertEquals

@RunWith(SpringRunner::class)
@Ignore
class ProductItemServiceIntegrationTest {

    private val logger = KotlinLogging.logger {}

    @Autowired
    lateinit var env: Environment;

    var sut: ProductItemsSourceImpl by Delegates.notNull()

    @Before
    fun setup() {
        this.sut = ProductItemsSourceImpl("http://localhost:8080/products");
        System.out.println(Arrays.toString(env.getActiveProfiles()));
    }

    @Test
    fun testExternalHttpRequest() {
        val productItems = sut.productItems
        assertEquals(50, productItems.size);
    }
}
