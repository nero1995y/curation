package com.blackMarket.curation.domain.post.dto;

import com.blackMarket.curation.domain.post.domain.Post;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostSaveRequestDto {

    @NotNull
    private String title;

    @NotNull
    private String content;

    @NotNull
    private Long memberId;

    @Builder
    public PostSaveRequestDto(String title,
                              String content,
                              Long memberId) {
        this.title = title;
        this.content = content;
        this.memberId = memberId;
    }

    public Post toEntity() {
        return Post.builder()
                .title(this.title)
                .content(this.content)
                .build();
    }
}
