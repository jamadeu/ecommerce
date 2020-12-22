package br.com.jamadeu.ecommerce.modules.product.controller;

import br.com.jamadeu.ecommerce.modules.product.domain.Product;
import br.com.jamadeu.ecommerce.modules.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    @Operation(summary = "List all products paginated",
            description = "The default size is 5, use the parameter to change the default value",
            tags = {"products"}
    )
    public ResponseEntity<Page<Product>> listAll(@ParameterObject Pageable pageable) {
        return new ResponseEntity<>(productService.listAll(pageable), HttpStatus.OK);
    }


}
