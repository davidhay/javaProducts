package com.ealanta.productapp.product

import com.ealanta.productapp.config.ProductComparatorConfig
import com.ealanta.productapp.domain.PriceDetails
import com.ealanta.productapp.misc.Constants
import com.ealanta.productapp.domain.PriceInfo
import com.ealanta.productapp.domain.PriceReduction
import com.ealanta.productapp.domain.Product
import org.javamoney.moneta.Money
import org.junit.Before
import org.junit.Test

import javax.money.MonetaryAmount
import java.math.BigDecimal
import java.util.Arrays
import kotlin.properties.Delegates
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ProductComparatorTest {

    private var pr1: Product by Delegates.notNull()
    private var pr2: Product by Delegates.notNull()
    private var pr3: Product by Delegates.notNull()

    private var nopr1: Product by Delegates.notNull()
    private var nopr2: Product by Delegates.notNull()
    private var nopr3: Product by Delegates.notNull()

    private val comparator:Comparator<Product> = ProductComparatorConfig().productComparator()

    @Before
    fun setup() {
        pr1 = getProduct("pr1", "1.00")
        pr2 = getProduct("pr2", "2.00")
        pr3 = getProduct("pr3", "3.00")

        nopr1 = getProduct("noPr1", null)
        nopr2 = getProduct("noPr2", null)
        nopr3 = getProduct("noPr3", null)
    }

    private fun getProduct(productId: String, reductionAmount: String?): Product {
        val result = Product()
        result.productId = productId
        val details = PriceDetails()
        details.currency = Constants.DEFAULT_CURRENCY_CODE
        val info = PriceInfo()
        info.priceReduction = reductionAmount?.let { ra -> PriceReduction(getMoney(ra), getMoney("0.00")) }
        return result
    }

    private fun sortAscending(source: List<Product>): List<Product> {
        return source.sortedWith(comparator)
    }

    private fun sortDescending(source: List<Product>): List<Product> {
        return source.sortedWith(comparator.reversed())
    }

    internal fun getMoney(amt: String): MonetaryAmount {
        return Money.of(BigDecimal(amt).setScale(2), "GBP")
    }

    @Test
    fun confirmMonetaryAmountComparator() {
        val m1 = getMoney("10")
        val m2 = getMoney("100")
        assertTrue(m1.compareTo(m2) < 0)
        assertTrue(m2.compareTo(m1) > 0)
        assertTrue(m2.compareTo(m2) == 0)
    }

    @Test
    fun testProductComparator1() {
        assertTrue(comparator.compare(pr1, pr2) < 0)
        assertTrue(comparator.compare(pr2, pr1) > 0)
        assertTrue(comparator.compare(pr2, pr2) == 0)
    }

    @Test
    fun testSortDescendingByPriceRedution() {
        val products = Arrays.asList(pr1, pr2, pr3)
        val sorted = sortDescending(products)
        assertEquals(Arrays.asList(pr3, pr2, pr1), sorted)
    }

    @Test
    fun testSortAscendingByPriceReduction() {
        val products = Arrays.asList(pr3, pr2, pr1)
        val sorted = sortAscending(products)
        assertEquals(Arrays.asList(pr1, pr2, pr3), sorted)
    }

    @Test
    fun testSortDescendingByNoPriceReduction() {
        val products = Arrays.asList(nopr1, nopr2, nopr3)
        val sorted = sortDescending(products)
        assertEquals(Arrays.asList(nopr3, nopr2, nopr1), sorted)
    }

    @Test
    fun testSortAscendingByNoPriceReduction() {
        val products = Arrays.asList(nopr3, nopr2, nopr1)
        val sorted = sortAscending(products)
        assertEquals(Arrays.asList(nopr1, nopr2, nopr3), sorted)
    }

    @Test
    fun testSortAscendingMixed() {
        val products = Arrays.asList(pr1, nopr1)
        val sorted = sortAscending(products)
        assertEquals(Arrays.asList(nopr1, pr1), sorted)
    }

    @Test
    fun testSortDescendingMixed() {
        val products = Arrays.asList(nopr1, pr1)
        val sorted = sortDescending(products)
        assertEquals(Arrays.asList(pr1, nopr1), sorted)
    }

}
