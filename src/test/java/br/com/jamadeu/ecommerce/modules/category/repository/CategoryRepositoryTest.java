package br.com.jamadeu.ecommerce.modules.category.repository;

import br.com.jamadeu.ecommerce.modules.category.domain.Category;
import br.com.jamadeu.ecommerce.modules.category.util.CategoryCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
@DisplayName("CategoryRepository tests")
class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("save persists category when successful")
    void save_PersistCategory_WhenSuccessful() {
        Category categoryToBeSaved = CategoryCreator.createCategoryToBeSaved();
        Category categorySaved = categoryRepository.save(categoryToBeSaved);

        Assertions.assertThat(categorySaved).isNotNull();
        Assertions.assertThat(categorySaved.getId()).isNotNull();
        Assertions.assertThat(categorySaved.getCategoryName())
                .isNotNull()
                .isEqualTo(categoryToBeSaved.getCategoryName());
    }

    @Test
    @DisplayName("save updates category when successful")
    void save_UpdatesCategory_WhenSuccessful() {
        Category categoryToBeSaved = CategoryCreator.createCategoryToBeSaved();
        Category categorySaved = categoryRepository.save(categoryToBeSaved);
        String newCategory = "new Category";
        categorySaved.setCategoryName(newCategory);
        Category updatedCategory = categoryRepository.save(categorySaved);

        Assertions.assertThat(updatedCategory).isNotNull();
        Assertions.assertThat(updatedCategory.getId())
                .isNotNull()
                .isEqualTo(categorySaved.getId());
        Assertions.assertThat(updatedCategory.getCategoryName())
                .isNotNull()
                .isEqualTo(newCategory);
    }

    @Test
    @DisplayName("delete deletes category when successful")
    void delete_DeleteCategory_WhenSuccessful() {
        Category category = categoryRepository.save(CategoryCreator.createCategoryToBeSaved());
        categoryRepository.delete(category);
        Optional<Category> categoryOptional = categoryRepository.findById(category.getId());

        Assertions.assertThat(categoryOptional).isEmpty();
    }

    @Test
    @DisplayName("findByCategory returns optional of user when successful")
    void findByCategory_ReturnsOptionalOfCategory_WhenSuccessful() {
        Category category = categoryRepository.save(CategoryCreator.createCategoryToBeSaved());
        Optional<Category> categoryOptional = categoryRepository.findByCategoryName(category.getCategoryName());

        Assertions.assertThat(categoryOptional)
                .isNotNull()
                .isNotEmpty();
        Assertions.assertThat(categoryOptional.get().getId())
                .isNotNull()
                .isEqualTo(category.getId());
    }

    @Test
    @DisplayName("findByCategory returns an empty optional when category is not found")
    void findByCategory_ReturnsAnEmptyOptional_WhenCategoryIsNotFound() {
        Optional<Category> categoryOptional = categoryRepository.findByCategoryName("not found category");

        Assertions.assertThat(categoryOptional)
                .isNotNull()
                .isEmpty();
    }


}