package com.ealanta.productapp.product;

import com.ealanta.productapp.item.PriceDetails;
import com.ealanta.productapp.misc.Constants;
import com.ealanta.productapp.price.PriceInfo;
import com.ealanta.productapp.price.PriceReduction;
import org.javamoney.moneta.Money;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.money.MonetaryAmount;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProductComparatorTest{

    @Before
    public void setup() {
        pr1 = getProduct("pr1", Optional.of("1.00"));
        pr2 = getProduct("pr2", Optional.of("2.00"));
        pr3 = getProduct("pr3", Optional.of("3.00"));

        nopr1 = getProduct("noPr1", Optional.empty());
        nopr2 = getProduct("noPr2", Optional.empty());
        nopr3 = getProduct("noPr3", Optional.empty());
    }

    private Product getProduct(String productId, Optional<String> reductionAmount) {
        Product result = new Product();
        result.setProductId(productId);
        PriceDetails details = new PriceDetails();
        details.setCurrency(Constants.DEFAULT_CURRENCY_CODE);
        PriceInfo info = new PriceInfo();
        Optional<PriceReduction> optPR = reductionAmount.map(ra -> new PriceReduction(getMoney(ra), getMoney("0.00")));
        info.setPriceReduction(optPR);
        return result;
    }

    Product pr1;
    Product pr2;
    Product pr3;

    Product nopr1;
    Product nopr2;
    Product nopr3;

    private ProductComparator comparator = new ProductComparator();

    private List<Product> sortAscending(List<Product> source) {
        List<Product> sorted = source.stream().sorted(comparator).collect(Collectors.toList());
        return sorted;
    }

    private List<Product> sortDescending(List<Product> source) {
        List<Product> sorted = source.stream().sorted(comparator.reversed()).collect(Collectors.toList());
        return sorted;
    }

    MonetaryAmount getMoney(String amt) {
        return Money.of(new BigDecimal(amt).setScale(2), "GBP");
    }

    @Test
    public void confirmMonetaryAmountComparator() {
        MonetaryAmount m1 = getMoney("10");
        MonetaryAmount m2 = getMoney("100");
        Assert.assertTrue(m1.compareTo(m2) < 0);
        Assert.assertTrue(m2.compareTo(m1) > 0);
        Assert.assertTrue(m2.compareTo(m2) == 0);
    }

    @Test
    public void testProductComparator1() {
        Assert.assertTrue(comparator.compare(pr1, pr2) < 0);
        Assert.assertTrue(comparator.compare(pr2, pr1) > 0);
        Assert.assertTrue(comparator.compare(pr2, pr2) == 0);
    }

    @Test
    public void testSortDescendingByPriceRedution() {
        List<Product> products = Arrays.asList(pr1, pr2, pr3);
        List<Product> sorted = sortDescending(products);
        Assert.assertEquals(Arrays.asList(pr3, pr2, pr1), sorted);
    }

    @Test
    public void testSortAscendingByPriceReduction() {
        List<Product> products = Arrays.asList(pr3, pr2, pr1);
        List<Product> sorted = sortAscending(products);
        Assert.assertEquals(Arrays.asList(pr1, pr2, pr3), sorted);
    }

    @Test
    public void testSortDescendingByNoPriceReduction() {
        List<Product> products = Arrays.asList(nopr1, nopr2, nopr3);
        List<Product> sorted = sortDescending(products);
        Assert.assertEquals(Arrays.asList(nopr3, nopr2, nopr1), sorted);
    }

    @Test
    public void testSortAscendingByNoPriceReduction() {
        List<Product> products = Arrays.asList(nopr3, nopr2, nopr1);
        List<Product> sorted = sortAscending(products);
        Assert.assertEquals(Arrays.asList(nopr1, nopr2, nopr3), sorted);
    }

    @Test
    public void testSortAscendingMixed() {
        List<Product> products = Arrays.asList(pr1, nopr1);
        List<Product> sorted = sortAscending(products);
        Assert.assertEquals(Arrays.asList(nopr1, pr1), sorted);
    }

    @Test
    public void testSortDescendingMixed() {
        List<Product> products = Arrays.asList(nopr1, pr1);
        List<Product> sorted = sortDescending(products);
        Assert.assertEquals(Arrays.asList(pr1, nopr1), sorted);
    }

}
