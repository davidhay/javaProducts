package com.ealanta.productapp.product;

import org.springframework.stereotype.Component;

import javax.money.MonetaryAmount;
import java.util.Comparator;
import java.util.Optional;

@Component
public class ProductComparator implements Comparator<Product> {

    @Override
    public int compare(Product left,Product right) {
        Optional<MonetaryAmount> leftRed = left.getReductionAmount();
        Optional<MonetaryAmount> rightRed = right.getReductionAmount();

        boolean thisHasRed = leftRed.isPresent();
        boolean otherHasRed = rightRed.isPresent();
        if (thisHasRed && otherHasRed) {
           return leftRed.get().compareTo(rightRed.get());
        } else if (!thisHasRed && !otherHasRed) {
           return left.getProductId().compareTo(right.getProductId());
        } else if (thisHasRed) {
            return 1;
        } else {
            return -1;
        }
    }
}
