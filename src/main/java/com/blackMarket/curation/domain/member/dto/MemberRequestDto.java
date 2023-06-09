package com.blackMarket.curation.domain.member.dto;

import com.blackMarket.curation.domain.member.domain.Member;
import com.blackMarket.curation.domain.member.domain.Role;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberRequestDto {

    @NotNull
    private String username;

    @NotNull
    private String nickname;

    @NotNull
    private String password;

    @Builder
    public MemberRequestDto(String username,
                            String nickname,
                            String password) {

        this.username = username;
        this.nickname = nickname;
        this.password = password;
    }

    public Member toEntity() {
        return Member.builder()
                .username(this.username)
                .nickname(this.nickname)
                .password(this.password)
                .role(Role.MEMBER)
                .build();
    }
}
