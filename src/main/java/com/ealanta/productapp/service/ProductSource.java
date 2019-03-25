package com.ealanta.productapp.service;

import com.ealanta.productapp.product.Product;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public interface ProductSource {

    public List<Product> getProducts();

    public List<Product> getFilteredAndSortedProducts (
            Predicate<Product> productFilter,
            Comparator<Product> comparator);
}
