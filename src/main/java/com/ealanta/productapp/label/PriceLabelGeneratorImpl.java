package com.ealanta.productapp.label;

import com.ealanta.productapp.price.PriceReduction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.money.MonetaryAmount;
import java.util.function.Function;

@Component
public class PriceLabelGeneratorImpl implements PriceLabelGenerator{

    private static final String FORMAT_WAS_THEN_NOW = "Was %s, then %s, now %s";
    private static final String FORMAT_WAS_NOW = "Was %s, now %s";
    private static final String FORMAT_DISCOUNT = "%s off - now %s";

    private final FormatUtils formatUtils;

    @Autowired
    public PriceLabelGeneratorImpl(FormatUtils formatUtils){
        this.formatUtils = formatUtils;
    }

    private Function<PriceReduction,String> getLabelGenerator(PriceReduction pr, LabelType labelType){
        switch (labelType) {
            case ShowPercDiscount:
                return this::getPriceLabelForDiscount;
            case ShowWasNow:
                return this::getPriceLabelForWasNow;
            case ShowWasThenNow:
                if(pr.getThen().isPresent()){
                    return this::getLabelForWasThenNow;
                } else {
                    return this::getPriceLabelForWasNow;
                }
            default: throw new RuntimeException("unreachable");
        }
    }

    @Override
    public String generatePriceLabel(PriceReduction priceReduction, LabelType labelType) {
        Function<PriceReduction,String> generator = getLabelGenerator(priceReduction,labelType);
        return generator.apply(priceReduction);
    }

    private String getLabelForWasThenNow(PriceReduction pr) {
        Assert.isTrue(pr.getThen().isPresent(), "Expected Then to be Present its empty");
        String formattedWas = formatAmount(pr.getWas());
        String formattedThen = formatAmount(pr.getThen().get());
        String formattedNow = formatAmount(pr.getNow());
        String result = String.format(FORMAT_WAS_THEN_NOW, formattedWas, formattedThen, formattedNow);
        return result;
    }

    private String getPriceLabelForWasNow(PriceReduction pr) {
        String formattedWas = formatAmount(pr.getWas());
        String formattedNow = formatAmount(pr.getNow());
        String result = String.format(FORMAT_WAS_NOW,formattedWas,formattedNow);
        return result;
    }

    private String formatAmount(MonetaryAmount ma){
        String formattedAmount = formatUtils.formatPrice(ma);
        return formattedAmount;
    }

    private String getPriceLabelForDiscount(PriceReduction pr) {
        String formattedNow = formatAmount(pr.getNow());
        String formattedPercent = formatUtils.formatPercentage(pr.getDiscount());
        String result = String.format(FORMAT_DISCOUNT,formattedPercent,formattedNow);
        return result;
    }

}
