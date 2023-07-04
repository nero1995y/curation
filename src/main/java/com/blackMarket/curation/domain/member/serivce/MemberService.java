package com.blackMarket.curation.domain.member.serivce;

import com.blackMarket.curation.domain.member.domain.Member;
import com.blackMarket.curation.domain.member.dto.MemberResponseDto;
import com.blackMarket.curation.domain.member.exception.MemberDuplicatedException;
import com.blackMarket.curation.domain.member.exception.MemberNotfoundException;
import com.blackMarket.curation.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public MemberResponseDto create(Member member) {

        memberRepository
                .findByNickname(member.getNickname())
                .ifPresent(user -> {
                    throw new MemberDuplicatedException();
                });

        Member save = memberRepository
                .save(member);

        return new MemberResponseDto(save);
    }

    public List<MemberResponseDto> getList() {

        List<Member> list= memberRepository.findAll();

        return list.stream()
                .map(member -> MemberResponseDto.builder()
                        .id(member.getId())
                        .username(member.getUsername())
                        .nickname(member.getNickname())
                        .role(member.getRole())
                        .build()
                )
                .collect(Collectors.toList());
    }

    public MemberResponseDto getDetail(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotfoundException::new);

        return MemberResponseDto.builder()
                .id(member.getId())
                .username(member.getUsername())
                .nickname(member.getNickname())
                .role(member.getRole())
                .build();
    }

    @Transactional
    public void remove(Long memberId) {
        memberRepository.findById(memberId)
                .orElseThrow(MemberNotfoundException::new);

        memberRepository.deleteById(memberId);
    }

    @Transactional
    public void update(Long memberId, Member updateMember) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotfoundException::new);

        member.update(updateMember);
    }
}
