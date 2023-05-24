package com.blackMarket.curation.member.serivce;

import com.blackMarket.curation.member.domain.Member;
import com.blackMarket.curation.member.dto.MemberResponseDto;
import com.blackMarket.curation.member.exception.MemberDuplicatedException;
import com.blackMarket.curation.member.repository.MemberRepository;
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
