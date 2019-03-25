package com.ealanta.productapp.color;

import com.ealanta.productapp.color.ColorLookup;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
public class ColorLookupTest {

    @Autowired
    private ColorLookup lookup;

    private void checkRGB(String color,String expectedRGB){
        Assert.assertEquals(Optional.ofNullable(expectedRGB),lookup.lookupRgbHexForColor(color));
    }

    @Test
    public void testPositiveLookup() {
        checkRGB("Red","FF0000");
        checkRGB("pink","FFC0CB");
        checkRGB("whiTe","FFFFFF");
        checkRGB("blue","0000FF");
        checkRGB("purple","800080");
        checkRGB("yellOW","FFFF00");
        checkRGB("black","000000");
        checkRGB("orange","FFA500");
        checkRGB("green","00FF00");
        checkRGB("GRAY","808080");
        checkRGB("grey","808080");
    }

    @Test
    public void testNegativeLookup() {
        checkRGB("Silver",null);
        checkRGB("",null);
        checkRGB("   ",null);
        checkRGB(null,null);
    }

}
