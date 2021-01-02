package br.com.jamadeu.ecommerce.modules.product.controller;

import br.com.jamadeu.ecommerce.modules.product.domain.Product;
import br.com.jamadeu.ecommerce.modules.product.requests.NewProductRequest;
import br.com.jamadeu.ecommerce.modules.product.requests.ReplaceProductRequest;
import br.com.jamadeu.ecommerce.modules.product.service.ProductService;
import br.com.jamadeu.ecommerce.shared.exception.BadRequestException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    @GetMapping(path = "/list-by-category/{category}")
    @Operation(summary = "List all products by category paginated",
            description = "The default size is 5, use the parameter to change the default value",
            tags = {"products"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "When category is null")
    })
    public ResponseEntity<Page<Product>> listAllByCategory(@ParameterObject Pageable pageable, @PathVariable String category) {
        if (category == null) throw new BadRequestException("Category can not be null");
        return new ResponseEntity<>(productService.listAllByCategory(pageable, category), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "find product by id",
            tags = {"products"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "When product is not found")
    })
    public ResponseEntity<Product> findById(@PathVariable Long id) {
        return new ResponseEntity<>(productService.findByIdOrThrowBadRequestException(id), HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Create a new product",
            description = "Name, description and category fields are mandatory, " +
                    "name must be unique",
            tags = {"products"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "When there is an error with some mandatory field")
    })
    public ResponseEntity<Product> create(@RequestBody @Valid NewProductRequest newProductRequest) {
        return new ResponseEntity<>(productService.create(newProductRequest), HttpStatus.CREATED);
    }

    @PutMapping
    @Operation(summary = "Replace a product",
            description = "Name, description, value and category fields are mandatory, " +
                    "name must be unique",
            tags = {"products"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "When there is an error with some mandatory field")
    })
    public ResponseEntity<Product> replace(@RequestBody @Valid ReplaceProductRequest replaceProductRequest) {
        productService.replace(replaceProductRequest);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
