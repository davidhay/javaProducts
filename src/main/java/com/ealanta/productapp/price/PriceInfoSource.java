package com.ealanta.productapp.price;

import com.ealanta.productapp.item.PriceDetails;

public interface PriceInfoSource {
    public PriceInfo getPriceInfo(PriceDetails details);
}
