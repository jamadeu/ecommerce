package br.com.jamadeu.ecommerce.modules.category.util;

import br.com.jamadeu.ecommerce.modules.category.domain.Category;
import org.jeasy.random.EasyRandom;

public class CategoryCreator {
    public static Category createCategoryToBeSaved() {
        Category category = new EasyRandom().nextObject(Category.class);
        category.setId(null);
        return category;
    }

    public static Category createValidCategory() {
        return new EasyRandom().nextObject(Category.class);
    }
}
