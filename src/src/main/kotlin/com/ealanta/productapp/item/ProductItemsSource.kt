package com.ealanta.productapp.item

import com.ealanta.productapp.domain.ProductItem

interface ProductItemsSource {
    val productItems: List<ProductItem>
}
