package com.ealanta.productapp.service;

import com.ealanta.productapp.item.ProductItem;
import com.ealanta.productapp.item.ProductItemsSource;
import com.ealanta.productapp.product.Product;
import com.ealanta.productapp.product.ProductItemConverter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import org.mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductSourceImplTest {

    @Mock
    private ProductItemsSource mProductItemSource;

    @Mock
    private ProductItemConverter mConverter;

    @Captor
    private ArgumentCaptor<ProductItem> argPI;

    private ProductSource sut;

    private Product p1;
    private Product p2;
    private Product p3;

    private ProductItem pItem1;
    private ProductItem pItem2;
    private ProductItem pItem3;

    private Comparator<Product> comparator = Comparator.comparing(Product::getProductId);

    @Before
    public void setup() {
        sut = new ProductSourceImpl(mProductItemSource,mConverter);

        this.p1 = new Product();
        this.p1.setProductId("p1");

        this.p2 = new Product();
        this.p2.setProductId("p2");

        this.p3 = new Product();
        this.p3.setProductId("p3");

        this.pItem1 = new ProductItem();

        this.pItem2 = new ProductItem();

        this.pItem3 = new ProductItem();
    }

    @Test
    public void testNoUnFilteredProducts(){
        when(mProductItemSource.getProductItems()).thenReturn(Collections.emptyList());
        List<Product> unfiltered= sut.getProducts();
        Assert.assertEquals(0,unfiltered.size());
        verify(mProductItemSource,times(1)).getProductItems();
        verifyNoMoreInteractions(mProductItemSource,mConverter);
    }

    @Test
    public void testSomeUnFilteredProducts(){
        when(mProductItemSource.getProductItems()).thenReturn(Arrays.asList(pItem1,pItem2));

        when(mConverter.convert(argPI.capture())).thenReturn(p1).thenReturn(p2);

        List<Product> unfiltered= sut.getProducts();

        Assert.assertEquals(2,unfiltered.size());
        Assert.assertEquals(p1, unfiltered.get(0));
        Assert.assertEquals(p2, unfiltered.get(1));

        verify(mProductItemSource,times(1)).getProductItems();
        verify(mConverter,times(2)).convert(any());
        verifyNoMoreInteractions(mProductItemSource,mConverter);
    }

    @Test
    public void testFilteringNoSorting() {
        when(mProductItemSource.getProductItems()).thenReturn(Arrays.asList(pItem1,pItem2,pItem3));

        when(mConverter.convert(argPI.capture())).thenReturn(p1).thenReturn(p2).thenReturn(p3);

        //this should filter out p1 !!
        List<Product> filtered = sut.getFilteredAndSortedProducts(p -> p != p1, comparator);

        Assert.assertEquals(2,filtered.size());
        Assert.assertEquals(p3, filtered.get(0));
        Assert.assertEquals(p2, filtered.get(1));

        verify(mProductItemSource,times(1)).getProductItems();
        verify(mConverter,times(3)).convert(any());


        verifyNoMoreInteractions(mProductItemSource,mConverter);

    }

    @Test
    public void testNoFilteringButSorting() {
        when(mProductItemSource.getProductItems()).thenReturn(Arrays.asList(pItem3,pItem2,pItem1));

        when(mConverter.convert(argPI.capture())).thenReturn(p3).thenReturn(p2).thenReturn(p1);

        //this should NOT filter out Products
        List<Product> filtered = sut.getFilteredAndSortedProducts(p -> p != null, comparator);

        Assert.assertEquals(3,filtered.size());
        Assert.assertEquals(p3, filtered.get(0));
        Assert.assertEquals(p2, filtered.get(1));
        Assert.assertEquals(p1, filtered.get(2));

        verify(mProductItemSource,times(1)).getProductItems();
        verify(mConverter,times(3)).convert(any());

        verifyNoMoreInteractions(mProductItemSource,mConverter);

    }

    @Test
    public void testilteringAndSorting() {
        when(mProductItemSource.getProductItems()).thenReturn(Arrays.asList(pItem3,pItem2,pItem1));

        when(mConverter.convert(argPI.capture())).thenReturn(p3).thenReturn(p2).thenReturn(p1);

        //this should filter out p2
        List<Product> filtered = sut.getFilteredAndSortedProducts(p -> p != p2, comparator);

        Assert.assertEquals(2,filtered.size());
        Assert.assertEquals(p3, filtered.get(0));
        Assert.assertEquals(p1, filtered.get(1));

        verify(mProductItemSource,times(1)).getProductItems();
        verify(mConverter,times(3)).convert(any());

        verifyNoMoreInteractions(mProductItemSource,mConverter);

    }


}
