package com.ealanta.productapp.item;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@ToString
public class ProductItem  {

    private String productId;
    private String title;
    private ColorSwatchItem[] colorSwatches;
    private PriceDetails price;
}
