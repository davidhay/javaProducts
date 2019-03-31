package com.ealanta.productapp.web

import com.ealanta.productapp.domain.LabelType
import com.ealanta.productapp.domain.LabelledProduct
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.hamcrest.Matchers.`is`

import kotlin.properties.Delegates

import com.ealanta.productapp.web.LabelledProductController.Companion.LABEL_TYPE
import com.ealanta.productapp.web.LabelledProductController.Companion.PATH
import com.nhaarman.mockito_kotlin.*
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import kotlin.test.assertEquals

@RunWith(SpringRunner::class)
@WebMvcTest(LabelledProductController::class)
class LabelledProductControllerTest {

    companion object {
        private const val JSON = MediaType.APPLICATION_JSON_UTF8_VALUE
    }

    @Autowired
    lateinit private var mockMvc: MockMvc

    @MockBean
    lateinit private var mLabelledProductsSource : (LabelType) -> List<LabelledProduct>

    private var argCaptor: KArgumentCaptor<LabelType> by Delegates.notNull()

    private val labelledProduct1 = LabelledProduct().apply {
        priceLabel="PRICE LABEL 1"
        productId="PRODUCT ID 1"
    }

    private val labelledProduct2 = LabelledProduct().apply {
        priceLabel="PRICE LABEL 2"
        productId="PRODUCT ID 2"
    }

    private val labelledProduct3 = LabelledProduct().apply {
        priceLabel="PRICE LABEL 3"
        productId="PRODUCT ID 3"
    }

    @Before
    fun setup() {
        labelledProduct3
        argCaptor = argumentCaptor()
        whenever(mLabelledProductsSource.invoke(argCaptor.capture())).thenReturn(listOf(labelledProduct1,labelledProduct2,labelledProduct3))
    }

    private fun checkBasicStuff(ra: ResultActions): ResultActions {
        ra.andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().contentType(JSON))

        for(idx in 0..2){
            ra.andExpect(jsonPath("$.products[$idx].productId", `is`("PRODUCT ID ${idx+1}")))
                .andExpect(jsonPath("$.products[$idx].priceLabel", `is`("PRICE LABEL ${idx+1}")))
        }
        return ra;
    }

    private fun doGet(): ResultActions  = mockMvc.perform(get(PATH))

    private fun doGetWithLabelType(labelType: String): ResultActions {
        return mockMvc.perform(get(PATH).param(LABEL_TYPE, labelType))
    }

    @Test
    fun testGetNoLabelTypeQueryString() {
        checkBasicStuff(doGet()).andReturn()
        assertEquals(LabelType.ShowWasNow, argCaptor.firstValue)
    }

    private fun checkGetWithLabelType(type: LabelType) {
        checkBasicStuff(doGetWithLabelType(type.name))
        assertEquals(type, argCaptor.firstValue)
        verify(mLabelledProductsSource, times(1)).invoke(argCaptor.firstValue)
        verifyNoMoreInteractions(mLabelledProductsSource)
    }

    @Test
    fun testWithInvalidLabelType() {
        doGetWithLabelType("BOB").andDo(print())
                .andExpect(status().isUnprocessableEntity)
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("The parameter labelType must have a value among : ShowWasNow, ShowWasThenNow, ShowPercDiscount"))
    }

    @Test
    fun testGetWithWasNow() = checkGetWithLabelType(LabelType.ShowWasNow)

    @Test
    fun testGetWithWasThenNow() = checkGetWithLabelType(LabelType.ShowWasThenNow)

    @Test
    fun testGetWithPercDiscount() = checkGetWithLabelType(LabelType.ShowPercDiscount)
}
