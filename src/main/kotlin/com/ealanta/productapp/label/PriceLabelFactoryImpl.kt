package com.ealanta.productapp.label


import com.ealanta.productapp.domain.LabelType
import com.ealanta.productapp.domain.PriceReduction
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.money.MonetaryAmount

typealias Labeller = (PriceReduction) -> String

@Component
class PriceLabelGeneratorImpl @Autowired
constructor(private val formatUtils: FormatUtils) : PriceLabelFactory {

    companion object {
        private const val FORMAT_WAS_THEN_NOW = "Was %s, then %s, now %s"
        private const val FORMAT_WAS_NOW = "Was %s, now %s"
        private const val FORMAT_DISCOUNT = "%s off - now %s"
    }

    private val logger = KotlinLogging.logger {}

    private fun getLabelGenerator(pr: PriceReduction, labelType: LabelType): Labeller =
            when (labelType) {
                LabelType.ShowPercDiscount -> this::getPriceLabelForDiscount
                LabelType.ShowWasNow -> this::getPriceLabelForWasNow
                LabelType.ShowWasThenNow -> if (pr.then == null) {
                    this::getPriceLabelForWasNow
                } else {
                    this::getLabelForWasThenNow
                }
            }

    override fun generatePriceLabel(priceReduction: PriceReduction, labelType: LabelType): String {
        val generator = getLabelGenerator(priceReduction, labelType)
        return generator(priceReduction)
    }

    private fun getLabelForWasThenNow(pr: PriceReduction): String {
        val formattedWas = formatAmount(pr.was)
        checkNotNull(pr.then) { "Expected Then to be Present its empty" }
        val formattedThen = formatAmount(pr.then)
        val formattedNow = formatAmount(pr.now)
        return String.format(FORMAT_WAS_THEN_NOW, formattedWas, formattedThen, formattedNow)
    }

    private fun getPriceLabelForWasNow(pr: PriceReduction): String {
        val formattedWas = formatAmount(pr.was)
        val formattedNow = formatAmount(pr.now)
        return String.format(FORMAT_WAS_NOW, formattedWas, formattedNow)
    }

    private fun formatAmount(ma: MonetaryAmount): String {
        return formatUtils.formatPrice(ma)
    }

    private fun getPriceLabelForDiscount(pr: PriceReduction): String {
        val formattedNow = formatAmount(pr.now)
        val formattedPercent = formatUtils.formatPercentage(pr.discount)
        return String.format(FORMAT_DISCOUNT, formattedPercent, formattedNow)
    }
}
