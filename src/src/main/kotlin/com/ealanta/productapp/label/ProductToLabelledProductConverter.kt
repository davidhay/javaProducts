package com.ealanta.productapp.label

import com.ealanta.productapp.domain.LabelType
import com.ealanta.productapp.domain.LabelledProduct
import com.ealanta.productapp.domain.Product
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ProductToLabelledProductConverter @Autowired constructor(
        val labelGenerator: PriceLabelFactory) {

    fun convert(product: Product, labelType: LabelType): LabelledProduct {
        val pr = product.priceInfo?.priceReduction;

        checkNotNull(pr) { "Cannot created a LabelledProduct  - the Product has no price reduction" }
        val label = labelGenerator.generatePriceLabel(pr, labelType)
        return LabelledProduct(product, label)
    }
}