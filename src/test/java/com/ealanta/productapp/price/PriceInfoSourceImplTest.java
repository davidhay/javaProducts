package com.ealanta.productapp.price;

import com.ealanta.productapp.item.PriceDetails;
import com.ealanta.productapp.misc.TestUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PriceInfoSourceImplTest {

    private PriceInfoSource sut;

    @Before
    public void setup(){
        sut = new PriceInfoSourceImpl();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullPriceDetails(){
        sut.getPriceInfo(null);
    }

    @Test
    public void testPriceDetailsWithNoCurrencyAndNoNow() {
        PriceDetails pd = new PriceDetails();
        //pd.setCurrency();
        pd.setNow(null);
        PriceInfo info = sut.getPriceInfo(pd);
        //PRICE REDUCTION
        Assert.assertTrue(info.getPriceReduction().isPresent() == false);

        //NOW PRICE
        Assert.assertEquals(TestUtils.getMoney("0"),info.getNowPrice());

    }

    @Test
    public void testPriceDetailsWithEmptyCurrencyAndNoNow() {
        PriceDetails pd = new PriceDetails();
        //pd.setCurrency();
        pd.setNow(null);
        PriceInfo info = sut.getPriceInfo(pd);
        //PRICE REDUCTION
        Assert.assertTrue(info.getPriceReduction().isPresent() == false);

        //NOW PRICE
        Assert.assertEquals(TestUtils.getMoney("0"),info.getNowPrice());

    }

    @Test
    public void testPriceDetailsWithNowOnly() {
        PriceDetails pd = new PriceDetails();

        TextNode nowNode = TextNode.valueOf("12.34");
        pd.setNow(nowNode);

        PriceInfo info = sut.getPriceInfo(pd);
        pd.setThen1("");
        pd.setThen2("");
        //PRICE REDUCTION
        Assert.assertTrue(info.getPriceReduction().isPresent() == false);

        //NOW PRICE
        Assert.assertEquals(TestUtils.getMoney("12.34"),info.getNowPrice());

    }

    @Test
    public void testPriceDetailsWithNowObjectNode() {
        PriceDetails pd = new PriceDetails();

        JsonNodeFactory fact = new JsonNodeFactory(true);
        Map<String, JsonNode> kids = new HashMap<>();
        kids.put("A",TextNode.valueOf("A1"));
        kids.put("B",TextNode.valueOf("B1"));
        ObjectNode nowNode = new ObjectNode(fact,kids);
        pd.setNow(nowNode);

        PriceInfo info = sut.getPriceInfo(pd);
        //PRICE REDUCTION
        Assert.assertTrue(info.getPriceReduction().isPresent() == false);

        //NOW PRICE
        Assert.assertEquals(TestUtils.getMoney("0"),info.getNowPrice());
    }

    private PriceDetails getPriceDetailsWithWasAndNow(){
        PriceDetails pd = new PriceDetails();

        TextNode nowNode = TextNode.valueOf("12.34");
        pd.setNow(nowNode);
        pd.setWas("21.12");
        return pd;
    }

    @Test
    public void testPriceDetailsWithWasAndNow() {

        PriceDetails pd = getPriceDetailsWithWasAndNow();
        pd.setThen2("");
        pd.setThen1("");

        PriceInfo info = sut.getPriceInfo(pd);

        //PRICE REDUCTION
        Assert.assertTrue(info.getPriceReduction().isPresent());
        PriceReduction pr = info.getPriceReduction().get();

        Assert.assertEquals(TestUtils.getMoney("21.12"),pr.getWas());
        Assert.assertEquals(TestUtils.getMoney("12.34"),pr.getNow());
        Assert.assertEquals(Optional.empty(), pr.getThen());

        //NOW PRICE
        Assert.assertEquals(TestUtils.getMoney("12.34"),info.getNowPrice());
    }

    private PriceReduction checkPriceInfoForWasNow(PriceInfo info){
        //PRICE REDUCTION
        Assert.assertTrue(info.getPriceReduction().isPresent());
        PriceReduction pr = info.getPriceReduction().get();

        Assert.assertEquals(TestUtils.getMoney("21.12"),pr.getWas());
        Assert.assertEquals(TestUtils.getMoney("12.34"),pr.getNow());
        //NOW PRICE
        Assert.assertEquals(TestUtils.getMoney("12.34"),info.getNowPrice());
        return pr;
    }

    @Test
    public void testPriceDetailsWithWasAndNowAndThen1() {
        PriceDetails pd = getPriceDetailsWithWasAndNow();
        pd.setThen1("15.16");

        PriceInfo info = sut.getPriceInfo(pd);
        PriceReduction pr = checkPriceInfoForWasNow(info);

        Assert.assertEquals(Optional.of(TestUtils.getMoney("15.16")), pr.getThen());
    }

    @Test
    public void testPriceDetailsWithWasAndNowAndThen2() {
        PriceDetails pd = getPriceDetailsWithWasAndNow();
        pd.setThen2("15.16");

        PriceInfo info = sut.getPriceInfo(pd);
        PriceReduction pr = checkPriceInfoForWasNow(info);

        Assert.assertEquals(Optional.of(TestUtils.getMoney("15.16")), pr.getThen());
    }

    @Test
    public void testPriceDetailsWithWasAndNowAndThen1AndThen2() {
        PriceDetails pd = getPriceDetailsWithWasAndNow();
        pd.setThen1("15.16");
        pd.setThen2("16.17");

        PriceInfo info = sut.getPriceInfo(pd);
        PriceReduction pr = checkPriceInfoForWasNow(info);

        Assert.assertEquals(Optional.of(TestUtils.getMoney("16.17")), pr.getThen());
    }

}
