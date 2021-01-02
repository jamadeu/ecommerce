package br.com.jamadeu.ecommerce.modules.product.requests;

import br.com.jamadeu.ecommerce.modules.category.domain.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplaceProductRequest {

    @NotNull(message = "The product id can not be empty")
    @Positive(message = "The product id cannot be zero or negative")
    @Schema(description = "This is the product's id", required = true)
    private Long id;

    @NotEmpty(message = "The product name can not be empty")
    @Schema(description = "This is the product's name")
    private String productName;

    @NotEmpty(message = "The product description can not be empty")
    @Size(max = 400)
    @Schema(description = "This is the product's description")
    private String description;

    @Schema(description = "This is the product's value", defaultValue = "0")
    private BigDecimal value;

    @NotNull
    @Schema(description = "This is the product's categories")
    private Category category;
}
