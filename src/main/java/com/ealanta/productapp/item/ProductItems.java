package com.ealanta.productapp.item;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * This data class holds a list of products extracts from a JSON response body.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@ToString
public class ProductItems {
    private final List<ProductItem> products = new ArrayList<>();
}
