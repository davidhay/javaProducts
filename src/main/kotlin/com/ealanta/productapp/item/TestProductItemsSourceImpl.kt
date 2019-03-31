package com.ealanta.productapp.item

import com.ealanta.productapp.domain.ProductItem
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Service
@Profile("test")
class TestProductItemsSourceImpl: ProductItemsSource {

    override val productItems: List<ProductItem>  = emptyList();

}
