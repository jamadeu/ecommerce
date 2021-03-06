package br.com.jamadeu.ecommerce.modules.category.util;

import br.com.jamadeu.ecommerce.modules.category.domain.Category;

public class CategoryCreator {
    public static Category createCategoryToBeSaved() {
        return Category.builder()
                .categoryName("category")
                .build();
    }

    public static Category createValidCategory() {
        return Category.builder()
                .id(1L)
                .categoryName("category")
                .build();
    }

    public static Category createUpdatedCategory() {
        return Category.builder()
                .id(1L)
                .categoryName("updated category")
                .build();
    }
}
