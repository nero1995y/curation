package com.blackMarket.curation.member.controller;

import com.blackMarket.curation.domain.member.controller.MemberController;
import com.blackMarket.curation.domain.member.domain.Role;
import com.blackMarket.curation.domain.member.dto.MemberRequestDto;
import com.blackMarket.curation.domain.member.dto.MemberResponseDto;
import com.blackMarket.curation.domain.member.dto.MemberUpdateRequestDto;
import com.blackMarket.curation.domain.member.exception.MemberDuplicatedException;
import com.blackMarket.curation.domain.member.exception.MemberNotfoundException;
import com.blackMarket.curation.domain.member.serivce.MemberService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
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
import static org.mockito.BDDMockito.doThrow;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
@MockBean(JpaMetamodelMappingContext.class)
@Slf4j
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @MockBean
    MemberService memberService;

    @DisplayName("멤버 등록 실패 MemberRequest null")
    @Test
    void createFail() throws Exception {
        //given
        String url = "/api/member";

        //when
        ResultActions actions = mockMvc.perform(post(url)
                .content(gson.toJson(null))
                .contentType(APPLICATION_JSON));

        //then
        actions.andExpect(status().isBadRequest())
                .andDo(print());
    }

    @DisplayName("멤버 등록실패 MemberService 예외 발생한다")
    @Test
    void createFailMemberServiceException() throws Exception {
        //given
        String url = "/api/member";

        doThrow(new MemberDuplicatedException())
                .when(memberService)
                .create(getMemberRequestDto().toEntity());

        //when
        ResultActions actions = mockMvc.perform(post(url)
                .content(gson.toJson(null))
                .contentType(APPLICATION_JSON));

        //then
        actions.andExpect(status().isBadRequest());
    }

    @DisplayName("멤버 등록 성공한다")
    @Test
    void createSuccess() throws Exception {
        //given
        String url = "/api/member";
        MemberResponseDto response = MemberResponseDto.builder()
                .id(-1L)
                .build();

        doReturn(response)
                .when(memberService)
                .create(any());

        //when
        ResultActions actions = mockMvc.perform(post(url)
                //.header()
                .content(gson.toJson(getMemberRequestDto()))
                .contentType(APPLICATION_JSON));

        //then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").value(-1))
                .andDo(print());
    }

    @DisplayName("멤버 목록 조회 성공")
    @Test
    void getListSuccess() throws Exception {
        //given
        String url = "/api/members";

        List<MemberResponseDto> list = Arrays.asList(
                MemberResponseDto.builder().build(),
                MemberResponseDto.builder().build(),
                MemberResponseDto.builder().build()
        );

        doReturn(list)
                .when(memberService)
                .getList();

        //when
        ResultActions actions = mockMvc.perform(get(url));

        //then
        actions.andExpect(status().isOk());

    }

    @DisplayName("멤버 조회 실패 id 값 없음")
    @Test
    void getDetailFail() throws Exception {
        //given
        String url = "/api/member/{id}";

        doThrow(new MemberNotfoundException())
                .when(memberService)
                .getDetail(-1L);

        //when
        ResultActions actions = mockMvc.perform(get(url,-1L ));

        //then
        actions.andExpect(status().isNotFound())
                .andDo(print());
    }

    @DisplayName("멤버 상세 조회 성공")
    @Test
    void getDetailSuccess() throws Exception {
        //given
        String url = "/api/member/{id}";
        MemberResponseDto response = MemberResponseDto.builder()
                .id(-1L).build();

        doReturn(response)
                .when(memberService)
                .getDetail(-1L);

        //when
        ResultActions actions = mockMvc.perform(get(url, -1L));

        //then
        actions.andExpect(status().isOk())
                .andDo(print());

    }

    @DisplayName("멤버삭제 성공")
    @Test
    void removeSuccess() throws Exception {
        //given
        String url = "/api/member/{id}";

        //when
        ResultActions actions = mockMvc.perform(delete(url, -1));

        //then
        actions.andExpect(status().isNoContent())
                .andDo(print());
    }

    @DisplayName("멤버 업데이트 실패 업데이트 id 없음 ")
    @Test
    void updateFail() throws Exception {
        //given
        String url = "/api/member/{id}";

        doThrow(new MemberNotfoundException())
                .when(memberService)
                .update(any(),any());

        //when
        ResultActions actions = mockMvc.perform(patch(url, -1)
                .content(gson.toJson(null))
                .contentType(APPLICATION_JSON));

        //then
        actions.andExpect(status().isNotFound())
                .andDo(print());
    }

    @DisplayName("멥버 업데이트 성공")
    @Test
    void updateSuccess() throws Exception {
        //given
        String url = "/api/member/{id}";

        MemberUpdateRequestDto request = MemberUpdateRequestDto.builder()
                .username("updateUsername")
                .build();

        doNothing()
                .when(memberService)
                .update(-1L,request.toEntity());


        //when
        ResultActions actions = mockMvc.perform(patch(url, -1)
                .content(gson.toJson(request))
                .contentType(APPLICATION_JSON));

        //then
        actions.andExpect(status().isNoContent())
                .andDo(print());

        verify(memberService,times(1)).update(any(),any());
    }


    private MemberRequestDto getMemberRequestDto() {
        return MemberRequestDto.builder()
                .username("testUsername")
                .nickname("testNickname")
                .password("testPassword")
                .role(Role.MEMBER)
                .build();
    }
}