package br.com.jamadeu.ecommerce.modules.product.repository;

import br.com.jamadeu.ecommerce.modules.category.domain.Category;
import br.com.jamadeu.ecommerce.modules.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByName(String name);

    List<Product> findByCategory(Category category);
}
