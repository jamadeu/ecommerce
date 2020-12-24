package br.com.jamadeu.ecommerce.modules.product.requests;

import br.com.jamadeu.ecommerce.modules.category.domain.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewProductRequest {
    @NotEmpty(message = "The product name can not be empty")
    @Schema(description = "This is the product's name")
    private String name;

    @NotEmpty(message = "The product description can not be empty")
    @Size(max = 400)
    @Schema(description = "This is the product's description")
    private String description;

    @NotNull
    @Schema(description = "This is the product's categories")
    private Category category;
}
