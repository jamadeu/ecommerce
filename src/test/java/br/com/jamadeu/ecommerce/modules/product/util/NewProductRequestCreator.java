package br.com.jamadeu.ecommerce.modules.product.util;

import br.com.jamadeu.ecommerce.modules.category.domain.Category;
import br.com.jamadeu.ecommerce.modules.category.util.CategoryCreator;
import br.com.jamadeu.ecommerce.modules.product.requests.NewProductRequest;

public class NewProductRequestCreator {

    public static NewProductRequest createNewProductRequest() {
        return NewProductRequest.builder()
                .productName("product")
                .category(CategoryCreator.createValidCategory())
                .description("description")
                .build();
    }

    public static NewProductRequest createNewProductRequest(Category category) {
        return NewProductRequest.builder()
                .productName("product")
                .category(category)
                .description("description")
                .build();
    }
}
