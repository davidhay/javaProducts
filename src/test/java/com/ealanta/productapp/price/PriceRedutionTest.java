package com.ealanta.productapp.price;

import com.ealanta.productapp.misc.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

public class PriceRedutionTest {

    private void checkWas100Now80(PriceReduction pr){
        Assert.assertEquals(TestUtils.getMoney("80"),pr.getNow());
        Assert.assertEquals(TestUtils.getMoney("100"),pr.getWas());
        Assert.assertEquals(TestUtils.getMoney("20"),pr.getReductionAmount());
        Assert.assertEquals(TestUtils.getBigDecimal(".2"),pr.getDiscount());
    }

    @Test
    public void testPriceReductionWasNow() {
        PriceReduction pr = new PriceReduction(
                TestUtils.getMoney("100"),
                TestUtils.getMoney("80"));
        checkWas100Now80(pr);
        Assert.assertTrue(pr.getThen().isPresent() == false);
    }

    @Test
    public void testPriceReductionWasThenNow1() {
        PriceReduction pr = new PriceReduction(
                TestUtils.getMoney("100"),
                Optional.of(TestUtils.getMoney("90")),
                TestUtils.getMoney("80"));
        checkWas100Now80(pr);
        Assert.assertTrue(pr.getThen().isPresent());
        Assert.assertEquals(TestUtils.getMoney("90"),pr.getThen().get());
    }

    @Test
    public void testPriceReductionWasThenNow2() {
        PriceReduction pr = new PriceReduction(
                TestUtils.getMoney("100"),
                Optional.empty(),
                TestUtils.getMoney("80"));
        checkWas100Now80(pr);
        Assert.assertTrue(pr.getThen().isPresent()==false);
    }


    @Test
    public void testPriceReductionThirdOff() {
        PriceReduction pr = new PriceReduction(
                TestUtils.getMoney("100"),
                TestUtils.getMoney("66.66"));
        Assert.assertEquals(TestUtils.getMoney("100"),pr.getWas());
        Assert.assertEquals(TestUtils.getMoney("66.66"),pr.getNow());
        Assert.assertTrue(pr.getThen().isPresent() == false);
        Assert.assertEquals(TestUtils.getMoney("33.34"),pr.getReductionAmount());
        Assert.assertEquals(TestUtils.getBigDecimal(".33"),pr.getDiscount());
    }

    @Test
    public void testPriceReductionTwoThirdOff() {
        PriceReduction pr = new PriceReduction(
                TestUtils.getMoney("100"),
                TestUtils.getMoney("33.33"));
        Assert.assertEquals(TestUtils.getMoney("100"),pr.getWas());
        Assert.assertEquals(TestUtils.getMoney("33.33"),pr.getNow());
        Assert.assertTrue(pr.getThen().isPresent() == false);
        Assert.assertEquals(TestUtils.getMoney("66.67"),pr.getReductionAmount());
        Assert.assertEquals(TestUtils.getBigDecimal(".67"),pr.getDiscount());
    }

    @Test
    public void testPriceReductionNowFree() {
        PriceReduction pr = new PriceReduction(
                TestUtils.getMoney("100"),
                TestUtils.getMoney("0"));
        Assert.assertEquals(TestUtils.getMoney("100"),pr.getWas());
        Assert.assertEquals(TestUtils.getMoney("0"),pr.getNow());
        Assert.assertTrue(pr.getThen().isPresent() == false);
        Assert.assertEquals(TestUtils.getMoney("100"),pr.getReductionAmount());
        Assert.assertEquals(TestUtils.getBigDecimal("1"),pr.getDiscount());
    }
}
