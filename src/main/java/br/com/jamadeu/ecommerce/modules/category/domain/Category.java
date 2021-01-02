package br.com.jamadeu.ecommerce.modules.category.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = false)
public class Category extends AbstractCategory {

    @NotEmpty(message = "The category name can not be empty")
    @Column(nullable = false, unique = true)
    @Schema(description = "This is the category's name")
    private String categoryName;

    @Builder
    public Category(Long id, @NotEmpty(message = "The category name can not be empty") String categoryName) {
        super(id);
        this.categoryName = categoryName;
    }
}
