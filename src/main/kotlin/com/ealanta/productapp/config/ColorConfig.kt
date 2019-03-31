package com.ealanta.productapp.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

/**
 * Used to help get color to rgb hex mappings into a Map from application.properties
 */
@Component
@ConfigurationProperties("color")
class ColorConfig {

    val config: java.util.HashMap<String, String> = java.util.HashMap()
}