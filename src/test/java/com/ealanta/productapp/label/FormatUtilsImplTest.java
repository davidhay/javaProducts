package com.ealanta.productapp.label;

import com.ealanta.productapp.misc.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class FormatUtilsImplTest {

    private FormatUtils utils = new FormatUtilsImpl();

    @Test(expected = IllegalArgumentException.class)
    public void testFormatPercentageWithNull(){
       utils.formatPercentage(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatPriceWithNull(){
        utils.formatPrice(null);
    }

    @Test
    public void testFormatPercentage() {
        checkPercentage("25%",".25");
        checkPercentage("0%","0");
        checkPercentage("-10%","-.1");
        checkPercentage("100%","1");
        checkPercentage("110%","1.1");
        checkPercentage("33%",".33");
        checkPercentage("66%",".66");
    }

    @Test
    public void testFormattingLessThanTenWithIntegerValue() {
        checkPrice("£0.00", "0");
        checkPrice("£0.00", "0.00");

        checkPrice("£1.00", "1");
        checkPrice("£1.00", "1.00");

        checkPrice("£9.00", "9");
        checkPrice("£9.00", "9.00");
    }

    @Test
    public void testFormattingLessThanTenWithNonIntegerValue() {
        checkPrice("£0.01", "0.01");
        checkPrice("£0.10", "0.1");

        checkPrice("£1.10", "1.1");
        checkPrice("£1.01", "1.01");

        checkPrice("£2.20", "2.2");
        checkPrice("£2.02", "2.02");

        checkPrice("£9.90", "9.9");
        checkPrice("£9.09", "9.09");
    }
    @Test
    public void testFormattingNotLessThanTenWithIntegerValue() {
        checkPrice("£10", "10");
        checkPrice("£10", "10.00");

        checkPrice("£11", "11");
        checkPrice("£11", "11.00");

        checkPrice("£19", "19");
        checkPrice("£19", "19.00");

    }

    public void testFormattingNotLessThanTenWithNonIntegerValue() {
        checkPrice("£10.01", "10.01");
        checkPrice("£10.10", "10.1");

        checkPrice("£11.10", "11.1");
        checkPrice("£11.01", "11.01");

        checkPrice("£12.20", "12.2");
        checkPrice("£12.02", "12.02");

        checkPrice("£19.90", "19.9");
        checkPrice("£19.09", "19.09");
    }

    

    private void checkPercentage(String expected, String amt){
        BigDecimal bd = TestUtils.getBigDecimal(amt);
        String result = utils.formatPercentage(bd);
        Assert.assertEquals(expected,result);
    }

    private void checkPrice(String expected, String amt){
        String result = utils.formatPrice(TestUtils.getMoney(amt));
        Assert.assertEquals(expected, result);
    }


}
