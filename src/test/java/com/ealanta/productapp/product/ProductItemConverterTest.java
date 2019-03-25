package com.ealanta.productapp.product;

import com.ealanta.productapp.color.ColorLookup;
import com.ealanta.productapp.item.ColorSwatchItem;
import com.ealanta.productapp.item.PriceDetails;
import com.ealanta.productapp.item.ProductItem;
import com.ealanta.productapp.label.FormatUtils;
import com.ealanta.productapp.misc.TestUtils;
import com.ealanta.productapp.price.PriceInfo;
import com.ealanta.productapp.price.PriceInfoSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;
import org.mockito.*;

import java.util.Arrays;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class ProductItemConverterTest {

    private ProductItemConverter sut;

    @Mock
    private ColorLookup mColorLookup;

    @Mock
    private PriceInfoSource mPriceInfo;

    @Mock
    private FormatUtils mFormatUtils;

    @Captor
    private ArgumentCaptor<PriceDetails> argPd;

    @Captor
    private ArgumentCaptor<String> argCol;

    private ColorSwatchItem swItem1;
    private ColorSwatchItem swItem2;

    @Before
    public void setup(){
        sut = new ProductItemConverter(mColorLookup,mPriceInfo,mFormatUtils);
        swItem1 = new ColorSwatch();
        swItem1.setBasicColor("BASIC-1");
        swItem1.setColor("COLOR-1");
        swItem1.setSkuId("SKU-ID");

        swItem2 = new ColorSwatch();
        swItem2.setBasicColor("BASIC-2");
        swItem2.setColor("COLOR-2");
        swItem2.setSkuId("SKU-ID");
    }

    @Test
    public void testConvertNoSwatches(){
        ProductItem item = new ProductItem();
        /*
        ColorSwatchItem[] swatches = new ColorSwatchItem[]{swItem1,swItem2};
        item.setColorSwatches(swatches);
        */

        item.setTitle("TITLE");
        item.setProductId("PRODUCTID");
        PriceDetails pd = new PriceDetails();
        item.setPrice(pd);

        PriceInfo pInfo = new PriceInfo();

        when(mPriceInfo.getPriceInfo(argPd.capture())).thenReturn(pInfo);

        Product product = sut.convert(item);

        Assert.assertEquals(pd,argPd.getValue());

        Assert.assertEquals(pInfo,product.getPriceInfo());
        Assert.assertEquals(0,product.getColorSwatches().size());
        Assert.assertEquals("PRODUCTID",product.getProductId());
        Assert.assertEquals("TITLE",product.getTitle());

        verify(mColorLookup, never()).lookupRgbHexForColor(any());
        verify(mPriceInfo, atLeastOnce()).getPriceInfo(argPd.getValue());
        verify(mFormatUtils, times(1)).formatPrice(any());
        verifyNoMoreInteractions(mColorLookup,mPriceInfo,mFormatUtils);
    }

    @Test
    public void testConvertWithSwatches(){
        ProductItem item = new ProductItem();

        ColorSwatchItem[] swatches = new ColorSwatchItem[]{swItem1,swItem2};
        item.setColorSwatches(swatches);

        item.setTitle("TITLE");
        item.setProductId("PRODUCTID");
        item.setColorSwatches(swatches);

        PriceDetails pd = new PriceDetails();
        item.setPrice(pd);

        PriceInfo pInfo = new PriceInfo();

        when(mPriceInfo.getPriceInfo(argPd.capture())).thenReturn(pInfo);
        when(mColorLookup.lookupRgbHexForColor(argCol.capture()))
                .thenReturn(Optional.of("HEX1"))
                .thenReturn(Optional.empty());

        Product product = sut.convert(item);

        Assert.assertEquals(pd,argPd.getValue());
        Assert.assertEquals(Arrays.asList("BASIC-1","BASIC-2"),argCol.getAllValues());

        Assert.assertEquals(pInfo,product.getPriceInfo());
        Assert.assertEquals(2,product.getColorSwatches().size());
        Assert.assertEquals("PRODUCTID",product.getProductId());
        Assert.assertEquals("TITLE",product.getTitle());

        ColorSwatch sw1 = product.getColorSwatches().get(0);
        ColorSwatch sw2 = product.getColorSwatches().get(1);

        checkSwatchItemMatcesSwatch(product.getColorSwatches().get(0),swItem1,"HEX1");
        checkSwatchItemMatcesSwatch(product.getColorSwatches().get(1),swItem2,null);

        verify(mColorLookup, times(2)).lookupRgbHexForColor(any());
        verify(mPriceInfo, atLeastOnce()).getPriceInfo(argPd.getValue());
        verify(mFormatUtils, times(1)).formatPrice(any());
        verifyNoMoreInteractions(mColorLookup,mPriceInfo,mFormatUtils);

    }

    private void checkSwatchItemMatcesSwatch(ColorSwatch result, ColorSwatchItem orig, String expectedRgb){
        Assert.assertEquals(orig.getBasicColor(),result.getBasicColor());
        Assert.assertEquals(orig.getColor(),result.getColor());
        Assert.assertEquals(orig.getSkuId(),result.getSkuId());
        Assert.assertEquals(expectedRgb,result.getRgbColor());
    }
}
