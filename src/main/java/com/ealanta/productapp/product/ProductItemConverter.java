package com.ealanta.productapp.product;

import com.ealanta.productapp.color.ColorLookup;
import com.ealanta.productapp.label.FormatUtils;
import com.ealanta.productapp.price.PriceInfo;
import com.ealanta.productapp.item.ColorSwatchItem;
import com.ealanta.productapp.item.ProductItem;
import com.ealanta.productapp.price.PriceInfoSource;
import org.springframework.stereotype.Component;

import java.text.Format;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ProductItemConverter {

    private final ColorLookup colorLookup;
    private final PriceInfoSource priceInfoSource;
    private final FormatUtils formatUtils;

    public ProductItemConverter(ColorLookup colorLookup, PriceInfoSource priceInfoSource, FormatUtils formatUtils){
        this.colorLookup = colorLookup;
        this.priceInfoSource = priceInfoSource;
        this.formatUtils = formatUtils;
    }

    public Product convert(ProductItem item) {
        Product result = convertBasic(item);

        //convert the swatches
        result.getColorSwatches().addAll(convertColorSwatchItems(item.getColorSwatches()));

        //generate the PriceInfo
        PriceInfo priceInfo = priceInfoSource.getPriceInfo(item.getPrice());
        result.setPriceInfo(priceInfo);

        //generate the 'formatted nowPrice' string
        result.setNowPrice(formatUtils.formatPrice(priceInfo.getNowPrice()));

        return result;
    }

    private Product convertBasic(ProductItem item) {
        Product result  = new Product();
        result.setProductId(item.getProductId());
        result.setTitle(item.getTitle());
        return result;
    }

    private List<ColorSwatch> convertColorSwatchItems(ColorSwatchItem[] colorSwatchItems){

        Stream<ColorSwatchItem> stream =
                colorSwatchItems == null
                        ? Stream.empty()
                        : Arrays.stream(colorSwatchItems);

        List<ColorSwatch> result =
               stream
                .map(this::convertColorSwatchItem)
                .collect(Collectors.toList());
        return result;
    }


    private ColorSwatch convertColorSwatchItem(ColorSwatchItem item){
        ColorSwatch result  = new ColorSwatch();
        result.setColor(item.getColor());
        result.setSkuId(item.getSkuId());
        String basic = item.getBasicColor();
        result.setBasicColor(basic);
        Optional<String> hexRgb = colorLookup.lookupRgbHexForColor(basic);
        hexRgb.ifPresent(result::setRgbColor);
        return result;
    }

}
