package br.com.jamadeu.ecommerce.modules.category.service;

import br.com.jamadeu.ecommerce.modules.category.domain.Category;
import br.com.jamadeu.ecommerce.modules.category.mapper.CategoryMapper;
import br.com.jamadeu.ecommerce.modules.category.repository.CategoryRepository;
import br.com.jamadeu.ecommerce.modules.category.requests.NewCategoryRequest;
import br.com.jamadeu.ecommerce.modules.category.requests.ReplaceCategoryRequest;
import br.com.jamadeu.ecommerce.shared.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Page<Category> listAll(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    public Category findByIdOrThrowBadRequestException(long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Category not found"));
    }


    public Category save(NewCategoryRequest newCategoryRequest) {
        Category categoryToBeSaved = CategoryMapper.INSTANCE.toCategory(newCategoryRequest);
        checkIfCategoryExists(categoryToBeSaved.getCategoryName());
        return categoryRepository.save(categoryToBeSaved);
    }

    public void replace(ReplaceCategoryRequest replaceCategoryRequest) {
        Category categoryToReplace = CategoryMapper.INSTANCE.toCategory(replaceCategoryRequest);
        Category foundedCategory = findByIdOrThrowBadRequestException(replaceCategoryRequest.getId());
        if (!foundedCategory.getCategoryName().equals(categoryToReplace.getCategoryName())) {
            checkIfCategoryExists(categoryToReplace.getCategoryName());
        }
        categoryRepository.save(categoryToReplace);
    }

    public void delete(Long id) {
        Category category = findByIdOrThrowBadRequestException(id);
        categoryRepository.delete(category);
    }

    private void checkIfCategoryExists(String category) {
        if (categoryRepository.findByCategoryName(category).isPresent()) {
            throw new BadRequestException("Category already exists");
        }
    }
}
