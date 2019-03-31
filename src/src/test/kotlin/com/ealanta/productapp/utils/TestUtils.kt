package com.ealanta.productapp.utils

import com.ealanta.productapp.misc.Constants
import org.javamoney.moneta.Money
import org.mockito.Mockito

import javax.money.MonetaryAmount
import java.math.BigDecimal

object TestUtils {

    fun getBigDecimal(amt: String?): BigDecimal? {
        return if (amt == null) {
            null
        } else BigDecimal(amt).setScale(2)
    }

    fun getMoney(amt: String?): MonetaryAmount? {
        return if (amt == null) {
            null
        } else Money.of(getBigDecimal(amt),Constants.DEFAULT_CURRENCY_CODE)

    }

    fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }
    private fun <T> uninitialized(): T = null as T
}
