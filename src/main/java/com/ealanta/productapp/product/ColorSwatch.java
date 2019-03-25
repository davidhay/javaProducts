package com.ealanta.productapp.product;

import com.ealanta.productapp.item.ColorSwatchItem;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties({"basicColor","skuId"})
@JsonPropertyOrder({"color","rgbColor","skuid"})
@ToString
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ColorSwatch extends ColorSwatchItem {

    private String rgbColor;

    /**
     * The instructions state that the field has to be 'skuid' not 'skuId'
     * @return
     */
    public String getSkuid(){
        return this.getSkuId();
    }

}
