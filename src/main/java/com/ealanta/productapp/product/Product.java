package com.ealanta.productapp.product;

import com.ealanta.productapp.price.PriceInfo;
import com.ealanta.productapp.price.PriceReduction;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.money.MonetaryAmount;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "productId")
/**
 * This data class is used to hold product data.
 * Instances of this class are created by Jackson Deserialization of JSON data.
 */
public class Product {

    private static final Logger LOGGER = LoggerFactory.getLogger(Product.class);

    private String productId;
    private String title;
    private final List<ColorSwatch> colorSwatches = new ArrayList<>();

    @JsonIgnore
    private PriceInfo priceInfo;

    private String nowPrice;

    /*
    This is a convenience method to get the price reduction amount if there is one.
    We will use this Reduction amount to help sort the products
     */
    @JsonIgnore
    public Optional<MonetaryAmount> getReductionAmount() {
        try {
            return this.priceInfo.getPriceReduction().map(PriceReduction::getReductionAmount);
        } catch (NullPointerException npe) {
            if(LOGGER.isTraceEnabled()) {
                LOGGER.trace("cannot extract Optional<MonetaryAmount>", npe);
            }
            return Optional.empty();
        }
    }

    @JsonIgnore
    public boolean hasPriceReduction(){
        return getReductionAmount().isPresent();
    }

}
