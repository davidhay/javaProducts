package com.ealanta.productapp.color

import com.ealanta.productapp.config.ColorConfig
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Implementation of ColorLookup.
 * Makes sure the colors are stored in lower case and the rgb hex values are stored in upper case.
 * @see ColorConfig
 */
@Component
class ColorLookupImpl @Autowired
constructor(colorConfig: ColorConfig) : ColorLookup {

    private val logger = KotlinLogging.logger {}

    private val config: Map<String, String> = colorConfig.config.map {
        it.key.toLowerCase() to it.value.toUpperCase()
    }.toMap()

    override fun lookupRgbHexForColor(basic: String): String? {
        val key = basic.toLowerCase().trim()
        val rgb = config[key]
        logger.trace( "RGB value for [$key] is [$rgb]")
        return rgb
    }

}