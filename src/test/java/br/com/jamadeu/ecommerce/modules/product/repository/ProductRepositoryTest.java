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

import java.util.List;
import java.util.Optional;

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
        Assertions.assertThat(productSaved.getProductName()).isEqualTo(productToBeSaved.getProductName());
        Assertions.assertThat(productSaved.getCategory())
                .isNotNull();
    }

    @Test
    @DisplayName("save updates product when successful")
    void save_UpdatesProduct_WhenSuccessful() {
        Product productSaved = productRepository.save(ProductCreator.createProductToBeSaved(category));
        productSaved.setProductName("new name");
        Product productUpdated = productRepository.save(productSaved);

        Assertions.assertThat(productUpdated).isNotNull();
        Assertions.assertThat(productUpdated.getId())
                .isNotNull()
                .isEqualTo(productSaved.getId());
        Assertions.assertThat(productUpdated.getProductName()).isEqualTo("new name");
        Assertions.assertThat(productSaved.getCategory())
                .isNotNull();
    }

    @Test
    @DisplayName("delete deletes product when successful")
    void delete_DeleteProduct_WhenSuccessful() {
        Product product = productRepository.save(ProductCreator.createProductToBeSaved(category));
        productRepository.delete(product);
        Optional<Product> productOptional = productRepository.findById(product.getId());

        Assertions.assertThat(productOptional).isEmpty();
    }

    @Test
    @DisplayName("findByName returns optional of product when successful")
    void findByName_ReturnsOptionalProduct_WhenSuccessful() {
        Product product = productRepository.save(ProductCreator.createProductToBeSaved(category));
        Optional<Product> productOptional = productRepository.findByProductName(product.getProductName());

        Assertions.assertThat(productOptional)
                .isNotNull()
                .isNotEmpty();
        Assertions.assertThat(productOptional.get().getId())
                .isEqualTo(product.getId());
    }

    @Test
    @DisplayName("findByName returns an empty optional when product is not found")
    void findByName_ReturnsAnEmptyOptional_WhenProductIsNotFound() {
        Optional<Product> productOptional = productRepository.findByProductName("not found product");

        Assertions.assertThat(productOptional)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("findByCategory returns list of product when successful")
    void findByCategory_ReturnsListProduct_WhenSuccessful() {
        Product product = productRepository.save(ProductCreator.createProductToBeSaved(category));
        List<Product> productOptional = productRepository.findByCategory(category);

        Assertions.assertThat(productOptional)
                .isNotNull()
                .isNotEmpty();
        Assertions.assertThat(productOptional.get(0).getId())
                .isEqualTo(product.getId());
    }

    @Test
    @DisplayName("findByCategory returns an empty list when product is not found")
    void findByCategory_ReturnsAnEmptyList_WhenProductIsNotFound() {
        List<Product> productOptional = productRepository.findByCategory(category);

        Assertions.assertThat(productOptional)
                .isNotNull()
                .isEmpty();
    }


}