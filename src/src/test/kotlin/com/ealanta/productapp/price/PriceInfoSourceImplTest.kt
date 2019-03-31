package com.ealanta.productapp.price

import com.ealanta.productapp.domain.PriceDetails
import com.ealanta.productapp.domain.PriceInfo
import com.ealanta.productapp.domain.PriceReduction
import com.ealanta.productapp.product.PriceInfoFactory
import com.ealanta.productapp.product.PriceInfoSourceImpl
import com.ealanta.productapp.utils.TestUtils
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.node.TextNode
import org.junit.Assert
import org.junit.Before
import org.junit.Test

import java.util.HashMap
import kotlin.properties.Delegates

class PriceInfoSourceImplTest {

    private var sut: PriceInfoFactory by Delegates.notNull()

    private val priceDetailsWithWasAndNow: PriceDetails
        get() {
            val pd = PriceDetails()

            val nowNode = TextNode.valueOf("12.34")
            pd.now = nowNode
            pd.was = "21.12"
            return pd
        }

    @Before
    fun setup() {
        sut = PriceInfoSourceImpl()
    }


    @Test
    fun testPriceDetailsWithNoCurrencyAndNoNow() {
        val pd = PriceDetails()
        //pd.setCurrency();
        pd.now = null
        val info = sut.getPriceInfo(pd)
        //PRICE REDUCTION
        Assert.assertNull(info.priceReduction)

        //NOW PRICE
        Assert.assertEquals(TestUtils.getMoney("0"), info.nowPrice)

    }

    @Test
    fun testPriceDetailsWithEmptyCurrencyAndNoNow() {
        val pd = PriceDetails()
        //pd.setCurrency();
        pd.now = null
        val info = sut.getPriceInfo(pd)
        //PRICE REDUCTION
        Assert.assertNull(info.priceReduction)

        //NOW PRICE
        Assert.assertEquals(TestUtils.getMoney("0"), info.nowPrice)

    }

    @Test
    fun testPriceDetailsWithNowOnly() {
        val pd = PriceDetails()

        val nowNode = TextNode.valueOf("12.34")
        pd.now = nowNode

        val info = sut.getPriceInfo(pd)
        pd.then1 = ""
        pd.then2 = ""
        //PRICE REDUCTION
        Assert.assertNull(info.priceReduction)

        //NOW PRICE
        Assert.assertEquals(TestUtils.getMoney("12.34"), info.nowPrice)

    }

    @Test
    fun testPriceDetailsWithNowObjectNode() {
        val pd = PriceDetails()

        val fact = JsonNodeFactory(true)
        val kids = HashMap<String, JsonNode>()
        kids["A"] = TextNode.valueOf("A1")
        kids["B"] = TextNode.valueOf("B1")
        val nowNode = ObjectNode(fact, kids)
        pd.now = nowNode

        val info = sut.getPriceInfo(pd)
        //PRICE REDUCTION
        Assert.assertNull(info.priceReduction)

        //NOW PRICE
        Assert.assertEquals(TestUtils.getMoney("0"), info.nowPrice)
    }

    @Test
    fun testPriceDetailsWithWasAndNow() {

        val pd = priceDetailsWithWasAndNow
        pd.then2 = ""
        pd.then1 = ""

        val info = sut.getPriceInfo(pd)

        //NOW PRICE
        Assert.assertEquals(TestUtils.getMoney("12.34"), info.nowPrice)

        //PRICE REDUCTION

        Assert.assertNotNull(info.priceReduction)
        info.priceReduction?.let { pr ->
            Assert.assertEquals(TestUtils.getMoney("21.12"), pr.was)
            Assert.assertEquals(TestUtils.getMoney("12.34"), pr.now)
            Assert.assertEquals(null, pr.then)
        }
    }

    private fun checkPriceInfoForWasNow(info: PriceInfo): PriceReduction {
        //NOW PRICE
        Assert.assertEquals(TestUtils.getMoney("12.34"), info.nowPrice)

        //PRICE REDUCTION
        Assert.assertNotNull(info.priceReduction)
        val pr= info.priceReduction!!
        Assert.assertEquals(TestUtils.getMoney("21.12"), pr.was)
        Assert.assertEquals(TestUtils.getMoney("12.34"), pr.now)
        return pr
    }

    @Test
    fun testPriceDetailsWithWasAndNowAndThen1() {
        val pd = priceDetailsWithWasAndNow
        pd.then1 = "15.16"

        val info = sut.getPriceInfo(pd)
        val pr = checkPriceInfoForWasNow(info)

        Assert.assertEquals(TestUtils.getMoney("15.16"), pr.then)
    }

    @Test
    fun testPriceDetailsWithWasAndNowAndThen2() {
        val pd = priceDetailsWithWasAndNow
        pd.then2 = "15.16"

        val info = sut.getPriceInfo(pd)
        val pr = checkPriceInfoForWasNow(info)

        Assert.assertEquals(TestUtils.getMoney("15.16"), pr.then)
    }

    @Test
    fun testPriceDetailsWithWasAndNowAndThen1AndThen2() {
        val pd = priceDetailsWithWasAndNow
        pd.then1 = "15.16"
        pd.then2 = "16.17"

        val info = sut.getPriceInfo(pd)
        val pr = checkPriceInfoForWasNow(info)

        Assert.assertEquals(TestUtils.getMoney("16.17"), pr.then)
    }

}
