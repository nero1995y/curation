package com.blackMarket.curation.domain.member.domain;

import com.blackMarket.curation.global.error.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public Member(Long id,
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

    public void update(Member member) {
        updateNickname(member.getNickname());
        updateName(member.getUsername());
        updatePassword(member.getPassword());
    }

    private void updateNickname(String nickname) {
        if (!nickname.isEmpty()) {
            this.nickname = nickname;
        }
    }

    private void updateName(String username) {
        if (!username.isEmpty()) {
            this.username = username;
        }
    }

    private void updatePassword(String password) {
        if (!password.isEmpty()) {
            this.password = password;
        }
    }

}
