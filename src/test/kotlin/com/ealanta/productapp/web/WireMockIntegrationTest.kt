package com.ealanta.productapp.web

import com.ealanta.productapp.ProductsApplication
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.junit.WireMockClassRule
import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.junit.*
import org.junit.runner.RunWith
import org.skyscreamer.jsonassert.JSONAssert
import org.skyscreamer.jsonassert.JSONCompareMode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.cloud.contract.wiremock.AutoConfigureHttpClient
import org.springframework.cloud.contract.wiremock.WireMockSpring
import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.util.StreamUtils
import org.springframework.web.client.RestTemplate
import java.nio.charset.Charset


@RunWith(SpringRunner::class)
@SpringBootTest(classes = [ProductsApplication::class], webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("wiremock")
@AutoConfigureHttpClient
class WireMockIntegrationTest {

    companion object {
        @ClassRule
        @JvmField
        var wiremock = WireMockClassRule(
                WireMockSpring.options().bindAddress("localhost").port(8888))

    }

    @get:Rule
    public val rule: WireMockRule = WireMockRule(WireMockConfiguration.wireMockConfig().withRootDirectory("src/test/resources/input"));

    @LocalServerPort
    var port: Int = 0

    @Autowired
    lateinit var appDriver: TestRestTemplate

    @Autowired
    lateinit var restTemplate: RestTemplate

    @Value("\${data.url}")
    lateinit var dataUrl: String

    @Value("\${spring.profiles.active:Unknown}")
    private val activeProfile: String? = null;

    @Value("classpath:output/labelledProducts.json")
    lateinit var expectedLabelledProducts: Resource;


    @Before
    fun setup() {
        stubFor("/products", MediaType.APPLICATION_JSON, "HELLO WORLD!!!");
    }

    private fun stubFor(path: String, contentType: MediaType, body: String) {
        wiremock.stubFor(get(urlPathEqualTo(path))
                .willReturn(aResponse().withHeader("Content-Type", contentType.toString()).withBodyFile("products.json")));
    }


    @Test
    fun testProfile() {
        Assert.assertEquals("wiremock", activeProfile);
    }

    @Test
    fun getRawData() {
        println("data url is ${dataUrl}")
        val rawJson = restTemplate.getForObject("http://localhost:8888/products", String::class.java)
        println(rawJson);
    }

    @Test
    fun getLabelledProducts() {
        val labelledProductsJson = appDriver.getForObject("/products", String::class.java)
        val expectedJson = StreamUtils.copyToString(expectedLabelledProducts.inputStream, Charset.defaultCharset());
        JSONAssert.assertEquals(expectedJson, labelledProductsJson, JSONCompareMode.LENIENT);
        println(labelledProductsJson);
    }
}