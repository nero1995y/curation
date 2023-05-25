package com.blackMarket.curation.domain.member.dto;

import com.blackMarket.curation.domain.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDto {
    private Long id;

    public MemberResponseDto(Member member) {
        this.id = member.getId();
    }
}
