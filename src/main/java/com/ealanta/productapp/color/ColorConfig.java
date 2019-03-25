package com.ealanta.productapp.color;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Used to help get color to rgb hex mappings into a Map from application.properties
 */
@Component
@ConfigurationProperties("color")
public class ColorConfig {

    private Map<String,String> config = new HashMap<>();

    public Map<String,String> getConfig(){
        return config;
    }
}
