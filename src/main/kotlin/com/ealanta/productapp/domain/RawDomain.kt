package com.ealanta.productapp.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode

@JsonIgnoreProperties(ignoreUnknown = true)
open class ProductItem {

    open var productId: String? = null
    var title: String? = null
    var colorSwatches: Array<ColorSwatchItem>? = null
    var price: PriceDetails? = null
}

@JsonIgnoreProperties(ignoreUnknown = true)
open class PriceDetails {
    var was: String? = null
    var then1: String? = null
    var then2: String? = null
    //we use a JsonNode for 'now' because the JSON value is either simple text or a nested JSON object.
    var now: JsonNode? = null
    var currency: String? = null
}

@JsonIgnoreProperties(ignoreUnknown = true)
open class ColorSwatchItem {

    var color: String? = null

    var basicColor: String? = null

    var skuId: String? = null

}

/**
 * This data class holds a list of products extracts from a JSON response body.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
open class ProductItems {
    val products: List<ProductItem> = arrayListOf()
}