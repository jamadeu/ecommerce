package br.com.jamadeu.ecommerce.modules.category.service;

import br.com.jamadeu.ecommerce.modules.category.domain.Category;
import br.com.jamadeu.ecommerce.modules.category.repository.CategoryRepository;
import br.com.jamadeu.ecommerce.modules.category.util.CategoryCreator;
import br.com.jamadeu.ecommerce.shared.exception.BadRequestException;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class CategoryServiceTest {
    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepositoryMock;

    private Category category;

    @BeforeEach
    void setup() {
        category = CategoryCreator.createValidCategory();
        PageImpl<Category> categoryPage = new PageImpl<>(List.of(category));
        BDDMockito.when(categoryRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(categoryPage);
        BDDMockito.when(categoryRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(category));
    }

    @Test
    @DisplayName("listAll returns list of categories inside page object when successful")
    void listAll_ReturnsListOfUsersInsidePageObject_WhenSuccessful() {
        String expectedCategory = category.getCategory();
        Page<Category> categoryPage = categoryService.listAll(PageRequest.of(1, 1));

        Assertions.assertThat(categoryPage).isNotNull();
        Assertions.assertThat(categoryPage.toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(categoryPage.toList().get(0).getCategory()).isEqualTo(expectedCategory);
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException returns category when successful")
    void findByIdOrThrowBadRequestException_ReturnsCategory_WhenSuccessful() {
        Long expectedId = category.getId();
        Category foundedCategory = categoryService.findByIdOrThrowBadRequestException(1);

        Assertions.assertThat(foundedCategory).isNotNull();
        Assertions.assertThat(foundedCategory.getId())
                .isNotNull()
                .isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException throws BadRequestException when category is not found")
    void findByIdOrThrowBadRequestException_ThrowsBadRequestException_WhenCategoryIsNotFound() {
        BDDMockito.when(categoryRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());
        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> categoryService.findByIdOrThrowBadRequestException(1));
    }

}