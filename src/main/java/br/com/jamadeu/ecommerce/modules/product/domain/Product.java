package br.com.jamadeu.ecommerce.modules.product.domain;

import br.com.jamadeu.ecommerce.modules.category.domain.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = false)
public class Product extends AbstractProduct {

    @NotEmpty(message = "The product name can not be empty")
    @Column(nullable = false)
    @Schema(description = "This is the product's name")
    private String name;

    @NotEmpty(message = "The product description can not be empty")
    @Size(max = 400)
    @Schema(description = "This is the product's description")
    private String description;

    @NotNull
    @Column(nullable = false)
    @Schema(description = "This is the product's name", defaultValue = "0")
    private BigDecimal value;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id", nullable = false)
    @Schema(description = "This is the product's categories")
    private Category category;

    @Builder
    public Product(Long id,
                   @NotEmpty(message = "The product name can not be empty") String name,
                   @NotEmpty(message = "The product description can not be empty")
                   @Size(max = 400) String description,
                   @NotNull BigDecimal value,
                   @NotNull Category category) {
        super(id);
        this.name = name;
        this.description = description;
        this.value = value;
        this.category = category;
    }
}
