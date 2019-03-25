package com.ealanta.productapp.label;

import com.ealanta.productapp.product.Product;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@JsonRootName("products")
@NoArgsConstructor
@JsonPropertyOrder({"productId","title","colorSwatches","nowPrice","priceLabel"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LabelledProduct extends Product {

    private String priceLabel;

    public LabelledProduct(Product product, String priceLabel){
        this.setProductId(product.getProductId());
        this.setTitle(product.getTitle());
        this.getColorSwatches().addAll(product.getColorSwatches());
        this.setNowPrice(product.getNowPrice());
        this.setPriceInfo(null);//no need to copy the price information over
        this.setPriceLabel(priceLabel);
    }

}
