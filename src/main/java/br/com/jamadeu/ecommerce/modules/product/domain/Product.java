package br.com.jamadeu.ecommerce.modules.product.domain;

import br.com.jamadeu.ecommerce.modules.category.domain.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
    private String productName;

    @NotEmpty(message = "The product description can not be empty")
    @Size(max = 400)
    @Schema(description = "This is the product's description")
    private String description;

    @Column(nullable = false)
    @Schema(description = "This is the product's value", defaultValue = "0")
    private BigDecimal value;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    @Schema(description = "This is the product's categories")
    private Category category;

    @Builder
    public Product(Long id,
                   @NotEmpty(message = "The product name can not be empty") String productName,
                   @NotEmpty(message = "The product description can not be empty")
                   @Size(max = 400) String description,
                   @NotNull BigDecimal value,
                   @NotNull Category category) {
        super(id);
        this.productName = productName;
        this.description = description;
        this.value = value;
        this.category = category;
    }
}
