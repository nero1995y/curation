package com.blackMarket.curation.domain.category.servcie;

import com.blackMarket.curation.domain.category.domain.Category;
import com.blackMarket.curation.domain.category.dto.CategoryResponseDto;
import com.blackMarket.curation.domain.category.exception.CategoryDuplicatedException;
import com.blackMarket.curation.domain.category.exception.CategoryNotFountException;
import com.blackMarket.curation.domain.category.repository.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    CategoryRepository categoryRepository;

    @InjectMocks
    CategoryService categoryService;

    @DisplayName("카테고리 저장이 실패한다. 이미 존재함")
    @Test
    void createFail() {
        //given
        Category category = Category.builder()
                .id(-1L)
                .name("Develop")
                .build();

        doReturn(Optional.of(category))
                .when(categoryRepository)
                .findByName(category.getName());

        //when then
        assertThatThrownBy(() -> categoryService.create(category))
                .isInstanceOf(CategoryDuplicatedException.class);
    }

    @DisplayName("카테를 등록한다")
    @Test
    void sample() {
        //given
        Category category = Category.builder()
                .id(-1L)
                .name("Develop")
                .build();

        //when
        doReturn(Optional.empty())
                .when(categoryRepository)
                .findByName(category.getName());

        doReturn(category)
                .when(categoryRepository)
                .save(any());

        //then
        CategoryResponseDto result = categoryService.create(category);

        assertThat(result).isNotNull();

        verify(categoryRepository,
                times(1))
                .findByName(category.getName());

        verify(categoryRepository,
                times(1))
                .save(any());
    }

    @DisplayName("카테고리를 상세 조회실패 존재하지 않는다")
    @Test
    void categoryDetailNull() {
        //given
        Category category = Category.builder()
                .id(-1L)
                .name("Develop")
                .build();

        doReturn(Optional.empty())
                .when(categoryRepository)
                .findById(category.getId());

        //when then
        assertThatThrownBy(() -> categoryService.getDetail(category.getId()))
                .isInstanceOf(CategoryNotFountException.class);
    }

    @DisplayName("카테고리를 조회한다")
    @Test
    void categoryDetailSuccess() {
        //given
        Category category = Category.builder()
                .id(-1L)
                .name("Develop")
                .build();

        doReturn(Optional.of(category))
                .when(categoryRepository)
                .findById(category.getId());

        //when
        CategoryResponseDto result = categoryService.getDetail(category.getId());

        //then
        assertThat(result.getName()).isEqualTo(category.getName());
    }

    @DisplayName("카테고리 삭제 실패 존재하지 않는다.")
    @Test
    void removeFail() {
        //given
        Category category = Category.builder()
                .id(-1L)
                .name("Develop")
                .build();

        doReturn(Optional.empty())
                .when(categoryRepository)
                .findById(category.getId());

        //when then
        assertThatThrownBy(() -> categoryService.remove(category.getId()))
                .isInstanceOf(CategoryNotFountException.class);
    }

    @DisplayName("카테고리 삭제한다.")
    @Test
    void removeSuccess() {
        //given
        Category category = Category.builder()
                .id(-1L)
                .name("Develop")
                .build();


        doReturn(Optional.of(category))
                .when(categoryRepository)
                .findById(category.getId());

        //when
        categoryService.remove(category.getId());

        //then
        verify(categoryRepository, times(1)).findById(category.getId());
        verify(categoryRepository, times(1)).deleteById(category.getId());
    }

    @DisplayName("카테고리 업데이트 실패 이름없음")
    @Test
    void updateFail() {
        //given
        Category category = Category.builder()
                .id(-1L)
                .name("Develop")
                .build();

        Category updateDevelop = Category.builder()
                .name("UpdateDevelop")
                .build();

        doReturn(Optional.empty())
                .when(categoryRepository)
                .findById(category.getId());

        //when then
        assertThatThrownBy(
                () -> categoryService.update(
                        category.getId(), updateDevelop))
                .isInstanceOf(CategoryNotFountException.class);
    }

    @DisplayName("카테고리 업데이트 실패 중복됨")
    @Test
    void updateFailDuplication() {
        //given
        Category category = Category.builder()
                .id(-1L)
                .name("Develop")
                .build();

        Category updateCategory = Category.builder()
                .name("UpdateDevelop")
                .build();

        doReturn(Optional.of(category))
                .when(categoryRepository)
                .findById(category.getId());

        doThrow(CategoryDuplicatedException.class)
                .when(categoryRepository)
                .findByName(updateCategory.getName());

        //when then
        assertThatThrownBy(
                () -> categoryService.update(
                        category.getId(), updateCategory))
                .isInstanceOf(CategoryDuplicatedException.class);
    }

    @DisplayName("카테고리 업데이트 성공")
    @Test
    void updateSuccess() {
        //given
        Category category = Category.builder()
                .id(-1L)
                .name("Develop")
                .build();

        Category updateCategory = Category.builder()
                .name("UpdateDevelop")
                .build();

        doReturn(Optional.of(category))
                .when(categoryRepository)
                .findById(category.getId());

        //when
        categoryService.update(category.getId(), updateCategory);

        //then
        CategoryResponseDto detail = categoryService.getDetail(category.getId());

        assertThat(detail.getName()).isEqualTo(updateCategory.getName());
        verify(categoryRepository, times(2)).findById(category.getId());
    }
}