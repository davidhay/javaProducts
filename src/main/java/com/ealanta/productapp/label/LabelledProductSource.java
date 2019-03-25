package com.ealanta.productapp.label;

import com.ealanta.productapp.price.PriceReduction;
import com.ealanta.productapp.product.Product;
import com.ealanta.productapp.service.ProductSource;
import com.ealanta.productapp.product.ProductComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LabelledProductSource {

    private final ProductComparator productComparator;
    private final ProductSource productSource;
    private final PriceLabelGenerator labelGenerator;

    @Autowired
    public LabelledProductSource(
            ProductComparator productComparator,
            ProductSource productSource,
            PriceLabelGenerator labelGenerator){
        this.productSource = productSource;
        this.productComparator = productComparator;
        this.labelGenerator = labelGenerator;
    }

    private List<Product> getProducts(){
        return this.productSource.getFilteredAndSortedProducts(
                Product::hasPriceReduction,
                productComparator);
    }

    public List<LabelledProduct> getProducts(LabelType labelType){
        return getProducts().stream()
                       .map(p -> getLabelledProduct(p,labelType))
                       .collect(Collectors.toList());
    }

    public LabelledProduct getLabelledProduct(Product product,LabelType labelType){
        if(product.hasPriceReduction() == false) {
            throw new IllegalArgumentException("Cannot created a LabelledProduct  - the Product has no price reduction");
        }
        PriceReduction pr = product.getPriceInfo().getPriceReduction().get();

        String label = labelGenerator.generatePriceLabel(pr,labelType);
        LabelledProduct result = new LabelledProduct(product,label);
        return result;
    }
}
