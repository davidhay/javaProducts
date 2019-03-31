package com.ealanta.productapp.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.annotation.JsonRootName

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName("products")
@JsonPropertyOrder("productId", "title", "colorSwatches", "nowPrice", "priceLabel")
@JsonInclude(JsonInclude.Include.NON_NULL)
class LabelledProduct() : Product() {

    var priceLabel: String? = null

    constructor(product: Product, priceLabel: String) : this() {
        this.productId = product.productId
        this.title = product.title
        this.colorSwatches.addAll(product.colorSwatches)
        this.nowPrice = product.nowPrice
        this.priceInfo = null//no need to copy the price information over
        this.priceLabel = priceLabel
    }

}

/**
 * This is a simple POJO which ensures that we return a JSON object with a field called 'products' whose
 * value is an JSON array.
 */

data class LabelledProducts(val products: List<LabelledProduct>)

enum class LabelType {
    ShowWasNow,
    ShowWasThenNow,
    ShowPercDiscount
}