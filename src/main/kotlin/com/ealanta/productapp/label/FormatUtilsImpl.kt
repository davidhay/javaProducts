package com.ealanta.productapp.label

import com.ealanta.productapp.misc.Constants
import org.javamoney.moneta.Money
import org.javamoney.moneta.format.CurrencyStyle
import org.springframework.stereotype.Component

import javax.money.MonetaryAmount
import javax.money.format.AmountFormatQuery
import javax.money.format.AmountFormatQueryBuilder
import javax.money.format.MonetaryAmountFormat
import javax.money.format.MonetaryFormats
import java.math.BigDecimal
import java.text.NumberFormat

/**
 * This singleton is used to format a monetary value into a formatted currency String.
 * The currency value represented as a string, including the currency, e.g. "£1.75".
 * For values that are integer, if they are less than £10 return a decimal price, otherwise show an integer price,
 * e.g. "£2.00" or "£10"
 * This class assumes all product prices are in GBP.
 */
@Component
class FormatUtilsImpl : FormatUtils {

    companion object {

        private val TEN_POUNDS = Money.of(BigDecimal(10), Constants.DEFAULT_CURRENCY_CODE)

        private val PATTERN = "pattern"
        private val SHORT_PATTERN = "¤0"
        private val LONG_PATTERN = "¤0.00"

        private val DEFAULT_PRICE_LABEL = "£??.??"
    }


    private val longFormat: MonetaryAmountFormat
    private val shortFormat: MonetaryAmountFormat
    private val defaultPrice: String

    init {
        this.longFormat = getFormatForPattern(LONG_PATTERN)
        this.shortFormat = getFormatForPattern(SHORT_PATTERN)
        this.defaultPrice = DEFAULT_PRICE_LABEL
    }

    private fun getFormatForPattern(pattern: String): MonetaryAmountFormat {
        val amtFmtQuery = AmountFormatQueryBuilder
                .of(Constants.DEFAULT_LOCALE)
                .set(CurrencyStyle.SYMBOL)
                .set(PATTERN, pattern).build()
        return MonetaryFormats.getAmountFormat(amtFmtQuery)
    }

    private fun isIntegerPrice(amount: MonetaryAmount): Boolean {
        return amount.number.amountFractionNumerator == 0L
    }

    override fun formatPrice(ma: MonetaryAmount): String {
        return if (isIntegerPrice(ma) && ma.isLessThan(TEN_POUNDS) == false) {
            shortFormat.format(ma)
        } else {
            longFormat.format(ma)
        }
    }

    override fun formatPercentage(per: BigDecimal): String {

        val formatter = NumberFormat.getPercentInstance()
        formatter.maximumFractionDigits = 0

        val result = String.format("%s%%", formatter.format(per))
        return formatter.format(per)

    }


}
