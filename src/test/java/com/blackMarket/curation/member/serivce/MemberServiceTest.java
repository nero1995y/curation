package com.blackMarket.curation.member.serivce;

import com.blackMarket.curation.domain.member.domain.Member;
import com.blackMarket.curation.domain.member.domain.Role;
import com.blackMarket.curation.domain.member.dto.MemberResponseDto;
import com.blackMarket.curation.domain.member.exception.MemberDuplicatedException;
import com.blackMarket.curation.domain.member.exception.MemberNotfoundException;
import com.blackMarket.curation.domain.member.repository.MemberRepository;
import com.blackMarket.curation.domain.member.serivce.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
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

    private final Long memberId = -1L;
    private final String nickname = "nero12";

    private final Member member = Member.builder()
            .username("nero")
            .nickname("nero12")
            .password("12345")
            .role(Role.MEMBER)
            .build();

    @DisplayName("멤버 저장이 실패한다. 이미 존재함")
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

    @DisplayName("멤버를 등록한다")
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

    @DisplayName("멤버 목록 조회한다.")
    @Test
    void memberLit() {
        //given
        List<Member> list = Arrays.asList(
                Member.builder().build(),
                Member.builder().build(),
                Member.builder().build());

        doReturn(list)
                .when(memberRepository)
                .findAll();
        //when
        List<MemberResponseDto> result = memberService.getList();

        //then
        assertThat(result.size()).isEqualTo(3);
    }

    @DisplayName("맴버 상세 조회 실패 예외 발생")
    @Test
    void memberDetailNull() {
        //given
        doReturn(Optional.empty())
                .when(memberRepository)
                .findById(memberId);

        //when then
        assertThatThrownBy(() -> memberService.getDetail(memberId))
                .isInstanceOf(MemberNotfoundException.class);
    }

    @DisplayName("멤버 상세 조회 성공")
    @Test
    void memberDetailSuccess() {
        //given
        doReturn(Optional.of(member))
                .when(memberRepository)
                .findById(memberId);

        //when
        MemberResponseDto result = memberService.getDetail(memberId);

        //then
        assertThat(result.getUsername()).isEqualTo(member.getUsername());
    }

    @DisplayName("멤버삭제 실패 존재하지 않는다")
    @Test
    void deleteFail() {
        //given
        doReturn(Optional.empty())
                .when(memberRepository)
                .findById(memberId);

        //when //then
        assertThatThrownBy(()-> memberService.remove(memberId))
                .isInstanceOf(MemberNotfoundException.class);
    }

    @DisplayName("멤버 삭제")
    @Test
    void remove() {
        //given
        doReturn(Optional.of(member))
                .when(memberRepository)
                .findById(memberId);

        //when
        memberService.remove(memberId);

        //then
        verify(memberRepository, times(1)).findById(memberId);
        verify(memberRepository, times(1)).deleteById(memberId);
    }



    @DisplayName("맴버 업데이트 실패 유저없음")
    @Test
    void updateFail() {
        //given
        Member updateMember = Member.builder()
                .id(1L)
                .username("updateUsername")
                .nickname("updateNickname")
                .password("12345")
                .role(Role.MEMBER)
                .build();


        doReturn(Optional.empty())
                .when(memberRepository)
                .findById(memberId);

        //when then
        assertThatThrownBy(()-> memberService.update(memberId, updateMember))
                .isInstanceOf(MemberNotfoundException.class);
    }

    @DisplayName("맴버 업데이트 성공")
    @Test
    void updateSuccess() {
        //given
        Member updateMember = Member.builder()
                .id(1L)
                .username("updateUsername")
                .nickname("updateNickname")
                .password("12345")
                .role(Role.MEMBER)
                .build();

        doReturn(Optional.of(member))
                .when(memberRepository)
                .findById(memberId);

        //when
        memberService.update(memberId, updateMember);

        //then
        MemberResponseDto detail = memberService.getDetail(memberId);

        assertThat(detail.getNickname()).isEqualTo(updateMember.getNickname());

        verify(memberRepository, times(2)).findById(memberId);
    }
}