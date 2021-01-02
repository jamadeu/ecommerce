package br.com.jamadeu.ecommerce.modules.product.util;

import br.com.jamadeu.ecommerce.modules.category.util.CategoryCreator;
import br.com.jamadeu.ecommerce.modules.product.requests.ReplaceProductRequest;

import java.math.BigDecimal;

public class ReplaceProductRequestCreator {
    public static ReplaceProductRequest createReplaceProductRequest() {
        return ReplaceProductRequest.builder()
                .id(1L)
                .productName("product")
                .description("description")
                .value(BigDecimal.valueOf(1L))
                .category(CategoryCreator.createValidCategory())
                .build();
    }
}
