package com.ealanta.productapp.domain

import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.databind.JsonNode
import mu.KotlinLogging
import java.math.BigDecimal
import java.math.RoundingMode
import javax.money.MonetaryAmount

@JsonIgnoreType
open class PriceInfo {

    var nowPrice: MonetaryAmount? = null

    var priceReduction: PriceReduction? = null

}

/**
 * This data class is used to store information about a product's price reduction
 * also supports methods to generate the product's price label.
 * Instances of this class are created by the PriceInfoSourceImpl
 *
 * The prices 'was' and 'now' are required.
 * The 'then' price is optional.
 * if 'then' is present - 'was' before 'then' before 'now'
 * if 'then' not present - 'was' before 'now'
 * @see PriceInfoFactory
 */

@JsonIgnoreType
class PriceReduction
/**
 *
 * @param now the current price - required
 * @param then the price between now and was - optional
 * @param was the price before now - required
 */
(//the price before 'now'
        val was: MonetaryAmount, //the optional price between 'now' and 'was'
        val then: MonetaryAmount?, //the current price
        val now: MonetaryAmount) {

    constructor(was: MonetaryAmount, now: MonetaryAmount) : this(was, null, now) {}

    val reductionAmount: MonetaryAmount
        get() = this.was.subtract(now)

    val discount: BigDecimal
        get() {
            val ma = reductionAmount.divide(toBigDecimal(was))
            return toBigDecimal(ma)
        }

    private fun toBigDecimal(amt: MonetaryAmount): BigDecimal {
        return amt.number.numberValue(BigDecimal::class.java).setScale(2, RoundingMode.HALF_EVEN)
    }

}

@JsonIgnoreProperties("basicColor", "skuId")
@JsonPropertyOrder("color", "rgbColor", "skuid")
@JsonInclude(JsonInclude.Include.NON_NULL)
class ColorSwatch : ColorSwatchItem() {

    var rgbColor: String? = null

    /**
     * The instructions state that the field has to be 'skuid' not 'skuId'
     * @return
     */
    val skuid: String?
        get() = this.skuId

}

@JsonIgnoreProperties(ignoreUnknown = true,value=["hasPriceReduction","reductionAmount"])
open class Product {

    private val logger = KotlinLogging.logger {}

    open var productId: String? = null
    var title: String? = null
    val colorSwatches: MutableList<ColorSwatch> = arrayListOf()

    @JsonIgnore
    var priceInfo: PriceInfo? = null

    var nowPrice: String? = null

    /*
    This is a convenience method to get the price reduction amount if there is one.
    We will use this Reduction amount to help sort the products
     */

    open val reductionAmount: MonetaryAmount?
        get() = this.priceInfo?.priceReduction?.reductionAmount

    open val hasPriceReduction: Boolean
        get() = this.reductionAmount != null
}