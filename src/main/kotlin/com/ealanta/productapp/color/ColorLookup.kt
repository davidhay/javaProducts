package com.ealanta.productapp.color

/*
 * Describes the lookup of basic color to Optional Rgb Hex String.
 */
interface ColorLookup {
    fun lookupRgbHexForColor(basic: String): String?
}