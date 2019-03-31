package com.ealanta.productapp.config

import com.ealanta.productapp.domain.LabelType
import com.ealanta.productapp.domain.LabelledProduct
import com.ealanta.productapp.domain.Product
import com.ealanta.productapp.item.ProductItemsSource
import com.ealanta.productapp.label.ProductToLabelledProductConverter
import com.ealanta.productapp.product.ProductItemToProductConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

typealias LabelledProductSource = (LabelType) -> List<LabelledProduct>
@Configuration
class LabelledProductSourceConfig (private val productItemsSource : ProductItemsSource,
             private val productToLabelledProduct: ProductToLabelledProductConverter,
             private val productItemToProduct: ProductItemToProductConverter,
             private val productComparator : Comparator<Product>){


    private fun getLabelledProducts(labelType: LabelType):List<LabelledProduct> =
            productItemsSource.productItems
                    .map(productItemToProduct::convert)
                    .filter{ it.hasPriceReduction }
                    .sortedWith(productComparator)
                    .map { productToLabelledProduct.convert(it,labelType) }


    @Bean
    fun labelledProductsSource():LabelledProductSource = {
        getLabelledProducts(it)
    }
}