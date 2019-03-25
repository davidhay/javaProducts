package com.ealanta.productapp.label;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
/**
 * This is a simple POJO which ensures that we return a JSON object with a field called 'products' whose
 * value is an JSON array.
 */
public class LabelledProducts {

    private final List<LabelledProduct> products = new ArrayList<>();

    public LabelledProducts(List<LabelledProduct> products){
        this.products.addAll(products);
    }

}
