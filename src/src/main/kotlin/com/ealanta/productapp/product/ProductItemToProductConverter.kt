package com.ealanta.productapp.product

import com.ealanta.productapp.color.ColorLookup
import com.ealanta.productapp.domain.ColorSwatch
import com.ealanta.productapp.domain.ColorSwatchItem
import com.ealanta.productapp.domain.Product
import com.ealanta.productapp.domain.ProductItem
import com.ealanta.productapp.label.FormatUtils
import org.springframework.stereotype.Component
import javax.money.MonetaryAmount


@Component
class ProductItemToProductConverter(
        private val colorLookup: ColorLookup,
        private val priceInfoFactory: PriceInfoFactory,
        private val formatUtils: FormatUtils) {

    fun convert(item: ProductItem): Product {
        val result = convertBasic(item)

        //convert the swatches
        result.colorSwatches.addAll(convertColorSwatchItems(item.colorSwatches))

        //generate the PriceInfo
        item.price?.let { price ->
            val priceInfo = priceInfoFactory.getPriceInfo(price)
            result.priceInfo = priceInfo

            //generate the 'formatted nowPrice' string
            result.nowPrice = priceInfo.nowPrice?.let { np:MonetaryAmount ->
                formatUtils.formatPrice(np)
            }
        }
        if(result.hasPriceReduction){
            println("Product ${result.productId} has price reduction")
        }
        return result
    }

    private fun convertBasic(item: ProductItem): Product {
        val result = Product()
        result.productId = item.productId
        result.title = item.title
        return result
    }

    private fun convertColorSwatchItems(colorSwatchItems: Array<ColorSwatchItem>?): List<ColorSwatch> {
        return colorSwatchItems?.map(::convertColorSwatchItem) ?: emptyList()
    }


    private fun convertColorSwatchItem(item: ColorSwatchItem): ColorSwatch {
        val result = ColorSwatch()
        result.color = item.color
        result.skuId = item.skuId
        val basic = item.basicColor
        result.basicColor = basic
        val hexRgb = basic?.let(colorLookup::lookupRgbHexForColor)
        result.rgbColor = hexRgb
        return result
    }

}
