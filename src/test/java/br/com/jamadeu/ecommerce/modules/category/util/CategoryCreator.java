package br.com.jamadeu.ecommerce.modules.category.util;

import br.com.jamadeu.ecommerce.modules.category.domain.Category;

public class CategoryCreator {
    public static Category createCategoryToBeSaved() {
        return Category.builder()
                .name("category")
                .build();
    }

    public static Category createValidCategory() {
        return Category.builder()
                .id(1L)
                .name("category")
                .build();
    }

    public static Category createUpdatedCategory() {
        return Category.builder()
                .id(1L)
                .name("updated category")
                .build();
    }
}
