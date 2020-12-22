package br.com.jamadeu.ecommerce.modules.category.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewCategoryRequest {

    @NotEmpty(message = "The category name can not be empty")
    @Schema(description = "This is the category's name")
    private String category;
}
