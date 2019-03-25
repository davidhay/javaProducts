package com.ealanta.productapp.price;

import com.ealanta.productapp.item.PriceDetails;
import com.ealanta.productapp.misc.Constants;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.NonNull;
import org.javamoney.moneta.Money;
import org.javamoney.moneta.format.MonetaryAmountDecimalFormatBuilder;
import org.springframework.stereotype.Component;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.UnknownCurrencyException;
import javax.money.format.MonetaryAmountFormat;
import javax.money.format.MonetaryParseException;
import java.util.Optional;
import java.util.function.Function;

@Component
public class PriceInfoSourceImpl implements PriceInfoSource {

    @Override
    public PriceInfo getPriceInfo(@NonNull PriceDetails details) {

        //CURRENCY
        CurrencyUnit cur = getCurrency(details.getCurrency());
        Function<String, Optional<MonetaryAmount>> parser = getMoneyParser(cur);

        PriceInfo info = new PriceInfo();
        info.setPriceReduction(getPriceReduction(parser, details));

        MonetaryAmount nowPrice = extractNow(parser, details);
        info.setNowPrice(nowPrice);

        return info;

    }

    private Optional<PriceReduction> getPriceReduction(Function<String, Optional<MonetaryAmount>> parser, PriceDetails details) {

        //WAS
        Optional<MonetaryAmount> wasOpt = parser.apply(details.getWas());
        if (wasOpt.isPresent() == false) {
            return Optional.empty();
        }

        MonetaryAmount now = extractNow(parser, details);
        MonetaryAmount was = wasOpt.get();

        MonetaryAmount reducedBy = was.subtract(now);

        //we only want Positive Reductions  !!
        if (reducedBy.isNegativeOrZero()) {
            return Optional.empty();
        }

        //THEN - we take then2 over then1
        Optional<MonetaryAmount> then = parser.apply(details.getThen2());

        if (then.isPresent() == false) {
            then = parser.apply(details.getThen1());
        }

        PriceReduction reduction = new PriceReduction(was, then, now);
        return Optional.of(reduction);

    }

    /**
     * This method extracts the price 'now'.
     * If a product doesn't have a "price.now" string which parses into an amount, the "price.now" is assumed to be ZERO
     *
     * @param parser
     * @param details
     * @return the monetary amount for the 'now' price.
     */
    private MonetaryAmount extractNow(Function<String, Optional<MonetaryAmount>> parser, PriceDetails details) {
        JsonNode node = details.getNow();
        Optional<MonetaryAmount> now = (node != null && node.isTextual()) ? parser.apply(node.asText()) : Optional.empty();
        return now.orElseGet(() -> Money.zero(getCurrency(details.getCurrency())));
    }

    private Optional<MonetaryAmount> extractWas(Function<String, Optional<MonetaryAmount>> parser, PriceDetails details) {
        return parser.apply(details.getWas());
    }

    /**
     * This is a function that takes a currency and returns monetary amount parser.
     * @param currency - the currency code.
     * @return a function that maps currency values into MonetaryAmounts with the specified currency.
     */
    private Function<String, Optional<MonetaryAmount>> getMoneyParser(CurrencyUnit currency) {
        MonetaryAmountDecimalFormatBuilder builder = MonetaryAmountDecimalFormatBuilder.of("##.##");
        builder.withCurrencyUnit(currency);
        MonetaryAmountFormat parser = builder.build();
        return (amt) -> {
            try {
                if(amt == null){
                    return Optional.empty();
                } else {
                    return Optional.ofNullable(parser.parse(amt));
                }
            } catch (MonetaryParseException mpe) {
                return Optional.empty();
            }
        };
    }

    private CurrencyUnit getCurrency(String currencyCode) {
        try {
            return Monetary.getCurrency(currencyCode);
        } catch (NullPointerException | UnknownCurrencyException ex) {
            return Monetary.getCurrency(Constants.DEFAULT_CURRENCY_CODE);
        }
    }

}
