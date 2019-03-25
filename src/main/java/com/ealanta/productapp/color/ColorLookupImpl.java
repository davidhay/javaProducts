package com.ealanta.productapp.color;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
/**
 * Implementation of ColorLookup.
 * Makes sure the colors are stored in lower case and the rgb hex values are stored in upper case.
 * @see ColorConfig
 */
public class ColorLookupImpl implements ColorLookup {

    private final Logger LOGGER = LoggerFactory.getLogger(ColorLookupImpl.class);

    private final Map<String, String> config;

    @Autowired
    public ColorLookupImpl(ColorConfig colorConfig) {

        this.config =
                colorConfig.getConfig().entrySet().stream().collect(Collectors.toMap(
                        e -> e.getKey().toLowerCase(),
                        e -> e.getValue().toUpperCase())
                );
    }

    @Override
    public Optional<String> lookupRgbHexForColor(String basic) {
        if (basic == null) {
            return Optional.empty();
        }
        String key = basic.toLowerCase().trim();
        String rgb = config.get(key);
        if (LOGGER.isTraceEnabled()) {
            LOGGER.debug("RGB value for [{}] is [{}]", key, rgb);
        }
        return Optional.ofNullable(rgb);
    }

}
