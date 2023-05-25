package com.blackMarket.curation.member.controller;

import com.blackMarket.curation.domain.member.controller.MemberController;
import com.blackMarket.curation.domain.member.domain.Role;
import com.blackMarket.curation.domain.member.dto.MemberRequestDto;
import com.blackMarket.curation.domain.member.dto.MemberResponseDto;
import com.blackMarket.curation.domain.member.exception.MemberDuplicatedException;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.doThrow;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @DisplayName("맴버 등록 성공한다")
    @Test
    void createSuccess() throws Exception {
        //given
        String url = "/api/member";
        MemberResponseDto response = new MemberResponseDto(-1L);

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

    private MemberRequestDto getMemberRequestDto() {
        return MemberRequestDto.builder()
                .username("testUsername")
                .nickname("testNickname")
                .password("testPassword")
                .role(Role.MEMBER)
                .build();
    }

}