package com.blackMarket.curation.domain.post.dto;

import com.blackMarket.curation.domain.post.domain.Post;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PostSaveRequestDto {

    @NotNull
    private String title;

    @NotNull
    private String content;

    @NotNull
    private Long memberId;

    @NotNull
    private Long categoryId;

    @Builder
    public PostSaveRequestDto(String title,
                              String content,
                              Long memberId,
                              Long categoryId
                              ) {
        this.title = title;
        this.content = content;
        this.memberId = memberId;
        this.categoryId = categoryId;
    }

    public Post toEntity() {
        return Post.builder()
                .title(this.title)
                .content(this.content)
                .build();
    }
}
