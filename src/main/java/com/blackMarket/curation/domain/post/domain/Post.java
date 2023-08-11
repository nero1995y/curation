package com.blackMarket.curation.domain.post.domain;

import com.blackMarket.curation.domain.category.domain.Category;
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

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    @Builder
    public Post(Long id,
                String title,
                String content,
                Member member,
                Category category) {

        this.id = id;
        this.title = title;
        this.content = content;
        this.member = member;

        if(member !=null) {
            changeMember(member);
        }

        if(category != null) {
            changeCategory(category);
        }
    }

    public void changeMember(Member member) {
        this.member = member;
    }

    public void changeCategory(Category category) {
        this.category = category;
    }

    public void update(Post post) {
        updateTitle(post.getTitle());
        updateContent(post.getContent());
    }

    private void updateTitle(String title) {
        if(!title.isEmpty()) {
            this.title = title;
        }
    }

    private void updateContent(String content) {
        if(!content.isEmpty()) {
            this.content = content;
        }
    }
}
