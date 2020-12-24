package br.com.jamadeu.ecommerce.modules.product.service;

import br.com.jamadeu.ecommerce.modules.category.domain.Category;
import br.com.jamadeu.ecommerce.modules.category.repository.CategoryRepository;
import br.com.jamadeu.ecommerce.modules.category.util.CategoryCreator;
import br.com.jamadeu.ecommerce.modules.product.domain.Product;
import br.com.jamadeu.ecommerce.modules.product.repository.ProductRepository;
import br.com.jamadeu.ecommerce.modules.product.util.ProductCreator;
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
class ProductServiceTest {
    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepositoryMock;

    @Mock
    private CategoryRepository categoryRepositoryMock;

    @BeforeEach
    void setup() {
        PageImpl<Product> productPage = new PageImpl<>(List.of(ProductCreator.createValidProduct()));
        BDDMockito.when(productRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(productPage);
        BDDMockito.when(categoryRepositoryMock.findByCategory(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(CategoryCreator.createValidCategory()));
        BDDMockito.when(productRepositoryMock.findByCategory(ArgumentMatchers.any(Category.class)))
                .thenReturn(List.of(ProductCreator.createValidProduct()));
    }

    @Test
    @DisplayName("listAll returns list of products inside page object when successful")
    void listAll_ReturnsListOfProductsInsidePageObject_WhenSuccessful() {
        String expectedName = ProductCreator.createValidProduct().getName();
        Page<Product> productPage = productService.listAll(PageRequest.of(1, 1));

        Assertions.assertThat(productPage).isNotNull();
        Assertions.assertThat(productPage.toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(productPage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("listAllByCategory returns list of products inside page object when successful")
    void listAllByCategory_ReturnsListOfProductsInsidePageObject_WhenSuccessful() {
        String expectedName = ProductCreator.createValidProduct().getName();
        String category = ProductCreator.createValidProduct().getCategory().getCategory();
        Page<Product> productPage = productService.listAllByCategory(PageRequest.of(1, 1), category);

        Assertions.assertThat(productPage).isNotNull();
        Assertions.assertThat(productPage.toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(productPage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("listAllByCategory throws BadRequestException when category is not found")
    void listAllByCategory_ThrowsBadRequestException_WhenCategoryIsNotFound() {
        BDDMockito.when(categoryRepositoryMock.findByCategory(ArgumentMatchers.anyString()))
                .thenReturn(Optional.empty());
        String categoryNotExists = "category not exists";
        PageRequest pageRequest = PageRequest.of(0, 5);

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> productService.listAllByCategory(pageRequest, categoryNotExists));
    }

}