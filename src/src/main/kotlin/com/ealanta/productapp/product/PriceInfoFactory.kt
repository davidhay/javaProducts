package com.ealanta.productapp.product

import com.ealanta.productapp.domain.PriceDetails
import com.ealanta.productapp.domain.PriceInfo

interface PriceInfoFactory {
    fun getPriceInfo(details: PriceDetails): PriceInfo
}
