package br.com.jamadeu.ecommerce.modules.category.integration;

import br.com.jamadeu.ecommerce.modules.category.domain.Category;
import br.com.jamadeu.ecommerce.modules.category.repository.CategoryRepository;
import br.com.jamadeu.ecommerce.modules.category.requests.NewCategoryRequest;
import br.com.jamadeu.ecommerce.modules.category.util.CategoryCreator;
import br.com.jamadeu.ecommerce.modules.category.util.NewCategoryRequestCreator;
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
class CategoryControllerIT {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("listAll returns list of category page object when successful")
    void list_ReturnsListOfCategoryInsidePageObject_WhenSuccessful() {
        Category savedCategory = categoryRepository.save(CategoryCreator.createCategoryToBeSaved());
        String expectedName = savedCategory.getCategoryName();
        PageableResponse<Category> response = testRestTemplate.exchange("/categories", HttpMethod.GET, null,
                new ParameterizedTypeReference<PageableResponse<Category>>() {
                }).getBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(response.toList().get(0).getCategoryName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findById returns category when successful")
    void findById_ReturnsCategory_WhenSuccessful() {
        Category savedCategory = categoryRepository.save(CategoryCreator.createCategoryToBeSaved());
        Long expectedId = savedCategory.getId();
        Category category = testRestTemplate.getForObject("/categories/{id}", Category.class, expectedId);

        Assertions.assertThat(category).isNotNull();
        Assertions.assertThat(category.getId())
                .isNotNull()
                .isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findById returns BadRequest when category is not found")
    void findById_ReturnsBadRequest_WhenCategoryIsNotFound() {
        ResponseEntity<Category> response = testRestTemplate.getForEntity(
                "/categories/{id}", Category.class, 1L);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("create returns category when successful")
    void create_ReturnsCategory_WhenSuccessful() {
        NewCategoryRequest request = NewCategoryRequestCreator.createNewCategoryRequest();
        ResponseEntity<Category> response = testRestTemplate.postForEntity("/categories", request, Category.class);
        Category createdCategory = response.getBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(createdCategory).isNotNull();
        Assertions.assertThat(createdCategory.getId()).isNotNull();
        Assertions.assertThat(createdCategory.getCategoryName()).isEqualTo(request.getCategoryName());
    }

    @Test
    @DisplayName("create returns BadRequest when categoryName is null")
    void create_ReturnsBadRequest_WhenCategoryName() {
        NewCategoryRequest request = NewCategoryRequestCreator.createNewCategoryRequest();
        request.setCategoryName(null);
        ResponseEntity<Category> response = testRestTemplate.postForEntity("/categories", request, Category.class);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("create returns BadRequest when category already exist")
    void create_ReturnsBadRequest_WhenCategoryAlreadyExist() {
        categoryRepository.save(CategoryCreator.createCategoryToBeSaved());
        NewCategoryRequest request = NewCategoryRequestCreator.createNewCategoryRequest();
        ResponseEntity<Category> response = testRestTemplate.postForEntity("/categories", request, Category.class);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
