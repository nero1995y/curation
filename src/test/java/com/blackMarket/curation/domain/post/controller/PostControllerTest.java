package com.blackMarket.curation.domain.post.controller;

import com.blackMarket.curation.domain.post.dto.PostResponseDto;
import com.blackMarket.curation.domain.post.dto.PostSaveRequestDto;
import com.blackMarket.curation.domain.post.dto.PostUpdateRequestDto;
import com.blackMarket.curation.domain.post.exception.PostDuplicatedException;
import com.blackMarket.curation.domain.post.exception.PostNotfoundException;
import com.blackMarket.curation.domain.post.service.PostService;
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

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
@MockBean(JpaMetamodelMappingContext.class)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @MockBean
    PostService postService;

    private PostSaveRequestDto getPostRequestDto() {
        return PostSaveRequestDto.builder()
                .title("테스트 제목")
                .content("테스트 내용")
                .memberId(-1L)
                .build();
    }


    @DisplayName("게시글 등록 실패 응답이 NULL")
    @Test
    void createFail() throws Exception {
        //given
        String url = "/api/post";

        //when
        ResultActions actions = mockMvc.perform(post(url)
                .content(gson.toJson(null))
                .contentType(APPLICATION_JSON));

        //then
        actions.andExpect(status().isBadRequest());

    }

    @DisplayName("게시글 등록실패 PostService 예외 발생")
    @Test
    void createFailPostServiceException() throws Exception {
        //given
        String url = "/api/post";

        doThrow(new PostDuplicatedException())
                .when(postService)
                .create(getPostRequestDto().toEntity());

        //when
        ResultActions actions = mockMvc.perform(post(url)
                .content(gson.toJson(null))
                .contentType(APPLICATION_JSON));

        //then
        actions.andExpect(status().isBadRequest());
    }

    @DisplayName("게시글 등록 성공한다")
    @Test
    void createSuccess() throws Exception {
        //given
        String url = "/api/post";
        PostResponseDto response = PostResponseDto.builder()
                .id(-1L)
                .build();

        doReturn(response)
                .when(postService)
                .create(any());

        //when
        ResultActions actions = mockMvc.perform(post(url)
                .content(gson.toJson(getPostRequestDto()))
                .contentType(APPLICATION_JSON));

        //then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").value(-1))
                .andDo(print());
    }

    @DisplayName("게시글 목록 조회 성공")
    @Test
    void getList() throws Exception {
        //given
        String url = "/api/posts";

        List<PostResponseDto> list = Arrays.asList(
                PostResponseDto.builder().build(),
                PostResponseDto.builder().build(),
                PostResponseDto.builder().build()
        );

        doReturn(list)
                .when(postService)
                .getList();

        //when
        ResultActions actions = mockMvc.perform(get(url));

        //then
        actions.andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("게시글 조회 실패 id 값 없음")
    @Test
    void getDetailFail() throws Exception {
        //given
        String url = "/api/post/{id}";

        doThrow(new PostNotfoundException())
                .when(postService)
                .getDetail(-1L);

        //when
        ResultActions actions = mockMvc.perform(get(url, -1L));

        //then
        actions.andExpect(status().isNotFound())
                .andDo(print());
    }

    @DisplayName("게시글 상세 조회 성공")
    @Test
    void getDetailSuccess() throws Exception {
        //given
        String url = "/api/post/{id}";
        PostResponseDto response = PostResponseDto.builder()
                .id(-1L)
                .build();

        doReturn(response)
                .when(postService)
                .getDetail(-1L);

        //when
        ResultActions actions = mockMvc.perform(get(url, -1L));

        //then
        actions.andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("게시글 삭제 성공")
    @Test
    void removeSuccess() throws Exception {
        //given
        String url = "/api/post/{id}";

        //when
        ResultActions actions = mockMvc.perform(delete(url, -1L));

        //then
        actions.andExpect(status().isNoContent())
                .andDo(print());
    }

    @DisplayName("게시글 업데이트 실패 id 없음")
    @Test
    void updateFail() throws Exception {
        //given
        String url = "/api/post/{id}";

        doThrow(new PostNotfoundException())
                .when(postService)
                .update(any(), any());

        //when
        ResultActions actions = mockMvc.perform(patch(url, -1L)
                .content(gson.toJson(null))
                .contentType(APPLICATION_JSON));

        //then
        actions.andExpect(status().isNotFound())
                .andDo(print());
    }

    @DisplayName("게시글 업데이트 성공")
    @Test
    void updateSuccess() throws Exception {
        //given
        String url = "/api/post/{id}";

        PostUpdateRequestDto request = PostUpdateRequestDto.builder()
                .title("updateTitle")
                .build();

        //when
        ResultActions actions = mockMvc.perform(patch(url, -1L)
                .content(gson.toJson(request))
                .contentType(APPLICATION_JSON));

        //then
        actions.andExpect(status().isNoContent())
                .andDo(print());

        verify(postService, times(1)).update(any(), any());
    }
}