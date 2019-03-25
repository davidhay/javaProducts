package com.ealanta.productapp.label;

import static com.ealanta.productapp.misc.TestUtils.*;
import com.ealanta.productapp.price.PriceReduction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
public class PriceLabelGeneratorImplTest {

    private PriceLabelGenerator priceLabelGenerator;

    @Before
    public void setup(){
        this.priceLabelGenerator = new PriceLabelGeneratorImpl(new FormatUtilsImpl());
    }

    private PriceReduction getWasNow(String was, String now){
        return new PriceReduction(getMoney(was),getMoney(now));
    }

    private PriceReduction getWasThenNow(String was, String then, String now){
        return new PriceReduction(getMoney(was), Optional.ofNullable(getMoney(then)),getMoney(now));
    }


    private void checkShowWasNow(String expectedLabel, String was, String now){
        PriceReduction wasNow = getWasNow(was,now);
        String label = priceLabelGenerator.generatePriceLabel(wasNow,LabelType.ShowWasNow);
        Assert.assertEquals(expectedLabel,label);
    }

    private void checkShowWasThenNow(String expectedLabel, String was, String then,String now){
        PriceReduction wasNow = getWasThenNow(was,then,now);
        String label = priceLabelGenerator.generatePriceLabel(wasNow,LabelType.ShowWasThenNow);
        Assert.assertEquals(expectedLabel,label);
    }

    private void checkShowDiscount(String expectedLabel, String was, String now){
        PriceReduction wasNow = getWasNow(was,now);
        String label = priceLabelGenerator.generatePriceLabel(wasNow,LabelType.ShowPercDiscount);
        Assert.assertEquals(expectedLabel,label);
    }

    @Test
    public void testShowWasNow(){
        checkShowWasNow("Was £100, now £9.00","100","9");
        checkShowWasNow("Was £100, now £10","100","10");
        checkShowWasNow("Was £100, now £11","100","11");

        checkShowWasNow("Was £9.00, now £8.00","9","8");
        checkShowWasNow("Was £10, now £8.00","10","8");
        checkShowWasNow("Was £11, now £8.00","11","8");

        checkShowWasNow("Was £100, now £90","100","90");
        checkShowWasNow("Was £9.99, now £6.66","9.99","6.66");

    }

    @Test
    public void testShowWasThenNow(){
        checkShowWasThenNow("Was £10, then £9.00, now £8.00","10","9","8");
        checkShowWasThenNow("Was £98.76, then £87.65, now £76.54","98.76","87.65","76.54");
        checkShowWasThenNow("Was £10, now £8.00","10",null,"8");
        checkShowWasThenNow("Was £98.76, now £76.54","98.76",null,"76.54");
     }

    @Test
    public void testShowPercDiscount(){
        checkShowDiscount("25% off - now £75","100","75");
        checkShowDiscount("0% off - now £100","100","100");
        checkShowDiscount("90% off - now £10","100","10");
        checkShowDiscount("91% off - now £9.00","100","9");
        checkShowDiscount("100% off - now £0.00","100","0");
        checkShowDiscount("33% off - now £66.66","100","66.66");
        checkShowDiscount("67% off - now £33.33","100","33.33");
        //TODO this is a bit of an edge case - maybe it should be an errors
        checkShowDiscount("200% off - now £-100.00","100","-100");
    }

}
