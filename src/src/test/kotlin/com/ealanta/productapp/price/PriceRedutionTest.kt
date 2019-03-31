package com.ealanta.productapp.price

import com.ealanta.productapp.domain.PriceReduction
import com.ealanta.productapp.utils.TestUtils
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class PriceRedutionTest {

    private fun checkWas100Now80(pr: PriceReduction) {
        assertEquals(TestUtils.getMoney("80"), pr.now)
        assertEquals(TestUtils.getMoney("100"), pr.was)
        assertEquals(TestUtils.getMoney("20"), pr.reductionAmount)
        assertEquals(TestUtils.getBigDecimal(".2"), pr.discount)
    }

    @Test
    fun testPriceReductionWasNow() {
        val pr = PriceReduction(
                TestUtils.getMoney("100")!!,
                TestUtils.getMoney("80")!!)
        checkWas100Now80(pr)
        assertNull(pr.then)
    }

    @Test
    fun testPriceReductionWasThenNow1() {
        val pr = PriceReduction(
                TestUtils.getMoney("100")!!,
                TestUtils.getMoney("90"),
                TestUtils.getMoney("80")!!)
        checkWas100Now80(pr)
        assertNotNull(pr.then)
        assertEquals(TestUtils.getMoney("90"), pr.then)
    }

    @Test
    fun testPriceReductionWasThenNow2() {
        val pr = PriceReduction(
                TestUtils.getMoney("100")!!,
                null,
                TestUtils.getMoney("80")!!)
        checkWas100Now80(pr)
        assertNull(pr.then)
    }

    @Test
    fun testPriceReductionThirdOff() {
        val pr = PriceReduction(
                TestUtils.getMoney("100")!!,
                TestUtils.getMoney("66.66")!!)
        assertEquals(TestUtils.getMoney("100"), pr.was)
        assertEquals(TestUtils.getMoney("66.66"), pr.now)
        assertNull(pr.then)
        assertEquals(TestUtils.getMoney("33.34"), pr.reductionAmount)
        assertEquals(TestUtils.getBigDecimal(".33"), pr.discount)
    }

    @Test
    fun testPriceReductionTwoThirdOff() {
        val pr = PriceReduction(
                TestUtils.getMoney("100")!!,
                TestUtils.getMoney("33.33")!!)
        assertEquals(TestUtils.getMoney("100"), pr.was)
        assertEquals(TestUtils.getMoney("33.33"), pr.now)
        assertNull(pr.then)
        assertEquals(TestUtils.getMoney("66.67"), pr.reductionAmount)
        assertEquals(TestUtils.getBigDecimal(".67"), pr.discount)
    }

    @Test
    fun testPriceReductionNowFree() {
        val pr = PriceReduction(
                TestUtils.getMoney("100")!!,
                TestUtils.getMoney("0")!!)
        assertEquals(TestUtils.getMoney("100"), pr.was)
        assertEquals(TestUtils.getMoney("0"), pr.now)
        assertNull(pr.then)
        assertEquals(TestUtils.getMoney("100"), pr.reductionAmount)
        assertEquals(TestUtils.getBigDecimal("1"), pr.discount)
    }
}
