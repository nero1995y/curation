package com.blackMarket.curation.member.serivce;

import com.blackMarket.curation.domain.member.domain.Member;
import com.blackMarket.curation.domain.member.domain.Role;
import com.blackMarket.curation.domain.member.dto.MemberResponseDto;
import com.blackMarket.curation.domain.member.exception.MemberDuplicatedException;
import com.blackMarket.curation.domain.member.repository.MemberRepository;
import com.blackMarket.curation.domain.member.serivce.MemberService;
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
class MemberServiceTest {

    @Mock
    MemberRepository memberRepository;

    @InjectMocks
    MemberService memberService;

    private final String memberId = "memberId";
    private final String nickname = "nero12";

    private final Member member = Member.builder()
            .username("nero")
            .nickname("nero12")
            .password("12345")
            .role(Role.MEMBER)
            .build();

    @DisplayName("Memeber를 저장이 실패한다. 이미 존재함")
    @Test
    void createFail() {
        //given
        doReturn(Optional.of(member))
                .when(memberRepository)
                .findByNickname(nickname);


        //when then
        assertThatThrownBy(() -> memberService.create(member))
                .isInstanceOf(MemberDuplicatedException.class);
    }

    @DisplayName("Member를 등록한다")
    @Test
    void create() {

        Member saveMember = Member.builder()
                .id(1L)
                .username("nero")
                .nickname("nero12")
                .password("12345")
                .role(Role.MEMBER)
                .build();

        //given
        doReturn(Optional.empty())
                .when(memberRepository)
                .findByNickname(nickname);

        doReturn(saveMember)
                .when(memberRepository)
                .save(any());

        //when
        MemberResponseDto result = memberService.create(member);

        // then
        assertThat(result).isNotNull();
        verify(memberRepository, times(1)).findByNickname(nickname);
        verify(memberRepository, times(1)).save(any());
    }
}