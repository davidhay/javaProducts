package com.ealanta.productapp.label

import javax.money.MonetaryAmount
import java.math.BigDecimal

interface FormatUtils {
    fun formatPrice(ma: MonetaryAmount): String

    fun formatPercentage(percentage: BigDecimal): String
}
