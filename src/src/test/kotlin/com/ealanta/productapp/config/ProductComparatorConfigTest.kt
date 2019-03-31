package com.ealanta.productapp.config

import com.ealanta.productapp.domain.PriceInfo
import com.ealanta.productapp.domain.PriceReduction
import com.ealanta.productapp.domain.Product
import com.ealanta.productapp.utils.TestUtils
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ProductComparatorConfigTest {

    companion object {
        val ZERO = TestUtils.getMoney("0.0")!!
        val COMP = ProductComparatorConfig().productComparator()
    }

    fun Product.comesAfter(p: Product): Boolean {
        return COMP.compare(this, p) > 0
    }

    fun Product.comesBefore(p: Product): Boolean {
        return COMP.compare(this, p) < 0
    }

    fun getProduct(amt: String?, productId: String = "dummy"): Product {
        return Product().apply {
            this.productId = productId

            priceInfo = PriceInfo().apply {
                priceReduction =
                        TestUtils.getMoney(amt)?.let {
                            PriceReduction(it, ZERO)
                        }
            }
        }
    }

    fun checkComesBefore(p1: Product, p2: Product) {
        assertTrue(COMP.compare(p1, p2) < 0)
    }

    fun checkComesAfter(p1: Product, p2: Product) {
        assertTrue(COMP.compare(p1, p2) > 0)
    }

    @Test
    fun testComparatorMonetaryAmount() {
        val p10 = getProduct("10.00")
        val p20 = getProduct("20.00")
        val p30 = getProduct("30.00")

        assertTrue(p10.comesAfter(p20))
        assertTrue(p20.comesBefore(p10))

        assertTrue(p10.comesAfter(p30))
        assertTrue(p30.comesBefore(p10))

        assertTrue(p20.comesAfter(p30))
        assertTrue(p30.comesBefore(p20))

        assertEquals(listOf(p30, p20, p10), listOf(p10, p20, p30).shuffled().sortedWith(COMP))
    }

    @Test
    fun testComparatorMonetaryAmountWithNull() {
        val pA = getProduct(null)
        val pB = getProduct("20.00")
        val pC = getProduct("30.00")

        assertEquals(listOf(pC, pB, pA), listOf(pA, pB, pC).shuffled().sortedWith(COMP))
    }

    @Test
    fun testComparatorMonetaryAmountWithMultipleNull() {
        val pA = getProduct(null, "productId1")
        val pB = getProduct(null, "productId2")
        val pC = getProduct("20.00")
        val pD = getProduct("30.00")

        assertEquals(listOf(pD, pC, pA, pB), listOf(pA, pB, pC, pD).shuffled().sortedWith(COMP))
    }

}