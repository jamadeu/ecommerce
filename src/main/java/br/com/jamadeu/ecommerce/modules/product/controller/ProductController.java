package br.com.jamadeu.ecommerce.modules.product.controller;

import br.com.jamadeu.ecommerce.modules.category.service.CategoryService;
import br.com.jamadeu.ecommerce.modules.product.domain.Product;
import br.com.jamadeu.ecommerce.modules.product.service.ProductService;
import br.com.jamadeu.ecommerce.shared.exception.BadRequestException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "List all products paginated",
            description = "The default size is 5, use the parameter to change the default value",
            tags = {"products"}
    )
    public ResponseEntity<Page<Product>> listAll(@ParameterObject Pageable pageable) {
        return new ResponseEntity<>(productService.listAll(pageable), HttpStatus.OK);
    }

    @GetMapping(path = "/list-by-category/{category}")
    @Operation(summary = "List all products by category paginated",
            description = "The default size is 5, use the parameter to change the default value",
            tags = {"products"}
    )
    public ResponseEntity<Page<Product>> listAllByCategory(@ParameterObject Pageable pageable, @PathVariable String category) {
        if (category == null) throw new BadRequestException("Category can not be null");
        return new ResponseEntity<>(productService.listAllByCategory(pageable, category), HttpStatus.OK);
    }
}
