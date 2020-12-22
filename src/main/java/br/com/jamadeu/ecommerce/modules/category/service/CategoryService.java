package br.com.jamadeu.ecommerce.modules.category.service;

import br.com.jamadeu.ecommerce.modules.category.domain.Category;
import br.com.jamadeu.ecommerce.modules.category.mapper.CategoryMapper;
import br.com.jamadeu.ecommerce.modules.category.repository.CategoryRepository;
import br.com.jamadeu.ecommerce.modules.category.requests.NewCategoryRequest;
import br.com.jamadeu.ecommerce.shared.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Page<Category> listAll(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    public Category findByIdOrThrowBadRequestException(long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Category not found"));
    }

    @Transactional
    public Category save(NewCategoryRequest newCategoryRequest) {
        Category categoryToBeSaved = CategoryMapper.INSTANCE.toCategory(newCategoryRequest);
        checkIfCategoryExists(categoryToBeSaved.getCategory());
        return categoryRepository.save(categoryToBeSaved);
    }

    private void checkIfCategoryExists(String category) {
        if (categoryRepository.findByCategory(category).isPresent()) {
            throw new BadRequestException("Category already exists");
        }
    }
}
