package br.com.jamadeu.ecommerce.modules.product.service;

import br.com.jamadeu.ecommerce.modules.category.domain.Category;
import br.com.jamadeu.ecommerce.modules.category.repository.CategoryRepository;
import br.com.jamadeu.ecommerce.modules.product.domain.Product;
import br.com.jamadeu.ecommerce.modules.product.mapper.ProductMapper;
import br.com.jamadeu.ecommerce.modules.product.repository.ProductRepository;
import br.com.jamadeu.ecommerce.modules.product.requests.NewProductRequest;
import br.com.jamadeu.ecommerce.modules.product.requests.ReplaceProductRequest;
import br.com.jamadeu.ecommerce.shared.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public Page<Product> listAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Page<Product> listAllByCategory(Pageable pageable, String category) {
        Category categoryFounded = categoryRepository.findByCategoryName(category)
                .orElseThrow(() -> new BadRequestException("Category not found"));
        List<Product> productList = productRepository.findByCategory(categoryFounded);
        return new PageImpl<>(productList, pageable, productList.size());
    }

    public Product findByIdOrThrowBadRequestException(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Product not found"));
    }


    public Product create(NewProductRequest newProductRequest) {
        Product productToCreate = ProductMapper.INSTANCE.toProduct(newProductRequest);
        checkIfCategoryExists(productToCreate.getCategory().getCategoryName());
        checkIfProductExists(productToCreate.getProductName());
        productToCreate.setValue(BigDecimal.ZERO);
        return productRepository.save(productToCreate);
    }

    public void replace(ReplaceProductRequest request) {
        Product product = findByIdOrThrowBadRequestException(request.getId());
        if (!product.getProductName().equals(request.getProductName())) {
            checkIfProductExists(request.getProductName());
        }
        if (product.getCategory() != request.getCategory()) {
            checkIfCategoryExists(request.getCategory().toString());
        }
        if (request.getValue().compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Value can not be negative");
        }
        productRepository.save(ProductMapper.INSTANCE.toProduct(request));
    }

    private void checkIfCategoryExists(String category) {
        if (categoryRepository.findByCategoryName(category).isPresent()) {
            throw new BadRequestException("Category already exists");
        }
    }

    private void checkIfProductExists(String product) {
        if (productRepository.findByProductName(product).isPresent()) {
            throw new BadRequestException("Product already exists");
        }
    }
}
