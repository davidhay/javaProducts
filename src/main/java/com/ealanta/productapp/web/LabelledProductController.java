package com.ealanta.productapp.web;

import com.ealanta.productapp.label.LabelledProduct;
import com.ealanta.productapp.label.LabelType;
import com.ealanta.productapp.label.LabelledProductSource;
import com.ealanta.productapp.label.LabelledProducts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class LabelledProductController {

    public  static final String LABEL_TYPE="labelType";
    public  static final String PATH="/products";

    private final LabelledProductSource labelledProductSource;
    private final LabelType DEFAULT_LABEL_TYPE = LabelType.ShowWasNow;

    @Autowired
    public LabelledProductController(LabelledProductSource labelledProductSource){
        this.labelledProductSource = labelledProductSource;
    }

    @RequestMapping(value = PATH, method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LabelledProducts getProducts(@RequestParam(value = LABEL_TYPE,required = false) LabelType labelTypeParam){

        LabelType labelType = Optional.ofNullable(labelTypeParam).orElse(DEFAULT_LABEL_TYPE);

        List<LabelledProduct> products = labelledProductSource.getProducts(labelType);
        return new LabelledProducts(products);
    }
}
