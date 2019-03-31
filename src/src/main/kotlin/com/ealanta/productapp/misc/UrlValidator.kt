package com.ealanta.productapp.misc

import java.net.URL

/**
 * This simple validator makes sure the URL for the remote web service is valid BEFORE we attempt to use the RestTemplate
 * with an invalid URL and get a strange error message.s
 */
object UrlValidator {

    fun isValid(url: String): Boolean {
        try {
            URL(url).toURI()
            return true
        } catch (e: Exception) {
            return false
        }
    }
}
