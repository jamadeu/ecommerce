package br.com.jamadeu.ecommerce.modules.category.util;

import br.com.jamadeu.ecommerce.modules.category.requests.ReplaceCategoryRequest;

public class ReplaceCategoryRequestCreator {
    public static ReplaceCategoryRequest createReplaceCategoryRequest() {
        return ReplaceCategoryRequest.builder()
                .id(1L)
                .categoryName("newCategory")
                .build();
    }
}
