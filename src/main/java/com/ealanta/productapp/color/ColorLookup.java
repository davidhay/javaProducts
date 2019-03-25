package com.ealanta.productapp.color;

import java.util.Optional;

/*
 * Describes the lookup of basic color to Optional Rgb Hex String.
 */
public interface ColorLookup {
    public Optional<String> lookupRgbHexForColor(String basic);
}
