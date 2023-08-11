package com.blackMarket.curation.domain.category.domain;

import com.blackMarket.curation.domain.post.domain.Post;
import com.blackMarket.curation.domain.post.service.PostService;
import com.blackMarket.curation.global.error.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Post> postList = new ArrayList<>();

    @Builder
    public Category(Long id, String name, List<Post> postList) {
        this.id = id;
        this.name = name;
        if (postList != null) {
            changePost(postList);
        }
    }

    public void update(Category category) {
        if (!category.getName().isEmpty()) {
            this.name = category.getName();
        }
    }

    public void changePost(List<Post> postList) {
        this.postList = postList;
    }
}
