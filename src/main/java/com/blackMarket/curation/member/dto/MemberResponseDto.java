package com.blackMarket.curation.member.dto;

import com.blackMarket.curation.member.domain.Member;
import lombok.Builder;

public class MemberResponseDto {
    private Long id;

    public MemberResponseDto(Member member) {
        this.id = member.getId();
    }
}
