package br.com.jamadeu.ecommerce.modules.product.domain;

import br.com.jamadeu.ecommerce.modules.category.domain.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

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
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "categories", nullable = false)
    @Schema(description = "This is the product's categories")
    private List<Category> categories;
}
