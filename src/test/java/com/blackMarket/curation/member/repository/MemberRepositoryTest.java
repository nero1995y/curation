package com.blackMarket.curation.member.repository;

import com.blackMarket.curation.member.domain.Member;
import com.blackMarket.curation.member.domain.Role;
import com.blackMarket.curation.member.exception.MemberNotfoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @DisplayName("Member 생성")
    @Test
    void create() {
        //given
        Member member = Member.builder()
                .username("nero")
                .nickname("nero12")
                .password("12345")
                .role(Role.MEMBER)
                .build();

        //when
        Member saveMember = memberRepository.save(member);

        //then
        Member findMember = memberRepository
                .findById(saveMember.getId())
                .orElseThrow(MemberNotfoundException::new);

        assertThat(saveMember).isEqualTo(findMember);
    }

    @DisplayName("Member username으로 검색한다.")
    @Test
    void findMemberByUsername() {
        //given
        Member member = Member.builder()
                .username("nero")
                .nickname("nero12")
                .password("12345")
                .role(Role.MEMBER)
                .build();
        memberRepository.save(member);

        //when
        Member findMember = memberRepository
                .findByUsername(member.getUsername())
                .orElseThrow(MemberNotfoundException::new);

        assertThat(findMember).isEqualTo(member);
    }

    @DisplayName("Member nickname으로 검색한다.")
    @Test
    void findMemberByNickname() {
        //given
        Member member = Member.builder()
                .username("nero")
                .nickname("nero12")
                .password("12345")
                .role(Role.MEMBER)
                .build();
        memberRepository.save(member);

        //when
        Member findMember = memberRepository
                .findByNickname(member.getNickname())
                .orElseThrow(MemberNotfoundException::new);

        //then
        assertThat(findMember).isEqualTo(member);
    }
}