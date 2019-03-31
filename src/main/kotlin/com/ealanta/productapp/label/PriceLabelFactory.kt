package com.ealanta.productapp.label

import com.ealanta.productapp.domain.LabelType
import com.ealanta.productapp.domain.PriceReduction

interface PriceLabelFactory {
    fun generatePriceLabel(priceReduction: PriceReduction, labelType: LabelType): String
}
