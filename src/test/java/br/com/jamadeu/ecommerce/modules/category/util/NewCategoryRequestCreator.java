package br.com.jamadeu.ecommerce.modules.category.util;

import br.com.jamadeu.ecommerce.modules.category.requests.NewCategoryRequest;
import org.jeasy.random.EasyRandom;

public class NewCategoryRequestCreator {
    public static NewCategoryRequest createNewCategoryRequest() {
        return new EasyRandom().nextObject(NewCategoryRequest.class);
    }
}
