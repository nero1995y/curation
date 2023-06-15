package com.blackMarket.curation.member.integration;

import com.blackMarket.curation.domain.member.domain.Member;
import com.blackMarket.curation.domain.member.dto.MemberRequestDto;
import com.blackMarket.curation.domain.member.exception.MemberNotfoundException;
import com.blackMarket.curation.domain.member.repository.MemberRepository;
import com.blackMarket.curation.domain.member.serivce.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MemberServiceIntegrationTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    @DisplayName("멤버를 생성한다.")
    @Test
    @Rollback(value = false)
    void createDB() {
        //given
        MemberRequestDto requestDto = MemberRequestDto.builder()
                .username("userNameTest1")
                .nickname("nero12")
                .password("12345")
                .build();

        //when
        memberService.create(requestDto.toEntity());

        //then
        Member member = memberRepository.findByNickname(requestDto.getNickname())
                .orElseThrow(MemberNotfoundException::new);

        assertThat(member.getNickname()).isEqualTo(requestDto.getNickname());
    }
}
