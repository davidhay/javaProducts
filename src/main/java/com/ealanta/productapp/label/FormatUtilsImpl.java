package com.ealanta.productapp.label;

import com.ealanta.productapp.misc.Constants;
import lombok.NonNull;
import org.javamoney.moneta.Money;
import org.javamoney.moneta.format.CurrencyStyle;
import org.springframework.stereotype.Component;

import javax.money.MonetaryAmount;
import javax.money.format.AmountFormatQuery;
import javax.money.format.AmountFormatQueryBuilder;
import javax.money.format.MonetaryAmountFormat;
import javax.money.format.MonetaryFormats;
import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * This singleton is used to format a monetary value into a formatted currency String.
 * The currency value represented as a string, including the currency, e.g. "£1.75".
 * For values that are integer, if they are less than £10 return a decimal price, otherwise show an integer price,
 * e.g. "£2.00" or "£10"
 * This class assumes all product prices are in GBP.
 */
@Component
public class FormatUtilsImpl implements FormatUtils {

    private static Money TEN_POUNDS = Money.of(new BigDecimal(10), Constants.DEFAULT_CURRENCY_CODE);

    private static final String PATTERN = "pattern";
    private static final String SHORT_PATTERN ="¤0";
    private static final String LONG_PATTERN ="¤0.00";

    private static final String DEFAULT_PRICE_LABEL = "£??.??";

    private final MonetaryAmountFormat longFormat;
    private final MonetaryAmountFormat shortFormat;
    private final String defaultPrice;

    public FormatUtilsImpl() {
        this.longFormat = getFormatForPattern(LONG_PATTERN);
        this.shortFormat = getFormatForPattern(SHORT_PATTERN);
        this.defaultPrice = DEFAULT_PRICE_LABEL;
    }

    private MonetaryAmountFormat getFormatForPattern(String pattern) {
        AmountFormatQuery amtFmtQuery = AmountFormatQueryBuilder
                                                .of(Constants.DEFAULT_LOCALE)
                                                .set(CurrencyStyle.SYMBOL)
                                                .set(PATTERN, pattern).build();
        return MonetaryFormats.getAmountFormat(amtFmtQuery);
    }

    private boolean isIntegerPrice(MonetaryAmount amount) {
        return amount.getNumber().getAmountFractionNumerator() == 0;
    }

    @Override
    public String formatPrice(@NonNull MonetaryAmount ma) {
        if (isIntegerPrice(ma) && ma.isLessThan(TEN_POUNDS) == false) {
            return shortFormat.format(ma);
        } else {
            return longFormat.format(ma);
        }
    }

    @Override
    public String formatPercentage(@NonNull BigDecimal percent){

        NumberFormat formatter = NumberFormat.getPercentInstance();
        formatter.setMaximumFractionDigits(0);

        String result = String.format("%s%%",formatter.format(percent));
        return formatter.format(percent);

    }

}
