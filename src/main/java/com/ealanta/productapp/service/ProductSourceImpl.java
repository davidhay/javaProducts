package com.ealanta.productapp.service;

import com.ealanta.productapp.product.Product;
import com.ealanta.productapp.item.ProductItemsSource;
import com.ealanta.productapp.product.ProductItemConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class ProductSourceImpl implements ProductSource {

    private ProductItemsSource productItemsSource;
    private ProductItemConverter converter;

    @Autowired
    public ProductSourceImpl(
            ProductItemsSource productItemsSource,
            ProductItemConverter converter) {
        this.productItemsSource = productItemsSource;
        this.converter = converter;
    }

    public List<Product> getProducts(){
        return productItemsSource.getProductItems()
                       .stream()
                       .map( converter::convert)
                       .collect(Collectors.toList());
    }

    public List<Product> getFilteredAndSortedProducts (
            Predicate<Product> productFilter,
            Comparator<Product> comparator) {
        return getProducts().stream().filter(productFilter).sorted(comparator.reversed()).collect(Collectors.toList());
    }

}
