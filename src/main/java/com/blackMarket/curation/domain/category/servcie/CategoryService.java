package com.blackMarket.curation.domain.category.servcie;

import com.blackMarket.curation.domain.category.domain.Category;
import com.blackMarket.curation.domain.category.dto.CategoryResponseDto;
import com.blackMarket.curation.domain.category.exception.CategoryDuplicatedException;
import com.blackMarket.curation.domain.category.exception.CategoryNotFountException;
import com.blackMarket.curation.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryResponseDto create(Category category) {

        categoryRepository.findByName(category.getName())
                .ifPresent(categoryEntity -> {
                    throw new CategoryDuplicatedException();
                });
        Category save = categoryRepository.save(category);

        return new CategoryResponseDto(save);
    }

    public List<CategoryResponseDto> getList() {

        List<Category> list = categoryRepository.findAll();

        return list.stream()
                .map(category -> CategoryResponseDto.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .build()
                )
                .collect(Collectors.toList());
    }

    public CategoryResponseDto getDetail(Long categoryId) {
        Category category = getCategory(categoryId);

        return CategoryResponseDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public Category getCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(CategoryNotFountException::new);
    }

    private void hasName(String categoryName) {
        categoryRepository.findByName(categoryName)
                .ifPresent(categoryEntity -> {
                    throw new CategoryDuplicatedException();
                });
    }


    @Transactional
    public void remove(Long categoryId) {

        getCategory(categoryId);

        categoryRepository.deleteById(categoryId);
    }

    @Transactional
    public void update(Long categoryId, Category updateCategory) {

        Category category = getCategory(categoryId);
        hasName(updateCategory.getName());

        category.update(updateCategory);
    }
}
