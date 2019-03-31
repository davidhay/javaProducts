package com.ealanta.productapp.config

import com.ealanta.productapp.domain.LabelType
import com.ealanta.productapp.domain.LabelledProduct
import com.ealanta.productapp.domain.Product
import com.ealanta.productapp.domain.ProductItem
import com.ealanta.productapp.item.ProductItemsSource
import com.ealanta.productapp.label.ProductToLabelledProductConverter
import com.ealanta.productapp.product.ProductItemToProductConverter
import com.ealanta.productapp.utils.TestUtils
import com.nhaarman.mockito_kotlin.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import javax.money.MonetaryAmount
import kotlin.properties.Delegates
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(MockitoJUnitRunner.Silent::class)
class LabelledProductSourceConfigTest {

    companion object {
        val TEN: MonetaryAmount? = TestUtils.getMoney("10.00")
        val TWENTY: MonetaryAmount? = TestUtils.getMoney("20.00")
        val THIRTY: MonetaryAmount? = TestUtils.getMoney("30.00")
    }

    val productItem1: ProductItem = ProductItem()
    val productItem2: ProductItem = ProductItem()
    val productItem3: ProductItem = ProductItem()
    val productItems = listOf(productItem1, productItem2, productItem3)
    val labelledProduct1 = LabelledProduct();
    val labelledProduct2 = LabelledProduct();
    val labelledProduct3 = LabelledProduct();
    var sut: LabelledProductSourceConfig by Delegates.notNull()

    private val productItemsSource: ProductItemsSource = mock()
    private val productToLabelledProduct: ProductToLabelledProductConverter = mock()
    private val productItemToProduct: ProductItemToProductConverter = mock()
    private val productComparator: Comparator<Product> = ProductComparatorConfig().productComparator()
    private var argsProductItem: KArgumentCaptor<ProductItem> by Delegates.notNull()
    private var argsProduct: KArgumentCaptor<Product> by Delegates.notNull()
    private var argsLabelType: KArgumentCaptor<LabelType> by Delegates.notNull()
    private val product1: Product = mock()
    private val product2: Product = mock()
    private val product3: Product = mock()

    @Before
    fun setup() {
        argsProductItem = argumentCaptor()
        argsProduct = argumentCaptor()
        argsLabelType = argumentCaptor()

        sut = LabelledProductSourceConfig(productItemsSource, productToLabelledProduct, productItemToProduct, productComparator);

        whenever(productItemsSource.productItems).thenReturn(productItems)
        whenever(productItemToProduct.convert(argsProductItem.capture()))
                .thenReturn(product1)
                .thenReturn(product2)
                .thenReturn(product3)

        labelledProduct1.productId = "PRODUCT ID 1"
        labelledProduct2.productId = "PRODUCT ID 2"
        labelledProduct3.productId = "PRODUCT ID 3"
    }

    @Test
    fun testConvertersAndFilter() {
        whenever(product1.hasPriceReduction).thenReturn(false)
        whenever(product2.hasPriceReduction).thenReturn(true)
        whenever(product3.hasPriceReduction).thenReturn(false)
        whenever(productToLabelledProduct.convert(argsProduct.capture(), argsLabelType.capture()))
                .thenReturn(labelledProduct2)

        val result = sut.labelledProductsSource().invoke(LabelType.ShowWasNow)
        assertEquals(1, result.size)
        assertEquals(labelledProduct2, result[0])

        verify(productItemsSource, times(1)).productItems
        verify(productItemToProduct, times(1)).convert(argsProductItem.firstValue)
        verify(productItemToProduct, times(1)).convert(argsProductItem.secondValue)
        verify(productItemToProduct, times(1)).convert(argsProductItem.thirdValue)
        verify(productToLabelledProduct, times(1)).convert(argsProduct.firstValue, argsLabelType.firstValue)

    }

    @Test
    fun testConvertersAndFilterWithComparator() {

        val amounts = listOf(TEN, TWENTY, THIRTY).shuffled();

        //SHUFFLE THE AMOUNTS GIVEN TO PRODUCTS
        whenever(product1.hasPriceReduction).thenReturn(true)
        whenever(product1.reductionAmount).thenReturn(amounts[0])
        whenever(product1.productId).thenReturn(labelledProduct1.productId)

        whenever(product2.hasPriceReduction).thenReturn(true)
        whenever(product2.reductionAmount).thenReturn(amounts[1])
        whenever(product2.productId).thenReturn(labelledProduct2.productId)

        whenever(product3.hasPriceReduction).thenReturn(true)
        whenever(product3.reductionAmount).thenReturn(amounts[2])
        whenever(product3.productId).thenReturn(labelledProduct3.productId)

        val products = listOf(product1, product2, product3);

        whenever(productToLabelledProduct.convert(argsProduct.capture(), argsLabelType.capture()))
                .thenReturn(labelledProduct1)
                .thenReturn(labelledProduct2)
                .thenReturn(labelledProduct3)

        val result = sut.labelledProductsSource().invoke(LabelType.ShowWasNow)

        assertEquals(3, result.size)

        assertTrue(result.contains(labelledProduct1))
        assertTrue(result.contains(labelledProduct2))
        assertTrue(result.contains(labelledProduct3))

        //check that the order of converting Product -> LabelledProduct is price reduction order
        assertEquals(listOf(THIRTY, TWENTY, TEN), argsProduct.allValues.map { it.reductionAmount })

        verify(productItemsSource, times(1)).productItems
        verify(productItemToProduct, times(1)).convert(argsProductItem.firstValue)
        verify(productItemToProduct, times(1)).convert(argsProductItem.secondValue)
        verify(productItemToProduct, times(1)).convert(argsProductItem.thirdValue)
        verify(productToLabelledProduct, times(1)).convert(argsProduct.firstValue, argsLabelType.firstValue)
        verify(productToLabelledProduct, times(1)).convert(argsProduct.secondValue, argsLabelType.secondValue)
        verify(productToLabelledProduct, times(1)).convert(argsProduct.thirdValue, argsLabelType.thirdValue)

    }

    fun checkLabelType(labelType: LabelType) {
        whenever(product1.hasPriceReduction).thenReturn(true)
        whenever(product2.hasPriceReduction).thenReturn(true)
        whenever(product3.hasPriceReduction).thenReturn(true)
        whenever(productToLabelledProduct.convert(argsProduct.capture(), argsLabelType.capture()))
                .thenReturn(labelledProduct2)
        val result = sut.labelledProductsSource().invoke(labelType)
        assertEquals(3, result.size)

        verify(productItemsSource, times(1)).productItems
        verify(productItemToProduct, times(1)).convert(argsProductItem.firstValue)
        verify(productItemToProduct, times(1)).convert(argsProductItem.secondValue)
        verify(productItemToProduct, times(1)).convert(argsProductItem.thirdValue)
        verify(productToLabelledProduct, times(1)).convert(product1, argsLabelType.firstValue)
        verify(productToLabelledProduct, times(1)).convert(product2, argsLabelType.firstValue)
        verify(productToLabelledProduct, times(1)).convert(product3, argsLabelType.firstValue)
    }

    @Test
    fun testLabelTypes() {
        for (labelType in LabelType.values()) {
            checkLabelType(labelType)
            Mockito.reset(productItemsSource, productToLabelledProduct, productItemToProduct,
                    product1, product2, product3)
            setup()
        }
    }
}