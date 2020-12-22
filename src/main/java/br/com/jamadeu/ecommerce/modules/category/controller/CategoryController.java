package br.com.jamadeu.ecommerce.modules.category.controller;

import br.com.jamadeu.ecommerce.modules.category.domain.Category;
import br.com.jamadeu.ecommerce.modules.category.service.CategoryService;
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
@RequestMapping("categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "List all categories paginated",
            description = "The default size is 5, use the parameter to change the default value",
            tags = {"categories"}
    )
    public ResponseEntity<Page<Category>> listAll(@ParameterObject Pageable pageable) {
        return new ResponseEntity<>(categoryService.listAll(pageable), HttpStatus.OK);
    }
}
