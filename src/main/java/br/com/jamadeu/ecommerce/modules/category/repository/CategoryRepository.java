package br.com.jamadeu.ecommerce.modules.category.repository;

import br.com.jamadeu.ecommerce.modules.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String category);
}
