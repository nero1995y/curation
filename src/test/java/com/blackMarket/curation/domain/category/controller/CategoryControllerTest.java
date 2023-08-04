package com.blackMarket.curation.domain.category.controller;

import com.blackMarket.curation.domain.category.dto.CategoryResponseDto;
import com.blackMarket.curation.domain.category.dto.CategorySaveRequestDto;
import com.blackMarket.curation.domain.category.dto.CategoryUpdateRequestDto;
import com.blackMarket.curation.domain.category.exception.CategoryDuplicatedException;
import com.blackMarket.curation.domain.category.exception.CategoryNotFountException;
import com.blackMarket.curation.domain.category.servcie.CategoryService;
import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
@MockBean(JpaMetamodelMappingContext.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @MockBean
    CategoryService categoryService;

    private CategorySaveRequestDto getCategoryRequestDto() {
        return CategorySaveRequestDto.builder()
                .name("develop")
                .build();
    }


    @DisplayName("카테고리 등록 실패 Request Null")
    @Test
    void createFail() throws Exception {
        //given
        String url = "/api/category";

        //when
        ResultActions actions = mockMvc.perform(post(url)
                .content(gson.toJson(null))
                .contentType(APPLICATION_JSON));

        //then
        actions.andExpect(status().isBadRequest())
                .andDo(print());
    }

    @DisplayName("카테고리 등록 실패 service 예외 발생한다")
    @Test
    void createFailCategoryServiceException() throws Exception {
        //given
        String url = "/api/category";

        doThrow(new CategoryDuplicatedException())
                .when(categoryService)
                .create(getCategoryRequestDto().toEntity());

        //when
        ResultActions actions = mockMvc.perform(post(url)
                .content(gson.toJson(null))
                .contentType(APPLICATION_JSON));

        //then
        actions.andExpect(status().isBadRequest())
                .andDo(print());
    }

    @DisplayName("카테고리 목록 조회 성공")
    @Test
    void getListSuccess() throws Exception {
        //given
        String url = "/api/categories";

        List<CategoryResponseDto> list = Arrays.asList(
                CategoryResponseDto.builder().build(),
                CategoryResponseDto.builder().build(),
                CategoryResponseDto.builder().build()

        );

        doReturn(list)
                .when(categoryService)
                .getList();
        //when
        ResultActions actions = mockMvc.perform(get(url));

        //then
        actions.andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("카테고리 상세조회 id 값 없음")
    @Test
    void getCategoryFail() throws Exception {
        //given
        String url = "/api/category/{id}";

        doThrow(new CategoryNotFountException())
                .when(categoryService)
                .getDetail(-1L);

        //when
        ResultActions actions = mockMvc.perform(get(url, -1L));

        //then
        actions.andExpect(status().isNotFound())
                .andDo(print());
    }

    @DisplayName("카테고리 상세 조회 성공")
    @Test
    void getDetailSuccess() throws Exception {
        //given
        String url = "/api/category/{id}";

        CategoryResponseDto response = CategoryResponseDto.builder()
                .id(-1L)
                .build();

        doReturn(response)
                .when(categoryService)
                .getDetail(-1L);

        //when
        ResultActions actions = mockMvc.perform(get(url, -1L));

        //then
        actions.andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("카테고리 삭제 성공")
    @Test
    void removeSuccess() throws Exception {
        //given
        String url = "/api/category/{id}";

        //when
        ResultActions actions = mockMvc.perform(delete(url, -1));

        //then
        actions.andExpect(status().isNoContent())
                .andDo(print());
    }


    @DisplayName("카테고리 업데이트 실패 id 없음")
    @Test
    void updateFail() throws Exception {
        //given
        String url = "/api/category/{id}";

        doThrow(new CategoryNotFountException())
                .when(categoryService)
                .update(any(),any());

        //when
        ResultActions actions = mockMvc.perform(patch(url, -1)
                .content(gson.toJson(null))
                .contentType(APPLICATION_JSON));

        //then
        actions.andExpect(status().isNotFound())
                .andDo(print());
    }

    @DisplayName("카테고리 업데이트 성공")
    @Test
    void updateSuccess() throws Exception {
        //given
        String url = "/api/category/{id}";

        CategoryUpdateRequestDto request = CategoryUpdateRequestDto.builder()
                .name("develop")
                .build();

        doNothing()
                .when(categoryService)
                .update(-1L, request.toEntity());
        //when
        ResultActions actions = mockMvc.perform(patch(url, -1)
                .content(gson.toJson(request))
                .contentType(APPLICATION_JSON));

        //then
        actions.andExpect(status().isNoContent())
                .andDo(print());

        verify(categoryService, times(1)).update(any(),any());
    }
}