package com.ealanta.productapp.label;

import com.ealanta.productapp.price.PriceReduction;

public interface PriceLabelGenerator {
    String generatePriceLabel(PriceReduction priceReduction, LabelType labelType);
}
