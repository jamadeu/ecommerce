package br.com.jamadeu.ecommerce.modules.category.util;

import br.com.jamadeu.ecommerce.modules.category.domain.Category;

public class CategoryCreator {
    public static Category createCategoryToBeSaved() {
        return Category.builder()
                .category("category")
                .build();
    }

    public static Category createValidCategory() {
        return Category.builder()
                .id(1L)
                .category("category")
                .build();
    }
}
