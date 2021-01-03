package br.com.jamadeu.ecommerce.modules.product.integration;

import br.com.jamadeu.ecommerce.modules.category.domain.Category;
import br.com.jamadeu.ecommerce.modules.category.repository.CategoryRepository;
import br.com.jamadeu.ecommerce.modules.category.util.CategoryCreator;
import br.com.jamadeu.ecommerce.modules.product.domain.Product;
import br.com.jamadeu.ecommerce.modules.product.repository.ProductRepository;
import br.com.jamadeu.ecommerce.modules.product.util.ProductCreator;
import br.com.jamadeu.ecommerce.shared.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ProductControllerIT {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("listAll returns list of product inside page object when successful")
    void listAll_ReturnsListOfProductInsidePageObject_WhenSuccessful() {
        Category savedCategory = categoryRepository.save(CategoryCreator.createCategoryToBeSaved());
        Product savedProduct = productRepository.save(ProductCreator.createProductToBeSaved(savedCategory));
        String expectedName = savedProduct.getProductName();
        PageableResponse<Product> response = testRestTemplate.exchange("/products", HttpMethod.GET, null,
                new ParameterizedTypeReference<PageableResponse<Product>>() {
                }).getBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(response.toList().get(0).getProductName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("listAllByCategory returns list of product inside page object when successful")
    void listAllByCategory_ReturnsListOfProductInsidePageObject_WhenSuccessful() {
        Category firstCategory = categoryRepository.save(Category.builder()
                .categoryName("firstCategory")
                .build());
        Category secondCategory = categoryRepository.save(Category.builder()
                .categoryName("secondCategory")
                .build());
        Product firstProduct = productRepository.save(ProductCreator.createProductToBeSaved(firstCategory));
        productRepository.save(ProductCreator.createProductToBeSaved(secondCategory));
        PageableResponse<Product> response = testRestTemplate.exchange("/products/list-by-category/{category}",
                HttpMethod.GET, null, new ParameterizedTypeReference<PageableResponse<Product>>() {
                },
                firstCategory.getCategoryName()).getBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(response.toList().get(0).getProductName()).isEqualTo(firstProduct.getProductName());
    }

    @Test
    @DisplayName("findById returns Product when successful")
    void findById_ReturnsProduct_WhenSuccessful() {
        Category savedCategory = categoryRepository.save(CategoryCreator.createCategoryToBeSaved());
        Product savedProduct = productRepository.save(ProductCreator.createProductToBeSaved(savedCategory));
        Long expectedId = savedProduct.getId();
        Product product = testRestTemplate.getForObject("/products/{id}", Product.class, expectedId);

        Assertions.assertThat(product).isNotNull();
        Assertions.assertThat(product.getId())
                .isNotNull()
                .isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findById returns BadRequest when product is not found")
    void findById_ReturnsBadRequest_WhenProductIsNotFound() {
        ResponseEntity<Product> response = testRestTemplate.getForEntity(
                "/products/{id}", Product.class, 1L);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
