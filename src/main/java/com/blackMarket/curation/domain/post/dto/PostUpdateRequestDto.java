package com.blackMarket.curation.domain.post.dto;

import com.blackMarket.curation.domain.post.domain.Post;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostUpdateRequestDto {

    @NotNull
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String content;

    @Builder
    public PostUpdateRequestDto(Long id,
                                String title,
                                String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public Post toEntity() {
        return Post.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .build();
    }
}
