package com.ealanta.productapp.label

import com.ealanta.productapp.domain.LabelType
import com.ealanta.productapp.domain.PriceReduction
import com.ealanta.productapp.utils.TestUtils.getMoney
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.junit4.SpringRunner

import kotlin.properties.Delegates
import kotlin.test.assertEquals

@RunWith(SpringRunner::class)
class PriceLabelGeneratorImplTest {

    private var priceLabelGenerator: PriceLabelFactory by Delegates.notNull()

    @Before
    fun setup() {
        this.priceLabelGenerator = PriceLabelGeneratorImpl(FormatUtilsImpl())
    }

    private fun getWasNow(was: String, now: String): PriceReduction {
        return PriceReduction(getMoney(was)!!, getMoney(now)!!)
    }

    private fun getWasThenNow(was: String, then: String?, now: String): PriceReduction {
        return PriceReduction(getMoney(was)!!, getMoney(then), getMoney(now)!!)
    }


    private fun checkShowWasNow(expectedLabel: String, was: String, now: String) {
        val wasNow = getWasNow(was, now)
        val label = priceLabelGenerator.generatePriceLabel(wasNow, LabelType.ShowWasNow)
        assertEquals(expectedLabel, label)
    }

    private fun checkShowWasThenNow(expectedLabel: String, was: String, then: String?, now: String) {
        val wasNow = getWasThenNow(was, then, now)
        val label = priceLabelGenerator.generatePriceLabel(wasNow, LabelType.ShowWasThenNow)
        assertEquals(expectedLabel, label)
    }

    private fun checkShowDiscount(expectedLabel: String, was: String, now: String) {
        val wasNow = getWasNow(was, now)
        val label = priceLabelGenerator.generatePriceLabel(wasNow, LabelType.ShowPercDiscount)
        assertEquals(expectedLabel, label)
    }

    @Test
    fun testShowWasNow() {
        checkShowWasNow("Was £100, now £9.00", "100", "9")
        checkShowWasNow("Was £100, now £10", "100", "10")
        checkShowWasNow("Was £100, now £11", "100", "11")

        checkShowWasNow("Was £9.00, now £8.00", "9", "8")
        checkShowWasNow("Was £10, now £8.00", "10", "8")
        checkShowWasNow("Was £11, now £8.00", "11", "8")

        checkShowWasNow("Was £100, now £90", "100", "90")
        checkShowWasNow("Was £9.99, now £6.66", "9.99", "6.66")

    }

    @Test
    fun testShowWasThenNow() {
        checkShowWasThenNow("Was £10, then £9.00, now £8.00", "10", "9", "8")
        checkShowWasThenNow("Was £98.76, then £87.65, now £76.54", "98.76", "87.65", "76.54")
        checkShowWasThenNow("Was £10, now £8.00", "10", null, "8")
        checkShowWasThenNow("Was £98.76, now £76.54", "98.76", null, "76.54")
    }

    @Test
    fun testShowPercDiscount() {
        checkShowDiscount("25% off - now £75", "100", "75")
        checkShowDiscount("0% off - now £100", "100", "100")
        checkShowDiscount("90% off - now £10", "100", "10")
        checkShowDiscount("91% off - now £9.00", "100", "9")
        checkShowDiscount("100% off - now £0.00", "100", "0")
        checkShowDiscount("33% off - now £66.66", "100", "66.66")
        checkShowDiscount("67% off - now £33.33", "100", "33.33")
        //TODO this is a bit of an edge case - maybe it should be an errors
        checkShowDiscount("200% off - now £-100.00", "100", "-100")
    }

}
