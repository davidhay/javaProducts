package com.ealanta.productapp.label;

import com.ealanta.productapp.domain.LabelType
import com.ealanta.productapp.domain.LabelledProduct
import com.ealanta.productapp.domain.PriceInfo
import com.ealanta.productapp.domain.PriceReduction
import com.ealanta.productapp.domain.Product
import com.ealanta.productapp.utils.TestUtils

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.util.*
import kotlin.Comparator
import kotlin.properties.Delegates
import com.nhaarman.mockito_kotlin.*
import kotlin.test.*

//@RunWith(MockitoJUnitRunner::class)
class LabelledProductSourceTest {

    /*
    val mProductSource: ProductSource = mock()

    val mLabelGenerator: PriceLabelFactory = mock()

    val prArg: KArgumentCaptor<PriceReduction> = argumentCaptor();

    val lArg: KArgumentCaptor<LabelType> = argumentCaptor();

    val comparator:Comparator<Product> = compareBy { it.productId }

    var sut: LabelledProductSource by Delegates.notNull()
    var productList: MutableList<Product> by Delegates.notNull()
    var p1: Product  by Delegates.notNull()
    var p2: Product  by Delegates.notNull()
    var p3: Product  by Delegates.notNull()
    var pr1: PriceReduction  by Delegates.notNull()
    var pr2: PriceReduction  by Delegates.notNull()
    var pr3: PriceReduction  by Delegates.notNull()

    @Before
    fun setup() {
        sut = LabelledProductSource( comparator, mProductSource, mLabelGenerator);
        p1 = getProduct("p1");
        p2 = getProduct("p2");
        p3 = getProduct("p3");
        productList = mutableListOf<Product>(p1, p2, p3);
        pr1 = p1.priceInfo!!.priceReduction!!;
        pr2 = p2.priceInfo!!.priceReduction!!;
        pr3 = p3.priceInfo!!.priceReduction!!;
    }

    @Test
    fun testShowWasNow() {
        checkGetProducts(LabelType.ShowWasNow);
    }

    @Test
    fun testShowWasThenNow() {
        checkGetProducts(LabelType.ShowWasThenNow);
    }

    @Test
    fun testGetProducts() {
        checkGetProducts(LabelType.ShowPercDiscount);
    }

    fun checkGetProducts(labelType: LabelType) {

        whenever(mProductSource.getFilteredAndSortedProducts(
                TestUtils.any(),
                TestUtils.any())).thenReturn(productList);


        whenever(mLabelGenerator.generatePriceLabel(prArg.capture(), lArg.capture()))
                .thenReturn("LABEL1")
                .thenReturn("LABEL2")
                .thenReturn("LABEL3");

        val result = sut.getProducts(labelType);

        result.forEach {
            System.out.printf("%s -> %s%n", it.productId, it.priceLabel);
        }

        assertEquals(prArg.allValues, Arrays.asList(pr1, pr2, pr3));
        assertEquals(3, lArg.allValues.size);
        assertTrue(lArg.allValues.all { it.equals(labelType) })

        val lp1: LabelledProduct = result[0]
        val lp2: LabelledProduct = result[1]
        val lp3: LabelledProduct = result[2]

        assertEquals("p1", lp1.productId);
        assertEquals("p2", lp2.productId);
        assertEquals("p3", lp3.productId);

        assertEquals("LABEL1", lp1.priceLabel);
        assertEquals("LABEL2", lp2.priceLabel);
        assertEquals("LABEL3", lp3.priceLabel);

        verify(mProductSource, times(1)).getFilteredAndSortedProducts(TestUtils.any(), TestUtils.any());
        verify(mLabelGenerator, times(3)).generatePriceLabel(TestUtils.any(), TestUtils.any());
        verifyNoMoreInteractions(mLabelGenerator, mProductSource);
    }

    fun getProduct(productId: String): Product {
        val result = Product();
        val info = PriceInfo();
        val pr = PriceReduction(
                TestUtils.getMoney("100")!!, TestUtils.getMoney("90")!!);
        info.priceReduction = pr
        result.priceInfo = info;
        result.productId = productId;
        return result;
    }

     */
}
