package com.ealanta.productapp.price;

import com.fasterxml.jackson.annotation.JsonIgnoreType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.money.MonetaryAmount;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Getter
@Setter
@ToString
@EqualsAndHashCode
/**
 * This data class is used to store information about a product's price reduction
 * also supports methods to generate the product's price label.
 * Instances of this class are created by the PriceInfoSourceImpl
 *
 * The prices 'was' and 'now' are required.
 * The 'then' price is optional.
 * if 'then' is present - 'was' before 'then' before 'now'
 * if 'then' not present - 'was' before 'now'
 * @see PriceInfoFactory
 */

@JsonIgnoreType
public class PriceReduction {

    //the current price
    private @NotNull MonetaryAmount now;

    //the optional price between 'now' and 'was'
    private @NotNull Optional<MonetaryAmount> then;

    //the price before 'now'
    private @NotNull MonetaryAmount was;

    /**
     *
     * @param now the current price - required
     * @param then the price between now and was - optional
     * @param was the price before now - required
     */
    public PriceReduction(MonetaryAmount was, Optional<MonetaryAmount> then, MonetaryAmount now){
        this.was = was;
        this.then = then;
        this.now = now;
    }

    public PriceReduction(MonetaryAmount was, MonetaryAmount now){
        this(was, Optional.empty(), now);
    }

    private BigDecimal toBigDecimal( MonetaryAmount amt ){
        return amt.getNumber().numberValue(BigDecimal.class).setScale(2,RoundingMode.HALF_EVEN);
    }

    public MonetaryAmount getReductionAmount(){
        return this.was.subtract(now);
    }

    public BigDecimal getDiscount() {
        MonetaryAmount ma = getReductionAmount().divide(toBigDecimal(was));
        return toBigDecimal(ma);
    }

}
