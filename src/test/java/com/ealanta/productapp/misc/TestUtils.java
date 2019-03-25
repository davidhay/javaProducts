package com.ealanta.productapp.misc;

import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;
import java.math.BigDecimal;

public class TestUtils {

    public static BigDecimal getBigDecimal(String amt){
        if(amt == null){
            return null;
        }
        return new BigDecimal(amt).setScale(2);
    }

    public static MonetaryAmount getMoney(String amt){
        if(amt == null){
            return null;
        }
        return Money.of(getBigDecimal(amt),
                Constants.DEFAULT_CURRENCY_CODE);

    }
}
