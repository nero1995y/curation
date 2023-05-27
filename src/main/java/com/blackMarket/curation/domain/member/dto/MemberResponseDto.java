package com.blackMarket.curation.domain.member.dto;

import com.blackMarket.curation.domain.member.domain.Member;
import com.blackMarket.curation.domain.member.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberResponseDto {
    private Long id;
    private String username;
    private String nickname;
    private String password;
    private Role role;

    @Builder
    public MemberResponseDto(Long id,
                             String username,
                             String nickname,
                             String password,
                             Role role) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.role = role;
    }

    public MemberResponseDto(Member member) {
        this.id = member.getId();
    }
}
