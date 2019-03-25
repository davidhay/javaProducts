package com.ealanta.productapp.label;

import com.ealanta.productapp.misc.TestUtils;
import com.ealanta.productapp.price.PriceInfo;
import com.ealanta.productapp.price.PriceReduction;
import com.ealanta.productapp.product.Product;
import com.ealanta.productapp.product.ProductComparator;
import com.ealanta.productapp.service.ProductSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

import java.util.*;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class LabelledProductSourceTest {

    @Mock
    private ProductSource mProductSource;

    @Mock
    PriceLabelGenerator mLabelGenerator;

    private LabelledProductSource sut;

    private List<Product> productList;
    private Product p1;
    private Product p2;
    private Product p3;
    private PriceReduction pr1;
    private PriceReduction pr2;
    private PriceReduction pr3;

    @Before
    public void setup() {
         sut = new LabelledProductSource(new ProductComparator(),mProductSource,mLabelGenerator);
         p1 = getProduct("p1");
         p2 = getProduct("p2");
         p3 = getProduct("p3");
         productList = Arrays.asList(p1,p2,p3);
         pr1 = p1.getPriceInfo().getPriceReduction().get();
         pr2 = p2.getPriceInfo().getPriceReduction().get();
         pr3 = p3.getPriceInfo().getPriceReduction().get();
    }

    @Test
    public void testShowWasNow(){
        checkGetProducts(LabelType.ShowWasNow);
    }

    @Test
    public void testShowWasThenNow(){
        checkGetProducts(LabelType.ShowWasThenNow);
    }

    @Test
    public void testGetProducts(){
        checkGetProducts(LabelType.ShowPercDiscount);
    }

    public void checkGetProducts(LabelType labelType){

        when(mProductSource.getFilteredAndSortedProducts(
                any(),
                any())).thenReturn(productList);

        ArgumentCaptor<PriceReduction> prArg = ArgumentCaptor.forClass(PriceReduction.class);
        ArgumentCaptor<LabelType> lArg = ArgumentCaptor.forClass(LabelType.class);

        when(mLabelGenerator.generatePriceLabel(prArg.capture(),lArg.capture()))
                .thenReturn("LABEL1")
                .thenReturn("LABEL2")
                .thenReturn("LABEL3");

        List<LabelledProduct> result = sut.getProducts(labelType);

        for(LabelledProduct lp : result){
            System.out.printf("%s -> %s%n",lp.getProductId(),lp.getPriceLabel());
        }

        Assert.assertEquals(prArg.getAllValues(),Arrays.asList(pr1,pr2,pr3));
        Assert.assertEquals(3,lArg.getAllValues().size());
        Assert.assertTrue(lArg.getAllValues().stream().allMatch(p -> p.equals(labelType)));

        LabelledProduct lp1 = result.get(0);
        LabelledProduct lp2 = result.get(1);
        LabelledProduct lp3 = result.get(2);

        Assert.assertEquals("p1", lp1.getProductId());
        Assert.assertEquals("p2", lp2.getProductId());
        Assert.assertEquals("p3", lp3.getProductId());

        Assert.assertEquals("LABEL1", lp1.getPriceLabel());
        Assert.assertEquals("LABEL2", lp2.getPriceLabel());
        Assert.assertEquals("LABEL3", lp3.getPriceLabel());

        verify(mProductSource, times(1)).getFilteredAndSortedProducts(any(),any());
        verify(mLabelGenerator, times(3)).generatePriceLabel(any(),any());
        verifyNoMoreInteractions(mLabelGenerator,mProductSource);
    }

    private Product getProduct(String productId){
        Product result = new Product();
        PriceInfo info = new PriceInfo();
        PriceReduction pr = new PriceReduction(
                TestUtils.getMoney("100"),
                TestUtils.getMoney("90"));
        info.setPriceReduction(Optional.of(pr));
        result.setPriceInfo(info);
        result.setProductId(productId);
        return result;
    }
}
