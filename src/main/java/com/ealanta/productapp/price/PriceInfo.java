package com.ealanta.productapp.price;

import com.fasterxml.jackson.annotation.JsonIgnoreType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.money.MonetaryAmount;
import java.util.Optional;

@Getter
@Setter
@ToString
@JsonIgnoreType
public class PriceInfo {

    private MonetaryAmount nowPrice;

    private Optional<PriceReduction> priceReduction;

}
