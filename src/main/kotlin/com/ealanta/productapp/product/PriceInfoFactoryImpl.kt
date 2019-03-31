package com.ealanta.productapp.product

import com.ealanta.productapp.domain.PriceDetails
import com.ealanta.productapp.domain.PriceInfo
import com.ealanta.productapp.domain.PriceReduction
import com.ealanta.productapp.misc.Constants
import org.javamoney.moneta.Money
import org.javamoney.moneta.format.MonetaryAmountDecimalFormatBuilder
import org.springframework.stereotype.Component

import javax.money.CurrencyUnit
import javax.money.Monetary
import javax.money.MonetaryAmount
import javax.money.UnknownCurrencyException
import javax.money.format.MonetaryParseException

typealias MoneyParser = (String?) -> MonetaryAmount?

@Component
class PriceInfoSourceImpl : PriceInfoFactory {

    companion object {
        fun getCurrency(currencyCode: String?): CurrencyUnit =
            try {
                val code = currencyCode ?: Constants.DEFAULT_CURRENCY_CODE
                Monetary.getCurrency(code)
            } catch (ex: UnknownCurrencyException) {
                Monetary.getCurrency(Constants.DEFAULT_CURRENCY_CODE)
            }
    }

    override fun getPriceInfo(details: PriceDetails): PriceInfo {

        val cur = getCurrency(details.currency)
        val parser = getMoneyParser(cur)

        val result = PriceInfo().apply{
            nowPrice = getNowAmount(details,parser)
            priceReduction = getPriceReduction(parser, details)
        }
        return result;
    }

    private fun getPriceReduction(parser: MoneyParser, details: PriceDetails): PriceReduction? {

        val was = parser(details.was)

        val result = was?.let { ws:MonetaryAmount ->
            val now = getNowAmount(details,parser)
            val reducedBy = ws.subtract(now)
            if (reducedBy.isNegativeOrZero) {
                null
            } else {
                val then =  parser(details.then2) ?: parser(details.then1)
                PriceReduction(ws, then, getNowAmount(details, parser))
            }
        }
        return result
    }

    fun getNowAmount(details: PriceDetails, parser: MoneyParser):MonetaryAmount {
        return parser(details.now?.asText()) ?: Money.zero(getCurrency(details.currency))
    }

    /**
     * This is a function that takes a currency and returns monetary amount parser.
     * @param currency - the currency code.
     * @return a function that maps currency values into MonetaryAmounts with the specified currency.
     */
    private fun getMoneyParser(currency: CurrencyUnit): MoneyParser {
        val builder = MonetaryAmountDecimalFormatBuilder.of("##.##")
        val parserInternal = builder.withCurrencyUnit(currency).build()
        val result: MoneyParser = { mny ->
            try {
                mny?.let{parserInternal.parse(it)}
            } catch (mpe: MonetaryParseException) {
                null
            }
        }
        return result;
    }
}
