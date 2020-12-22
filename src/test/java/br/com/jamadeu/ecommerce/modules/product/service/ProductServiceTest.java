package br.com.jamadeu.ecommerce.modules.product.service;

import br.com.jamadeu.ecommerce.modules.product.domain.Product;
import br.com.jamadeu.ecommerce.modules.product.repository.ProductRepository;
import br.com.jamadeu.ecommerce.modules.product.util.ProductCreator;
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

@ExtendWith(SpringExtension.class)
class ProductServiceTest {
    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setup() {
        PageImpl<Product> productPage = new PageImpl<>(List.of(ProductCreator.createValidProduct()));
        BDDMockito.when(productService.listAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(productPage);
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

}