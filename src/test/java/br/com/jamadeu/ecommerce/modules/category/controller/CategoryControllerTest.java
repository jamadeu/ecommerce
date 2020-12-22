package br.com.jamadeu.ecommerce.modules.category.controller;

import br.com.jamadeu.ecommerce.modules.category.domain.Category;
import br.com.jamadeu.ecommerce.modules.category.requests.NewCategoryRequest;
import br.com.jamadeu.ecommerce.modules.category.requests.ReplaceCategoryRequest;
import br.com.jamadeu.ecommerce.modules.category.service.CategoryService;
import br.com.jamadeu.ecommerce.modules.category.util.CategoryCreator;
import br.com.jamadeu.ecommerce.modules.category.util.NewCategoryRequestCreator;
import br.com.jamadeu.ecommerce.modules.category.util.ReplaceCategoryRequestCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
class CategoryControllerTest {
    @InjectMocks
    private CategoryController categoryController;

    @Mock
    private CategoryService categoryServiceMock;

    @BeforeEach
    void setup() {
        PageImpl<Category> categoryPage = new PageImpl<>(List.of(CategoryCreator.createValidCategory()));
        BDDMockito.when(categoryServiceMock.listAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(categoryPage);
        BDDMockito.when(categoryServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()))
                .thenReturn(CategoryCreator.createValidCategory());
        BDDMockito.when(categoryServiceMock.save(ArgumentMatchers.any(NewCategoryRequest.class)))
                .thenReturn(CategoryCreator.createValidCategory());
        BDDMockito.doNothing().when(categoryServiceMock).replace(ArgumentMatchers.any(ReplaceCategoryRequest.class));
        BDDMockito.doNothing().when(categoryServiceMock).delete(ArgumentMatchers.anyLong());
    }

    @Test
    @DisplayName("listAll returns list of categories inside page object when successful")
    void listAll_ReturnsListOfCategoriesInsidePageObject_WhenSuccessful() {
        String expectedCategory = CategoryCreator.createValidCategory().getCategory();
        Page<Category> categoryPage = categoryController.listAll(PageRequest.of(1, 1)).getBody();

        Assertions.assertThat(categoryPage).isNotNull();
        Assertions.assertThat(categoryPage.toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(categoryPage.toList().get(0).getCategory()).isEqualTo(expectedCategory);
    }

    @Test
    @DisplayName("findById returns category when successful")
    void findById_ReturnsCategory_WhenSuccessful() {
        Category category = CategoryCreator.createValidCategory();
        ResponseEntity<Category> response = categoryController.findById(1);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode())
                .isNotNull()
                .isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody())
                .isNotNull()
                .isEqualTo(category);
    }

    @Test
    @DisplayName("save returns category when successful")
    void save_ReturnsCategory_WhenSuccessful() {
        Category category = categoryController.create(NewCategoryRequestCreator.createNewCategoryRequest()).getBody();

        Assertions.assertThat(category)
                .isNotNull()
                .isEqualTo(CategoryCreator.createValidCategory());
    }

    @Test
    @DisplayName("replace updates category when successful")
    void replace_UpdatesCategory_WhenSuccessful() {
        ResponseEntity<Category> response = categoryController.replace(
                ReplaceCategoryRequestCreator.createReplaceCategoryRequest());

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete deletes category when successful")
    void delete_DeletesCategory_WhenSuccessful() {
        ResponseEntity<Void> response = categoryController.delete(1L);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}