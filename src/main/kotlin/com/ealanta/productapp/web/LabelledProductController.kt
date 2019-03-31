package com.ealanta.productapp.web

import com.ealanta.productapp.domain.LabelType
import com.ealanta.productapp.domain.LabelledProduct
import com.ealanta.productapp.domain.LabelledProducts
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
class LabelledProductController @Autowired
constructor(private val labelledProductsSource:(LabelType) -> List<LabelledProduct>) {

    companion object {
        const val LABEL_TYPE = "labelType"
        const val PATH = "/products"
        val DEFAULT_LABEL_TYPE = LabelType.ShowWasNow
    }

    @RequestMapping(value = [PATH], method = [RequestMethod.GET], produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    @ResponseBody
    fun getProducts(@RequestParam(value = LABEL_TYPE, required = false) labelTypeParam: LabelType?): LabelledProducts {

        val labelType = labelTypeParam ?: DEFAULT_LABEL_TYPE

        val result = LabelledProducts(labelledProductsSource(labelType))

        return result;
    }

}
