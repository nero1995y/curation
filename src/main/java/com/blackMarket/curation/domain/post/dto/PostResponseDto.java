package com.blackMarket.curation.domain.post.dto;

import com.blackMarket.curation.domain.post.domain.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime modifiedDate;
    private LocalDateTime createDate;

    @Builder
    public PostResponseDto(Long id,
                           String title,
                           String content,
                           LocalDateTime modifiedDate,
                           LocalDateTime createDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.modifiedDate = modifiedDate;
        this.createDate = createDate;
    }

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.modifiedDate = post.getModifiedDate();
        this.createDate = post.getCreateDate();
    }
}
