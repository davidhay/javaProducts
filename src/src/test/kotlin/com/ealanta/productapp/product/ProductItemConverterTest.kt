package com.ealanta.productapp.product

import com.ealanta.productapp.color.ColorLookup
import com.ealanta.productapp.domain.*
import com.ealanta.productapp.label.FormatUtils
import com.ealanta.productapp.utils.TestUtils
import com.nhaarman.mockito_kotlin.argumentCaptor
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.mockito.junit.MockitoJUnitRunner
import com.ealanta.productapp.utils.TestUtils.any
import org.junit.Ignore
import java.util.Arrays

import org.mockito.Mockito.*
import kotlin.properties.Delegates
import kotlin.test.assertEquals

@RunWith(MockitoJUnitRunner::class)
class ProductItemConverterTest {

    lateinit var mColorLookup: ColorLookup
    lateinit var mPriceInfo: PriceInfoFactory
    lateinit var mFormatUtils: FormatUtils

    private var swItem1: ColorSwatchItem by Delegates.notNull()
    private var swItem2: ColorSwatchItem by Delegates.notNull()
    private var sut: ProductItemToProductConverter by Delegates.notNull()

    @Before
    fun setup() {
        swItem1 = ColorSwatch()
        swItem1.basicColor = "BASIC-1"
        swItem1.color = "COLOR-1"
        swItem1.skuId = "SKU-ID"

        swItem2 = ColorSwatch()
        swItem2.basicColor = "BASIC-2"
        swItem2.color = "COLOR-2"
        swItem2.skuId = "SKU-ID"

        mColorLookup = mock()
        mFormatUtils = mock();
        mPriceInfo = mock();
        sut = ProductItemToProductConverter(mColorLookup, mPriceInfo, mFormatUtils)

    }

    class MyPriceInfo(val label:String) : PriceInfo() {
        override fun toString():String = label;
    }

    @Test
    fun testConvertNoSwatches() {
        val item = ProductItem()

        item.title = "TITLE"
        item.productId = "PRODUCTID"
        val pd = PriceDetails()
        item.price = pd

        val pInfo = PriceInfo()
        val ten = TestUtils.getMoney("10.00")
        pInfo.nowPrice=ten

        argumentCaptor<PriceDetails>().let { captor ->
            whenever(mPriceInfo.getPriceInfo(captor.capture())).thenReturn(pInfo)

            val product = sut.convert(item)

            assertEquals(pd, captor.firstValue)

            assertEquals(pInfo, product.priceInfo)
            assertEquals(0, product.colorSwatches.size.toLong())
            assertEquals("PRODUCTID", product.productId)
            assertEquals("TITLE", product.title)

            verify<ColorLookup>(mColorLookup, never()).lookupRgbHexForColor(any())
            verify(mPriceInfo, atLeastOnce()).getPriceInfo(captor.firstValue)
            verify(mFormatUtils, times(1)).formatPrice(ten!!)}
    }

    @Test
    fun testConvertWithSwatches() {
        val item = ProductItem()

        val swatches = arrayOf<ColorSwatchItem>(swItem1, swItem2)
        item.colorSwatches = swatches

        item.title = "TITLE"
        item.productId = "PRODUCTID"
        item.colorSwatches = swatches

        val pd = PriceDetails()
        item.price = pd

        val pInfo = PriceInfo()
        val ten = TestUtils.getMoney("10.00")
        pInfo.nowPrice=ten

        argumentCaptor<PriceDetails>().let { argPd ->
            whenever(mPriceInfo.getPriceInfo(argPd.capture())).thenReturn(pInfo)

            argumentCaptor<String>().let { argCol ->
                whenever(mColorLookup.lookupRgbHexForColor(argCol.capture()))
                        .thenReturn("HEX1")
                        .thenReturn(null)

                val product = sut.convert(item)

                assertEquals(pd, argPd.firstValue)
                assertEquals(Arrays.asList("BASIC-1", "BASIC-2"), argCol.allValues)

                assertEquals(pInfo, product.priceInfo)
                assertEquals(2, product.colorSwatches.size.toLong())
                assertEquals("PRODUCTID", product.productId)
                assertEquals("TITLE", product.title)

                val sw1 = product.colorSwatches[0]
                val sw2 = product.colorSwatches[1]

                checkSwatchItemMatchesSwatch(sw1, swItem1, "HEX1")
                checkSwatchItemMatchesSwatch(sw2, swItem2, null)

                verify(mColorLookup, times(2)).lookupRgbHexForColor(any())
                verify(mPriceInfo, atLeastOnce()).getPriceInfo(argPd.firstValue)
                verify<FormatUtils>(mFormatUtils, times(1)).formatPrice(ten!!)
                verifyNoMoreInteractions(mColorLookup, mPriceInfo, mFormatUtils)
            }
        }
    }


    fun checkSwatchItemMatchesSwatch(result: ColorSwatch, orig: ColorSwatchItem, expectedRgb: String?) {
        assertEquals(orig.basicColor, result.basicColor)
        assertEquals(orig.color, result.color)
        assertEquals(orig.skuId, result.skuId)
        assertEquals(expectedRgb, result.rgbColor)
    }
}
