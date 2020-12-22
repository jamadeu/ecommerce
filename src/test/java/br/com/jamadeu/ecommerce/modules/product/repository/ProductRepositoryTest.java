package br.com.jamadeu.ecommerce.modules.product.repository;

import br.com.jamadeu.ecommerce.modules.category.domain.Category;
import br.com.jamadeu.ecommerce.modules.category.repository.CategoryRepository;
import br.com.jamadeu.ecommerce.modules.category.util.CategoryCreator;
import br.com.jamadeu.ecommerce.modules.product.domain.Product;
import br.com.jamadeu.ecommerce.modules.product.util.ProductCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@DisplayName("ProductRepository tests")
class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    private Category category;

    @BeforeEach
    void setup() {
        category = categoryRepository.save(CategoryCreator.createValidCategory());
    }

    @Test
    @DisplayName("save persists product when successful")
    void save_PersistProduct_WhenSuccessful() {
        Product productToBeSaved = ProductCreator.createProductToBeSaved(category);
        Product productSaved = productRepository.save(productToBeSaved);

        Assertions.assertThat(productSaved).isNotNull();
        Assertions.assertThat(productSaved.getId()).isNotNull();
        Assertions.assertThat(productSaved.getName()).isEqualTo(productToBeSaved.getName());
        Assertions.assertThat(productSaved.getCategory())
                .isNotNull();
    }

    @Test
    @DisplayName("save updates product when successful")
    void save_UpdatesProduct_WhenSuccessful() {
        Product productSaved = productRepository.save(ProductCreator.createProductToBeSaved(category));
        productSaved.setName("new name");
        Product productUpdated = productRepository.save(productSaved);

        Assertions.assertThat(productUpdated).isNotNull();
        Assertions.assertThat(productUpdated.getId())
                .isNotNull()
                .isEqualTo(productSaved.getId());
        Assertions.assertThat(productUpdated.getName()).isEqualTo("new name");
        Assertions.assertThat(productSaved.getCategory())
                .isNotNull();
    }

}