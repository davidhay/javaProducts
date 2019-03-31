package com.ealanta.productapp.config;

import com.ealanta.productapp.domain.Product
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ProductComparatorConfig  {

    @Bean
    fun productComparator(): java.util.Comparator<Product> {
        val cProdId = compareBy<Product> { it.productId  }
        val cRedAmt = compareBy<Product> { it.reductionAmount  }
        //sort by descending reduction amount followed by ascending productIds
        return cRedAmt.reversed().thenComparing(cProdId);
    }

}
