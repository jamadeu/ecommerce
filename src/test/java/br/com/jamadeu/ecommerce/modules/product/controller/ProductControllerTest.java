package br.com.jamadeu.ecommerce.modules.product.controller;

import br.com.jamadeu.ecommerce.modules.product.domain.Product;
import br.com.jamadeu.ecommerce.modules.product.requests.NewProductRequest;
import br.com.jamadeu.ecommerce.modules.product.service.ProductService;
import br.com.jamadeu.ecommerce.modules.product.util.NewProductRequestCreator;
import br.com.jamadeu.ecommerce.modules.product.util.ProductCreator;
import br.com.jamadeu.ecommerce.shared.exception.BadRequestException;
import lombok.extern.log4j.Log4j2;
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

@Log4j2
@ExtendWith(SpringExtension.class)
class ProductControllerTest {
    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productServiceMock;

    @BeforeEach
    void setup() {
        PageImpl<Product> productPage = new PageImpl<>(List.of(ProductCreator.createValidProduct()));
        BDDMockito.when(productServiceMock.listAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(productPage);
        BDDMockito.when(productServiceMock.listAllByCategory(ArgumentMatchers.any(PageRequest.class), ArgumentMatchers.anyString()))
                .thenReturn(productPage);
        BDDMockito.when(productServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()))
                .thenReturn(ProductCreator.createValidProduct());
        BDDMockito.when(productServiceMock.create(ArgumentMatchers.any(NewProductRequest.class)))
                .thenReturn(ProductCreator.createValidProduct());
    }

    @Test
    @DisplayName("listAll returns list of products inside page object when successful")
    void listAll_ReturnsListOfProductsInsidePageObject_WhenSuccessful() {
        String expectedName = ProductCreator.createValidProduct().getProductName();
        ResponseEntity<Page<Product>> response = productController.listAll(PageRequest.of(1, 1));
        Page<Product> productPage = response.getBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
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
        ResponseEntity<Page<Product>> response = productController.listAllByCategory(PageRequest.of(1, 1), category);
        Page<Product> productPage = response.getBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(productPage).isNotNull();
        Assertions.assertThat(productPage.toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(productPage.toList().get(0).getProductName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("listAllByCategory throws BadRequestException when category is null")
    void listAllByCategory_ThrowsBadRequestException_WhenCategoryIsNull() {
        PageRequest pageRequest = PageRequest.of(1, 1);

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> productController.listAllByCategory(pageRequest, null));
    }

    @Test
    @DisplayName("findById returns product when successful")
    void findById_ReturnsProduct_WhenSuccessful() {
        Product product = ProductCreator.createValidProduct();
        ResponseEntity<Product> response = productController.findById(1L);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode())
                .isNotNull()
                .isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody())
                .isNotNull()
                .isEqualTo(product);
    }

    @Test
    @DisplayName("create returns product when successful")
    void create_ReturnsProduct_WhenSuccessful() {
        NewProductRequest newProductRequest = NewProductRequestCreator.createNewProductRequest();
        ResponseEntity<Product> response = productController.create(newProductRequest);
        Product createdProduct = response.getBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode())
                .isNotNull()
                .isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(createdProduct)
                .isNotNull()
                .isEqualTo(ProductCreator.createValidProduct());
    }


}