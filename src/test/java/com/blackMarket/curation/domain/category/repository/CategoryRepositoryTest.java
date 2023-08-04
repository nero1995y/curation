package com.blackMarket.curation.domain.category.repository;

import com.blackMarket.curation.domain.category.domain.Category;
import com.blackMarket.curation.domain.category.exception.CategoryNotFountException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;

    @DisplayName("카테고리를 생성한다")
    @Test
    void create() {
        //given
        Category category = Category.builder()
                .name("Develop")
                .build();

        //when
        Category saveCategory = categoryRepository.save(category);

        //then
        Category findCategory = categoryRepository
                .findById(saveCategory.getId())
                .orElseThrow(CategoryNotFountException::new);

        assertThat(saveCategory).isEqualTo(findCategory);
    }

    @DisplayName("카테고리를 검색한다")
    @Test
    void sample() {
        //given
        Category category = Category.builder()
                .name("Develop")
                .build();

        categoryRepository.save(category);

        //when
        Category findCategory = categoryRepository
                .findByName(category.getName())
                .orElseThrow(CategoryNotFountException::new);

        //then
        assertThat(findCategory).isEqualTo(category);
    }

    @DisplayName("카테고리 조회 결과가 2이다")
    @Test
    void getList() {
        //given
        Category category = Category.builder()
                .name("Develop")
                .build();

        Category category2 = Category.builder()
                .name("Develop2")
                .build();

        categoryRepository.save(category);
        categoryRepository.save(category2);

        //when
        List<Category> result = categoryRepository.findAll();

        //then
        assertThat(result.size()).isEqualTo(2);
    }

    @DisplayName("카테고리를 삭제한다")
    @Test
    void remove() {
        //given
        Category category = Category.builder()
                .name("Develop")
                .build();

        Category save = categoryRepository.save(category);

        //when
        categoryRepository.deleteById(save.getId());

        //then
        Optional<Category> result = categoryRepository.findById(category.getId());

        assertThat(result).isEmpty();
    }
}