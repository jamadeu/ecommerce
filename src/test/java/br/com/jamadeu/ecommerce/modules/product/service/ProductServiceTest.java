package br.com.jamadeu.ecommerce.modules.product.service;

import br.com.jamadeu.ecommerce.modules.category.domain.Category;
import br.com.jamadeu.ecommerce.modules.category.repository.CategoryRepository;
import br.com.jamadeu.ecommerce.modules.category.util.CategoryCreator;
import br.com.jamadeu.ecommerce.modules.product.domain.Product;
import br.com.jamadeu.ecommerce.modules.product.repository.ProductRepository;
import br.com.jamadeu.ecommerce.modules.product.requests.NewProductRequest;
import br.com.jamadeu.ecommerce.modules.product.requests.ReplaceProductRequest;
import br.com.jamadeu.ecommerce.modules.product.util.NewProductRequestCreator;
import br.com.jamadeu.ecommerce.modules.product.util.ProductCreator;
import br.com.jamadeu.ecommerce.modules.product.util.ReplaceProductRequestCreator;
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

import java.math.BigDecimal;
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
        BDDMockito.when(categoryRepositoryMock.findByCategoryName(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(CategoryCreator.createValidCategory()));
        BDDMockito.when(productRepositoryMock.findByCategory(ArgumentMatchers.any(Category.class)))
                .thenReturn(List.of(ProductCreator.createValidProduct()));
        BDDMockito.when(productRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(ProductCreator.createValidProduct()));
        BDDMockito.when(categoryRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(CategoryCreator.createValidCategory()));
        BDDMockito.when(productRepositoryMock.findByProductName(ArgumentMatchers.anyString()))
                .thenReturn(Optional.empty());
        BDDMockito.when(productRepositoryMock.save(ArgumentMatchers.any(Product.class)))
                .thenReturn(ProductCreator.createValidProduct());
    }

    @Test
    @DisplayName("listAll returns list of products inside page object when successful")
    void listAll_ReturnsListOfProductsInsidePageObject_WhenSuccessful() {
        String expectedName = ProductCreator.createValidProduct().getProductName();
        Page<Product> productPage = productService.listAll(PageRequest.of(1, 1));

        Assertions.assertThat(productPage).isNotNull();
        Assertions.assertThat(productPage.toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(productPage.toList().get(0).getProductName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("listAllByCategory returns list of products inside page object when successful")
    void listAllByCategory_ReturnsListOfProductsInsidePageObject_WhenSuccessful() {
        String expectedName = ProductCreator.createValidProduct().getProductName();
        String category = ProductCreator.createValidProduct().getCategory().getCategoryName();
        Page<Product> productPage = productService.listAllByCategory(PageRequest.of(1, 1), category);

        Assertions.assertThat(productPage).isNotNull();
        Assertions.assertThat(productPage.toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(productPage.toList().get(0).getProductName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("listAllByCategory throws BadRequestException when category is not found")
    void listAllByCategory_ThrowsBadRequestException_WhenCategoryIsNotFound() {
        BDDMockito.when(categoryRepositoryMock.findByCategoryName(ArgumentMatchers.anyString()))
                .thenReturn(Optional.empty());
        String categoryNotExists = "category not exists";
        PageRequest pageRequest = PageRequest.of(0, 5);

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> productService.listAllByCategory(pageRequest, categoryNotExists));
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException returns product when successful")
    void findByIdOrThrowBadRequestException_ReturnsProduct_WhenSuccessful() {
        Long expectedId = ProductCreator.createValidProduct().getId();
        Product foundedProduct = productService.findByIdOrThrowBadRequestException(1L);

        Assertions.assertThat(foundedProduct).isNotNull();
        Assertions.assertThat(foundedProduct.getId())
                .isNotNull()
                .isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException throws BadRequestException when product is not found")
    void findByIdOrThrowBadRequestException_ThrowsBadRequestException_WhenProductIsNotFound() {
        BDDMockito.when(productRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> productService.findByIdOrThrowBadRequestException(1L));
    }

    @Test
    @DisplayName("create returns product when successful")
    void create_ReturnsProduct_WhenSuccessful() {
        NewProductRequest request = NewProductRequestCreator.createNewProductRequest();
        Product createdProduct = productService.create(request);

        Assertions.assertThat(createdProduct).isNotNull();
        Assertions.assertThat(createdProduct.getId()).isNotNull();
        Assertions.assertThat(createdProduct.getValue())
                .isEqualTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("create throws BadRequestException when category is not found")
    void create_ThrowsBadRequestException_WhenCategoryIsNotFound() {
        BDDMockito.when(categoryRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());
        NewProductRequest request = NewProductRequestCreator.createNewProductRequest();

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> productService.create(request));
    }

    @Test
    @DisplayName("create throws BadRequestException when product already exists")
    void create_ThrowsBadRequestException_WhenProductAlreadyExists() {
        BDDMockito.when(productRepositoryMock.findByProductName(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(ProductCreator.createValidProduct()));
        NewProductRequest request = NewProductRequestCreator.createNewProductRequest();

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> productService.create(request));
    }

    @Test
    @DisplayName("replace returns product when successful")
    void replace_ReturnsProduct_WhenSuccessful() {
        ReplaceProductRequest request = ReplaceProductRequestCreator.createReplaceProductRequest();

        Assertions.assertThatCode(() -> productService.replace(request))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("replace throws BadRequestException when category is not found")
    void replace_ThrowsBadRequestException_WhenCategoryIsNotFound() {
        BDDMockito.when(categoryRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());
        ReplaceProductRequest request = ReplaceProductRequestCreator.createReplaceProductRequest();
        Category categoryNotExists = Category.builder()
                .id(2L)
                .categoryName("categoryNotExists")
                .build();
        request.setCategory(categoryNotExists);

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> productService.replace(request));
    }

    @Test
    @DisplayName("replace throws BadRequestException when product already exists")
    void replace_ThrowsBadRequestException_WhenProductAlreadyExists() {
        BDDMockito.when(productRepositoryMock.findByProductName(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(ProductCreator.createValidProduct()));
        ReplaceProductRequest request = ReplaceProductRequestCreator.createReplaceProductRequest();
        request.setProductName("anotherProduct");

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> productService.replace(request));
    }

    @Test
    @DisplayName("replace throws BadRequestException when product value is negative")
    void replace_ThrowsBadRequestException_WhenProductValueIsNegative() {
        ReplaceProductRequest request = ReplaceProductRequestCreator.createReplaceProductRequest();
        request.setValue(request.getValue().negate());

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> productService.replace(request));
    }

}