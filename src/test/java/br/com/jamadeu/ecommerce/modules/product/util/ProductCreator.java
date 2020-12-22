package br.com.jamadeu.ecommerce.modules.product.util;

import br.com.jamadeu.ecommerce.modules.category.domain.Category;
import br.com.jamadeu.ecommerce.modules.category.util.CategoryCreator;
import br.com.jamadeu.ecommerce.modules.product.domain.Product;

import java.math.BigDecimal;

public class ProductCreator {
    public static Product createProductToBeSaved() {
        return Product.builder()
                .name("product")
                .description("description")
                .value(BigDecimal.ZERO)
                .category(CategoryCreator.createValidCategory())
                .build();
    }

    public static Product createProductToBeSaved(Category category) {
        return Product.builder()
                .name("product")
                .description("description")
                .value(BigDecimal.ZERO)
                .category(category)
                .build();
    }
}
