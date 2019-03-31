package com.ealanta.productapp.item

import com.ealanta.productapp.domain.ProductItem
import com.ealanta.productapp.domain.ProductItems
import com.ealanta.productapp.misc.UrlValidator
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
@Profile("!test")
class ProductItemsSourceImpl
@Autowired constructor(@param:Value("\${data.url}") private val url: String) : ProductItemsSource {

    private val logger = KotlinLogging.logger {}

    private val restTemplate: RestTemplate

    init {
        checkURL(url)
        logger.info("USING REMOTE URL {}", url)
        this.restTemplate = RestTemplate()
    }

    override val productItems: List<ProductItem>
        get() {
            val productItems = restTemplate.getForObject(url, ProductItems::class.java)
            return productItems!!.products
        }

    private fun checkURL(url: String) {
        if (!UrlValidator.isValid(url)) {
            val msg = String.format("The URL [%s] is not valid.", url)
            logger.error(msg)
            throw IllegalArgumentException(msg)
        }
    }
}
