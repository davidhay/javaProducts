package com.ealanta.productapp.label;

import javax.money.MonetaryAmount;
import java.math.BigDecimal;

public interface FormatUtils {
    public String formatPrice(MonetaryAmount ma);

    public String formatPercentage(BigDecimal percentage);
}
