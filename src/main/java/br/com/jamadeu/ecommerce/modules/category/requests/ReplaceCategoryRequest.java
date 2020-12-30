package br.com.jamadeu.ecommerce.modules.category.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplaceCategoryRequest {

    @NotNull(message = "The category id can not be empty")
    @Positive(message = "The category id cannot be zero or negative")
    @Schema(description = "This is the category's id", required = true)
    private Long id;

    @NotEmpty(message = "The category name can not be empty")
    @Schema(description = "This is the category's name")
    private String name;
}
