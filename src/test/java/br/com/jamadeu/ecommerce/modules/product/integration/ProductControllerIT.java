package br.com.jamadeu.ecommerce.modules.product.integration;

import br.com.jamadeu.ecommerce.modules.category.domain.Category;
import br.com.jamadeu.ecommerce.modules.category.repository.CategoryRepository;
import br.com.jamadeu.ecommerce.modules.category.util.CategoryCreator;
import br.com.jamadeu.ecommerce.modules.product.domain.Product;
import br.com.jamadeu.ecommerce.modules.product.repository.ProductRepository;
import br.com.jamadeu.ecommerce.modules.product.requests.NewProductRequest;
import br.com.jamadeu.ecommerce.modules.product.util.NewProductRequestCreator;
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

import java.math.BigDecimal;

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

    @Test
    @DisplayName("create returns product when successful")
    void create_ReturnsCategory_WhenSuccessful() {
        Category savedCategory = categoryRepository.save(CategoryCreator.createCategoryToBeSaved());
        NewProductRequest request = NewProductRequestCreator.createNewProductRequest(savedCategory);
        ResponseEntity<Product> response = testRestTemplate.postForEntity("/products", request, Product.class);
        Product createdProduct = response.getBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(createdProduct).isNotNull();
        Assertions.assertThat(createdProduct.getId()).isNotNull();
        Assertions.assertThat(createdProduct.getProductName()).isEqualTo(request.getProductName());
        Assertions.assertThat(createdProduct.getValue()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("create returns BadRequest when product already exist")
    void create_ReturnsBadRequest_WhenProductAlreadyExist() {
        Category savedCategory = categoryRepository.save(CategoryCreator.createCategoryToBeSaved());
        productRepository.save(ProductCreator.createProductToBeSaved(savedCategory));
        NewProductRequest request = NewProductRequestCreator.createNewProductRequest(savedCategory);
        ResponseEntity<Category> response = testRestTemplate.postForEntity("/categories", request, Category.class);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("create returns BadRequest when productName is null")
    void create_ReturnsBadRequest_WhenProductName() {
        Category savedCategory = categoryRepository.save(CategoryCreator.createCategoryToBeSaved());
        NewProductRequest request = NewProductRequestCreator.createNewProductRequest(savedCategory);
        request.setProductName(null);
        ResponseEntity<Category> response = testRestTemplate.postForEntity("/categories", request, Category.class);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("create returns BadRequest when category is not exist")
    void create_ReturnsBadRequest_WhenCategoryIsNotExist() {
        NewProductRequest request = NewProductRequestCreator.createNewProductRequest();
        ResponseEntity<Category> response = testRestTemplate.postForEntity("/categories", request, Category.class);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("create returns BadRequest when category is null")
    void create_ReturnsBadRequest_WhenCategoryIsNull() {
        NewProductRequest request = NewProductRequestCreator.createNewProductRequest();
        request.setCategory(null);
        ResponseEntity<Category> response = testRestTemplate.postForEntity("/categories", request, Category.class);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("create returns BadRequest when description has more than 400 characters")
    void create_ReturnsBadRequest_WhenDescriptionHasMoreThan400Characters() {
        NewProductRequest request = NewProductRequestCreator.createNewProductRequest();
        request.setDescription("Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula " +
                "eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur " +
                "ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat " +
                "massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, " +
                "rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer " +
                "tincidunt. Cras dapibus. Vivamus elementum semper nisi. Aenean vulputate eleifend tellus. Aenean leo " +
                "ligula, porttitor eu, consequat vitae, eleifend ac, enim. Aliquam lorem ante, dapibus in, viverra quis, " +
                "feugiat a, tellus. Phasellus viverra nulla ut metus varius laoreet. Quisque rutrum. Aenean imperdiet. " +
                "Etiam ultricies nisi vel augue. Curabitur ullamcorper ultricies nisi. Nam eget dui. Etiam rhoncus. " +
                "Maecenas tempus, tellus eget condimentum rhoncus, sem quam semper libero, sit amet adipiscing sem neque " +
                "sed ipsum. Nam quam nunc, blandit vel, luctus pulvinar, hendrerit id, lorem. Maecenas nec odio et ante " +
                "tincidunt tempus. Donec vitae sapien ut libero venenatis faucibus. Nullam quis ante. Etiam sit amet " +
                "orci eget eros faucibus tincidunt. Duis leo. Sed fringilla mauris sit amet nibh. Donec sodales sagittis " +
                "magna. Sed consequat, leo eget bibendum sodales, augue velit cursus nunc, quis gravida magna mi a libero. " +
                "Fusce vulputate eleifend sapien. Vestibulum purus quam, scelerisque ut, mollis sed, nonummy id, metus. " +
                "Nullam accumsan lorem in dui. Cras ultricies mi eu turpis hendrerit fringilla. Vestibulum ante ipsum " +
                "primis in faucibus orci luctus et ultrices posuere cubilia Curae; In ac dui quis mi consectetuer " +
                "lacinia. Nam pretium turpis et arcu. Duis arcu tortor, suscipit eget, imperdiet nec, imperdiet iaculis," +
                " ipsum. Sed aliquam ultrices mauris. Integer ante arcu, accumsan a, consectetuer eget, posuere ut, " +
                "mauris. Praesent adipiscing. Phasellus ullamcorper ipsum rutrum nunc. Nunc nonummy metus. Vestibulum " +
                "volutpat pretium libero. Cras id dui. Aenean ut eros et nisl sagittis vestibulum. Nullam nulla eros, " +
                "ultricies sit amet, nonummy id, imperdiet feugiat, pede. Sed lectus. Donec mollis hendrerit risus. " +
                "Phasellus nec sem in justo pellentesque facilisis. Etiam imperdiet imperdiet orci. Nunc nec neque. " +
                "Phasellus leo dolor, tempus non, auctor et, hendrerit quis, nisi. Curabitur ligula sapien, tincidunt " +
                "non, euismod vitae, posuere imperdiet, leo. Maecenas malesuada. Praesent congue erat at massa. " +
                "Sed cursus turpis vitae tortor. Donec posuere vulputate arcu. Phasellus accumsan cursus velit. " +
                "Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Sed aliquam," +
                " nisi quis porttitor congue, elit erat euismod orci, ac placerat");
        ResponseEntity<Category> response = testRestTemplate.postForEntity("/categories", request, Category.class);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("create returns BadRequest when descripton is null")
    void create_ReturnsBadRequest_WhenDescriptionIsNull() {
        NewProductRequest request = NewProductRequestCreator.createNewProductRequest();
        request.setDescription(null);
        ResponseEntity<Category> response = testRestTemplate.postForEntity("/categories", request, Category.class);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
