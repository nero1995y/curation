package com.blackMarket.curation.domain.member.serivce;

import com.blackMarket.curation.domain.member.domain.Member;
import com.blackMarket.curation.domain.member.dto.MemberResponseDto;
import com.blackMarket.curation.domain.member.exception.MemberDuplicatedException;
import com.blackMarket.curation.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
