package com.ealanta.productapp.item;

import com.ealanta.productapp.web.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ProductItemsSourceImpl implements ProductItemsSource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductItemsSourceImpl.class);

    private final String url;
    private RestTemplate restTemplate;

    private void checkURL(String url){
        if(UrlValidator.isValid(url) == false) {
            String msg = String.format("The URL [%s] is not valid.",url);
            LOGGER.error(msg);
            throw new IllegalArgumentException(msg);
        }
    }

    @Autowired
    ProductItemsSourceImpl(@Value("${data.url}") String url){
        checkURL(url);
        LOGGER.info("USING REMOTE URL {}",url);
        this.url = url;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public List<ProductItem> getProductItems() {
        ProductItems productItems = restTemplate.getForObject(url, ProductItems.class);
        return productItems.getProducts();
    }

}
