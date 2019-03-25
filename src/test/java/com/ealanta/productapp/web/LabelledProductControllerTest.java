package com.ealanta.productapp.web;

import com.ealanta.productapp.label.LabelType;
import com.ealanta.productapp.label.LabelledProduct;
import com.ealanta.productapp.label.LabelledProductSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.io.IOException;
import java.util.Arrays;

import static com.ealanta.productapp.web.LabelledProductController.LABEL_TYPE;
import static com.ealanta.productapp.web.LabelledProductController.PATH;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(LabelledProductController.class)
public class LabelledProductControllerTest {

    private static String JSON = MediaType.APPLICATION_JSON_UTF8_VALUE;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    LabelledProductSource labelledProductSource;

    @Captor
    ArgumentCaptor<LabelType> argLabelType;

    LabelledProduct lprod1;
    LabelledProduct lprod2;
    LabelledProduct lprod3;

    @Before
    public void setup() throws IOException {
        lprod1 = new LabelledProduct();
        lprod2 = new LabelledProduct();
        lprod3 = new LabelledProduct();

        when(labelledProductSource.getProducts(argLabelType.capture())).thenReturn(Arrays.asList(lprod1, lprod2, lprod3));
    }

    private ResultActions checkBasicStuff(ResultActions ra) {
        try {
            return ra.andDo(print())
                           .andExpect(status().isOk())
                           .andExpect(content().contentType(JSON));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private ResultActions doGet() {
        try {
            return mockMvc.perform(get(PATH));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private ResultActions doGetWithLabelType(String labelType) {
        try {
            return mockMvc.perform(get(PATH).param(LABEL_TYPE, labelType));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Test
    public void testGetNoLabelTypeQueryString() {
        checkBasicStuff(doGet()).andReturn();
        Assert.assertEquals(LabelType.ShowWasNow, argLabelType.getValue());
    }

    private void checkGetWithLableType(LabelType type) {
        checkBasicStuff(doGetWithLabelType(type.name()));
        Assert.assertEquals(type, argLabelType.getValue());
        verify(labelledProductSource, times(1)).getProducts(argLabelType.getValue());
        verifyNoMoreInteractions(labelledProductSource);
    }

    @Test
    public void testWithInvalidLabelType() throws Exception {
        doGetWithLabelType("BOB").andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("The parameter labelType must have a value among : ShowWasNow, ShowWasThenNow, ShowPercDiscount"));
    }

    @Test
    public void testGetWithWasNow() throws Exception {
        checkGetWithLableType(LabelType.ShowWasNow);
    }

    @Test
    public void testGetWithWasThenNow() throws Exception {
        checkGetWithLableType(LabelType.ShowWasThenNow);
    }

    @Test
    public void testGetWithPercDiscount() throws Exception {
        checkGetWithLableType(LabelType.ShowPercDiscount);
    }


}
