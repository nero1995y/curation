package com.blackMarket.curation.domain.post.domain;

import com.blackMarket.curation.domain.member.domain.Member;
import com.blackMarket.curation.global.error.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Post(Long id,
                String title,
                String content,
                Member member) {

        this.id = id;
        this.title = title;
        this.content = content;
        this.member = member;

        if(member !=null) {
            changeMember(member);
        }
    }

    public void changeMember(Member member) {
        this.member = member;
    }
}
