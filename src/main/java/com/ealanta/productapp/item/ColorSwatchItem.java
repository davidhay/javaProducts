package com.ealanta.productapp.item;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
@Getter
@Setter
public class ColorSwatchItem {

    @JsonProperty(value="color")
    private String color;

    @JsonProperty(value="basicColor")
    private String basicColor;

    @JsonProperty(value="skuId")
    private String skuId;

}
