package br.com.jamadeu.ecommerce.modules.category.util;

import br.com.jamadeu.ecommerce.modules.category.requests.NewCategoryRequest;

public class NewCategoryRequestCreator {
    public static NewCategoryRequest createNewCategoryRequest() {
        return NewCategoryRequest.builder()
                .categoryName("newCategory")
                .build();
    }
}
